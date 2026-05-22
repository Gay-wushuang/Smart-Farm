package com.wisdom.farm.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.farm.common.Result;
import com.wisdom.farm.common.UserContext;
import com.wisdom.farm.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final JwtUtil jwtUtil;

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            JwtUtil.Claims claims = jwtUtil.parseClaims(token);
            UserContext.setUserId(claims.userId());
            UserContext.setAdmin(claims.admin());
            return true;
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(Result.error("登录已失效，请重新登录")));
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
