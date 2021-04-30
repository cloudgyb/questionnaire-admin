package com.github.cloudgyb.questionnaire.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.common.exception.RRException;
import com.github.cloudgyb.questionnaire.common.utils.Constant;
import com.github.cloudgyb.questionnaire.common.utils.QueryPageBuilder;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysRoleDao;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysRoleEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 角色service
 *
 * @author Mark
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleDao, SysRoleEntity> {
    private final SysRoleMenuService sysRoleMenuService;
    private final SysUserService sysUserService;
    private final SysUserRoleService sysUserRoleService;

    public SysRoleService(SysRoleMenuService sysRoleMenuService,
                          SysUserService sysUserService,
                          SysUserRoleService sysUserRoleService) {
        this.sysRoleMenuService = sysRoleMenuService;
        this.sysUserService = sysUserService;
        this.sysUserRoleService = sysUserRoleService;
    }

    public IPage<SysRoleEntity> queryPage(Map<String, Object> params) {
        String roleName = (String) params.get("roleName");
        Long createUserId = (Long) params.get("createUserId");
        return this.page(
                new QueryPageBuilder<SysRoleEntity>().getPage(params),
                new QueryWrapper<SysRoleEntity>()
                        .like(StringUtils.isNotBlank(roleName), "role_name", roleName)
                        .eq(createUserId != null, "create_user_id", createUserId)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRoleEntity role) {
        role.setCreateTime(new Date());
        this.save(role);
        //检查权限是否越权
        checkPrems(role);
        //保存角色与菜单关系
        //sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleEntity role) {
        SysRoleEntity roleEntity = this.getById(role.getRoleId());
        if (roleEntity == null)
            return;
        roleEntity.setRoleName(role.getRoleName());
        roleEntity.setRemark(role.getRemark());
        this.updateById(roleEntity);
        //检查权限是否越权
        checkPrems(role);
        //更新角色与菜单关系
        //sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
        this.removeByIds(Arrays.asList(roleIds));

        //删除角色与菜单关联
        sysRoleMenuService.deleteBatch(roleIds);

        //删除角色与用户关联
        sysUserRoleService.deleteBatch(roleIds);
    }


    public List<Long> queryRoleIdList(Long createUserId) {
        return baseMapper.queryRoleIdList(createUserId);
    }

    /**
     * 检查权限是否越权
     */
    private void checkPrems(SysRoleEntity role) {
        //如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
        if (role.getCreateUserId() == Constant.SUPER_ADMIN) {
            return;
        }

        //查询用户所拥有的菜单列表
        List<Long> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());

        //判断是否越权
        if (!menuIdList.containsAll(role.getMenuIdList())) {
            throw new RRException("新增角色的权限，已超出你的权限范围");
        }
    }
}
