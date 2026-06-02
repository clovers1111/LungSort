package com.clovers1111.pdfsortspring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@ConfigurationProperties(prefix = "app.file")
public record AppFileProperties(
        String imagePrefix,
        Path saveDirectory,
        Integer defaultDpi,
        Integer defaultFileRetrievalNumber,
        List<Integer> dpiList
) {

    public AppFileProperties {
        if (imagePrefix == null || imagePrefix.isBlank()) {
            throw new IllegalArgumentException("app.file.image-prefix must be configured");
        }

        Objects.requireNonNull(saveDirectory, "app.file.save-directory must be configured");
        Objects.requireNonNull(defaultDpi, "app.file.default-dpi must be configured");
        Objects.requireNonNull(defaultFileRetrievalNumber, "app.file.default-file-retrieval-number must be configured");

        if (defaultDpi <= 0) {
            throw new IllegalArgumentException("app.file.default-dpi must be greater than 0");
        }

        if (defaultFileRetrievalNumber <= 0) {
            throw new IllegalArgumentException("app.file.default-file-retrieval-number must be greater than 0");
        }

        dpiList = dpiList == null ? List.of() : List.copyOf(dpiList);
    }
}



