package com.github.cloudgyb.questionnaire.datasource.aspect;

import com.github.cloudgyb.questionnaire.datasource.annotation.DataSource;
import com.github.cloudgyb.questionnaire.datasource.config.DynamicContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 多数据源动态切换，切面处理类
 *
 * @author Mark
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAspect {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.github.cloudgyb.questionnaire.datasource.annotation.DataSource) " +
            "|| @within(com.github.cloudgyb.questionnaire.datasource.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();

        DataSource targetDataSource = targetClass.getAnnotation(DataSource.class);
        DataSource methodDataSource = method.getAnnotation(DataSource.class);
        if (targetDataSource != null || methodDataSource != null) {
            String value = Objects.requireNonNullElse(methodDataSource, targetDataSource).value();
            DynamicContextHolder.push(value);
            logger.debug("set datasource is {}", value);
        }
        try {
            return point.proceed();
        } finally {
            DynamicContextHolder.poll();
            logger.debug("clean datasource");
        }
    }
}