package com.clovers1111.pdfsortspring.file;

import java.util.Arrays;
import java.util.Optional;

public enum FileTypes {
    PDF(".pdf", "application/pdf"),
    PNG(".png", "image/png"),
    JPG(".jpg", "image/jpeg"),
    JPEG(".jpeg", "image/jpeg"),
    JSON(".json", "application/json");

    private final String extension;
    private final String mimeType;

    FileTypes(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getExtensionWithoutDot() {
        return extension.replace(".", "");
    }

    public String getMimeType() {
        return mimeType;
    }

    public static Optional<FileTypes> fromMimeType(String mimeType) {
        return Arrays.stream(values())
                .filter(fileType -> fileType.mimeType.equalsIgnoreCase(mimeType))
                .findFirst();
    }

    public static Optional<FileTypes> fromExtension(String extension) {
        if (extension == null || extension.isBlank()) {
            return Optional.empty();
        }
        final String validatedExtension = extension.charAt(0) != '.'
                ? "." + extension
                : extension;

        return Arrays.stream(values())
                .filter(fileType -> fileType.extension.equalsIgnoreCase(validatedExtension))
                .findFirst();
    }

    public boolean isImage() {
        return this == PNG || this == JPG || this == JPEG;
    }
}

