package com.clovers1111.pdfsortspring.image;

import com.clovers1111.pdfsortspring.Config;
import com.clovers1111.pdfsortspring.pdf.PdfRendererService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Deprecated
public class ImageEvaluationServiceImpl implements ImageEvaluationService {

    private static final Logger logger = LoggerFactory.getLogger(ImageEvaluationService.class);

    private final ImageEditingService imageEditingService;

    private final PdfRendererService pdfRendererService;


    private final Integer MARGIN_OF_ERROR_AS_PERCENTAGE = 95;

    // TODO: Remove coupled kernel, if we're even going with this implementation
    private final int[][] LAPLACE_KERNEL = {
            {0, -1, 0},
            {-1, 4,-1},
            {0, -1, 0}
    };

    private final int SAMPLE_WIDTH = 100;

    private final int SAMPLE_HEIGHT = 100;

    private final List<Integer> dpis = Config.getIntListProperty("config.dpi-list", ",");


    public ImageEvaluationServiceImpl(ImageEditingService imageEditingService,
                                      PdfRendererService pdfRendererService) {
        this.imageEditingService = imageEditingService;
        this.pdfRendererService = pdfRendererService;
    }

    //TODO: fix margin of error to be more standardized
    @Override
    public Integer dpiEvaluation(PDDocument pdDocument) {
        Queue<Double> variances = new LinkedList<>();
        Integer returnDPI = 0;

        // Find the DPI for which the predecessor's variance is equal to or
        // greater than it, and then return the predecessor.\

        final var mapRenderedDpiToBimEntrySet = mapRenderedDpiToBim(pdDocument).entrySet();

        for (var entry : mapRenderedDpiToBimEntrySet) {
            imageEditingService.laplaceTransform(entry.getValue(), LAPLACE_KERNEL);
            Double tempVariance = laplaceVariance(entry.getValue());

            if (!variances.isEmpty() && (variances.peek() >= tempVariance-MARGIN_OF_ERROR_AS_PERCENTAGE)) {
                System.out.println("DPI selected: " + returnDPI);
                return returnDPI;
            } else {
                variances.add(tempVariance);
                returnDPI = entry.getKey();
            }

        }
        System.out.println("DPI selected: " + returnDPI);
        return returnDPI;
    }


    public Double laplaceVariance (BufferedImage bimProcessee) {
        double sumOfLi = 0;
        int x = bimProcessee.getWidth();
        int y = bimProcessee.getHeight();

        for (int yi = 0; yi < y; yi++) {
            for (int xi = 0; xi < x; xi++) {
                sumOfLi += bimProcessee.getRGB(xi, yi);
            }
        }

        // Equation for variance
        return (sumOfLi * sumOfLi) / (x * y)
                - (sumOfLi / (x * y)) * (sumOfLi / (x * y));
    }

    @Override
    public SortedMap<Integer, BufferedImage> mapRenderedDpiToBim(PDDocument pdDocument) {
        // Grab a page in the middle of the document
        PDDocument pageToBeRendered = new PDDocument();
        final PDPage evalPage = pdDocument.getPage(
                pdDocument.getNumberOfPages() / 2);

        // Set the selection region
        // Do this before rendering the image to prevent problematic compression issues
        final PDRectangle sectionOfInterest = new PDRectangle(evalPage.getMediaBox().getWidth() / 2,
                evalPage.getMediaBox().getHeight() / 2,
                SAMPLE_WIDTH, SAMPLE_HEIGHT);

        try {
            evalPage.setCropBox(sectionOfInterest);
        } catch (Exception e) {
            logger.error("Selected page wasn't large enough to accommodate {}", STR."\{SAMPLE_WIDTH}x\{SAMPLE_HEIGHT}");
        }

        // Add the page to the document for it to be rendered later
        pageToBeRendered.addPage(evalPage);

        // Translate the dpis into a sorted map of dpis and their corresponding rendered pdDocument (pages)
        return dpis.stream().collect(
                Collectors.toMap(dpi -> dpi,
                        dpi -> {  // Render the page at the given dpi and return the resulting bim
                            try {
                                return pdfRendererService.pdDocumentToBimList(pdDocument, dpi).get(0); // As an individual object
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        (oldValue, newValue) -> oldValue, TreeMap::new));
    }
}
