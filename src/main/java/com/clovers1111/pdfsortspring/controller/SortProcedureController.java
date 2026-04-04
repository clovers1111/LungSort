package com.clovers1111.pdfsortspring.controller;

import com.clovers1111.pdfsortspring.Config;
import com.clovers1111.pdfsortspring.file.FileRetrievalFacade;
import com.clovers1111.pdfsortspring.image.ImageProcessorService;
import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.job.JobConfigService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;


/**
 * All the essential sorting procedure stuff with occur here.
 *
 * <p>
 *     Receives an HTTPs payload with a UUID for the job and a path
 *     for the image that we expect to process. This is done piecemeal
 *     to allow for multiple files to be processed at once at the
 *     behest of the requesting API (frontend).
 * </p>
 *
 */
@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class SortProcedureController {

    private static final Logger logger = LoggerFactory.getLogger(SortProcedureController.class);

    public static final Integer NUMBER_OF_FILES = Config.getIntProperty("default-file-retrieval-number");

    public final JobConfigService jobConfigService;

    public final FileRetrievalFacade fileRetrievalFacade;



    @GetMapping(path = "/retrieve")
    public ResponseEntity<byte[]> retrieveImageFile(@RequestParam("jobId") @NonNull final UUID jobId,
                                                    @RequestBody @NonNull final Path imagePath) throws IOException {

        return ResponseEntity.ok(ImageProcessorService.fileToByteArray(imagePath));
    }
}
