package com.clovers1111.pdfsortspring.pdf;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfRendererServiceImpl implements PdfRendererService {

    private static final Logger logger = LoggerFactory.getLogger(PdfRendererServiceImpl.class);

    public Object renderPdfPageAtIndex(PDDocument pdDocument, Integer index) {
        if (pdDocument.getNumberOfPages() < index - 1)
            logger.warn("Rendered PDF does not contain {}", index);
        return null;
    }

    public PDDocument multipartFileToPDDocument(MultipartFile multipartFile) throws IOException {
        return Loader.loadPDF(multipartFile.getBytes());
    }

    public List<BufferedImage> pdDocumentToBimList(PDDocument document, Integer dpi) throws IOException {
        final PDFRenderer pdfRenderer = new PDFRenderer(document);
        List<BufferedImage> bimList = new ArrayList<>();

        for (int i = 0; i < document.getNumberOfPages(); i++) {
            bimList.add(pdfRenderer.renderImageWithDPI(i, dpi));
        }
        return bimList;
    }

    @Override
    public PDDocument fileToPdDocument(Path path) throws IOException {
        return Loader.loadPDF(path.toFile());
    }


}

