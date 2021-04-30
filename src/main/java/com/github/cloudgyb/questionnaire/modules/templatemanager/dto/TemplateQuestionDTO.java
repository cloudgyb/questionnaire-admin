package com.github.cloudgyb.questionnaire.modules.templatemanager.dto;

import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplateQuestion;
import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplateQuestionOption;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 调查问卷模板问题DTO
 *
 * @author cloudgyb
 * 2021/4/12 16:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateQuestionDTO extends QuestionnaireTemplateQuestion {
    private List<QuestionnaireTemplateQuestionOption> options;
}