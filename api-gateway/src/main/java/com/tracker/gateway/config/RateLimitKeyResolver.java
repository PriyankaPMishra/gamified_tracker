package com.tracker.gateway.config;


import org.springframework.web.servlet.function.ServerRequest;

import java.util.function.Function;

public class RateLimitKeyResolver {
    private RateLimitKeyResolver() {
    }

    public static Function<ServerRequest, String> byUserIdOrIp() {
        return serverRequest -> {
            String userId = serverRequest.servletRequest().getHeader("userId");
            if (userId != null && !userId.isBlank()) return "user:" + userId;
            String xff = serverRequest.servletRequest().getHeader("X-Forwarded-For");
            String ip = (xff != null && !xff.isBlank()) ? xff.split(",")[0].trim() : serverRequest.servletRequest().getRemoteAddr();
            return "ip:" + ip;
        };
    }
}
