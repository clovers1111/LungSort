package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;

import java.io.IOException;

public interface FileOrchestratorService {

    public void processFileIntoImages(JobConfig jobConfig) throws IOException;
}
