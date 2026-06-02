package com.clovers1111.pdfsortspring.controller;

import com.clovers1111.pdfsortspring.file.FileStorageFacade;
import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.job.JobConfigService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final FileStorageFacade fileStorageFacade;

    private final JobConfigService jobConfigService;

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
        return ResponseEntity.ok(jobConfig);
    }
}
