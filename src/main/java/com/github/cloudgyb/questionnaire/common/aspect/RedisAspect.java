package com.github.cloudgyb.questionnaire.common.aspect;

import com.github.cloudgyb.questionnaire.common.exception.RRException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Redis切面处理类
 *
 * @author Mark
 */
@Aspect
@Configuration
public class RedisAspect {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * redis缓存开关
     * open=true开启 false关闭
     */
    @Value("${spring.redis.open: false}")
    private boolean open;

    @Around("execution(* com.github.cloudgyb.questionnaire.common.utils.RedisUtils.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        if (open) {
            try {
                result = point.proceed();
            } catch (Exception e) {
                logger.error("redis error", e);
                throw new RRException("Redis服务异常");
            }
        }
        return result;
    }
}
