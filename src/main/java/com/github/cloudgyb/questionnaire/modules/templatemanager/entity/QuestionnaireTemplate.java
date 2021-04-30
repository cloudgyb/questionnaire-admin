package com.github.cloudgyb.questionnaire.modules.templatemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 调查问卷模板实体类
 *
 * @author cloudgyb
 * 2021/4/11 21:23
 */
@Data
@TableName("template")
public class QuestionnaireTemplate {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private java.sql.Timestamp createDate;
    private java.sql.Timestamp publishDate;
    private long authorId;
    @TableField(exist = false)
    private String authorName;
    private long typeId;
    private int questionCount;
    /**
     * 当前模板的状态，0未发布，1已发布
     * 未发布的模板对用户不可见
     */
    private int status;
}
