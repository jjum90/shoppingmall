package com.daou.shoppingmall.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * TODO AOP를 이용하여 시작 전, 시작 후 관심사 메소드에 로깅을 남김
 * 1. 처리 시간
 * 2. 처리 Thread 이름
 * 3. 처리 메소드 이름
 * 4. 처리 패러미터 정보
 */

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Around("execution(* com.daou.shoppingmall..*.*(..))")
    public Object logging(ProceedingJoinPoint point) throws Throwable {
        StringBuilder builder = new StringBuilder();
        LocalDateTime startTime = LocalDateTime.now();
        String currentThreadName = Thread.currentThread().getName();

        builder.append("Thread Name: ");
        builder.append(currentThreadName);
        builder.append(", ");
        builder.append("Location : ");
        builder.append(point.getSignature().getClass());
        builder.append(", ");
        builder.append("Method : ");
        builder.append(point.getSignature().getName());
        builder.append(", ");
        builder.append(String.join(", ", Arrays.stream(point.getArgs()).map(arg -> arg.getClass().getSimpleName()).collect(Collectors.toList())));

        Object object = point.proceed();

        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.toMillis() / 1000;
        builder.append("Start Time : ");
        builder.append(startTime);
        builder.append(", ");
        builder.append("End Time : ");
        builder.append(endTime);
        builder.append(", ");
        builder.append("Duration Time : ");
        builder.append(seconds);
        builder.append( " seconds");

        log.info(builder.toString());

        return object;
    }
}
