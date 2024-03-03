package com.poly.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class WebMVCConfig {

    @Bean("messageSource")
    public MessageSource loadMess() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.addBasenames("classpath:i18n/messages");
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return source;
    }
}
