package com.clovers1111.pdfsortspring.job;

import com.clovers1111.pdfsortspring.config.AppFileProperties;
import com.clovers1111.pdfsortspring.file.utility.DirectoryHelper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class JobConfigServiceImpl implements JobConfigService {

    private static final Logger logger = LoggerFactory.getLogger(JobConfigServiceImpl.class);

    private static final String DEFAULT_FILE_NAME = "upload.bin";

    private final AppFileProperties appFileProperties;

    private final JobConfigFileService jobConfigFileService;

    // Cache the jobConfigs
    private final Map<UUID, JobConfig> jobConfigsCache = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper;



    @Override
    public JobConfig createJobConfig(final MultipartFile multipartFile) throws IOException {
        Objects.requireNonNull(multipartFile, "multipartFile must not be null");

        /*
         * Generating a random UUID is to keep jobs separate from each other.
         * The directories take the form ROOT_DIR/UUID
         *
         * Instant probability of collision is comparable to the likelihood of a
         * person being hit by a meteorite over the course of a year.
         */
        final UUID jobId = UUID.randomUUID();
        final Path rootDir = appFileProperties.saveDirectory();
        final Path jobDir = rootDir.resolve(jobId.toString());

        final String fileNameWithExtension = validateFileName(multipartFile.getOriginalFilename());
        final JobConfig jobConfig = new JobConfig(jobId, fileNameWithExtension, jobDir);

        DirectoryHelper.createDirectory(jobConfig.getJobDir());
        jobConfigFileService.saveJobConfigFile(jobConfig);

        jobConfigsCache.put(jobId, jobConfig);


        logger.info("Created job config: jobId={}, jobDir={}, fileName={}", jobId, jobDir, fileNameWithExtension);
        return jobConfig;
    }

    @Override
    public JobConfig getJobConfig(final UUID jobId) throws IOException {
        Objects.requireNonNull(jobId, "jobId must not be null");

        // TODO: Missing instance where jobConfig is null
        if (!jobConfigsCache.containsKey(jobId)) { // try to find jobConfig
            logger.warn("JobConfig {} wasn't found in cache. Attempting to resolve . . .", jobId);
            final Path rootDir = appFileProperties.saveDirectory();
            final Path jobConfigPath = jobConfigFileService.buildJobConfigPath(rootDir, jobId);
            // jobConfigPath shouldn't ever be null since we pass the root directory in.
            if (jobConfigPath == null || !jobConfigPath.toFile().exists()) {
                throw new NoSuchElementException("No JobConfig found for jobId: " + jobId);
            } else if (!jobConfigPath.toFile().canRead()) {
                throw new IOException("Cannot read" + jobConfigPath);
            }

            cacheJobConfig(jobId, jobConfigPath);

        }

        final JobConfig jobConfig = jobConfigsCache.get(jobId);
        return jobConfig;
    }

    private void cacheJobConfig(UUID jobId, Path jobConfigPath) {
        try {
            jobConfigsCache.put(jobId,
                    objectMapper.readValue(jobConfigPath, JobConfig.class));
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    private String validateFileName(final String originalFileName) {
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
