package com.github.cloudgyb.questionnaire.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.common.exception.RRException;
import com.github.cloudgyb.questionnaire.common.utils.Constant;
import com.github.cloudgyb.questionnaire.common.utils.QueryPageBuilder;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysUserDao;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 系统用户service
 *
 * @author Mark
 */
@Service
public class SysUserService extends ServiceImpl<SysUserDao, SysUserEntity> {
    private final SysUserRoleService sysUserRoleService;
    @Resource(type = SysRoleService.class)
    private SysRoleService sysRoleService;

    public SysUserService(SysUserRoleService sysUserRoleService) {
        this.sysUserRoleService = sysUserRoleService;
    }

    public IPage<SysUserEntity> queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        Long createUserId = (Long) params.get("createUserId");
        IPage<SysUserEntity> page = this.page(
                new QueryPageBuilder<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .ne("user_id", getUserId())
                        .eq(createUserId != null, "create_user_id", createUserId)
                        .like(StringUtils.isNotBlank(username), "username", username)
        );
        List<SysUserEntity> records = page.getRecords();
        if (records != null) {
            records.forEach(u -> {
                Long userId = u.getUserId();
                List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
                u.setRoleIdList(roleIdList);
            });
        }
        return page;
    }

    /**
     * 切换用户的锁定状态
     */
    public R switchStatus(Long userId) {
        SysUserEntity userEntity = this.getById(userId);
        if (userEntity == null)
            return R.error("用户不存在！");
        Integer status = userEntity.getStatus();
        if (status == 0)
            userEntity.setStatus(1);
        else
            userEntity.setStatus(0);
        this.updateById(userEntity);
        return R.ok();
    }

    private SysUserEntity getUser() {
        return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    private Long getUserId() {
        return getUser().getUserId();
    }

    public List<String> queryAllPerms(Long userId) {
        return baseMapper.queryAllPerms(userId);
    }


    public List<Long> queryAllMenuId(Long userId) {
        return baseMapper.queryAllMenuId(userId);
    }


    public SysUserEntity queryByUserName(String username) {
        return baseMapper.queryByUserName(username);
    }

    @Transactional
    public void saveUser(SysUserEntity user) {
        user.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
        user.setSalt(salt);
        user.setStatus(1);
        this.save(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    /**
     * 更新用户信息
     * 如果前端没有设置password参数，则不修改密码
     */
    @Transactional
    public R update(SysUserEntity user) {
        SysUserEntity userEntity = this.getById(user.getUserId());
        if (userEntity == null)
            return R.error("用户不存在!");
        userEntity.setUsername(user.getUsername());
        String password = user.getPassword();
        if (StringUtils.isNotBlank(password) && !password.equals(userEntity.getPassword())) {
            String salt = userEntity.getSalt();
            userEntity.setPassword(new Sha256Hash(password, salt).toHex());
        }
        userEntity.setMobile(user.getMobile());
        userEntity.setEmail(user.getEmail());
        userEntity.setRoleIdList(user.getRoleIdList());
        this.updateById(userEntity);
        //检查角色是否越权
        checkRole(userEntity);
        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
        return R.ok();
    }

    @Transactional
    public void deleteBatch(Long[] userId) {
        this.removeByIds(Arrays.asList(userId));
    }

    public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new QueryWrapper<SysUserEntity>().eq("user_id", userId)
                        .eq("password", password));
    }

    /**
     * 检查角色是否越权
     */
    private void checkRole(SysUserEntity user) {
        if (user.getRoleIdList() == null || user.getRoleIdList().size() == 0) {
            return;
        }
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if (user.getCreateUserId() == Constant.SUPER_ADMIN) {
            return;
        }

        //查询用户创建的角色列表
        List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

        //判断是否越权
        if (!roleIdList.containsAll(user.getRoleIdList())) {
            throw new RRException("新增用户所选角色，不是本人创建");
        }
    }
}