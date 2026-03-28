package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.Config;
import com.clovers1111.pdfsortspring.job.JobConfig;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private static final Path TEMP_DIR = Config.getDirectory();

    //TODO: Let users specify DPI per image
    private static final Integer DEFAULT_DPI = Config.getIntProperty("default-dpi");

    private static final String IMAGE_PREFIX = "temp";

    final private FileConversionService fileConversionService;

    final private DirectoryWorkerService directoryWorkerService;

    public FileStorageServiceImpl(FileConversionService fileConversionService,
                                   DirectoryWorkerService directoryWorkerService) {
        this.fileConversionService = fileConversionService;
        this.directoryWorkerService = directoryWorkerService;
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

        logger.info("Saving file of type {} for job {}", fileType, jobConfig.getJobId());
        switch (fileType) {
            case PDF -> savePdfFile(multipartFile, jobConfig);
            case PNG, JPG, JPEG -> logger.warn("Image file saving not yet implemented for type: {}", fileType);
        }
    }

    @Override
    public void savePdfAsImageFiles(PDDocument pdDocument, Integer dpi) throws IOException {
        List<BufferedImage> bimList = fileConversionService.pdDocumentToBimList(pdDocument, dpi);
        logger.info("Rendering {} pages at {} DPI to {}", bimList.size(), dpi, TEMP_DIR);

        IntStream.range(0, bimList.size())
                .forEach(i -> { // TODO: Add dot
                    Path output = TEMP_DIR.resolve("temp" + i + FileTypes.PNG.getExtension());
                    try {
                        ImageIO.write(bimList.get(i), FileTypes.PNG.getExtension(), output.toFile());
                        logger.debug("Wrote page {} to {}", i, output);
                    } catch (IOException e) {
                        logger.error("Failed to write page {} to {}", i, output, e);
                        throw new RuntimeException(e);
                    }
                });
    }

    private void savePdfFile(MultipartFile file, JobConfig jobConfig) throws IOException {
        final Path targetPathWithFile = directoryWorkerService.buildPathWithFile(jobConfig);
        directoryWorkerService.createDirectory(jobConfig);

        logger.debug("Saving PDF file for job {} to {}", jobConfig.getJobId(), targetPathWithFile);
        try (PDDocument document = fileConversionService.multipartFileToPDDocument(file)) {
            document.save(targetPathWithFile.toFile());
        }
        logger.info("PDF file saved for job {}: {}", jobConfig.getJobId(), targetPathWithFile);
    }

}
