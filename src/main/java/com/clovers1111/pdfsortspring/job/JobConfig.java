package com.clovers1111.pdfsortspring.job;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

@Data
public class JobConfig {
    private final UUID jobId;
    private final String fileNameWithExtension;
    private final Path jobDir;
    private final Instant date;

    @JsonCreator
    public JobConfig(
        @JsonProperty("jobId") UUID jobId,
        @JsonProperty("fileNameWithExtension") String fileNameWithExtension,
        @JsonProperty("jobDir") Path jobDir
    ) {
        this.jobId = jobId;
        this.fileNameWithExtension = fileNameWithExtension;
        this.jobDir = jobDir;
        this.date = Instant.now();
    }

    // E.g., /root/dir/my-file.pdf
    public Path getJobConfigPrimaryFile() {
        return getJobDir().resolve(getFileNameWithExtension());
    }

}
