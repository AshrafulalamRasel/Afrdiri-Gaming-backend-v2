package com.afridi.gamingbackend.config;


import com.afridi.gamingbackend.util.DateTimeUtil;
import com.afridi.gamingbackend.util.UuidUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DateTimeUtil dateTimeUtil() {
        return new DateTimeUtil();
    }

    @Bean
    public UuidUtil uuidUtil() {
        return new UuidUtil();
    }
}
