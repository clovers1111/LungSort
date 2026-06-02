package com.clovers1111.pdfsortspring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.web")
public record AppWebProperties(String mvcImages) {

    public AppWebProperties {
        if (mvcImages == null || mvcImages.isBlank()) {
            throw new IllegalArgumentException("app.web.mvc-images must be configured");
        }

        mvcImages = mvcImages.startsWith("/") ? mvcImages : "/" + mvcImages;
        mvcImages = mvcImages.endsWith("/") ? mvcImages : mvcImages + "/";
    }
}


