package com.github.cloudgyb.questionnaire.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.common.exception.RRException;
import com.github.cloudgyb.questionnaire.common.utils.QueryPageBuilder;
import com.github.cloudgyb.questionnaire.modules.sys.dao.SysConfigDao;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysConfigEntity;
import com.github.cloudgyb.questionnaire.modules.sys.redis.SysConfigRedis;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统配置信息
 *
 * @author Mark
 */
@Service("sysConfigService")
public class SysConfigService extends ServiceImpl<SysConfigDao, SysConfigEntity> {
    private final SysConfigRedis sysConfigRedis;

    SysConfigService(SysConfigRedis sysConfigRedis) {
        this.sysConfigRedis = sysConfigRedis;
    }

    public IPage<SysConfigEntity> queryPage(Map<String, Object> params) {
        String paramKey = (String) params.get("paramKey");
        return this.page(
                new QueryPageBuilder<SysConfigEntity>().getPage(params, "id", false),
                new QueryWrapper<SysConfigEntity>()
                        .like(StringUtils.isNotBlank(paramKey), "param_key", paramKey)
                        .eq("status", 1)
        );
    }

    public void saveConfig(SysConfigEntity config) {
        this.saveOrUpdate(config);
        sysConfigRedis.saveOrUpdate(config);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(SysConfigEntity config) {
        this.updateById(config);
        sysConfigRedis.saveOrUpdate(config);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateValueByKey(String key, String value) {
        baseMapper.updateValueByKey(key, value);
        sysConfigRedis.delete(key);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] ids) {
        for (Long id : ids) {
            SysConfigEntity config = this.getById(id);
            sysConfigRedis.delete(config.getParamKey());
        }
        this.removeByIds(Arrays.asList(ids));
    }

    public String getValue(String key) {
        SysConfigEntity config = sysConfigRedis.get(key);
        if (config == null) {
            config = baseMapper.queryByKey(key);
            sysConfigRedis.saveOrUpdate(config);
        }

        return config == null ? null : config.getParamValue();
    }

    public <T> T getConfigObject(String key, Class<T> clazz) {
        String value = getValue(key);
        if (StringUtils.isNotBlank(value)) {
            return new Gson().fromJson(value, clazz);
        }
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RRException("获取参数失败");
        }
    }

    public Map<String,String> getSystemConfig(){
        String systemName = getValue("system_name");
        String systemVersion = getValue("system_version");
        String systemCopyright = getValue("system_copyright");
        Map<String,String> map = new HashMap<>();
        map.put("systemName", systemName);
        map.put("systemVersion", systemVersion);
        map.put("systemCopyright", systemCopyright);
        return map;
    }
}