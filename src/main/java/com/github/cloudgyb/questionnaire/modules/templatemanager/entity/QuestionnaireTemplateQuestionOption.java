package com.github.cloudgyb.questionnaire.modules.templatemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 调查问卷模板问题选项实体类
 *
 * @author cloudgyb
 * 2021/4/12 16:19
 */
@Data
@TableName("template_question_option")
public class QuestionnaireTemplateQuestionOption {
    @TableId(type = IdType.AUTO)
    private Long id;
    private long questionId;
    private String optionText;
    private int optionOrder;
}
