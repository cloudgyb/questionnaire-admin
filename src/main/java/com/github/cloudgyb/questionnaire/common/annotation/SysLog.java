package com.github.cloudgyb.questionnaire.common.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author Mark
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    /**
     * 生成日志的名称
     */
    String value() default "";
}
