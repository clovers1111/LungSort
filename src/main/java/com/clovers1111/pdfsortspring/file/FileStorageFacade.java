package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.Config;
import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.pdf.PdfStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

// This is essentially a facade for interactions with JobConfig objects.
// It deals with orchestrating all the prerequisites for the APIs we are
// interacting with based on file types, for instance.
@Service
public class FileStorageFacade {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageFacade.class);

    private final PdfStorageService pdfStorageService;


    public FileStorageFacade(PdfStorageService pdfStorageService) {
        this.pdfStorageService = pdfStorageService;
    }

    // This is the only method that is called in the controller to persist files from the API,
    // but will likely come back to bite me for being so coupled with the creation of the
    // JobConfig object.
    public void saveMultipartFile(final MultipartFile multipartFile, final JobConfig jobConfig) throws IOException {
        final String contentType = multipartFile.getContentType();
        logger.debug("Received multipart file with content type: {}", contentType);

        final FileTypes fileType = FileTypes.fromMimeType(contentType)
                .orElseThrow(() ->
                    new IllegalArgumentException("Unsupported file type: " + contentType)
                );

        logger.info("Saving file of type {} for job {}", fileType, jobConfig.getJobId());
        switch (fileType) {
            case PDF -> savePdfFile(multipartFile, jobConfig);
            case PNG, JPG, JPEG -> logger.warn("Image file saving not yet implemented for type: {}", fileType);
        }
    }

    private void savePdfFile(MultipartFile file, JobConfig jobConfig) throws IOException {
        final Path targetPathWithFile = jobConfig.getJobConfigDocumentFile();

        logger.debug("Delegating PDF save for job {} to {}", jobConfig.getJobId(), targetPathWithFile);
        pdfStorageService.savePdfFile(file, targetPathWithFile);
    }

}
