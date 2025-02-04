package com.vita.back.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

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
}
