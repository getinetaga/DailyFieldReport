package com.example.dailyfieldreport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.time.LocalDate;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.io.File;

// PDFBox and POI imports (require dependencies in pom.xml).
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class DailyFieldReport {

    // Form components (so we can read their data later) The data layer should be side by side when exporting to PDF/Word, so we need to capture the entire form as an image for export. We'll also collect the text data for fallback plain-text export.
    private final JTextField projectNameField = new JTextField();
    private JTextField projectNoField = new JTextField();
    private JTextField locationField = new JTextField();
    private JTextField dateField = new JTextField(LocalDate.now().toString());
    private JTextField weatherField = new JTextField();
    private JTextField temperatureField = new JTextField();
    private JTextField windField = new JTextField();

    // root panel that contains entire form (header + tabs). We'll capture this for export.
    private JPanel rootPanel;

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

    // photo attachments
    private final List<Path> photoPaths = new ArrayList<>();
    private JPanel photoThumbPanel;
    private JScrollPane photoScrollPane;

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

        // Build rootPanel (we will capture this entire panel for exports)
        rootPanel = new JPanel(new BorderLayout());

        // Header: logo on the left, title on the right
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel logoLabel = new JLabel();
        // Try to load bundled logo (/logo.png). If not present, show small placeholder.
        try {
            ImageIcon logoIcon = loadLogoIcon();
            if (logoIcon != null) {
                logoLabel.setIcon(logoIcon);
            } else {
                logoLabel.setText(" "); // keep space so header layout stays consistent
                logoLabel.setPreferredSize(new Dimension(90, 60));
            }
        } catch (Throwable t) {
            LOGGER.log(Level.FINER, "Error loading logo", t);
            logoLabel.setText(" ");
            logoLabel.setPreferredSize(new Dimension(90, 60));
        }

        JLabel titleLabel = new JLabel("Daily Field Report");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JPanel titleWrapper = new JPanel(new BorderLayout());
        titleWrapper.add(titleLabel, BorderLayout.CENTER);
        titleWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        header.add(logoLabel, BorderLayout.WEST);
        header.add(titleWrapper, BorderLayout.CENTER);

        rootPanel.add(header, BorderLayout.NORTH);

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

        rootPanel.add(tabbedPane, BorderLayout.CENTER);
        rootPanel.add(saveButton, BorderLayout.SOUTH);

        frame.setContentPane(rootPanel);
        frame.setVisible(true);
    }

    // Load logo.png from classpath (src/main/resources/logo.png) and return a scaled ImageIcon.
    // Returns null if no logo is found or on error. If logo resource isn't present, generate a simple
    // placeholder image so the header still shows a visual.
    private ImageIcon loadLogoIcon() {
        try (InputStream is = getClass().getResourceAsStream("/logo.png")) {
            if (is != null) {
                BufferedImage img = ImageIO.read(is);
                if (img != null) {
                    int targetHeight = 60; // desired displayed height in px
                    int width = (int) ((double) img.getWidth() * targetHeight / img.getHeight());
                    Image scaled = img.getScaledInstance(width, targetHeight, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Failed to load logo.png from classpath", e);
        } catch (Throwable t) {
            LOGGER.log(Level.FINER, "Unexpected error while loading logo resource", t);
        }

        // Resource not found or failed to read: create a simple placeholder image programmatically
        try {
            int targetHeight = 60;
            int width = 140;
            BufferedImage placeholder = new BufferedImage(width, targetHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = placeholder.createGraphics();
            try {
                // background
                g.setColor(new Color(0x2E6FB3));
                g.fillRect(0, 0, width, targetHeight);
                // draw a simple white square and initials
                g.setColor(Color.WHITE);
                g.fillRect(6, 6, targetHeight - 12, targetHeight - 12);
                // draw initials of app
                g.setFont(g.getFont().deriveFont(Font.BOLD, 18f));
                g.setColor(new Color(0xFFFFFF));
                FontMetrics fm = g.getFontMetrics();
                String initials = "DFR";
                int tx = (6 + (targetHeight - 12) - fm.stringWidth(initials)) / 2 + 6;
                int ty = (targetHeight - (fm.getHeight())) / 2 + fm.getAscent();
                g.drawString(initials, tx, ty);
                // small app name to the right
                g.setFont(g.getFont().deriveFont(Font.PLAIN, 14f));
                g.setColor(Color.WHITE);
                g.drawString("Daily Field Report", targetHeight, targetHeight / 2 + 6);
            } finally {
                g.dispose();
            }
            return new ImageIcon(placeholder);
        } catch (Throwable t) {
            LOGGER.log(Level.FINER, "Failed to generate placeholder logo", t);
            return null;
        }
    }

    // Expose form data collection for external callers / tests
    public String exportFormDataToString() {
        return collectFormData();
    }

    private JPanel createGeneralInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Left column
        addField(panel, gbc, 0, 0, "Project Name:", projectNameField);
        addField(panel, gbc, 0, 1, "Project No.:", projectNoField);
        addField(panel, gbc, 0, 2, "Location:", locationField);
        addField(panel, gbc, 0, 3, "Date:", dateField);

        // Right column
        addField(panel, gbc, 2, 0, "Weather AM / PM:", weatherField);
        addField(panel, gbc, 2, 1, "Temperature AM / PM:", temperatureField);
        addField(panel, gbc, 2, 2, "Wind:", windField);

        // Make fields expand
        gbc.weightx = 1.0;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 4;

        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc,
                          int column, int row,
                          String labelText, JComponent field) {

        gbc.gridx = column;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        JLabel label = new JLabel(labelText);
        panel.add(label, gbc);

        gbc.gridx = column + 1;
        gbc.weightx = 1.0;

        field.setPreferredSize(new Dimension(180, 28));
        panel.add(field, gbc);
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

    // Attachments panel now supports attaching photos and showing thumbnails
    private JPanel createAttachmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5,5));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton attachBtn = new JButton("Attach Photos...");
        JButton clearBtn = new JButton("Clear Photos");

        attachBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            chooser.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
            int res = chooser.showOpenDialog(null);
            if (res == JFileChooser.APPROVE_OPTION) {
                File[] files = chooser.getSelectedFiles();
                addPhotos(files);
            }
        });

        clearBtn.addActionListener(e -> {
            photoPaths.clear();
            refreshPhotoThumbs();
        });

        top.add(attachBtn);
        top.add(clearBtn);

        photoThumbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        photoScrollPane = new JScrollPane(photoThumbPanel);
        photoScrollPane.setPreferredSize(new Dimension(800, 200));
        panel.add(top, BorderLayout.NORTH);
        panel.add(photoScrollPane, BorderLayout.CENTER);

        // keep existing attachments checkboxes for compatibility
        JPanel bottomChecks = new JPanel(new GridLayout(1, 3));
        bottomChecks.add(photosCheck);
        bottomChecks.add(testsCheck);
        bottomChecks.add(drawingsCheck);
        panel.add(bottomChecks, BorderLayout.SOUTH);

        return panel;
    }

    private void addPhotos(File[] files) {
        for (File f : files) {
            try {
                Path p = f.toPath();
                if (Files.exists(p) && Files.isRegularFile(p)) {
                    photoPaths.add(p);
                }
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Failed to add photo: " + f, t);
            }
        }
        photosCheck.setSelected(!photoPaths.isEmpty());
        refreshPhotoThumbs();
    }

    private void refreshPhotoThumbs() {
        SwingUtilities.invokeLater(() -> {
            photoThumbPanel.removeAll();
            for (Path p : photoPaths) {
                try {
                    BufferedImage img = ImageIO.read(p.toFile());
                    if (img == null) continue;
                    int targetH = 90;
                    int w = (int) ((double) img.getWidth() * targetH / img.getHeight());
                    Image scaled = img.getScaledInstance(w, targetH, Image.SCALE_SMOOTH);
                    JLabel lbl = new JLabel(new ImageIcon(scaled));
                    lbl.setToolTipText(p.getFileName().toString());
                    lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    photoThumbPanel.add(lbl);
                } catch (Throwable t) {
                    LOGGER.log(Level.WARNING, "Failed to load thumbnail for " + p, t);
                }
            }
            photoThumbPanel.revalidate();
            photoThumbPanel.repaint();
        });
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

            // Capture the current form UI as an image so export preserves exact design
            BufferedImage snapshot = null;
            try {
                snapshot = captureComponentAsImage(rootPanel);
            } catch (Exception captureEx) {
                LOGGER.log(Level.WARNING, "Failed to capture form image", captureEx);
            }

            String reportContent = collectFormData();

            // Validate chosen path and ensure parent directory exists and is writable
            try {
                java.nio.file.Path chosenPath = Paths.get(chosen);
                java.nio.file.Path parent = chosenPath.getParent();
                if (parent != null) {
                    if (!Files.exists(parent)) {
                        try {
                            Files.createDirectories(parent);
                        } catch (IOException | SecurityException dirEx) {
                            String msg = "Unable to create directory '" + parent + "': " + dirEx.getMessage();
                            LOGGER.log(Level.SEVERE, msg, dirEx);
                            JOptionPane.showMessageDialog(null, msg);
                            return;
                        }
                    }
                    if (!Files.isWritable(parent)) {
                        String msg = "Directory '" + parent + "' is not writable.";
                        LOGGER.warning(msg);
                        JOptionPane.showMessageDialog(null, msg);
                        return;
                    }
                }
            } catch (java.nio.file.InvalidPathException ipEx) {
                String msg = "Invalid file path: " + ipEx.getInput();
                LOGGER.log(Level.SEVERE, msg, ipEx);
                JOptionPane.showMessageDialog(null, msg);
                return;
            } catch (SecurityException secEx) {
                String msg = "Security manager prevents validating/creating path: " + secEx.getMessage();
                LOGGER.log(Level.SEVERE, msg, secEx);
                JOptionPane.showMessageDialog(null, msg);
                return;
            }

            try {
                if (choice == 0) { // PDF (use snapshot if available)
                    if (!chosen.toLowerCase().endsWith(".pdf")) chosen += ".pdf";
                    if (snapshot != null) saveAsPDFImage(chosen, snapshot);
                    else saveAsPDF(chosen, reportContent);
                } else if (choice == 1) { // Word
                    if (!chosen.toLowerCase().endsWith(".docx")) chosen += ".docx";
                    if (snapshot != null) saveAsWordImage(chosen, snapshot);
                    else saveAsWord(chosen, reportContent);
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
            } catch (NoClassDefFoundError | ClassNotFoundException libEx) {
                // Handle missing library for PDF/Word export
                writeFallbackAndLog(chosen, reportContent, libEx, "Required libraries (PDFBox/POI) are missing.");
            } catch (SecurityException secEx) {
                // Permission problem writing file
                writeFallbackAndLog(chosen, reportContent, secEx, "Security error while attempting to write the file.");
            } catch (IOException ioEx) {
                // IO problems (disk full, permission, etc.)
                writeFallbackAndLog(chosen, reportContent, ioEx, "I/O error while saving the file.");
            } catch (IllegalArgumentException iaEx) {
                // invalid arguments / path
                writeFallbackAndLog(chosen, reportContent, iaEx, "Invalid argument while saving the file.");
            } catch (OutOfMemoryError oom) {
                LOGGER.log(Level.SEVERE, "Out of memory while generating the report", oom);
                JOptionPane.showMessageDialog(null, "Not enough memory to generate the report. Try saving a smaller report.");
            } catch (Exception e) {
                // Generic fallback
                writeFallbackAndLog(chosen, reportContent, e, "Unexpected error while saving the report.");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error saving report", ex);
            JOptionPane.showMessageDialog(null, "Error saving report: " + ex.getMessage());
        }
    }

    // Capture a Swing component to a BufferedImage. The component must be realized (displayed) for accurate rendering.
    private BufferedImage captureComponentAsImage(Component comp) throws IllegalArgumentException {
        if (comp == null) throw new IllegalArgumentException("Component to capture is null");
        int w = comp.getWidth();
        int h = comp.getHeight();
        if (w <= 0 || h <= 0) {
            // try to layout and use preferred size
            Dimension pref = comp.getPreferredSize();
            w = Math.max(1, pref.width);
            h = Math.max(1, pref.height);
            comp.setSize(w, h);
        }
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        try {
            // white background
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, w, h);
            comp.paintAll(g2);
        } finally {
            g2.dispose();
        }
        return img;
    }

    // Save a BufferedImage into a PDF page, scaling to fit the page while preserving aspect ratio.
    private void saveAsPDFImage(String filePath, BufferedImage image) throws Exception {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            // convert image to byte array (PNG)
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

            // append attached photos as additional pages
            appendPhotosToPdf(document);

            document.save(filePath);
        }
    }

    // Save a BufferedImage into a .docx by embedding the image as a PNG.
    private void saveAsWordImage(String filePath, BufferedImage image) throws Exception {
        try (XWPFDocument doc = new XWPFDocument()) {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            byte[] bytes = baos.toByteArray();

            XWPFParagraph p = doc.createParagraph();
            XWPFRun run = p.createRun();
            try (java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(bytes)) {
                run.addPicture(in, org.apache.poi.xwpf.usermodel.Document.PICTURE_TYPE_PNG, filePath, org.apache.poi.util.Units.toEMU(image.getWidth()), org.apache.poi.util.Units.toEMU(image.getHeight()));
            }

            // append attached photos to Word
            appendPhotosToWord(doc);

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

    // Centralized fallback writer and logger
    private void writeFallbackAndLog(String chosen, String reportContent, Throwable cause, String userMessage) {
        try {
            LOGGER.log(Level.SEVERE, userMessage, cause);
            String fallbackPath = chosen + ".fallback.txt";
            try {
                Files.write(Paths.get(fallbackPath), reportContent.getBytes(StandardCharsets.UTF_8));
                JOptionPane.showMessageDialog(null, userMessage + "\nA plain-text fallback was saved to:\n" + fallbackPath);
            } catch (IOException ioEx) {
                // last-resort: attempt to write to temp directory
                try {
                    java.nio.file.Path tmp = Files.createTempFile("DailyFieldReport-fallback-", ".txt");
                    Files.write(tmp, reportContent.getBytes(StandardCharsets.UTF_8));
                    String msg = userMessage + "\nFailed to write to chosen location; fallback saved to temp file:\n" + tmp.toAbsolutePath();
                    JOptionPane.showMessageDialog(null, msg);
                    LOGGER.log(Level.SEVERE, "Saved fallback to temp file", ioEx);
                } catch (IOException tempEx) {
                    String msg = userMessage + "\nFailed to write fallback file: " + tempEx.getMessage();
                    JOptionPane.showMessageDialog(null, msg);
                    LOGGER.log(Level.SEVERE, "Failed to save any fallback file", tempEx);
                }
            }
        } catch (Throwable logEx) {
            // If logging or dialogs also fail, there's not much we can do programmatically.
            try {
                StringWriter sw = new StringWriter();
                cause.printStackTrace(new PrintWriter(sw));
                Files.write(Paths.get(System.getProperty("java.io.tmpdir"), "DailyFieldReport-error.log"), sw.toString().getBytes(StandardCharsets.UTF_8));
            } catch (Throwable ignored) {
                // swallow - we can't do more
            }
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
        if (photosCheck.isSelected()) sb.append(" - Photos attached: ").append(photoPaths.size()).append("\n");
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

            // Append attached photos as extra pages
            appendPhotosToPdf(document);

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

    private void appendPhotosToPdf(PDDocument document) {
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

            // append photos
            appendPhotosToWord(doc);

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

    private void appendPhotosToWord(XWPFDocument doc) {
        for (Path p : photoPaths) {
            try {
                File f = p.toFile();
                if (!f.exists()) continue;
                BufferedImage img = ImageIO.read(f);
                if (img == null) continue;
                int width = img.getWidth();
                int height = img.getHeight();

                XWPFParagraph picPara = doc.createParagraph();
                XWPFRun picRun = picPara.createRun();
                try (java.io.FileInputStream in = new java.io.FileInputStream(f)) {
                    picRun.addPicture(in, org.apache.poi.xwpf.usermodel.Document.PICTURE_TYPE_PNG, f.getName(),
                            org.apache.poi.util.Units.toEMU(width), org.apache.poi.util.Units.toEMU(height));
                }
            } catch (Throwable t) {
                LOGGER.log(Level.WARNING, "Failed to append photo to Word doc: " + p, t);
            }
        }
    }

    // Try to open a file using the desktop integration when available, otherwise fall back to
    // platform-specific commands (Windows cmd start, macOS open, Linux xdg-open).
    private void openFile(String path) {
        try {
            if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)) {
                try {
                    java.awt.Desktop.getDesktop().open(new java.io.File(path));
                    return;
                } catch (IOException | SecurityException e) {
                    LOGGER.log(Level.WARNING, "Desktop.open failed, will try platform fallback", e);
                }
            }
        } catch (Throwable ignored) {
            // try platform fallback
            LOGGER.log(Level.FINE, "Desktop API not available or failed", ignored);
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
        } catch (IOException ioe) {
            String msg = "Saved but couldn't open file automatically: " + ioe.getMessage();
            JOptionPane.showMessageDialog(null, msg);
            LOGGER.log(Level.WARNING, "Error opening file automatically", ioe);
        } catch (SecurityException se) {
            String msg = "Saved but automatic opening is blocked by security manager: " + se.getMessage();
            JOptionPane.showMessageDialog(null, msg);
            LOGGER.log(Level.WARNING, "Security exception opening file", se);
        }
    }
}
