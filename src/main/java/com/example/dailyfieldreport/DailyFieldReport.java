package com.example.dailyfieldreport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

// PDFBox and POI imports (require dependencies in pom.xml). If your IDE hasn't downloaded
// the dependencies yet, re-import the Maven project so these resolve.
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class DailyFieldReport {

    // Form components (so we can read their data later)
    private JTextField projectNameField = new JTextField();
    private JTextField projectNoField = new JTextField();
    private JTextField locationField = new JTextField();
    private JTextField dateField = new JTextField(LocalDate.now().toString());
    private JTextField weatherField = new JTextField();
    private JTextField temperatureField = new JTextField();
    private JTextField windField = new JTextField();

    private JTable personnelTable;
    private JTable equipmentTable;
    private JTextArea workPerformedArea;
    private JTable materialsTable;
    private JTable inspectionsTable;
    private JCheckBox safetyMeetingCheck = new JCheckBox("Safety Meeting Held");
    private JTextField safetyTopicField = new JTextField();
    private JTextField safetyAttendeesField = new JTextField();
    private JCheckBox safetyIncidentCheck = new JCheckBox("Incidents / Near Misses");
    private JTable delaysTable;
    private JTextField visitorsField = new JTextField();
    private JTextField meetingsField = new JTextField();
    private JTextField directionsField = new JTextField();
    private JCheckBox photosCheck = new JCheckBox("Photos attached");
    private JCheckBox testsCheck = new JCheckBox("Test results attached");
    private JCheckBox drawingsCheck = new JCheckBox("Drawings / Sketches attached");

    // add logger
    private static final Logger LOGGER = Logger.getLogger(DailyFieldReport.class.getName());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DailyFieldReport().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Daily Field Report");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("1. General Info", createGeneralInfoPanel());
        tabbedPane.add("2. Personnel", createPersonnelPanel());
        tabbedPane.add("3. Equipment", createEquipmentPanel());
        tabbedPane.add("4. Work Performed", createWorkPerformedPanel());
        tabbedPane.add("5. Materials", createMaterialsPanel());
        tabbedPane.add("6. Inspections", createInspectionsPanel());
        tabbedPane.add("7. Safety", createSafetyPanel());
        tabbedPane.add("8. Delays/Issues", createDelaysPanel());
        tabbedPane.add("9. Coordination", createCoordinationPanel());
        tabbedPane.add("10. Attachments", createAttachmentsPanel());

        JButton saveButton = new JButton("Save Report");
        saveButton.addActionListener(e -> saveReport());

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(saveButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private JPanel createGeneralInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.add(new JLabel("Project Name:"));
        panel.add(projectNameField);
        panel.add(new JLabel("Project No.:"));
        panel.add(projectNoField);
        panel.add(new JLabel("Location:"));
        panel.add(locationField);
        panel.add(new JLabel("Date:"));
        panel.add(dateField);
        panel.add(new JLabel("Weather AM / PM:"));
        panel.add(weatherField);
        panel.add(new JLabel("Temperature AM / PM:"));
        panel.add(temperatureField);
        panel.add(new JLabel("Wind:"));
        panel.add(windField);
        return panel;
    }

    private JPanel createPersonnelPanel() {
        String[] columns = {"Company", "Trade / Role", "No. of Workers", "Hours Worked", "Foreman / Supervisor"};
        personnelTable = new JTable(new DefaultTableModel(columns, 5));
        return wrapTableWithLabel(personnelTable);
    }

    private JPanel createEquipmentPanel() {
        String[] columns = {"Equipment", "Type / Size", "Quantity", "Operating (Y/N)", "Idle Reason"};
        equipmentTable = new JTable(new DefaultTableModel(columns, 5));
        return wrapTableWithLabel(equipmentTable);
    }

    private JPanel createWorkPerformedPanel() {
        workPerformedArea = new JTextArea(10, 70);
        return wrapTextAreaWithLabel(workPerformedArea, "Describe work activities, including locations, quantities, and methods used:");
    }

    private JPanel createMaterialsPanel() {
        String[] columns = {"Material", "Supplier", "Quantity", "Location Used / Stored", "Inspection Status"};
        materialsTable = new JTable(new DefaultTableModel(columns, 5));
        return wrapTableWithLabel(materialsTable);
    }

    private JPanel createInspectionsPanel() {
        String[] columns = {"Inspection / Test", "Inspector / Agency", "Result", "Remarks"};
        inspectionsTable = new JTable(new DefaultTableModel(columns, 5));
        return wrapTableWithLabel(inspectionsTable);
    }

    private JPanel createSafetyPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));
        panel.add(safetyMeetingCheck);
        panel.add(new JLabel("Topic:"));
        panel.add(safetyTopicField);
        panel.add(new JLabel("Attendees:"));
        panel.add(safetyAttendeesField);
        panel.add(safetyIncidentCheck);
        return panel;
    }

    private JPanel createDelaysPanel() {
        String[] columns = {"Description", "Impact (Schedule / Cost / Safety)", "Corrective Action / Remarks"};
        delaysTable = new JTable(new DefaultTableModel(columns, 5));
        return wrapTableWithLabel(delaysTable);
    }

    private JPanel createCoordinationPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));
        panel.add(new JLabel("Visitors (Owner, CM, Inspector, Others):"));
        panel.add(visitorsField);
        panel.add(new JLabel("Meetings / Coordination Notes:"));
        panel.add(meetingsField);
        panel.add(new JLabel("Directions / Instructions Received:"));
        panel.add(directionsField);
        return panel;
    }

    private JPanel createAttachmentsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(photosCheck);
        panel.add(testsCheck);
        panel.add(drawingsCheck);
        return panel;
    }

    private JPanel wrapTableWithLabel(JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel wrapTextAreaWithLabel(JTextArea area, String label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }

    private void saveReport() {
        try {
            String[] options = {"PDF (.pdf)", "Word (.docx)", "Plain Text (.txt)", "Cancel"};
            int choice = JOptionPane.showOptionDialog(null, "Choose an export format:", "Save Report",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice < 0 || choice == 3) return; // Cancel

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Report");

            String suggestedName = "DailyFieldReport";
            if (choice == 0) suggestedName += ".pdf";
            else if (choice == 1) suggestedName += ".docx";
            else suggestedName += ".txt";

            chooser.setSelectedFile(new java.io.File(suggestedName));
            int result = chooser.showSaveDialog(null);
            if (result != JFileChooser.APPROVE_OPTION) return;

            String chosen = chooser.getSelectedFile().getAbsolutePath();
            String reportContent = collectFormData();

            try {
                if (choice == 0) { // PDF
                    if (!chosen.toLowerCase().endsWith(".pdf")) chosen += ".pdf";
                    saveAsPDF(chosen, reportContent);
                } else if (choice == 1) { // Word
                    if (!chosen.toLowerCase().endsWith(".docx")) chosen += ".docx";
                    saveAsWord(chosen, reportContent);
                } else { // Plain text
                    if (!chosen.toLowerCase().endsWith(".txt")) chosen += ".txt";
                    Files.write(Paths.get(chosen), reportContent.getBytes(StandardCharsets.UTF_8));
                }

                int openNow = JOptionPane.showConfirmDialog(null, "Saved report to:\n" + chosen + "\n\nOpen it now?", "Report Saved", JOptionPane.YES_NO_OPTION);
                if (openNow == JOptionPane.YES_OPTION) {
                    openFile(chosen);
                } else {
                    JOptionPane.showMessageDialog(null, "Report saved: " + chosen);
                }
            } catch (NoClassDefFoundError e) {
                // Handle missing library for PDF/Word export
                String msg = "Export failed because required libraries are missing.\n\n" +
                        "To enable full PDF/Word export, re-import the Maven project so PDFBox and Apache POI dependencies are downloaded.";
                JOptionPane.showMessageDialog(null, msg);
                LOGGER.log(Level.SEVERE, "Export failed - missing libraries", e);

                // Fallback: write report as plain text
                try {
                    Files.write(Paths.get(chosen + ".fallback.txt"), reportContent.getBytes(StandardCharsets.UTF_8));
                    JOptionPane.showMessageDialog(null, "A plain-text fallback was saved to:\n" + chosen + ".fallback.txt");
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Error saving plain-text fallback: " + ioException.getMessage());
                    LOGGER.log(Level.SEVERE, "Error saving plain-text fallback", ioException);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "IO error saving report: " + e.getMessage());
                LOGGER.log(Level.SEVERE, "IO error saving report", e);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error saving report: " + e.getMessage());
                LOGGER.log(Level.SEVERE, "Unexpected error saving report", e);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error saving report", ex);
            JOptionPane.showMessageDialog(null, "Error saving report: " + ex.getMessage());
        }
    }

    private String collectFormData() {
        StringBuilder sb = new StringBuilder();

        sb.append("DAILY FIELD REPORT\n\n");
        sb.append("Project Name: ").append(projectNameField.getText()).append("\n");
        sb.append("Project No.: ").append(projectNoField.getText()).append("\n");
        sb.append("Location: ").append(locationField.getText()).append("\n");
        sb.append("Date: ").append(dateField.getText()).append("\n");
        sb.append("Weather: ").append(weatherField.getText()).append("\n");
        sb.append("Temperature: ").append(temperatureField.getText()).append("\n");
        sb.append("Wind: ").append(windField.getText()).append("\n\n");

        sb.append("WORK PERFORMED:\n").append(workPerformedArea.getText()).append("\n\n");

        sb.append("SAFETY:\n");
        sb.append("Meeting Held: ").append(safetyMeetingCheck.isSelected() ? "Yes" : "No").append("\n");
        sb.append("Topic: ").append(safetyTopicField.getText()).append("\n");
        sb.append("Attendees: ").append(safetyAttendeesField.getText()).append("\n");
        sb.append("Incidents/Near Misses: ").append(safetyIncidentCheck.isSelected() ? "Yes" : "No").append("\n\n");

        sb.append("COORDINATION:\n");
        sb.append("Visitors: ").append(visitorsField.getText()).append("\n");
        sb.append("Meetings: ").append(meetingsField.getText()).append("\n");
        sb.append("Directions: ").append(directionsField.getText()).append("\n\n");

        sb.append("ATTACHMENTS:\n");
        if (photosCheck.isSelected()) sb.append(" - Photos attached\n");
        if (testsCheck.isSelected()) sb.append(" - Test results attached\n");
        if (drawingsCheck.isSelected()) sb.append(" - Drawings/Sketches attached\n");

        return sb.toString();
    }

    private void saveAsPDF(String filePath, String content) throws Exception {
        // Create a simple PDF with left-aligned text. For multi-page text we'll add pages as needed.
        try (PDDocument document = new PDDocument()) {
            float margin = 50;
            float fontSize = 12;
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            // Pick a standard Type1 font via reflection so the code works across PDFBox versions
            PDFont font = null;
            String[] candidateFontNames = {"HELVETICA", "TIMES_ROMAN", "COURIER", "TIMES_BOLD", "HELVETICA_BOLD"};
            try {
                Class<?> pdType1Class = Class.forName("org.apache.pdfbox.pdmodel.font.PDType1Font");
                for (String fname : candidateFontNames) {
                    try {
                        java.lang.reflect.Field f = pdType1Class.getField(fname);
                        Object val = f.get(null);
                        if (val instanceof PDFont) { font = (PDFont) val; break; }
                    } catch (Throwable t) {
                        // ignore
                    }
                }
                if (font == null) {
                    for (java.lang.reflect.Field f : pdType1Class.getFields()) {
                        Object val = f.get(null);
                        if (val instanceof PDFont) { font = (PDFont) val; break; }
                    }
                }
            } catch (Throwable t) {
                // PDFBox not available or class layout unexpected
            }
            if (font == null) throw new IllegalStateException("No standard PDType1Font available on classpath");
            float leading = 1.2f * fontSize;

            PDPageContentStream cs = new PDPageContentStream(document, page);
            cs.beginText();
            cs.setFont(font, fontSize);
            cs.setLeading(leading);
            cs.newLineAtOffset(margin, page.getMediaBox().getHeight() - margin);

            float maxWidth = page.getMediaBox().getWidth() - 2 * margin;

            for (String paragraph : content.split("\n")) {
                // simple wrap by characters as fallback
                String line = "";
                for (String word : paragraph.split("\\s+")) {
                    String candidate = line.isEmpty() ? word : line + " " + word;
                    float textWidth;
                    try {
                        textWidth = (font.getStringWidth(candidate) / 1000f) * fontSize;
                    } catch (Throwable t) {
                        // fallback to character-count approximation if font metrics unavailable
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
            cs.close();
            document.save(filePath);
        } catch (Exception e) {
            // Log and show detailed error
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();

            String msg = "Error saving as PDF: " + e.getMessage() + "\n\nStack Trace:\n" + stackTrace;
            JOptionPane.showMessageDialog(null, msg, "PDF Save Error", JOptionPane.ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, "Error saving as PDF", e);
            throw e; // rethrow after logging
        }
    }

    private void saveAsWord(String filePath, String content) throws Exception {
        try (XWPFDocument doc = new XWPFDocument()) {
            for (String paragraphText : content.split("\n\n")) {
                XWPFParagraph p = doc.createParagraph();
                XWPFRun run = p.createRun();
                run.setFontFamily("Calibri");
                run.setFontSize(11);
                // write lines inside paragraph preserving single line breaks
                String[] lines = paragraphText.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    run.setText(lines[i], i);
                }
            }
            try (java.io.FileOutputStream out = new java.io.FileOutputStream(filePath)) {
                doc.write(out);
            }
        } catch (Exception e) {
            // Log and show detailed error
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();

            String msg = "Error saving as Word document: " + e.getMessage() + "\n\nStack Trace:\n" + stackTrace;
            JOptionPane.showMessageDialog(null, msg, "Word Save Error", JOptionPane.ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, "Error saving as Word document", e);
            throw e; // rethrow after logging
        }
    }

    // Try to open a file using the desktop integration when available, otherwise fall back to
    // platform-specific commands (Windows cmd start, macOS open, Linux xdg-open).
    private void openFile(String path) {
        try {
            if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)) {
                java.awt.Desktop.getDesktop().open(new java.io.File(path));
                return;
            }
        } catch (Throwable ignored) {
            // try platform fallback
        }

        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                // cmd /c start "" "path"
                new ProcessBuilder("cmd", "/c", "start", "", path).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", path).start();
            } else {
                new ProcessBuilder("xdg-open", path).start();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Saved but couldn't open file automatically: " + e.getMessage());
            LOGGER.log(Level.WARNING, "Error opening file automatically", e);
        }
    }
}
