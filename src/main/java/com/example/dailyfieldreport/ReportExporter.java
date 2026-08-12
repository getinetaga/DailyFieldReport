package com.example.dailyfieldreport;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class ReportExporter {
    private static final Logger LOGGER = Logger.getLogger(ReportExporter.class.getName());
    private static final Color CLOUDY_BLUE = new Color(0xDCE9F7);
    private static final Color TITLE_TEXT = new Color(0x1F3B57);
    private static final float PAGE_MARGIN = 50f;
    private static final float PHOTO_MARGIN = 36f;
    private static final float PHOTO_GAP = 14f;
    private static final float PHOTO_CAPTION_HEIGHT = 52f;

    public static void saveTextAsPDF(String content, List<Path> photoPaths, String filePath) throws Exception {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDFont fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            float fontSize = 12f;
            float leading = 1.2f * fontSize;

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                cs.beginText();
                cs.setFont(font, fontSize);
                cs.setLeading(leading);
                cs.newLineAtOffset(PAGE_MARGIN, page.getMediaBox().getHeight() - PAGE_MARGIN);

                float maxWidth = page.getMediaBox().getWidth() - (2 * PAGE_MARGIN);
                for (String paragraph : content.split("\n")) {
                    String line = "";
                    boolean boldLine = paragraph.startsWith("Total Hours:");
                    for (String word : paragraph.split("\\s+")) {
                        String candidate = line.isEmpty() ? word : line + " " + word;
                        float textWidth = measureWidth(font, candidate, fontSize);
                        if (textWidth <= maxWidth) {
                            line = candidate;
                        } else {
                            if (!line.isEmpty()) {
                                cs.showText(line);
                                cs.newLine();
                            }
                            line = word;
                        }
                    }
                    if (!line.isEmpty()) {
                        cs.setFont(boldLine ? fontBold : font, fontSize);
                        cs.showText(line);
                        cs.newLine();
                    }
                    cs.setFont(font, fontSize);
                }
                cs.endText();
            }

            appendPhotosToPdf(document, photoPaths);
            document.save(filePath);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.log(Level.SEVERE, "Error saving PDF: " + e.getMessage() + "\n" + sw, e);
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

            org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject pdImage =
                    org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject.createFromByteArray(document, bytes, "screenshot");

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

    public static void saveTextAsWord(String content, List<Path> photos, String filePath) throws Exception {
        try (XWPFDocument doc = new XWPFDocument()) {
            for (String paragraphText : content.split("\n\n")) {
                XWPFParagraph p = doc.createParagraph();
                XWPFRun run = p.createRun();
                run.setFontFamily("Calibri");
                run.setFontSize(11);
                String[] lines = paragraphText.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].startsWith("Total Hours:")) {
                        run.setBold(true);
                    }
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
                        XWPFParagraph picPara = doc.createParagraph();
                        XWPFRun picRun = picPara.createRun();
                        try (java.io.FileInputStream in = new java.io.FileInputStream(f)) {
                            picRun.addPicture(
                                    in,
                                    org.apache.poi.xwpf.usermodel.Document.PICTURE_TYPE_PNG,
                                    f.getName(),
                                    org.apache.poi.util.Units.toEMU(img.getWidth()),
                                    org.apache.poi.util.Units.toEMU(img.getHeight()));
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

    public static void saveSectionsAsPDF(Map<String, String> sections, List<Path> photoPaths, String filePath) throws Exception {
        try (PDDocument document = new PDDocument()) {
            PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDFont fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            float bodyFontSize = 12f;
            float leading = 1.2f * bodyFontSize;

            for (Map.Entry<String, String> entry : sections.entrySet()) {
                PDPage page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);

                try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                    drawSectionTitle(cs, page, font, entry.getKey());
                    cs.beginText();
                    cs.setFont(font, bodyFontSize);
                    cs.setLeading(leading);
                    cs.newLineAtOffset(PAGE_MARGIN, page.getMediaBox().getHeight() - PAGE_MARGIN - 34f);

                    float maxWidth = page.getMediaBox().getWidth() - (2 * PAGE_MARGIN);
                    String content = entry.getValue() == null ? "" : entry.getValue();
                    for (String paragraph : content.split("\n")) {
                        String line = "";
                        boolean boldLine = paragraph.startsWith("Total Hours:");
                        for (String word : paragraph.split("\\s+")) {
                            String candidate = line.isEmpty() ? word : line + " " + word;
                            float textWidth = measureWidth(font, candidate, bodyFontSize);
                            if (textWidth <= maxWidth) {
                                line = candidate;
                            } else {
                                if (!line.isEmpty()) {
                                    cs.showText(line);
                                    cs.newLine();
                                }
                                line = word;
                            }
                        }
                        if (!line.isEmpty()) {
                            cs.setFont(boldLine ? fontBold : font, bodyFontSize);
                            cs.showText(line);
                            cs.newLine();
                        }
                        cs.setFont(font, bodyFontSize);
                    }
                    cs.endText();
                }
            }

            appendPhotosToPdf(document, photoPaths);
            document.save(filePath);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.log(Level.SEVERE, "Error saving sections PDF: " + e.getMessage() + "\n" + sw, e);
            throw e;
        }
    }

    private static void appendPhotosToPdf(PDDocument document, List<Path> photoPaths) {
        if (photoPaths == null || photoPaths.isEmpty()) return;

        float pageW = PDRectangle.LETTER.getWidth();
        float pageH = PDRectangle.LETTER.getHeight();
        float usableW = pageW - (2 * PHOTO_MARGIN) - PHOTO_GAP;
        float cellW = usableW / 2f;
        float cellH = 300f;
        float imageAreaH = cellH - PHOTO_CAPTION_HEIGHT;
        int cellsPerPage = 4;

        int cellIndex = 0;
        PDPage page = null;
        PDPageContentStream cs = null;
        try {
            for (Path p : photoPaths) {
                if (cellIndex % cellsPerPage == 0) {
                    if (cs != null) {
                        cs.close();
                    }
                    page = new PDPage(PDRectangle.LETTER);
                    document.addPage(page);
                    cs = new PDPageContentStream(document, page);
                }

                File f = p.toFile();
                if (!f.exists()) {
                    cellIndex++;
                    continue;
                }

                int slot = cellIndex % cellsPerPage;
                int row = slot / 2;
                int col = slot % 2;

                org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject pdImg =
                        org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject.createFromFileByContent(f, document);
                float x = PHOTO_MARGIN + col * (cellW + PHOTO_GAP);
                float top = pageH - PHOTO_MARGIN - row * (cellH + PHOTO_GAP);
                float bottom = top - cellH;

                float imgW = pdImg.getWidth();
                float imgH = pdImg.getHeight();
                float scale = Math.min(cellW / imgW, imageAreaH / imgH);
                float drawW = imgW * scale;
                float drawH = imgH * scale;
                float imgX = x + (cellW - drawW) / 2f;
                float imgY = bottom + PHOTO_CAPTION_HEIGHT + (imageAreaH - drawH) / 2f;

                cs.setStrokingColor(new Color(0xC7D8E8));
                cs.addRect(x, bottom, cellW, cellH);
                cs.stroke();
                cs.drawImage(pdImg, imgX, imgY, drawW, drawH);

                cs.moveTo(x + 8, bottom + PHOTO_CAPTION_HEIGHT);
                cs.lineTo(x + cellW - 8, bottom + PHOTO_CAPTION_HEIGHT);
                cs.stroke();

                cs.beginText();
                cs.setNonStrokingColor(TITLE_TEXT);
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9f);
                cs.newLineAtOffset(x + 10, bottom + 34);
                cs.showText("Description:");
                cs.endText();

                cs.beginText();
                cs.setNonStrokingColor(new Color(0x667788));
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8f);
                cs.newLineAtOffset(x + 10, bottom + 18);
                cs.showText(p.getFileName().toString());
                cs.endText();

                cellIndex++;
            }
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "Failed to append photo grid to PDF", ioe);
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private static void drawSectionTitle(PDPageContentStream cs, PDPage page, PDFont font, String title) throws IOException {
        float x = PAGE_MARGIN;
        float y = page.getMediaBox().getHeight() - PAGE_MARGIN;
        float width = page.getMediaBox().getWidth() - (2 * PAGE_MARGIN);
        float height = 24f;

        cs.setNonStrokingColor(CLOUDY_BLUE);
        cs.addRect(x, y - height + 4f, width, height);
        cs.fill();

        cs.setNonStrokingColor(TITLE_TEXT);
        cs.beginText();
        cs.setFont(font, 14f);
        cs.newLineAtOffset(x + 10, y - 12);
        cs.showText(title);
        cs.endText();
    }

    private static float measureWidth(PDFont font, String text, float fontSize) {
        try {
            return (font.getStringWidth(text) / 1000f) * fontSize;
        } catch (Throwable t) {
            return text.length() * fontSize * 0.5f;
        }
    }
}
