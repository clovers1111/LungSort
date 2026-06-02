package com.clovers1111.pdfsortspring.controller;

import com.clovers1111.pdfsortspring.config.AppFileProperties;
import com.clovers1111.pdfsortspring.file.FileOrchestratorService;
import com.clovers1111.pdfsortspring.image.ImagePathService;
import com.clovers1111.pdfsortspring.image.utility.ImagePathToUrlMapper;
import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.job.JobConfigService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class JobOperationController {

    public static Logger logger = LoggerFactory.getLogger(JobOperationController.class);

    private final FileOrchestratorService fileOrchestratorService;

    private final ImagePathService imagePathService;

    private final JobConfigService jobConfigService;

    private final AppFileProperties appFileProperties;

    private final ImagePathToUrlMapper imagePathToUrlMapper;



    @PostMapping(path = "/process")
    public ResponseEntity<ImageProcessResponseDto> processFile(@RequestParam("jobId") @NonNull final UUID jobId) throws IOException {
        //resolve job config/recreate cache
        final JobConfig jobConfig = jobConfigService.getJobConfig(jobId);
        if (jobConfig == null) {
            logger.error("JobConfig with UUID {} does not exist", jobId);
            return null;
        }

        logger.info("Beginning to process job with jobID {}", jobId);
        fileOrchestratorService.processFileIntoImages(jobConfig);
        logger.info("Successfully persisted job {}", jobConfig.getJobId());

        // Get image files for user to request later; we'll do this now to make frontend retrieval more seamless.
        final Set<Path> imageFilePaths = imagePathService.retrieveImageFiles(
                jobConfig,
                appFileProperties.defaultFileRetrievalNumber());
        final Set<String> imageUrls = imagePathToUrlMapper.imagePathToUrl(imageFilePaths, jobConfig);

        final ImageProcessResponseDto response = new ImageProcessResponseDto(imageUrls, jobConfig.getJobId());

        return ResponseEntity.ok(response);
    }

}
