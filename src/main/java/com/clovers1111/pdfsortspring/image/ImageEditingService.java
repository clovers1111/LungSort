package com.clovers1111.pdfsortspring.image;


import java.awt.image.BufferedImage;

public interface ImageEditingService {
    public void laplaceTransform(BufferedImage bim, int[][] kernel);

}
