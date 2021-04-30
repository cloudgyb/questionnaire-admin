package com.github.cloudgyb.questionnaire.modules.templatemanager.dto;

import com.github.cloudgyb.questionnaire.modules.templatemanager.entity.QuestionnaireTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 调查问卷模板DTO
 *
 * @author cloudgyb
 * 2021/4/12 16:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionnaireTemplateDTO extends QuestionnaireTemplate {
    private List<TemplateQuestionDTO> questions;
}
