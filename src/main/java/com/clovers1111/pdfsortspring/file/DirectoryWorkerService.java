package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface DirectoryWorkerService {
    Path createDirectory(Path path) throws IOException;
}
