package com.fieldreport.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fieldreport.model.FieldReport;

/**
 * Simplified Report Export Service for standalone compilation.
 * 
 * This service provides functionality to export daily field reports to HTML and text formats.
 * HTML files can be easily converted to PDF via browser printing and include a professional
 * logo and styling. Text files provide a formatted document suitable for word processing.
 * 
 * Features:
 * - Export single reports to HTML/PDF format with logo
 * - Export single reports to formatted text/Word format
 * - Export multiple reports as collections
 * - Professional styling with CSS for print-ready documents
 * - Automatic directory creation for exported files
 * - Timestamp-based file naming for organization
 * 
 * @author Daily Field Report System
 * @version 1.0
 * @since 2025-10-24
 */
public class ReportExportService {
    
    /** Directory name for storing exported reports */
    private static final String EXPORT_DIR = "exports";
    
    /** Date format pattern for file naming timestamps */
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    /**
     * Constructor that initializes the export service and creates the export directory.
     * Automatically creates the exports folder if it doesn't exist.
     */
    public ReportExportService() {
        createExportDirectory();
    }
    
    /**
     * Create the exports directory if it doesn't exist.
     * This method ensures that the export directory structure is ready for file creation.
     */
    private void createExportDirectory() {
        java.io.File exportDir = new java.io.File(EXPORT_DIR);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
    }
    
    /**
     * Export a single field report to HTML format suitable for PDF conversion.
     * 
     * Generates a professional HTML document with embedded CSS styling and company logo.
     * The output can be opened in any web browser and printed to PDF for professional
     * document distribution.
     * 
     * @param report The field report to export - must be a valid FieldReport object
     * @return The absolute file path to the generated HTML file
     * @throws IOException If there's an error creating the file or writing content
     */
    public String exportReportToPDF(FieldReport report) throws IOException {
        return exportReportToHTML(report, "PDF_");
    }
    
    /**
     * Export multiple field reports to a single HTML collection suitable for PDF conversion.
     * 
     * Creates a comprehensive document containing all reports with a title page, 
     * table of contents, and individual report sections. Each report is formatted
     * for page breaks when printed.
     * 
     * @param reports List of field reports to include in the collection
     * @param title Descriptive title for the report collection (e.g., "Weekly Reports", "October 2025")
     * @return The absolute file path to the generated HTML collection file
     * @throws IOException If there's an error creating the file or writing content
     */
    public String exportMultipleReportsToPDF(List<FieldReport> reports, String title) throws IOException {
        return exportMultipleReportsToHTML(reports, title, "PDF_");
    }
    
    /**
     * Export a single field report to formatted text suitable for word processing.
     * 
     * Generates a well-structured text document with professional formatting,
     * headers, and consistent spacing. Compatible with any text editor or
     * word processing software.
     * 
     * @param report The field report to export - must be a valid FieldReport object
     * @return The absolute file path to the generated text file
     * @throws IOException If there's an error creating the file or writing content
     */
    public String exportReportToWord(FieldReport report) throws IOException {
        return exportReportToText(report, "Word_");
    }
    
    /**
     * Export multiple field reports to a formatted text collection.
     * 
     * Creates a comprehensive text document containing all reports with clear
     * section divisions, headers, and consistent formatting throughout.
     * 
     * @param reports List of field reports to include in the collection
     * @param title Descriptive title for the report collection
     * @return The absolute file path to the generated text collection file
     * @throws IOException If there's an error creating the file or writing content
     */
    public String exportMultipleReportsToWord(List<FieldReport> reports, String title) throws IOException {
        return exportMultipleReportsToText(reports, title, "Word_");
    }
    
