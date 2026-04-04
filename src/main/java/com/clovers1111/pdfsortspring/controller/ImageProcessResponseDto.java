package com.clovers1111.pdfsortspring.controller;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ImageProcessResponseDto {
    private Set<Path> imagePaths;
    private UUID jobId;

    public ImageProcessResponseDto(Set<Path> imagePaths, UUID jobId) {
        this.imagePaths = imagePaths;
        this.jobId = jobId;
    }

    // Getters/Setters
    public Set<Path> getImagePaths() {
        return imagePaths;
    }

    public UUID getJobId() {
        return jobId;
    }
}