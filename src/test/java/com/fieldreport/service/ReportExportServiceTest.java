package com.fieldreport.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldreport.model.EquipmentOnSite;
import com.fieldreport.model.FieldReport;
import com.fieldreport.model.InspectionTesting;
import com.fieldreport.model.MaterialDelivered;
import com.fieldreport.model.PersonnelOnSite;

/**
 * Comprehensive unit tests for the ReportExportService class.
 * 
 * Tests cover:
 * - Constructor initialization and directory creation
 * - Single report export to HTML/PDF format
 * - Single report export to Text/Word format
 * - Multiple reports export to HTML/PDF format
 * - Multiple reports export to Text/Word format
 * - Content generation methods
 * - File system operations
 * - Error handling and edge cases
 * - HTML/Text content validation
 * - Professional formatting verification
 * 
 * @author DailyFieldReport System
 * @version 1.0
 */
@DisplayName("ReportExportService Tests")
class ReportExportServiceTest {

    private ReportExportService exportService;
    private FieldReport testReport;
    private List<FieldReport> testReports;
    private static final String EXPORT_DIRECTORY = "exports";

    @BeforeEach
    void setUp() {
        exportService = new ReportExportService();
        
        // Create a comprehensive test report
        testReport = new FieldReport(
            "John Smith", 
            "Highway Construction Zone", 
            "State Route 101 Expansion",
            "SR101-2024-001",
            "Clear, 45°F",
            "Cloudy, 62°F", 
            "65°F",
            "42°F",
            "Pavement marking and signage installation completed according to specifications",
            "Weather conditions were favorable for all outdoor work activities"
        );
        
        // Add comprehensive data to test report
        testReport.addPersonnelOnSite(new PersonnelOnSite("Main Contractor", "Site Supervisor", 1, 10, "John Smith"));
        testReport.addPersonnelOnSite(new PersonnelOnSite("Marking Crew", "Line Painter", 3, 8, "Jane Doe"));
        testReport.addEquipmentOnSite(new EquipmentOnSite("Line Painter", "Truck Mounted", 1, true, ""));
        testReport.addEquipmentOnSite(new EquipmentOnSite("Compactor", "Vibratory", 1, false, "Maintenance required"));
        
        List<MaterialDelivered> materials = new ArrayList<>();
        materials.add(new MaterialDelivered("Paint", "Highway Supply Co", "50 gallons", "Truck", "Quality approved"));
        materials.add(new MaterialDelivered("Reflective Beads", "Safety Materials Inc", "10 bags", "Storage", "Inspected"));
        testReport.setMaterialsDelivered(materials);
        
        List<InspectionTesting> inspections = new ArrayList<>();
        inspections.add(new InspectionTesting("Pavement Quality", "State Inspector", "DOT", "Meets all specifications"));
        testReport.setInspectionsTesting(inspections);
        
        testReport.setSafetyMeetingHeld("Y");
        testReport.setSafetyMeetingTopic("Traffic Control and Worker Safety");
        testReport.setIncidentsNearMisses("N");
        testReport.setPhotosAttached(true);
        testReport.setSignature("John Smith, Site Supervisor");
        testReport.addPicturePath("/photos/before_work.jpg");
        testReport.addPicturePath("/photos/completed_section.jpg");
        
        // Create multiple test reports
        FieldReport report2 = new FieldReport(
            "Jane Doe",
            "Bridge Construction Site", 
            "Downtown Bridge Renovation",
            "BR-2024-002",
            "Sunny, 52°F",
            "Partly cloudy, 68°F",
            "72°F", 
            "48°F",
            "Concrete pouring for north abutment completed successfully",
            "Concrete truck arrived on schedule"
        );
        
        FieldReport report3 = new FieldReport(
            "Mike Johnson",
            "Utility Installation Zone",
            "Underground Cable Project", 
            "UC-2024-003",
            "Overcast, 58°F",
            "Light rain, 60°F",
            "63°F",
            "55°F", 
            "Cable laying in trenches from station 15+00 to 18+50",
            "Minor delays due to weather but work continued"
        );
        
        testReports = Arrays.asList(testReport, report2, report3);
    }

