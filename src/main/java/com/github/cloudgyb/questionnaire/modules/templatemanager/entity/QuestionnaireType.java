package com.github.cloudgyb.questionnaire.modules.templatemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 调查问卷分类实体类
 *
 * @author cloudgyb
 * 2021/4/11 15:11
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("questionnaire_type")
public class QuestionnaireType {
    @TableId(type = IdType.AUTO)
    private long id;
    private String typeName;
    private String typeDesc;
}