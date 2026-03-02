package com.omy.mcp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 在请求进入时捕获所有 HTTP Header，存入 HeaderContextHolder，
 * 使得 MCP 工具执行线程（即使是子线程）也能通过 HeaderContextHolder.get() 获取。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestContextPropagationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        HeaderContextHolder.set(headers);
        try {
            filterChain.doFilter(request, response);
        } finally {
            HeaderContextHolder.clear();
        }
    }
}
