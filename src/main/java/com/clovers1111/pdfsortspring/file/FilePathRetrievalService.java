package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;

import java.nio.file.Path;
import java.util.Set;

public interface FilePathRetrievalService {
    public Set<Path> retrieveImageFiles(final JobConfig jobConfig, final Integer numOfFiles);
}
