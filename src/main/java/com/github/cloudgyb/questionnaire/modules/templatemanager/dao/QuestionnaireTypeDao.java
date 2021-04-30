package com.github.cloudgyb.questionnaire.modules.templatemanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调查问卷类型dao
 *
 * @author cloudgyb
 * 2021/4/11 15:08
 */
@Mapper
public interface QuestionnaireTypeDao extends BaseMapper<QuestionnaireType> {
}
