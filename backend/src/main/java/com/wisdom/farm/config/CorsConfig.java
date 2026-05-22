package com.wisdom.farm.config;

import com.wisdom.farm.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    public CorsConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        "/auth/bind-phone",
                        "/user/**",
                        "/lands/*/claim",
                        "/monitor/**",
                        "/orders/**",
                        "/pay/**",
                        "/admin/**",
                        "/notifications/**"
                )
                .excludePathPatterns(
                        "/auth/wx-login",
                        "/auth/admin-login",
                        "/monitor/report",
                        "/pay/notify/wechat"
                );
    }
}
