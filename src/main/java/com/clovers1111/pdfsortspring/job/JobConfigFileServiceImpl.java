package com.clovers1111.pdfsortspring.job;

import com.clovers1111.pdfsortspring.Config;
import com.clovers1111.pdfsortspring.file.FileTypes;
import com.clovers1111.pdfsortspring.file.utility.FileRetrievalHelper;
import com.clovers1111.pdfsortspring.job.utility.JobRetrievalHelper;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class JobConfigFileServiceImpl implements JobConfigFileService {

    private final JobConfigService jobConfigService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public JobConfigFileServiceImpl(JobConfigService jobConfigService){
        this.jobConfigService = jobConfigService;
    }

    // Job config folder has been created; we just need to persist the file as json
    public void saveJobConfigFile(JobConfig jobConfig) throws IOException {
        Files.write(jobConfig.getJobDir().resolve(jobConfig.getJobId() + FileTypes.JSON.getExtension()), jobConfigToJson(jobConfig).getBytes());
    }

    public String jobConfigToJson(JobConfig jobConfig) {
        return objectMapper.writeValueAsString(jobConfig);
    }

    public Path buildJobConfigPath(Path rootDir, UUID jobId) {
        return JobRetrievalHelper.getJobConfig(rootDir, jobId);
    }

    public FileTypes getJobConfigFileType(final JobConfig jobConfig) {
        return FileTypes.fromExtension(FileRetrievalHelper
                        .getFileExtension(Path.of(jobConfig
                                .getFileNameWithExtension())))
                .orElseThrow(/*some exception*/);
    }

}
