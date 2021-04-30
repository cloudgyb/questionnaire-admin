package com.github.cloudgyb.questionnaire.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统配置信息
 *
 * @author Mark
 */
@Mapper
public interface SysConfigDao extends BaseMapper<SysConfigEntity> {

    /**
     * 根据key，查询value
     */
    SysConfigEntity queryByKey(String paramKey);

    /**
     * 根据key，更新value
     */
    void updateValueByKey(@Param("paramKey") String paramKey, @Param("paramValue") String paramValue);

}
