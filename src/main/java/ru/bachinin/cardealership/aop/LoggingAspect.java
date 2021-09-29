package ru.bachinin.cardealership.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Aspect
public class LoggingAspect {

    private final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Pointcut("within(ru.bachinin.cardealership.service..*)")
    public void stringProcessingMethods() {
    }

    @Before("stringProcessingMethods()")
    public void logMethodCall(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        logger.log(Level.INFO, "Method " + methodName + " start working");
    }

    @After("stringProcessingMethods()")
    public void logMethodFinish(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        logger.log(Level.INFO, "Method " + methodName + " finish");
    }

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        logger.log(Level.INFO, "Method " +  joinPoint.getSignature() + " completed for " + executionTime + " ms");
        return proceed;
    }
}
