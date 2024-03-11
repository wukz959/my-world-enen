package com.myworld.enen.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName GlobalCorsConfig
 * @Author wkz
 * @Date 2023/12/13 21:15
 * @Version 1.0
 */
@Slf4j
@Configuration
public class GlobalCorsConfig {
    @Value("${origin.allow}")
    private String[] allow;
    @Bean
    public CorsFilter corsFilter() {
        //1. 添加 CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        List<String> allowOrigins = Arrays.asList(allow);
        log.info("放行域名：");
        for (int i = 0; i < allowOrigins.size(); i++) {
            log.info(allowOrigins.get(i));
        }
//        config.setAllowedOriginPatterns(allowOrigins);
        config.addAllowedOriginPattern("*");
        //是否发送 Cookie
        config.setAllowCredentials(true);
        //放行哪些请求方式
        config.addAllowedMethod("*");
        //放行哪些原始请求头部信息
        config.addAllowedHeader("*");
        //暴露哪些头部信息
//        config.addExposedHeader("*");
        //2. 添加映射路径
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**",config);
        //3. 返回新的CorsFilter
        return new CorsFilter(corsConfigurationSource);
    }
}

