package com.tracker.gateway.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@Order(2)
public class AuthRateLimitFilter extends OncePerRequestFilter {
    private final ProxyManager<String> proxyManager;
    private final BucketConfiguration configuration;

    public AuthRateLimitFilter(ProxyManager<String> proxyManager, RateLimitProperties props) {
        this.proxyManager = proxyManager;
        var b = props.auth();
        this.configuration = BucketConfiguration.builder()
                .addLimit(Bandwidth.builder().capacity(b.capacity())
                        .refillGreedy(b.capacity(), Duration.ofSeconds(b.periodSeconds())).build())
                .build();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        var bucket = proxyManager.builder().build("auth" + ip, () -> configuration);
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too many requests\"}");
        }
    }

}
