package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.job.JobConfigService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FileProcessorServiceImpl implements FileProcessorService {

    private final JobConfigService jobConfigService;
    private final FileStorageService fileStorageService;

    FileProcessorServiceImpl(JobConfigService jobConfigService,
                             FileStorageService fileStorageService) {
        this.jobConfigService = jobConfigService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public void processFileIntoImages(JobConfig jobConfig) throws IOException {
        final FileTypes fileType = jobConfigService.getJobConfigFileType(jobConfig);

        switch(fileType) {
            case PDF -> fileStorageService.savePdfAsImageFiles(jobConfig);
        }

    }
}
