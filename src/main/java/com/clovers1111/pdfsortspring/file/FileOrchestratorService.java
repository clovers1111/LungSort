package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.pdf.PdfStorageServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileOrchestratorService {

    private static final Logger logger = LoggerFactory.getLogger(FileOrchestratorService.class);

    private final PdfStorageServiceFacade pdfStorageServiceFacade;

    FileOrchestratorService(PdfStorageServiceFacade pdfStorageServiceFacade) {
        this.pdfStorageServiceFacade = pdfStorageServiceFacade;
    }

    public static final Pattern FILE_EXTENSION = Pattern.compile("\\.([^.]+)$");

    public void processFileIntoImages(JobConfig jobConfig) throws IOException {
        final FileTypes fileType = getFileTypeFromFileName(jobConfig.getFileNameWithExtension());

        switch(fileType) {
            case PDF -> pdfStorageServiceFacade.processIntoImageFiles(jobConfig);
        }
    }


    private static FileTypes getFileTypeFromFileName(String file) {
        final Matcher fileExtensionMatcher = FILE_EXTENSION.matcher(file);
            if (fileExtensionMatcher.find()) {
                return FileTypes.fromExtension(fileExtensionMatcher.group(1)).get();
            }
            throw new NoSuchElementException(
                    String.format("No supported file type found for %s", file));
    }


}
