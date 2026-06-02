package com.clovers1111.pdfsortspring.pdf;

import com.clovers1111.pdfsortspring.config.AppFileProperties;
import com.clovers1111.pdfsortspring.file.FileTypes;
import com.clovers1111.pdfsortspring.file.StorageFacade;
import com.clovers1111.pdfsortspring.job.JobConfig;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PdfStorageServiceFacade implements StorageFacade {

    private static final Logger logger = LoggerFactory.getLogger(PdfStorageServiceFacade.class);

    private final PdfRendererService pdfRendererService;

    private final PdfStorageService pdfStorageService;

    private final AppFileProperties appFileProperties;

    @Override
    public void processIntoImageFiles(final JobConfig jobConfig) throws IOException {
        try (PDDocument pdDocument = pdfRendererService.fileToPdDocument(jobConfig.getJobConfigDocumentFile())) {
            final Integer targetDpi = appFileProperties.defaultDpi(); // change later to incorporate job config resolution for dpi
            // TODO: Let user select image type
            final FileTypes typeOfImage = FileTypes.PNG; //jobConfigFileService.getJobConfigFileType(jobConfig);

            logger.debug("Delegating PDF to image files for job {} to {}", jobConfig.getJobId(), jobConfig.getJobDir());
            pdfStorageService.savePdfAsImageFiles(pdDocument, targetDpi, jobConfig.getJobDir(), typeOfImage);
        }
    }

    @Override
    public void saveFile(JobConfig jobConfig) {

    }
}
