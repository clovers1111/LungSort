package com.clovers1111.pdfsortspring.job;


import java.io.IOException;

public interface JobConfigFileService {

    void saveJobConfigFile(JobConfig jobConfig) throws IOException;

    public String jobConfigToJson(JobConfig jobConfig);
}
