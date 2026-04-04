package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface FileRetrievalFacade {
    public Set<Path> retrieveImageFiles(final JobConfig jobConfig, final Integer numOfFiles);
}
