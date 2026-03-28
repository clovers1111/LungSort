package com.clovers1111.pdfsortspring.job;

import com.clovers1111.pdfsortspring.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JobConfigServiceImpl implements JobConfigService {

    private static final Logger logger = LoggerFactory.getLogger(JobConfigServiceImpl.class);
    private static final Path ROOT_DIR = Config.getDirectory();
    private static final String DEFAULT_FILE_NAME = "upload.bin";

    // Cache the jobConfigs
    private final Map<UUID, JobConfig> jobConfigs = new ConcurrentHashMap<>();

    @Override
    public JobConfig createJobConfig(final MultipartFile multipartFile) throws IOException {
        Objects.requireNonNull(multipartFile, "multipartFile must not be null");

        /*
         * Generating a random UUID is to keep jobs separate from each other.
         * The directories take the form ROOT_DIR/UUID
         *
         * Probability of collision is comparable to the likelihood of a
         * person being hit by a meteorite over the course of a year.
         */
        final UUID jobId = UUID.randomUUID();
        final Path jobDir = ROOT_DIR.resolve(jobId.toString());

        final String fileNameWithExtension = sanitizeFileName(multipartFile.getOriginalFilename());
        final JobConfig jobConfig = new JobConfig(jobId, fileNameWithExtension, jobDir);
        jobConfigs.put(jobId, jobConfig);

        logger.info("Created job config: jobId={}, jobDir={}, fileName={}", jobId, jobDir, fileNameWithExtension);
        return jobConfig;
    }

    @Override
    public JobConfig getJobConfig(final UUID jobId) {
        Objects.requireNonNull(jobId, "jobId must not be null");

        final JobConfig jobConfig = jobConfigs.get(jobId);
        if (jobConfig == null) {

            throw new NoSuchElementException("No JobConfig found for jobId: " + jobId);
        }
        return jobConfig;
    }

    private String sanitizeFileName(final String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            return DEFAULT_FILE_NAME;
        }

        try {
            final Path fileNamePath = Path.of(originalFileName).getFileName();
            if (fileNamePath == null) {
                return DEFAULT_FILE_NAME;
            }
            final String sanitized = fileNamePath.toString();
            return sanitized.isBlank() ? DEFAULT_FILE_NAME : sanitized;
        } catch (InvalidPathException ex) {
            logger.warn("Invalid original filename received: {}", originalFileName);
            return DEFAULT_FILE_NAME;
        }
    }
}
