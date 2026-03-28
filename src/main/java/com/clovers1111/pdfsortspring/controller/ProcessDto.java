package com.clovers1111.pdfsortspring.controller;

import lombok.Data;

import java.io.File;

@Data
public class ProcessDto {
    private String fileName;
    private String filePath;

    public ProcessDto(Path path) {
        this.fileName = file.getName();
        this.filePath = file.getAbsolutePath();
    }


}
