package com.clovers1111.pdfsortspring.pdf;

import com.clovers1111.pdfsortspring.file.FileConversionService;
import com.clovers1111.pdfsortspring.file.FileTypes;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class PdfStorageServiceImpl implements PdfStorageService {

    private static final Logger logger = LoggerFactory.getLogger(PdfStorageServiceImpl.class);
    private static final String IMAGE_PREFIX = "temp";

    private final FileConversionService fileConversionService;

    public PdfStorageServiceImpl(FileConversionService fileConversionService) {
        this.fileConversionService = fileConversionService;
    }

    @Override
    public void savePdfFile(final MultipartFile file, final Path targetPathWithFile) throws IOException {
        logger.debug("Saving PDF file to {}", targetPathWithFile);
        try (PDDocument document = fileConversionService.multipartFileToPDDocument(file)) {
            document.save(targetPathWithFile.toFile());
        }
        logger.info("PDF file saved: {}", targetPathWithFile);
    }

    @Override
    public void savePdfAsImageFiles(final PDDocument pdDocument, final Integer dpi, final Path outputDir) throws IOException {
        final List<BufferedImage> bimList = fileConversionService.pdDocumentToBimList(pdDocument, dpi);
        logger.info("Rendering {} pages at {} DPI to {}", bimList.size(), dpi, outputDir);

        IntStream.range(0, bimList.size())
                .forEach(i -> {
                    final Path output = outputDir.resolve(IMAGE_PREFIX + i + FileTypes.PNG.getExtension());
                    try {
                        ImageIO.write(bimList.get(i), FileTypes.PNG.getExtension(), output.toFile());
                        logger.debug("Wrote page {} to {}", i, output);
                    } catch (IOException e) {
                        logger.error("Failed to write page {} to {}", i, output, e);
                        throw new RuntimeException(e);
                    }
                });
    }
}

