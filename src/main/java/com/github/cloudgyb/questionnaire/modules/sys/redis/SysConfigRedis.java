package com.github.cloudgyb.questionnaire.modules.sys.redis;


import com.github.cloudgyb.questionnaire.common.utils.RedisKeys;
import com.github.cloudgyb.questionnaire.common.utils.RedisUtils;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysConfigEntity;
import org.springframework.stereotype.Component;

/**
 * 系统配置Redis
 *
 * @author Mark
 */
@Component
public class SysConfigRedis {
    private final RedisUtils redisUtils;

    public SysConfigRedis(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    public void saveOrUpdate(SysConfigEntity config) {
        if (config == null) {
            return;
        }
        String key = RedisKeys.getSysConfigKey(config.getParamKey());
        redisUtils.set(key, config);
    }

    public void delete(String configKey) {
        String key = RedisKeys.getSysConfigKey(configKey);
        redisUtils.delete(key);
    }

    public SysConfigEntity get(String configKey) {
        String key = RedisKeys.getSysConfigKey(configKey);
        return redisUtils.get(key, SysConfigEntity.class);
    }
}
