package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.job.JobConfigFileService;
import com.clovers1111.pdfsortspring.job.JobConfigService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FileOrchestratorServiceImpl implements FileOrchestratorService {

    private final JobConfigFileService jobConfigFileService;
    private final FileStorageFacade fileStorageFacade;

    FileOrchestratorServiceImpl(JobConfigFileService jobConfigFileService,
                                FileStorageFacade fileStorageFacade) {
        this.jobConfigFileService = jobConfigFileService;
        this.fileStorageFacade = fileStorageFacade;
    }

    @Override
    public void processFileIntoImages(JobConfig jobConfig) throws IOException {
        final FileTypes fileType = jobConfigFileService.getJobConfigFileType(jobConfig);

        switch(fileType) {
            case PDF -> fileStorageFacade.savePdfAsImageFiles(jobConfig);
        }
    }
}
