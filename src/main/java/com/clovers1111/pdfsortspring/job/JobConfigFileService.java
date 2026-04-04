package com.clovers1111.pdfsortspring.job;


import com.clovers1111.pdfsortspring.file.FileTypes;

import java.io.IOException;

public interface JobConfigFileService {

    void saveJobConfigFile(JobConfig jobConfig) throws IOException;

    public String jobConfigToJson(JobConfig jobConfig);

    public FileTypes getJobConfigFileType(final JobConfig jobConfig);
}
