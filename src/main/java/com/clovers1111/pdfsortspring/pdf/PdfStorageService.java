package com.clovers1111.pdfsortspring.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface PdfStorageService {

    void savePdfFile(MultipartFile file, Path targetPathWithFile) throws IOException;

    void savePdfAsImageFiles(PDDocument pdDocument, Integer dpi, Path outputDir) throws IOException;
}
