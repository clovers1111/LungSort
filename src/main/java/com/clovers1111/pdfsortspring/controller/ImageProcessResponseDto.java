package com.clovers1111.pdfsortspring.controller;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class ImageProcessResponseDto {
    private Set<Path> imagePaths;
    private String jobId;

    public ImageProcessResponseDto(Set<Path> imagePaths, String jobId) {
        this.imagePaths = imagePaths;
        this.jobId = jobId;
    }

    // Getters/Setters
    public Set<Path> getImagePaths() {
        return imagePaths;
    }

    public String getJobId() {
        return jobId;
    }
}