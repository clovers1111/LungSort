package com.clovers1111.pdfsortspring.controller;

import com.clovers1111.pdfsortspring.file.FileStorageService;
import com.clovers1111.pdfsortspring.file.utility.FileConversionService;
import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.job.JobConfigService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final FileStorageService fileStorageService;
    private final JobConfigService jobConfigService;

    public FileUploadController(
            FileStorageService fileStorageService,
            JobConfigService jobConfigService) {
        this.fileStorageService = fileStorageService;
        this.jobConfigService = jobConfigService;
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
        fileStorageService.saveMultipartFile(file, jobConfig);
        logger.info("File uploaded successfully: jobId={}, file={}", jobConfig.getJobId(), jobConfig.getFileNameWithExtension());


        // Pulls out the file with the path (e.g., /job-directories/jobUUID/jobUUID.json)
        final Path storedFile = jobConfig.getJobDir().resolve(jobConfig.getFileNameWithExtension());
        return ResponseEntity.ok(jobConfig);
    }

    /*

    //TODO: Refactor to worth with paths
    @GetMapping(path = "/retrieve")
    public ResponseEntity<byte[]> retrieveThumbnail() throws IOException {
        logger.debug("Thumbnail retrieval request received");

        final Path imgFile = fileStorageService.getRandomImageFile();
        logger.debug("Selected image file for thumbnail: {}", imgFile.getName());

        final byte[] body = fileConversionService.fileToByteArray(imgFile);
        final String contentType = FileStorageService.getContentType(imgFile);
        logger.info("Returning thumbnail: file={}, contentType={}, size={} bytes",
                imgFile.getName(), contentType, body.length);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(body);
    }
    */
}
