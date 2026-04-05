package com.clovers1111.pdfsortspring.job;


import com.clovers1111.pdfsortspring.file.FileTypes;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public interface JobConfigFileService {

    void saveJobConfigFile(JobConfig jobConfig) throws IOException;

    public String jobConfigToJson(JobConfig jobConfig) throws JsonProcessingException;

    public FileTypes getJobConfigFileType(final JobConfig jobConfig);

    Path buildJobConfigPath(Path path, UUID jobId);
}
