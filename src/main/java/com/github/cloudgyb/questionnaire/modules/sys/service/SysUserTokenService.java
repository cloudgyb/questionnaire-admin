package com.github.cloudgyb.questionnaire.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysUserTokenDao;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysUserTokenEntity;
import com.github.cloudgyb.questionnaire.modules.sys.oauth2.TokenGenerator;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 系统用户TOKEN服务
 *
 * @author geng
 * 2021/04/29 16:31
 */

@Service
public class SysUserTokenService extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> {
    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    public SysUserTokenEntity createToken(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
        //判断是否生成过token
        SysUserTokenEntity tokenEntity = this.getById(userId);
        if (tokenEntity == null) {
            tokenEntity = new SysUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //保存token
            this.save(tokenEntity);
        } else {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //更新token
            this.updateById(tokenEntity);
        }
        return tokenEntity;
    }

    public void logout(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //修改token
        SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        this.updateById(tokenEntity);
    }
}
