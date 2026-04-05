package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.Config;
import com.clovers1111.pdfsortspring.file.utility.DirectoryHelper;
import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.job.JobConfigFileService;
import com.clovers1111.pdfsortspring.pdf.PdfRendererService;
import com.clovers1111.pdfsortspring.pdf.PdfStorageService;
import org.apache.pdfbox.pdmodel.PDDocument;
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
public class FileStorageFacadeImpl implements FileStorageFacade {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageFacadeImpl.class);

    private static final Path ROOT_DIR = Config.getDirectory();

    //TODO: Let users specify DPI per image
    private static final Integer DEFAULT_DPI = Config.getIntProperty("default-dpi");

    private final JobConfigFileService jobConfigFileService;
    private final PdfStorageService pdfStorageService;
    private final PdfRendererService pdfRendererService;


    public FileStorageFacadeImpl(JobConfigFileService jobConfigFileService,
                                 PdfStorageService pdfStorageService,
                                 PdfRendererService pdfRendererService) {
        this.jobConfigFileService = jobConfigFileService;
        this.pdfStorageService = pdfStorageService;
        this.pdfRendererService = pdfRendererService;
    }

    // This is the only method that is called in the controller to persist files from the API,
    // but will likely come back to bite me for being so coupled with the creation of the
    // JobConfig object.
    @Override
    public void saveMultipartFile(final MultipartFile multipartFile, final JobConfig jobConfig) throws IOException {
        final String contentType = multipartFile.getContentType();
        logger.debug("Received multipart file with content type: {}", contentType);

        final FileTypes fileType = FileTypes.fromMimeType(contentType)
                .orElseThrow(() -> {
                    logger.error("Unsupported file type: {}", contentType);
                    return new IllegalArgumentException("Unsupported file type: " + contentType);
                });

        DirectoryHelper.createDirectory(jobConfig.getJobDir());
        jobConfigFileService.saveJobConfigFile(jobConfig);

        logger.info("Saving file of type {} for job {}", fileType, jobConfig.getJobId());
        switch (fileType) {
            case PDF -> savePdfFile(multipartFile, jobConfig);
            case PNG, JPG, JPEG -> logger.warn("Image file saving not yet implemented for type: {}", fileType);
        }
    }

    // Called by the FileOrchestratorService using only the JobConfig. Again orchestration
    // is done by this specific method to call the lower-level APIs.
    @Override
    public void savePdfAsImageFiles(final JobConfig jobConfig) throws IOException {
        final PDDocument pdDocument = pdfRendererService.fileToPdDocument(jobConfig.getJobConfigPrimaryFile());
        final Integer targetDpi = DEFAULT_DPI; // change later to incorporate job config resolution for dpi
        final FileTypes type = jobConfigFileService.getJobConfigFileType(jobConfig);

        logger.debug("Delegating PDF to image files for job {} to {}", jobConfig.getJobId(), jobConfig.getJobDir());
        pdfStorageService.savePdfAsImageFiles(pdDocument, targetDpi, jobConfig.getJobDir(), type);
    }

    private void savePdfFile(MultipartFile file, JobConfig jobConfig) throws IOException {
        final Path targetPathWithFile = jobConfig.getJobConfigPrimaryFile();

        logger.debug("Delegating PDF save for job {} to {}", jobConfig.getJobId(), targetPathWithFile);
        pdfStorageService.savePdfFile(file, targetPathWithFile);
    }

}
