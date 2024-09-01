package com.shinhan.knockknock.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ApiLoggingInterceptor.class);

    // ANSI 색상 코드
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ipAddress = request.getRemoteAddr();

        // 메서드에 따라 색상 변경
        String methodColor = switch (method) {
            case "GET" -> GREEN;
            case "POST" -> BLUE;
            case "PUT" -> YELLOW;
            case "DELETE" -> RED;
            default -> RESET;
        };

        logger.info("🔍 [{}{}{}] {}{}{}, | 🌐 {}{}{}",
                methodColor, method, RESET,
                RESET, uri, RESET,
                RESET, ipAddress, RESET);
        return true;
    }
}
