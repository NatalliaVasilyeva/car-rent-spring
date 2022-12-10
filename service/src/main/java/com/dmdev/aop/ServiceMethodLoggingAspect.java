package com.dmdev.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ServiceMethodLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.dmdev.service.*Service.*(..))")
    public void anyPublicServiceMethod(){
    }

    @AfterThrowing(pointcut = "anyPublicServiceMethod()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getMessage() : "NULL");
    }

    @Around("anyPublicServiceMethod()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        final String joinPoints = Arrays.toString(joinPoint.getArgs());
        logger.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), joinPoints);

        long startTime = System.currentTimeMillis();
        final Object result = joinPoint.proceed();
        long timeTakenInMilliseconds = System.currentTimeMillis() - startTime;

        logger.debug("Exit: {}.{}() with result = {} and time {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), result.toString(), timeTakenInMilliseconds);
        return result;
    }
}