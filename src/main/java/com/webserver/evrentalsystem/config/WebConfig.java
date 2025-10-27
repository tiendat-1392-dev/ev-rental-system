package com.webserver.evrentalsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /uploads/** ra thư mục uploads trong project
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // Map /public/uploads/** ra thư mục public/uploads
        registry.addResourceHandler("/public/uploads/**")
                .addResourceLocations("file:public/uploads/");
    }
}
