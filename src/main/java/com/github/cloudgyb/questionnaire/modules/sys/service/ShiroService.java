package com.github.cloudgyb.questionnaire.modules.sys.service;

import com.github.cloudgyb.questionnaire.common.utils.Constant;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysMenuDao;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysUserDao;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysUserTokenDao;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysMenuEntity;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysUserEntity;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysUserTokenEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户认证与权限service
 *
 * @author geng
 * 2021/04/29 16:22
 */
@Service
public class ShiroService {
    private final SysMenuDao sysMenuDao;
    private final SysUserDao sysUserDao;
    private final SysUserTokenDao sysUserTokenDao;

    public ShiroService(SysMenuDao sysMenuDao, SysUserDao sysUserDao,
                        SysUserTokenDao sysUserTokenDao) {
        this.sysMenuDao = sysMenuDao;
        this.sysUserDao = sysUserDao;
        this.sysUserTokenDao = sysUserTokenDao;
    }

    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    public SysUserEntity queryUser(Long userId) {
        return sysUserDao.selectById(userId);
    }
}
