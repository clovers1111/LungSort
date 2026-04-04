package com.clovers1111.pdfsortspring.controller;

import com.clovers1111.pdfsortspring.Config;
import com.clovers1111.pdfsortspring.file.FileOrchestratorService;
import com.clovers1111.pdfsortspring.file.FileRetrievalFacade;
import com.clovers1111.pdfsortspring.file.FileStorageFacade;
import com.clovers1111.pdfsortspring.file.utility.FileRetrievalService;
import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.job.JobConfigService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private static final Integer NUMBER_OF_FILES = Config.getIntProperty("default-file-retrieval-number");

    private final FileStorageFacade fileStorageFacade;
    private final JobConfigService jobConfigService;
    private final FileOrchestratorService fileOrchestratorService;
    private final FileRetrievalFacade fileRetrievalFacade;

    public FileUploadController(
            FileStorageFacade fileStorageFacade,
            JobConfigService jobConfigService,
            FileOrchestratorService fileOrchestratorService,
            FileRetrievalFacade fileRetrievalFacade) {
        this.fileStorageFacade = fileStorageFacade;
        this.jobConfigService = jobConfigService;
        this.fileOrchestratorService = fileOrchestratorService;
        this.fileRetrievalFacade = fileRetrievalFacade;
    }

    /**
     * The entrance into our backend: makes calls to initialize the procedure
     * for sorting document-like files.
     *
     * @param file from our frontend
     * @return
     * @throws IOException
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/upload")
    public ResponseEntity<JobConfig> uploadFile(@RequestParam("file") @NonNull final MultipartFile file) throws IOException {
        logger.debug("Upload request received: name={}, size={}, contentType={}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());

        // Create our JobConfig
        // All persistence operations NEED to go through the JobConfig object avoid inconsistent writes/reads.
        final JobConfig jobConfig = jobConfigService.createJobConfig(file);
        fileStorageFacade.saveMultipartFile(file, jobConfig);
        logger.info("File uploaded successfully: jobId={}, file={}", jobConfig.getJobId(), jobConfig.getFileNameWithExtension());


        // Pulls out the file with the path (e.g., /job-directories/jobUUID/jobUUID.json)
        final Path storedFile = jobConfig.getJobDir().resolve(jobConfig.getFileNameWithExtension());
        return ResponseEntity.ok(jobConfig);
    }

    @PostMapping(path = "/process")
    public ResponseEntity<ImageProcessResponseDto> processFile(@RequestParam("jobId") @NonNull final UUID jobId) throws IOException {
        JobConfig jobConfig = jobConfigService.getJobConfig(jobId);
        if (jobConfig == null) {
            //resolve job config/recreate cache
            logger.error("JobConfig with UUID {} does not exist", jobId);
            return null;
        }

        logger.info("Beginning to process job with jobID {}", jobId);
        fileOrchestratorService.processFileIntoImages(jobConfig);
        logger.info("Successfully persisted job {}", jobConfig.getJobId());

        // Get image files for user to request later; we'll do this now to make frontend retrieval more seamless.
        final Set<Path> imageFilePaths = fileRetrievalFacade.retrieveImageFiles(jobConfig, NUMBER_OF_FILES);

        final ImageProcessResponseDto response = new ImageProcessResponseDto(imageFilePaths, jobConfig.getJobId());

        return ResponseEntity.ok(response);
    }

    /*
    //TODO: Refactor to worth with paths
    @GetMapping(path = "/retrieve")
    public ResponseEntity<byte[]> retrieveThumbnail() throws IOException {
        logger.debug("Thumbnail retrieval request received");

        final Path imgFile = fileStorageFacade.getRandomImageFile();
        logger.debug("Selected image file for thumbnail: {}", imgFile.getName());

        final byte[] body = fileConversionService.fileToByteArray(imgFile);
        final String contentType = FileStorageFacade.getContentType(imgFile);
        logger.info("Returning thumbnail: file={}, contentType={}, size={} bytes",
                imgFile.getName(), contentType, body.length);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(body);
    }
    */
}
