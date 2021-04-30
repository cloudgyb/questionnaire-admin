package com.github.cloudgyb.questionnaire.modules.templatemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 调查问卷模板问题实体类
 *
 * @author cloudgyb
 * 2021/4/12 16:18
 */
@Data
@TableName("template_question")
public class QuestionnaireTemplateQuestion {
    @TableId(type = IdType.AUTO)
    private Long id;
    private long templateId;
    private int questionType;
    private String questionTitle;
    private int questionOrder;
    private int questionNum;
    private String inputPlaceholder;
}
