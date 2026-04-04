package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageFacade {

    public void saveMultipartFile(final MultipartFile multipartFile, JobConfig jobConfig) throws IOException;

    public void savePdfAsImageFiles(final JobConfig jobConfig) throws IOException;

}
