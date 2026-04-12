package com.clovers1111.pdfsortspring.controller;

import com.clovers1111.pdfsortspring.Config;
import com.clovers1111.pdfsortspring.file.FilePathRetrievalService;
import com.clovers1111.pdfsortspring.image.ImageProcessorService;
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

    public final JobConfigService jobConfigService;

    public final FilePathRetrievalService fileRetrievalFacade;


    /**
     * Return a byte array for the corresponding file path
     *
     * <p>
     *     Receives an HTTPs payload with a UUID for the job and a path
     *     for the image that we expect to process. This is done piecemeal
     *     to allow for multiple files to be processed at once at the
     *     behest of the requesting API (frontend).
     * </p>
     *
     *
     * @param jobId for the job that we're working on
     * @param imagePath for the path of the image we're returning
     * @return a response containing an image
     * @throws IOException if something explodes
     */
    @GetMapping(path = "/retrieve")
    public ResponseEntity<byte[]> retrieveImageFile(@RequestParam("jobId") @NonNull final UUID jobId,) throws IOException {

        return ResponseEntity.ok(ImageProcessorService.fileToByteArray(imagePath));
    }
}
