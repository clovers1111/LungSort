package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;

import java.io.IOException;

public interface StorageFacade {

    void processIntoImageFiles(JobConfig jobConfig) throws IOException;

    void saveFile(JobConfig jobConfig);
}
