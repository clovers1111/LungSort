package com.clovers1111.pdfsortspring.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface PdfRendererService {

    public PDDocument multipartFileToPDDocument(MultipartFile multipartFile) throws IOException;

    public List<BufferedImage> pdDocumentToBimList(PDDocument document, Integer dpi) throws IOException;

    public PDDocument fileToPdDocument(Path path) throws IOException;
}
