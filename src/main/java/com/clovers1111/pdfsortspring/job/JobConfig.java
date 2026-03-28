package com.clovers1111.pdfsortspring.job;

import lombok.Data;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

@Data
public class JobConfig {

    private final UUID jobId;
    private final String fileNameWithExtension;
    private final Path jobDir;
    private final Instant date = Instant.now();

    protected JobConfig(UUID jobId, String fileNameWithExtension, Path jobDir) {
        this.jobId = jobId;
        this.fileNameWithExtension = fileNameWithExtension;
        this.jobDir = jobDir;

    }


}
