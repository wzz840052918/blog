package com.minzheng.blog.aspect;

import com.google.common.util.concurrent.RateLimiter;
import com.minzheng.blog.enums.StatusCodeEnum;
import com.minzheng.blog.exception.BizException;
import com.minzheng.blog.util.RateLimiterContextHolder;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author: Revenz
 * @date: 2022/6/1 10:52
 * @copyright: Copyright (c) 2022 迅策科技
 **/
@Component
@Aspect
public class RateLimitAspect {
    private RateLimiter rateLimiter;

    @Autowired
    RateLimiterContextHolder rateLimiterContextHolder;

    @PostConstruct
    public void init() {
        this.rateLimiter = rateLimiterContextHolder.getRateLimiter();
    }

    @Pointcut(value = "execution(* com.minzheng.blog.controller.*.*(..))")
    public void rateLimitPointCut() {}

    @SneakyThrows
    @Around(value = "rateLimitPointCut()")
    public void rateLimit(ProceedingJoinPoint pjp) {
        boolean success = rateLimiter.tryAcquire(1);

        if(success) {
            pjp.proceed();
        } else {
            throw new BizException(StatusCodeEnum.INTERFACE_RATE_LIMITER);
        }
    }
}
