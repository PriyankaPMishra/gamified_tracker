package com.tracker.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rate-limit")
public record RateLimitProperties(Bucket activity, Bucket gamification, Bucket auth) {
    public record Bucket(long capacity, long periodSeconds) {
    }
}