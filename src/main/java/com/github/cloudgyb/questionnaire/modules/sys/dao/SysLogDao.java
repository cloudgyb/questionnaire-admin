package com.github.cloudgyb.questionnaire.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 *
 * @author Mark
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {

}