    /**
     * Internal method to export a single field report to HTML format.
     * 
     * Handles the file creation, naming, and content generation for individual reports.
     * Includes professional styling, company logo, and structured data presentation.
     * 
     * @param report The field report to export
     * @param prefix File name prefix to distinguish file types (e.g., "PDF_", "Word_")
     * @return The absolute file path to the generated HTML file
     * @throws IOException If file creation or writing fails
     */
    private String exportReportToHTML(FieldReport report, String prefix) throws IOException {
        String fileName = String.format("%s/%sFieldReport_%s_%s.html", 
            EXPORT_DIR, 
            prefix,
            report.getDate().toString(), 
            LocalDateTime.now().format(FILE_DATE_FORMAT));
        
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(generateHTMLContent(report, "Daily Field Report"));
        }
        
        System.out.println("✅ HTML report exported successfully: " + fileName);
        System.out.println("💡 Open in browser and use Print -> Save as PDF for PDF conversion");
        return fileName;
    }
    
    /**
     * Internal method to export multiple field reports to HTML format.
     * 
     * Creates a comprehensive HTML document with title page, navigation, and
     * individual report sections. Optimized for printing with page breaks.
     * 
     * @param reports List of field reports to include in the collection
     * @param title Collection title for the document header
     * @param prefix File name prefix to distinguish file types
     * @return The absolute file path to the generated HTML collection file
     * @throws IOException If file creation or writing fails
     */
    private String exportMultipleReportsToHTML(List<FieldReport> reports, String title, String prefix) throws IOException {
        String fileName = String.format("%s/%sFieldReports_%s_%s.html", 
            EXPORT_DIR, 
            prefix,
            title.replaceAll("[^a-zA-Z0-9]", "_"), 
            LocalDateTime.now().format(FILE_DATE_FORMAT));
        
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(generateMultipleHTMLContent(reports, title));
        }
        
        System.out.println("✅ HTML collection exported successfully: " + fileName);
        System.out.println("💡 Open in browser and use Print -> Save as PDF for PDF conversion");
        return fileName;
    }
    
    /**
     * Internal method to export a single field report to formatted text.
     * 
     * Generates professional text formatting with headers, borders, and
     * consistent spacing suitable for word processing applications.
     * 
     * @param report The field report to export
     * @param prefix File name prefix to distinguish file types
     * @return The absolute file path to the generated text file
     * @throws IOException If file creation or writing fails
     */
    private String exportReportToText(FieldReport report, String prefix) throws IOException {
        String fileName = String.format("%s/%sFieldReport_%s_%s.txt", 
            EXPORT_DIR, 
            prefix,
            report.getDate().toString(), 
            LocalDateTime.now().format(FILE_DATE_FORMAT));
        
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(generateTextContent(report));
        }
        
        System.out.println("✅ Text report exported successfully: " + fileName);
        return fileName;
    }
    
    /**
     * Internal method to export multiple field reports to formatted text.
     * 
     * Creates a comprehensive text document with clear section divisions,
     * professional formatting, and consistent structure throughout.
     * 
     * @param reports List of field reports to include in the collection
     * @param title Collection title for the document header
     * @param prefix File name prefix to distinguish file types
     * @return The absolute file path to the generated text collection file
     * @throws IOException If file creation or writing fails
     */
    private String exportMultipleReportsToText(List<FieldReport> reports, String title, String prefix) throws IOException {
        String fileName = String.format("%s/%sFieldReports_%s_%s.txt", 
            EXPORT_DIR, 
            prefix,
            title.replaceAll("[^a-zA-Z0-9]", "_"), 
            LocalDateTime.now().format(FILE_DATE_FORMAT));
        
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(generateMultipleTextContent(reports, title));
        }
        
        System.out.println("✅ Text collection exported successfully: " + fileName);
        return fileName;
    }
    
    /**
     * Generate complete HTML content for a single field report.
     * 
     * Creates a professional HTML document with:
     * - Company logo (DFR) in top-left corner
     * - Responsive CSS styling for web and print
     * - Structured data table with report details
     * - Formatted work description and notes sections
     * - Print-optimized layout with proper margins
     * 
     * @param report The field report data to format
     * @param title Document title for the HTML head section
     * @return Complete HTML document as a string
     */
    private String generateHTMLContent(FieldReport report, String title) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<title>").append(title).append("</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; margin: 40px; }\n");
        html.append(".logo-container { position: absolute; top: 20px; left: 20px; }\n");
        html.append(".logo { width: 80px; height: 80px; background: linear-gradient(45deg, #2196F3, #21CBF3); border-radius: 50%; display: flex; align-items: center; justify-content: center; color: white; font-weight: bold; font-size: 16px; }\n");
        html.append(".header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 20px; margin-top: 60px; }\n");
        html.append(".report-table { width: 100%; border-collapse: collapse; margin: 20px 0; }\n");
        html.append(".report-table th, .report-table td { border: 1px solid #ddd; padding: 12px; text-align: left; }\n");
        html.append(".report-table th { background-color: #f2f2f2; font-weight: bold; }\n");
        html.append(".section-title { font-weight: bold; margin-top: 20px; font-size: 16px; }\n");
        html.append(".content { margin: 10px 0; line-height: 1.6; }\n");
        html.append("@media print { body { margin: 20px; } .logo-container { position: absolute; top: 10px; left: 10px; } }\n");
        html.append("</style>\n");
        html.append("</head>\n<body>\n");
        
        // Logo
        html.append("<div class='logo-container'>\n");
        html.append("<div class='logo'>DFR</div>\n");
        html.append("</div>\n");
        
        // Header
        html.append("<div class='header'>\n");
        html.append("<h1>DAILY FIELD REPORT</h1>\n");
        html.append("<p>Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("</p>\n");
        html.append("</div>\n");
        
        // Report details table
        html.append("<table class='report-table'>\n");
        html.append("<tr><th>Report ID</th><td>").append(report.getId()).append("</td></tr>\n");
        html.append("<tr><th>Date</th><td>").append(report.getDate()).append("</td></tr>\n");
        html.append("<tr><th>Created</th><td>").append(report.getCreatedAt()).append("</td></tr>\n");
        html.append("<tr><th>Reporter</th><td>").append(report.getReporterName()).append("</td></tr>\n");
        html.append("<tr><th>Location</th><td>").append(report.getLocation()).append("</td></tr>\n");
        html.append("<tr><th>Project</th><td>").append(report.getProjectName()).append("</td></tr>\n");
        html.append("<tr><th>Weather</th><td>").append(report.getWeatherConditions()).append("</td></tr>\n");
        html.append("</table>\n");
        
        // Work description
        html.append("<div class='section-title'>Work Description:</div>\n");
        html.append("<div class='content'>").append(report.getWorkDescription().replace("\n", "<br>")).append("</div>\n");
        
        // Notes
        if (report.getNotes() != null && !report.getNotes().trim().isEmpty()) {
            html.append("<div class='section-title'>Notes:</div>\n");
            html.append("<div class='content'>").append(report.getNotes().replace("\n", "<br>")).append("</div>\n");
        }
        
        html.append("</body>\n</html>");
        
        return html.toString();
    }
    
    /**
     * Generate complete HTML content for multiple field reports collection.
     * 
     * Creates a comprehensive HTML document with:
     * - Title page with collection information
     * - Company logo (DFR) in top-left corner
     * - Individual report sections with page breaks
     * - Consistent styling throughout the document
     * - Print-optimized layout for professional presentation
     * 
     * @param reports List of field reports to include in the collection
     * @param title Collection title for headers and navigation
     * @return Complete HTML document as a string
     */
    private String generateMultipleHTMLContent(List<FieldReport> reports, String title) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<title>").append(title).append("</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; margin: 40px; }\n");
        html.append(".logo-container { position: absolute; top: 20px; left: 20px; }\n");
        html.append(".logo { width: 80px; height: 80px; background: linear-gradient(45deg, #2196F3, #21CBF3); border-radius: 50%; display: flex; align-items: center; justify-content: center; color: white; font-weight: bold; font-size: 16px; }\n");
        html.append(".header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 20px; margin-bottom: 30px; margin-top: 60px; }\n");
        html.append(".report { page-break-after: always; margin-bottom: 40px; }\n");
        html.append(".report:last-child { page-break-after: avoid; }\n");
        html.append(".report-header { background-color: #f0f0f0; padding: 10px; border: 1px solid #ccc; }\n");
        html.append(".report-table { width: 100%; border-collapse: collapse; margin: 20px 0; }\n");
        html.append(".report-table th, .report-table td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        html.append(".report-table th { background-color: #f2f2f2; font-weight: bold; }\n");
        html.append(".section-title { font-weight: bold; margin-top: 15px; font-size: 14px; }\n");
        html.append(".content { margin: 8px 0; line-height: 1.5; }\n");
        html.append("@media print { body { margin: 20px; } .logo-container { position: absolute; top: 10px; left: 10px; } }\n");
        html.append("</style>\n");
        html.append("</head>\n<body>\n");
        
        // Logo
        html.append("<div class='logo-container'>\n");
        html.append("<div class='logo'>DFR</div>\n");
        html.append("</div>\n");
        
        // Header
        html.append("<div class='header'>\n");
        html.append("<h1>FIELD REPORTS COLLECTION</h1>\n");
        html.append("<h2>").append(title).append("</h2>\n");
        html.append("<p>Total Reports: ").append(reports.size()).append("</p>\n");
        html.append("<p>Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("</p>\n");
        html.append("</div>\n");
        
        // Individual reports
        for (int i = 0; i < reports.size(); i++) {
            FieldReport report = reports.get(i);
            html.append("<div class='report'>\n");
            html.append("<div class='report-header'><h3>Report #").append(i + 1).append("</h3></div>\n");
            
            // Report details table
            html.append("<table class='report-table'>\n");
            html.append("<tr><th>Report ID</th><td>").append(report.getId()).append("</td></tr>\n");
            html.append("<tr><th>Date</th><td>").append(report.getDate()).append("</td></tr>\n");
            html.append("<tr><th>Reporter</th><td>").append(report.getReporterName()).append("</td></tr>\n");
            html.append("<tr><th>Location</th><td>").append(report.getLocation()).append("</td></tr>\n");
            html.append("<tr><th>Project</th><td>").append(report.getProjectName()).append("</td></tr>\n");
            html.append("<tr><th>Weather</th><td>").append(report.getWeatherConditions()).append("</td></tr>\n");
            html.append("</table>\n");
            
            // Work description
            html.append("<div class='section-title'>Work Description:</div>\n");
            html.append("<div class='content'>").append(report.getWorkDescription().replace("\n", "<br>")).append("</div>\n");
            
            // Notes
            if (report.getNotes() != null && !report.getNotes().trim().isEmpty()) {
                html.append("<div class='section-title'>Notes:</div>\n");
                html.append("<div class='content'>").append(report.getNotes().replace("\n", "<br>")).append("</div>\n");
            }
            
            html.append("</div>\n");
        }
        
        html.append("</body>\n</html>");
        
        return html.toString();
    }
    
    /**
     * Generate text content for a single report
     * @param report The field report to export
     * @return Formatted text content as string
     */
    private String generateTextContent(FieldReport report) {
        StringBuilder text = new StringBuilder();
        
        text.append("=".repeat(80)).append("\n");
        text.append("                           DAILY FIELD REPORT\n");
        text.append("=".repeat(80)).append("\n");
        text.append("Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");
        
        text.append("REPORT DETAILS\n");
        text.append("-".repeat(40)).append("\n");
        text.append("Report ID:     ").append(report.getId()).append("\n");
        text.append("Date:          ").append(report.getDate()).append("\n");
        text.append("Created:       ").append(report.getCreatedAt()).append("\n");
        text.append("Reporter:      ").append(report.getReporterName()).append("\n");
        text.append("Location:      ").append(report.getLocation()).append("\n");
        text.append("Project:       ").append(report.getProjectName()).append("\n");
        text.append("Weather:       ").append(report.getWeatherConditions()).append("\n\n");
        
        text.append("WORK DESCRIPTION\n");
        text.append("-".repeat(40)).append("\n");
        text.append(wrapText(report.getWorkDescription(), 76)).append("\n\n");
        
        if (report.getNotes() != null && !report.getNotes().trim().isEmpty()) {
            text.append("NOTES\n");
            text.append("-".repeat(40)).append("\n");
            text.append(wrapText(report.getNotes(), 76)).append("\n\n");
        }
        
        text.append("=".repeat(80)).append("\n");
        
        return text.toString();
    }
    
    /**
     * Generate formatted text content for multiple field reports.
     * 
     * Creates a professional text document with:
     * - Collection title page with summary information
     * - Individual report sections with clear divisions
     * - Consistent formatting and spacing
     * - Text wrapping for optimal readability
     * - Section separators for easy navigation
     * 
     * @param reports List of field reports to include in the collection
     * @param title Collection title for the document header
     * @return Formatted text document as a string
     */
    private String generateMultipleTextContent(List<FieldReport> reports, String title) {
        StringBuilder text = new StringBuilder();
        
        text.append("=".repeat(80)).append("\n");
        text.append("                       FIELD REPORTS COLLECTION\n");
        text.append("=".repeat(80)).append("\n");
        text.append("Title: ").append(title).append("\n");
        text.append("Total Reports: ").append(reports.size()).append("\n");
        text.append("Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        text.append("=".repeat(80)).append("\n\n");
        
        for (int i = 0; i < reports.size(); i++) {
            FieldReport report = reports.get(i);
            text.append("REPORT #").append(i + 1).append("\n");
            text.append("-".repeat(20)).append("\n");
            
            text.append("Report ID:     ").append(report.getId()).append("\n");
            text.append("Date:          ").append(report.getDate()).append("\n");
            text.append("Reporter:      ").append(report.getReporterName()).append("\n");
            text.append("Location:      ").append(report.getLocation()).append("\n");
            text.append("Project:       ").append(report.getProjectName()).append("\n");
            text.append("Weather:       ").append(report.getWeatherConditions()).append("\n\n");
            
            text.append("Work Description:\n");
            text.append(wrapText(report.getWorkDescription(), 76)).append("\n\n");
            
            if (report.getNotes() != null && !report.getNotes().trim().isEmpty()) {
                text.append("Notes:\n");
                text.append(wrapText(report.getNotes(), 76)).append("\n\n");
            }
            
            if (i < reports.size() - 1) {
                text.append("-".repeat(80)).append("\n\n");
            }
        }
        
        text.append("=".repeat(80)).append("\n");
        
        return text.toString();
    }
    
    /**
     * Utility method to wrap long text to specified line width.
     * 
     * Breaks text into lines without splitting words, ensuring proper
     * formatting for text-based documents. Handles edge cases like
     * very long words and maintains consistent spacing.
     * 
     * @param text The text to wrap - can be null or empty
     * @param width Maximum line width in characters
     * @return Text formatted with line breaks at appropriate positions
     */
    private String wrapText(String text, int width) {
        if (text == null || text.length() <= width) {
            return text;
        }
        
        StringBuilder wrapped = new StringBuilder();
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        
        for (String word : words) {
            if (line.length() + word.length() + 1 <= width) {
                if (!line.isEmpty()) {
                    line.append(" ");
                }
                line.append(word);
            } else {
                if (!line.isEmpty()) {
                    wrapped.append(line).append("\n");
                    line = new StringBuilder(word);
                } else {
                    wrapped.append(word).append("\n");
                }
            }
        }
        
        if (!line.isEmpty()) {
            wrapped.append(line);
        }
        
        return wrapped.toString();
    }
}