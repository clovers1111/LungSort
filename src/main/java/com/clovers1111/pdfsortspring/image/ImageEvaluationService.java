package com.clovers1111.pdfsortspring.image;


import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.SortedMap;

public interface ImageEvaluationService {
    /**
     * Uses Laplace transformation on an image to derive sharpness at different DPIs
     *
     * <p>Using metadata for image files is unreliable since people may mistakenly
     * save their PDFs at DPI incoherent with the actual DPI, thereby decreasing
     * performance without any upside. This function intends to evaluate the DPI
     * of a document by taking a random page from the PDF and rendering a small
     * section of it at different DPIs with the same resolution. When the Laplacian
     * variance is low, there is a high amount of distortion and vice versa. This
     * is ran BEFORE rendering the entire PDF to avoid the aforementioned
     * performance issues.</p>
     *
     * @param dpiRenderedToImage a wrapped PDF file derived from {@link FileUploadController#uploadFile}.
     * @return Integer
     */
    public Integer dpiEvaluation(PDDocument pdDocument);

    public SortedMap<Integer, BufferedImage> mapRenderedDpiToBim(PDDocument pdDocument);

    /**
     *
     * Calculates Laplace variance to determine best DPI for image.
     *
     * @param bimProcessee
     * @return
     */
    public Double laplaceVariance(BufferedImage bimProcessee);
}
