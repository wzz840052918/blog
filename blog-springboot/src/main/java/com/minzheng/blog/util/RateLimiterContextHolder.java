package com.minzheng.blog.util;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Revenz
 * @date: 2022/6/1 10:34
 **/
@Component
public class RateLimiterContextHolder {
    private static RateLimiter rateLimiter;

    public RateLimiterContextHolder() {
        // 启动平滑预热限流
        rateLimiter = RateLimiter.create(4, 2, TimeUnit.SECONDS);
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }
}
