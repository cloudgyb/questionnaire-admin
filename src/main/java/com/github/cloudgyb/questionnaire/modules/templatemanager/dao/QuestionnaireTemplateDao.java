package com.github.cloudgyb.questionnaire.modules.templatemanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调查问卷模板DAO
 *
 * @author cloudgyb
 * 2021/4/11 21:25
 */
@Mapper
public interface QuestionnaireTemplateDao extends BaseMapper<QuestionnaireTemplate> {
}
