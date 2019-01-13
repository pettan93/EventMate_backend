package gr.tei.erasmus.pp.eventmate.backend;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GeneratePreviewTest {

    @Test
    public void testPreview() {

        File pdf = new File("reports/out2.pdf");

        File preview = generatePreview(pdf);

    }

    public File generatePreview(File pdf) {
        File preview = new File("reports/preview.jpg");
        try (final PDDocument document = PDDocument.load(pdf)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 30, ImageType.RGB);
            ImageIOUtil.writeImage(bim, preview.getAbsolutePath(), 30);
        } catch (IOException e) {
            System.err.println("Exception while trying to create pdf document - " + e);
        }
        return preview;
    }

}