package com.github.cloudgyb.questionnaire.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.common.utils.MapUtils;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysUserRoleDao;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysUserRoleEntity;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户与角色对应关系service
 *
 * @author Mark
 * @author geng
 */
@Service
public class SysUserRoleService extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> {

    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        //先删除用户与角色关系
        this.removeByMap(new MapUtils().put("user_id", userId));
        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }
        //保存用户与角色关系
        for (Long roleId : roleIdList) {
            SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
            sysUserRoleEntity.setUserId(userId);
            sysUserRoleEntity.setRoleId(roleId);
            this.save(sysUserRoleEntity);
        }
    }

    public List<Long> queryRoleIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }

    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }
}
