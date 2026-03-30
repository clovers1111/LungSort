package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface FileStorageService {

    public void saveMultipartFile(final MultipartFile multipartFile, JobConfig jobConfig) throws IOException;

    public void savePdfAsImageFiles(final JobConfig jobConfig) throws IOException;

}
