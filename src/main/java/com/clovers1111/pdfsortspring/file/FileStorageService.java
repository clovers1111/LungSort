package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface FileStorageService {

    // Save the pddocument as a list of files written to with imageio
    public void savePdfAsImageFiles(final PDDocument pdDocument, Integer dpi, JobConfig jobConfig) throws IOException;

    public void saveMultipartFile(final MultipartFile multipartFile, JobConfig jobConfig) throws IOException;

    public static String getContentType(Path path) throws IOException {
        return Files.probeContentType(path);
    }

    public static String getContentType(MultipartFile multipartFile) {
        return multipartFile.getContentType();
    }

}
