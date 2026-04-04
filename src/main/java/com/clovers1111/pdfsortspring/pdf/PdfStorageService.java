package com.clovers1111.pdfsortspring.pdf;

import com.clovers1111.pdfsortspring.file.FileTypes;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface PdfStorageService {

    void savePdfFile(MultipartFile file, Path targetPathWithFile) throws IOException;

    void savePdfAsImageFiles(final PDDocument pdDocument, final Integer dpi, final Path outputDir, final FileTypes type) throws IOException;
}