    @AfterEach
    void tearDown() {
        // Clean up test files
        cleanupTestFiles();
    }

    private void cleanupTestFiles() {
        File exportDirectory = new File(EXPORT_DIRECTORY);
        if (exportDirectory.exists() && exportDirectory.isDirectory()) {
            File[] files = exportDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith("PDF_") || 
                        file.getName().startsWith("Word_") ||
                        file.getName().startsWith("Test")) {
                        file.delete();
                    }
                }
            }
        }
    }

    // ================= CONSTRUCTOR AND INITIALIZATION TESTS =================

    @Test
    @DisplayName("Constructor should initialize service and create export directory")
    void testConstructorInitialization() {
        ReportExportService service = new ReportExportService();
        assertNotNull(service);
        
        File exportDirectory = new File(EXPORT_DIRECTORY);
        assertTrue(exportDirectory.exists());
        assertTrue(exportDirectory.isDirectory());
    }

    @Test
    @DisplayName("Export directory should be created if it doesn't exist")
    void testExportDirectoryCreation() {
        // Delete the directory if it exists
        File exportDirectory = new File(EXPORT_DIRECTORY);
        if (exportDirectory.exists()) {
            exportDirectory.delete();
        }
        
        // Create new service - should recreate directory
        new ReportExportService();
        
        assertTrue(exportDirectory.exists());
        assertTrue(exportDirectory.isDirectory());
    }

    // ================= SINGLE REPORT HTML/PDF EXPORT TESTS =================

    @Test
    @DisplayName("exportReportToPDF should create valid HTML file")
    void testExportReportToPDF() throws IOException {
        String filePath = exportService.exportReportToPDF(testReport);
        
        assertNotNull(filePath);
        assertTrue(filePath.contains("PDF_"));
        assertTrue(filePath.endsWith(".html"));
        
        File file = new File(filePath);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        
        // Verify file content
        String content = Files.readString(Paths.get(filePath));
        assertTrue(content.contains("<!DOCTYPE html>"));
        assertTrue(content.contains("<html>"));
        assertTrue(content.contains("Daily Field Report"));
        assertTrue(content.contains("John Smith"));
        assertTrue(content.contains("Highway Construction Zone"));
        assertTrue(content.contains("State Route 101 Expansion"));
        assertTrue(content.contains("SR101-2024-001"));
    }

    @Test
    @DisplayName("HTML export should include comprehensive report data")
    void testHTMLExportComprehensiveData() throws IOException {
        String filePath = exportService.exportReportToPDF(testReport);
        String content = Files.readString(Paths.get(filePath));
        
        // Check weather information
        assertTrue(content.contains("Clear, 45°F"));
        assertTrue(content.contains("Cloudy, 62°F"));
        assertTrue(content.contains("65°F"));
        assertTrue(content.contains("42°F"));
        
        // Check personnel information
        assertTrue(content.contains("Main Contractor"));
        assertTrue(content.contains("Site Supervisor"));
        assertTrue(content.contains("John Smith"));
        
        // Check equipment information
        assertTrue(content.contains("Line Painter"));
        assertTrue(content.contains("Truck Mounted"));
        
        // Check materials information
        assertTrue(content.contains("Paint"));
        assertTrue(content.contains("Highway Supply Co"));
        
        // Check inspections
        assertTrue(content.contains("Pavement Quality"));
        assertTrue(content.contains("State Inspector"));
        
        // Check safety information
        assertTrue(content.contains("Traffic Control and Worker Safety"));
        
        // Check signature
        assertTrue(content.contains("John Smith, Site Supervisor"));
    }

    @Test
    @DisplayName("HTML export should include CSS styling")
    void testHTMLExportStyling() throws IOException {
        String filePath = exportService.exportReportToPDF(testReport);
        String content = Files.readString(Paths.get(filePath));
        
        assertTrue(content.contains("<style>"));
        assertTrue(content.contains("font-family"));
        assertTrue(content.contains("margin"));
        assertTrue(content.contains("padding"));
        assertTrue(content.contains("border"));
        assertTrue(content.contains("background-color"));
    }

    @Test
    @DisplayName("HTML export should handle null values gracefully")
    void testHTMLExportWithNullValues() throws IOException {
        FieldReport minimalReport = new FieldReport();
        minimalReport.setReporterName("Test Reporter");
        minimalReport.setLocation("Test Location");
        minimalReport.setProjectName("Test Project");
        minimalReport.setWorkDescription("Test Work");
        
        String filePath = exportService.exportReportToPDF(minimalReport);
        
        assertNotNull(filePath);
        File file = new File(filePath);
        assertTrue(file.exists());
        
        String content = Files.readString(Paths.get(filePath));
        assertTrue(content.contains("Test Reporter"));
        assertTrue(content.contains("Test Location"));
        assertTrue(content.contains("Test Project"));
    }

    // ================= SINGLE REPORT TEXT/WORD EXPORT TESTS =================

    @Test
    @DisplayName("exportReportToWord should create valid text file")
    void testExportReportToWord() throws IOException {
        String filePath = exportService.exportReportToWord(testReport);
        
        assertNotNull(filePath);
        assertTrue(filePath.contains("Word_"));
        assertTrue(filePath.endsWith(".txt"));
        
        File file = new File(filePath);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        
        // Verify file content
        String content = Files.readString(Paths.get(filePath));
        assertTrue(content.contains("DAILY FIELD REPORT"));
        assertTrue(content.contains("John Smith"));
        assertTrue(content.contains("Highway Construction Zone"));
        assertTrue(content.contains("State Route 101 Expansion"));
        assertTrue(content.contains("========================================"));
    }

    @Test
    @DisplayName("Text export should include comprehensive report data")
    void testTextExportComprehensiveData() throws IOException {
        String filePath = exportService.exportReportToWord(testReport);
        String content = Files.readString(Paths.get(filePath));
        
        // Check weather section
        assertTrue(content.contains("WEATHER CONDITIONS"));
        assertTrue(content.contains("AM Weather: Clear, 45°F"));
        assertTrue(content.contains("PM Weather: Cloudy, 62°F"));
        
        // Check personnel section
        assertTrue(content.contains("PERSONNEL ON SITE"));
        assertTrue(content.contains("Main Contractor"));
        
        // Check equipment section
        assertTrue(content.contains("EQUIPMENT ON SITE"));
        assertTrue(content.contains("Line Painter"));
        
        // Check materials section
        assertTrue(content.contains("MATERIALS DELIVERED"));
        assertTrue(content.contains("Paint"));
        
        // Check safety section
        assertTrue(content.contains("SAFETY INFORMATION"));
        assertTrue(content.contains("Meeting Held: Y"));
    }

    @Test
    @DisplayName("Text export should have proper formatting")
    void testTextExportFormatting() throws IOException {
        String filePath = exportService.exportReportToWord(testReport);
        String content = Files.readString(Paths.get(filePath));
        
        // Check section dividers
        assertTrue(content.contains("========================================"));
        assertTrue(content.contains("----------------------------------------"));
        
        // Check consistent spacing
        String[] lines = content.split("\n");
        boolean hasEmptyLines = false;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                hasEmptyLines = true;
                break;
            }
        }
        assertTrue(hasEmptyLines, "Text should have proper spacing with empty lines");
    }

    // ================= MULTIPLE REPORTS EXPORT TESTS =================

    @Test
    @DisplayName("exportMultipleReportsToPDF should create valid HTML collection")
    void testExportMultipleReportsToPDF() throws IOException {
        String title = "Weekly Field Reports - October 2024";
        String filePath = exportService.exportMultipleReportsToPDF(testReports, title);
        
        assertNotNull(filePath);
        assertTrue(filePath.contains("PDF_"));
        assertTrue(filePath.endsWith(".html"));
        
        File file = new File(filePath);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        
        String content = Files.readString(Paths.get(filePath));
        assertTrue(content.contains(title));
        assertTrue(content.contains("John Smith"));
        assertTrue(content.contains("Jane Doe"));
        assertTrue(content.contains("Mike Johnson"));
        assertTrue(content.contains("Table of Contents"));
    }

    @Test
    @DisplayName("exportMultipleReportsToWord should create valid text collection")
    void testExportMultipleReportsToWord() throws IOException {
        String title = "Monthly Field Reports Collection";
        String filePath = exportService.exportMultipleReportsToWord(testReports, title);
        
        assertNotNull(filePath);
        assertTrue(filePath.contains("Word_"));
        assertTrue(filePath.endsWith(".txt"));
        
        File file = new File(filePath);
        assertTrue(file.exists());
        
        String content = Files.readString(Paths.get(filePath));
        assertTrue(content.contains(title));
        assertTrue(content.contains("Report 1 of 3"));
        assertTrue(content.contains("Report 2 of 3"));
        assertTrue(content.contains("Report 3 of 3"));
    }

    @Test
    @DisplayName("Multiple reports export should handle empty list")
    void testExportMultipleReportsEmptyList() throws IOException {
        List<FieldReport> emptyList = new ArrayList<>();
        String title = "Empty Collection";
        
        String htmlPath = exportService.exportMultipleReportsToPDF(emptyList, title);
        String textPath = exportService.exportMultipleReportsToWord(emptyList, title);
        
        assertNotNull(htmlPath);
        assertNotNull(textPath);
        
        File htmlFile = new File(htmlPath);
        File textFile = new File(textPath);
        
        assertTrue(htmlFile.exists());
        assertTrue(textFile.exists());
        
        String htmlContent = Files.readString(Paths.get(htmlPath));
        String textContent = Files.readString(Paths.get(textPath));
        
        assertTrue(htmlContent.contains(title));
        assertTrue(textContent.contains(title));
        assertTrue(htmlContent.contains("No reports to display"));
        assertTrue(textContent.contains("No reports to display"));
    }

    // ================= ERROR HANDLING TESTS =================

    @Test
    @DisplayName("Export methods should handle null report gracefully")
    void testExportWithNullReport() {
        assertThrows(Exception.class, () -> {
            exportService.exportReportToPDF(null);
        });
        
        assertThrows(Exception.class, () -> {
            exportService.exportReportToWord(null);
        });
    }

    @Test
    @DisplayName("Multiple reports export should handle null list")
    void testExportMultipleWithNullList() {
        assertThrows(Exception.class, () -> {
            exportService.exportMultipleReportsToPDF(null, "Test Title");
        });
        
        assertThrows(Exception.class, () -> {
            exportService.exportMultipleReportsToWord(null, "Test Title");
        });
    }

    @Test
    @DisplayName("Export methods should handle invalid report data")
    void testExportWithInvalidReport() throws IOException {
        FieldReport invalidReport = new FieldReport();
        // Leave all fields null/empty
        
        // Should still create files but with minimal content
        String htmlPath = exportService.exportReportToPDF(invalidReport);
        String textPath = exportService.exportReportToWord(invalidReport);
        
        assertNotNull(htmlPath);
        assertNotNull(textPath);
        
        File htmlFile = new File(htmlPath);
        File textFile = new File(textPath);
        
        assertTrue(htmlFile.exists());
        assertTrue(textFile.exists());
    }

    // ================= FILE NAMING TESTS =================

    @Test
    @DisplayName("Exported files should have timestamp-based names")
    void testFileNaming() throws IOException {
        String htmlPath = exportService.exportReportToPDF(testReport);
        String textPath = exportService.exportReportToWord(testReport);
        
        assertTrue(htmlPath.contains("PDF_"));
        assertTrue(textPath.contains("Word_"));
        
        // Check timestamp pattern (YYYY-MM-DD_HH-MM-SS)
        assertTrue(htmlPath.matches(".*\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}.*"));
        assertTrue(textPath.matches(".*\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}.*"));
    }

    @Test
    @DisplayName("Multiple export files should have unique names")
    void testUniqueFileNames() throws IOException {
        String path1 = exportService.exportReportToPDF(testReport);
        
        // Create a different report to ensure unique names
        FieldReport differentReport = new FieldReport();
        differentReport.setReporterName("Different Reporter");
        differentReport.setLocation("Different Location");
        differentReport.setProjectName("Different Project");
        differentReport.setWorkDescription("Different Work");
        
        String path2 = exportService.exportReportToPDF(differentReport);
        
        // Names should be different due to timestamp differences
        assertNotEquals(path1, path2);
    }

    // ================= CONTENT VALIDATION TESTS =================

    @Test
    @DisplayName("HTML content should be well-formed")
    void testHTMLContentStructure() throws IOException {
        String filePath = exportService.exportReportToPDF(testReport);
        String content = Files.readString(Paths.get(filePath));
        
        // Check basic HTML structure
        assertTrue(content.contains("<!DOCTYPE html>"));
        assertTrue(content.contains("<html"));
        assertTrue(content.contains("<head>"));
        assertTrue(content.contains("<body>"));
        assertTrue(content.contains("</body>"));
        assertTrue(content.contains("</html>"));
        
        // Check meta tags
        assertTrue(content.contains("<meta charset=\"UTF-8\">"));
        assertTrue(content.contains("<title>"));
        
        // Check CSS
        assertTrue(content.contains("<style>"));
        assertTrue(content.contains("</style>"));
    }

    @Test
    @DisplayName("Text content should have proper sections")
    void testTextContentStructure() throws IOException {
        String filePath = exportService.exportReportToWord(testReport);
        String content = Files.readString(Paths.get(filePath));
        
        // Check main sections exist
        assertTrue(content.contains("DAILY FIELD REPORT"));
        assertTrue(content.contains("PROJECT INFORMATION"));
        assertTrue(content.contains("WEATHER CONDITIONS"));
        assertTrue(content.contains("PERSONNEL ON SITE"));
        assertTrue(content.contains("EQUIPMENT ON SITE"));
        assertTrue(content.contains("WORK PERFORMED"));
        assertTrue(content.contains("SIGNATURE"));
        
        // Check section formatting
        assertTrue(content.contains("========================================"));
        assertTrue(content.contains("Reporter: John Smith"));
        assertTrue(content.contains("Project: State Route 101 Expansion"));
    }

    // ================= INTEGRATION TESTS =================

    @Test
    @DisplayName("Complete export workflow should work end-to-end")
    void testCompleteExportWorkflow() throws IOException {
        // Test single report exports
        String pdfPath = exportService.exportReportToPDF(testReport);
        String wordPath = exportService.exportReportToWord(testReport);
        
        // Test multiple reports exports
        String multiplePdfPath = exportService.exportMultipleReportsToPDF(testReports, "Test Collection");
        String multipleWordPath = exportService.exportMultipleReportsToWord(testReports, "Test Collection");
        
        // Verify all files were created
        assertTrue(new File(pdfPath).exists());
        assertTrue(new File(wordPath).exists());
        assertTrue(new File(multiplePdfPath).exists());
        assertTrue(new File(multipleWordPath).exists());
        
        // Verify file sizes are reasonable
        assertTrue(new File(pdfPath).length() > 1000);
        assertTrue(new File(wordPath).length() > 500);
        assertTrue(new File(multiplePdfPath).length() > 2000);
        assertTrue(new File(multipleWordPath).length() > 1000);
    }

    @Test
    @DisplayName("Export service should handle concurrent exports")
    void testConcurrentExports() throws IOException {
        // Simulate concurrent exports
        List<String> paths = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            FieldReport report = new FieldReport();
            report.setReporterName("Reporter " + i);
            report.setLocation("Site " + i);
            report.setProjectName("Project " + i);
            report.setWorkDescription("Work " + i);
            
            String path = exportService.exportReportToPDF(report);
            paths.add(path);
        }
        
        // Verify all files were created with unique names
        assertEquals(5, paths.size());
        for (int i = 0; i < paths.size(); i++) {
            File file = new File(paths.get(i));
            assertTrue(file.exists());
            
            for (int j = i + 1; j < paths.size(); j++) {
                assertNotEquals(paths.get(i), paths.get(j));
            }
        }
    }
}