package com.clovers1111.pdfsortspring.controller;

import java.util.Set;
import java.util.UUID;

public class ImageProcessResponseDto {
    private Set<String> imagePaths;
    private UUID jobId;

    public ImageProcessResponseDto(Set<String> imagePaths, UUID jobId) {
        this.imagePaths = imagePaths;
        this.jobId = jobId;
    }

    // Getters/Setters
    public Set<String> getImagePaths() {
        return imagePaths;
    }

    public UUID getJobId() {
        return jobId;
    }
}