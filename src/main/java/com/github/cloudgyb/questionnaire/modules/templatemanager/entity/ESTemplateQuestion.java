package com.github.cloudgyb.questionnaire.modules.templatemanager.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * ElasticSearch 调查问卷模板问题
 *
 * @author cloudgyb
 * 2021/4/22 13:22
 */
@Data
public class ESTemplateQuestion {
    private Long id;
    private long templateId;
    private int questionType;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String questionTitle;
    private int questionOrder;
    private int questionNum;
    private String inputPlaceholder;
}
