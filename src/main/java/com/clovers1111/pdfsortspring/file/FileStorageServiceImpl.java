package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.Config;
import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.job.JobConfigFileService;
import com.clovers1111.pdfsortspring.pdf.PdfStorageService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private static final Path ROOT_DIR = Config.getDirectory();

    //TODO: Let users specify DPI per image
    private static final Integer DEFAULT_DPI = Config.getIntProperty("default-dpi");

    private final JobConfigFileService jobConfigFileService;
    private final DirectoryWorkerService directoryWorkerService;
    private final PdfStorageService pdfStorageService;


    public FileStorageServiceImpl(DirectoryWorkerService directoryWorkerService,
                                  JobConfigFileService jobConfigFileService,
                                  PdfStorageService pdfStorageService) {
        this.directoryWorkerService = directoryWorkerService;
        this.jobConfigFileService = jobConfigFileService;
        this.pdfStorageService = pdfStorageService;
    }

    // This is the only method that is called in the controller to persist files from the API
    @Override
    public void saveMultipartFile(final MultipartFile multipartFile, final JobConfig jobConfig) throws IOException {
        final String contentType = multipartFile.getContentType();
        logger.debug("Received multipart file with content type: {}", contentType);

        final FileTypes fileType = FileTypes.fromMimeType(contentType)
                .orElseThrow(() -> {
                    logger.error("Unsupported file type: {}", contentType);
                    return new IllegalArgumentException("Unsupported file type: " + contentType);
                });

        directoryWorkerService.createDirectory(jobConfig.getJobDir());
        jobConfigFileService.saveJobConfigFile(jobConfig);

        logger.info("Saving file of type {} for job {}", fileType, jobConfig.getJobId());
        switch (fileType) {
            case PDF -> savePdfFile(multipartFile, jobConfig);
            case PNG, JPG, JPEG -> logger.warn("Image file saving not yet implemented for type: {}", fileType);
        }
    }

    @Override
    public void savePdfAsImageFiles(final PDDocument pdDocument, Integer dpi, JobConfig jobConfig) throws IOException {
        final Integer targetDpi = dpi != null ? dpi : DEFAULT_DPI;

        logger.debug("Delegating PDF to image files for job {} to {}", jobConfig.getJobId(), jobConfig.getJobDir());
        pdfStorageService.savePdfAsImageFiles(pdDocument, targetDpi, jobConfig.getJobDir());
    }

    // Coupled with jobConfig
    private void savePdfFile(MultipartFile file, JobConfig jobConfig) throws IOException {
        final Path targetPathWithFile = jobConfig.getJobConfigPrimaryFile();

        logger.debug("Delegating PDF save for job {} to {}", jobConfig.getJobId(), targetPathWithFile);
        pdfStorageService.savePdfFile(file, targetPathWithFile);
    }

}
