package com.clovers1111.pdfsortspring.file;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

// I am turning this into a service for modularity
public interface FileConversionService {

    // BufferedImage pdDocumentToBim(PDDocument pdDocument, Integer dpi) throws IOException;

    List<BufferedImage> pdDocumentToBimList(PDDocument documentToBeRendered, Integer dpi) throws IOException;

    BufferedImage fileToBim(Path file) throws IOException;

    // void convertImageArrayToPdf();

    PDDocument multipartFileToPDDocument(MultipartFile multipartFile) throws IOException;

    byte[] fileToByteArray(Path file) throws IOException;

}
