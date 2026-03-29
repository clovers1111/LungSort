package com.clovers1111.pdfsortspring.file.utility;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


@Service
public class FileConversionService {

    private static final Logger logger = LoggerFactory.getLogger(FileConversionService.class);

    public static List<BufferedImage> pdDocumentToBimList(PDDocument pageToBeRendered, Integer dpi) throws IOException {
        final PDFRenderer pdfRenderer = new PDFRenderer(pageToBeRendered);
        List<BufferedImage> bimList = new ArrayList<>();

        for (int i = 0; i < pageToBeRendered.getNumberOfPages(); i++) {
            bimList.add(pdfRenderer.renderImageWithDPI(i, dpi));
        }
        return bimList;
    }


    public static BufferedImage fileToBim(Path path) throws IOException {
        return ImageIO.read(path.toFile());
    }


    public static PDDocument multipartFileToPDDocument(MultipartFile multipartFile) throws IOException {
        return Loader.loadPDF(multipartFile.getBytes());
    }


    public static byte[] fileToByteArray(Path path) throws IOException {
        return Files.readAllBytes(path);
    }


}
