package com.example.dailyfieldreport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.poi.xwpf.usermodel.*;

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
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Report");
            chooser.setSelectedFile(new java.io.File("DailyFieldReport"));
            int result = chooser.showSaveDialog(null);
            if (result != JFileChooser.APPROVE_OPTION) return;

            String basePath = chooser.getSelectedFile().getAbsolutePath();
            String reportContent = collectFormData();

            saveAsPDF(basePath + ".pdf", reportContent);
            saveAsWord(basePath + ".docx", reportContent);

            JOptionPane.showMessageDialog(null, "Report saved successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
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
        float margin = 50;
        float fontSize = 12;
        // use a Standard 14 font that exists across PDFBox versions
        PDType1Font font = PDType1Font.TIMES_ROMAN;
        float leading = 14.5f;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            float startY = page.getMediaBox().getHeight() - margin;
            float width = page.getMediaBox().getWidth() - 2 * margin;

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                cs.setFont(font, fontSize);
                cs.beginText();
                cs.setLeading(leading);
                cs.newLineAtOffset(margin, startY);

                for (String paragraph : content.split("\n")) {
                    List<String> lines = wrapText(paragraph, font, fontSize, width);
                    for (String line : lines) {
                        cs.showText(line);
                        cs.newLine();
                    }
                }

                cs.endText();
            }

            document.save(filePath);
        }
    }

    private List<String> wrapText(String text, PDType1Font font, float fontSize, float maxWidth) throws java.io.IOException {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }

        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            String candidate = line.length() == 0 ? word : line + " " + word;
            float textWidth = (font.getStringWidth(candidate) / 1000f) * fontSize;
            if (textWidth <= maxWidth) {
                if (line.length() == 0) line.append(word); else { line.append(' ').append(word); }
            } else {
                if (line.length() > 0) {
                    lines.add(line.toString());
                    line.setLength(0);
                }
                if ((font.getStringWidth(word) / 1000f) * fontSize > maxWidth) {
                    int approxChars = Math.max(1, (int)(maxWidth / (fontSize * 0.5f)));
                    for (int i = 0; i < word.length(); i += approxChars) {
                        int end = Math.min(word.length(), i + approxChars);
                        lines.add(word.substring(i, end));
                    }
                } else {
                    line.append(word);
                }
            }
        }
        if (line.length() > 0) lines.add(line.toString());
        return lines;
    }

    private void saveAsWord(String filePath, String content) throws Exception {
        try (XWPFDocument doc = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(filePath)) {

            for (String paragraphText : content.split("\n\n")) {
                XWPFParagraph p = doc.createParagraph();
                XWPFRun run = p.createRun();
                run.setFontFamily("Calibri");
                run.setFontSize(12);
                // preserve single-line breaks inside paragraphs
                run.setText(paragraphText.replace("\n", "\n"), 0);
            }

            doc.write(out);
        }
    }
}
