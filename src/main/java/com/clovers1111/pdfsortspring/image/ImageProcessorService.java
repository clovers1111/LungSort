package com.clovers1111.pdfsortspring.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class ImageProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(ImageProcessorService.class);


    public static BufferedImage fileToBim(Path path) throws IOException {
        return ImageIO.read(path.toFile());
    }


    public static byte[] fileToByteArray(Path path) throws IOException {
        return Files.readAllBytes(path);
    }


}
