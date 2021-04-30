package com.github.cloudgyb.questionnaire.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysRoleMenuDao;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysRoleMenuEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * 角色与菜单对应关系service;
 * 超级管理员角色的菜单并不依赖于此关系,默认超级管理员角色应包括所有菜单。
 *
 * @author Mark
 * @author geng
 */
@Service
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> {

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        //先删除角色与菜单关系
        deleteByRoleId(roleId);
        if (menuIdList.size() == 0) {
            return;
        }
        //保存角色与菜单关系
        for (Long menuId : menuIdList) {
            SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setRoleId(roleId);
            this.save(sysRoleMenuEntity);
        }
    }

    public List<Long> queryMenuIdList(Long roleId) {
        return baseMapper.queryMenuIdList(roleId);
    }

    public void deleteByRoleId(Long roleId) {
        baseMapper.deleteByMap(Map.of("role_id", roleId));
    }

    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }

}
