package com.clovers1111.pdfsortspring.pdf;

import com.clovers1111.pdfsortspring.config.AppFileProperties;
import com.clovers1111.pdfsortspring.file.FileTypes;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class PdfStorageServiceImpl implements PdfStorageService {

    private static final Logger logger = LoggerFactory.getLogger(PdfStorageServiceImpl.class);

    //TODO: Figure out if you should make static or just refactor this class into the respective domains
    private final PdfRendererService pdfRendererService;

    private final AppFileProperties appFileProperties;

    @Override
    public void savePdfFile(final MultipartFile file, final Path targetPathWithFile) throws IOException {
        logger.debug("Saving PDF file to {}", targetPathWithFile);
        try (PDDocument document = pdfRendererService.multipartFileToPDDocument(file)) {
            document.save(targetPathWithFile.toFile());
        }
        logger.info("PDF file saved: {}", targetPathWithFile);
    }

    @Override
    public void savePdfAsImageFiles(final PDDocument pdDocument, final Integer dpi, final Path outputDir, final FileTypes type) throws IOException {
        final List<BufferedImage> bimList = pdfRendererService.pdDocumentToBimList(pdDocument, dpi);
        logger.info("Rendering {} pages at {} DPI to {}", bimList.size(), dpi, outputDir);


        for (int i = 0; i < bimList.size(); i++) {
            final String fileName = seqFileNameGenerator(i, type);
            final Path output = outputDir.resolve(fileName);

            try {
                ImageIO.write(bimList.get(i), FileTypes.PNG.getExtensionWithoutDot(), output.toFile());
                logger.debug("Wrote page {} to {}", i, output);
            } catch (IOException e) {
                logger.error("Failed to write page {} to {}", i, output, e);
                throw new RuntimeException(e);
            }

        }
    }

    public String seqFileNameGenerator(int sequenceNum, FileTypes fileType) {
        return appFileProperties.imagePrefix() + sequenceNum + fileType.getExtension();
    }
}

