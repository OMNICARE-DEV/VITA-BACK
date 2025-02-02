package com.vita.back.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAsync
public class Config {
    @Configuration
    @Profile("local")
    @PropertySource("classpath:config.properties")
    public class LocalConfig {}

    @Configuration
    @Profile("edu")
    @PropertySource("classpath:config-edu.properties")
    public class EduConfig {}

    @Configuration
    @Profile("real")
    @PropertySource("classpath:config-real.properties")
    public class RealConfig {}
    
    @Value("${front.domain}")
    private String allowedOrigin;
    
    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigin)
                        .allowedMethods("*");
            }
        };
    }
}
