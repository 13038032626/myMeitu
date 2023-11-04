package com.example.meitu2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class filterConfig {

    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration config = new CorsConfiguration();
        //允许所有域名进行跨域调用
        config.addAllowedOriginPattern("*");
        //允许所有请求头
        config.addAllowedHeader("*");
        //允许所有方法
        config.addAllowedMethod("*");
        //是否发送Cookie信息
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //添加映射路径，拦截一切请求
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}
