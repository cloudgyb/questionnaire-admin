package com.github.cloudgyb.questionnaire.common.constant;

/**
 * 调查问卷模板状态枚举
 *
 * @author cloudgyb
 * 2021/4/17 21:25
 */
public enum TemplateStatusEnum {
    /**
     * 创建完成，用户不可见
     */
    CREATED(0),
    /**
     * 发布完成，用户可用
     */
    PUBLISHED(1),
    /**
     * 下线，用户不可见
     */
    OFFLINE(2);

    int status;

    TemplateStatusEnum(int s) {
        this.status = s;
    }

    public int getStatus() {
        return this.status;
    }
}
