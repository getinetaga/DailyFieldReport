package com.example.dailyfieldreport;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class ReportExporter {
    private static final Logger LOGGER = Logger.getLogger(ReportExporter.class.getName());

    public static void saveTextAsPDF(String content, List<Path> photoPaths, String filePath) throws Exception {
        try (PDDocument document = new PDDocument()) {
            float margin = 50;
            float fontSize = 12;
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            // attempt to locate a standard PDFBox Type1 font via reflection
            PDFont font = null;
            String[] candidateFontNames = {"HELVETICA", "TIMES_ROMAN", "COURIER", "TIMES_BOLD", "HELVETICA_BOLD"};
            try {
                Class<?> pdType1Class = Class.forName("org.apache.pdfbox.pdmodel.font.PDType1Font");
                for (String fname : candidateFontNames) {
                    try {
                        java.lang.reflect.Field f = pdType1Class.getField(fname);
                        Object val = f.get(null);
                        if (val instanceof PDFont) { font = (PDFont) val; break; }
                    } catch (Throwable t) { /* ignore */ }
                }
                if (font == null) {
                    for (java.lang.reflect.Field f : pdType1Class.getFields()) {
                        Object val = f.get(null);
                        if (val instanceof PDFont) { font = (PDFont) val; break; }
                    }
                }
            } catch (Throwable t) {
                LOGGER.log(Level.FINE, "PDType1Font not available", t);
            }
            if (font == null) throw new IllegalStateException("No standard PDType1Font available on classpath");

            float leading = 1.2f * fontSize;

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                cs.beginText();
                cs.setFont(font, fontSize);
                cs.setLeading(leading);
                cs.newLineAtOffset(margin, page.getMediaBox().getHeight() - margin);

                float maxWidth = page.getMediaBox().getWidth() - 2 * margin;

                for (String paragraph : content.split("\n")) {
                    String line = "";
                    for (String word : paragraph.split("\\s+")) {
                        String candidate = line.isEmpty() ? word : line + " " + word;
                        float textWidth;
                        try {
                            textWidth = (font.getStringWidth(candidate) / 1000f) * fontSize;
                        } catch (Throwable t) {
                            textWidth = candidate.length() * fontSize * 0.5f;
                        }
                        if (textWidth <= maxWidth) {
                            line = candidate;
                        } else {
                            cs.showText(line);
                            cs.newLine();
                            line = word;
                        }
                    }
                    if (!line.isEmpty()) {
                        cs.showText(line);
                        cs.newLine();
                    }
                }

                cs.endText();
            }

            appendPhotosToPdf(document, photoPaths);

            document.save(filePath);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.log(Level.SEVERE, "Error saving PDF: " + e.getMessage() + "\n" + sw.toString(), e);
            throw e;
        }
    }

    public static void saveImageAsPDF(BufferedImage image, List<Path> photoPaths, String filePath) throws Exception {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            byte[] bytes = baos.toByteArray();

            org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject pdImage = org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject.createFromByteArray(document, bytes, "screenshot");

            float pageW = page.getMediaBox().getWidth();
            float pageH = page.getMediaBox().getHeight();
            float margin = 40f;
            float maxW = pageW - 2 * margin;
            float maxH = pageH - 2 * margin;

            float imgW = pdImage.getWidth();
            float imgH = pdImage.getHeight();
            float scale = Math.min(maxW / imgW, maxH / imgH);
            float drawW = imgW * scale;
            float drawH = imgH * scale;

            float x = (pageW - drawW) / 2f;
            float y = (pageH - drawH) / 2f;

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                cs.drawImage(pdImage, x, y, drawW, drawH);
            }

            appendPhotosToPdf(document, photoPaths);

            document.save(filePath);
        }
    }

    private static void appendPhotosToPdf(PDDocument document, List<Path> photoPaths) {
        if (photoPaths == null) return;
        for (Path p : photoPaths) {
            try {
                File f = p.toFile();
                if (!f.exists()) continue;
                org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject pdImg = org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject.createFromFileByContent(f, document);
                PDPage page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);

                float pageW = page.getMediaBox().getWidth();
                float pageH = page.getMediaBox().getHeight();
                float margin = 40f;
                float maxW = pageW - 2 * margin;
                float maxH = pageH - 2 * margin;

                float imgW = pdImg.getWidth();
                float imgH = pdImg.getHeight();
                float scale = Math.min(maxW / imgW, maxH / imgH);
                float drawW = imgW * scale;
                float drawH = imgH * scale;

                float x = (pageW - drawW) / 2f;
                float y = (pageH - drawH) / 2f;

                try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                    cs.drawImage(pdImg, x, y, drawW, drawH);
                }
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Failed to append photo to PDF: " + p, t);
            }
        }
    }

    public static void saveTextAsWord(String content, List<Path> photos, String filePath) throws Exception {
        try (XWPFDocument doc = new XWPFDocument()) {
            for (String paragraphText : content.split("\n\n")) {
                XWPFParagraph p = doc.createParagraph();
                XWPFRun run = p.createRun();
                run.setFontFamily("Calibri");
                run.setFontSize(11);
                String[] lines = paragraphText.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    run.setText(lines[i], i);
                }
            }
            if (photos != null) {
                for (Path pth : photos) {
                    try {
                        File f = pth.toFile();
                        if (!f.exists()) continue;
                        BufferedImage img = ImageIO.read(f);
                        if (img == null) continue;
                        int width = img.getWidth();
                        int height = img.getHeight();
                        XWPFParagraph picPara = doc.createParagraph();
                        XWPFRun picRun = picPara.createRun();
                        try (java.io.FileInputStream in = new java.io.FileInputStream(f)) {
                            picRun.addPicture(in, org.apache.poi.xwpf.usermodel.Document.PICTURE_TYPE_PNG, f.getName(), org.apache.poi.util.Units.toEMU(width), org.apache.poi.util.Units.toEMU(height));
                        }
                    } catch (Throwable t) {
                        LOGGER.log(Level.WARNING, "Failed to append photo to Word doc: " + pth, t);
                    }
                }
            }

            try (java.io.FileOutputStream out = new java.io.FileOutputStream(filePath)) {
                doc.write(out);
            }
        }
    }
}

