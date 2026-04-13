package com.clovers1111.pdfsortspring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageResourceWebConfig implements WebMvcConfigurer {

    private final static String IMAGES_PATH = Config.getProperty("mvc-images-path");

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        final String location = Config.getDirectory().toUri().toString();
        registry.addResourceHandler(IMAGES_PATH + "**")
                .addResourceLocations(location)
                .setCachePeriod(0);
    }
}

