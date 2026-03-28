package com.clovers1111.pdfsortspring.image;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
@Deprecated
public class ImageEditingServiceImpl implements ImageEditingService {
    public void laplaceTransform(BufferedImage bim, int[][] kernel) {
        // Iterate over all the necessary pixels in our image
        for (int y = 1; y < bim.getHeight() - 1; y++) {
            for (int x = 1; x < bim.getWidth() - 1; x++) {

                int sum = 0;

                // Apply operation on a given pixel with the kernel values
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++){

                        // The current value of the one of the pixels with respect to the kernel matrix values
                        int rgb = bim.getRGB(x + kx, y + ky);
                        int grey = (rgb >> 16) & 0xff;

                        // The
                        sum += grey * kernel[ky+1][kx+1];


                    }
                }
                sum = Math.abs(sum);
                sum = Math.min(255, Math.max(0, sum));


                int newPixel = new Color(sum, sum, sum).getRGB();
                bim.setRGB(x, y, newPixel);

            }
        }
    }

    private void resizeImage(BufferedImage bimProcessee) {
        Graphics2D graphics2D = bimProcessee.createGraphics();

        // Set rendering hints for quality
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2D.drawImage(bimProcessee, 0, 0,
                bimProcessee.getWidth(), bimProcessee.getHeight(),
                null);
        graphics2D.dispose();
    }
}
