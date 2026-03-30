package com.clovers1111.pdfsortspring.job;

import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;

@Service
public class JobConfigFileServiceImpl implements JobConfigFileService {

    private final JobConfigService jobConfigService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JobConfigFileServiceImpl(JobConfigService jobConfigService){
        this.jobConfigService = jobConfigService;
    }

    // Job config folder has been created; we just need to persist the file as json
    public void saveJobConfigFile(JobConfig jobConfig) throws IOException {
        Files.write(jobConfig.getJobDir().resolve(jobConfig.getJobId() + ".json"), jobConfigToJson(jobConfig).getBytes());
    }

    public String jobConfigToJson(JobConfig jobConfig) {
        return objectMapper.writeValueAsString(jobConfig);
    }
}
