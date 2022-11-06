package com.yuhua.backgroundmanagement.config;


import com.yuhua.backgroundmanagement.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(JwtInterceptor())
                .addPathPatterns("/**") // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
                .excludePathPatterns("/user/login","/user/register","/**/export","/**/import","/file/*");
    }

    @Bean
    public JwtInterceptor JwtInterceptor() {
        return new JwtInterceptor();
    }
}

