package com.clovers1111.pdfsortspring.job;

import com.clovers1111.pdfsortspring.file.FileTypes;
import com.clovers1111.pdfsortspring.file.utility.FileRetrievalHelper;
import com.clovers1111.pdfsortspring.job.utility.JobRetrievalHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class JobConfigFileServiceImpl implements JobConfigFileService {

    private final ObjectMapper objectMapper = new ObjectMapper();


    // Job config folder has been created; we just need to persist the file as json
    public void saveJobConfigFile(JobConfig jobConfig) throws IOException {
        Files.write(jobConfig.getJobDir().resolve(jobConfig.getJobId() + FileTypes.JSON.getExtension()), jobConfigToJson(jobConfig).getBytes());
    }

    public String jobConfigToJson(JobConfig jobConfig) throws JsonProcessingException {
        // Module registration is necessary to deal with Instant object.
        return objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(jobConfig);
    }

    public Path buildJobConfigPath(Path rootDir, UUID jobId) {
        final Path jobConfigDir = rootDir.resolve(jobId.toString());
        return JobRetrievalHelper.getJobConfigFile(jobConfigDir, jobId);
    }

    public FileTypes getJobConfigFileType(final JobConfig jobConfig) {
        return FileTypes.fromExtension(FileRetrievalHelper
                        .getFileExtension(Path.of(jobConfig
                                .getFileNameWithExtension())))
                .orElseThrow(/*some exception*/);
    }

}
