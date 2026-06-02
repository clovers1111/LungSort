package com.clovers1111.pdfsortspring;

import com.clovers1111.pdfsortspring.config.AppFileProperties;
import com.clovers1111.pdfsortspring.config.AppWebProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ImageResourceWebConfig implements WebMvcConfigurer {

    private final AppWebProperties appWebProperties;

    private final AppFileProperties appFileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        final String location = appFileProperties.saveDirectory().toUri().toString();
        registry.addResourceHandler(appWebProperties.mvcImages() + "**")
                .addResourceLocations(location)
                .setCachePeriod(0);
    }
}

