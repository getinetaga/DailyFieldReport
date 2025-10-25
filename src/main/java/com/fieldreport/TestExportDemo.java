package com.fieldreport;

import java.util.Arrays;
import java.util.List;

import com.fieldreport.model.FieldReport;
import com.fieldreport.service.ReportExportService;

/**
 * Test demonstration class for the Daily Field Report export functionality.
 * 
 * This class provides a controlled testing environment for the report export
 * features without requiring user input or infinite loops. It creates sample
 * field reports and demonstrates the HTML and text export capabilities with
 * the DFR logo integration.
 * 
 * Key Features:
 * - Creates realistic sample field report data
 * - Tests single report export to HTML format
 * - Tests multiple report export to HTML format
 * - Tests text format export functionality
 * - Validates file creation and content generation
 * - Provides console feedback for each operation
 * - Terminates cleanly after one complete test cycle
 * 
 * Usage:
 * Run this class independently to test export functionality without
 * the full application interface. All exported files will be saved
 * to the current working directory with timestamped filenames.
 * 
 * Test Scenarios:
 * 1. Single Report HTML Export - Tests individual report processing
 * 2. Multiple Reports HTML Export - Tests batch processing capabilities
 * 3. Text Format Export - Tests alternative export format
 * 4. File Validation - Confirms successful file creation
 * 
 * @author Daily Field Report System
 * @version 1.0
 * @since 2025-10-24
 */
public class TestExportDemo {
    
    /**
     * Main method to execute the export functionality demonstration.
     * 
     * Creates sample data, tests various export scenarios, and provides
     * console feedback about the results. Runs once and terminates cleanly
     * to avoid infinite loop issues in testing environments.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("🗂️  Daily Field Report Export Demo");
        System.out.println("============================================================");
        
        try {
            // Create sample field reports
            FieldReport report1 = createSampleReport1();
            FieldReport report2 = createSampleReport2();
            
            // Create export service
            ReportExportService exportService = new ReportExportService();
            
            System.out.println("\n📋 Created sample reports:");
            System.out.println("Report 1: " + report1.getReporterName() + " - " + report1.getProjectName());
            System.out.println("Report 2: " + report2.getReporterName() + " - " + report2.getProjectName());
            
            System.out.println("\n🔄 Testing export functionality...\n");
            
            // Test 1: Export single report to HTML (PDF format)
            System.out.println("📄 Test 1: Exporting single report to HTML (PDF format)");
            String pdfFile = exportService.exportReportToPDF(report1);
            System.out.println("✅ Created: " + pdfFile + "\n");
            
            // Test 2: Export single report to Text (Word format)
            System.out.println("📝 Test 2: Exporting single report to Text (Word format)");
            String wordFile = exportService.exportReportToWord(report1);
            System.out.println("✅ Created: " + wordFile + "\n");
            
            // Test 3: Export multiple reports to HTML
            System.out.println("📄 Test 3: Exporting multiple reports to HTML collection");
            List<FieldReport> reports = Arrays.asList(report1, report2);
            String multiPdfFile = exportService.exportMultipleReportsToPDF(reports, "October 2025 Reports");
            System.out.println("✅ Created: " + multiPdfFile + "\n");
            
            // Test 4: Export multiple reports to Text
            System.out.println("📝 Test 4: Exporting multiple reports to Text collection");
            String multiWordFile = exportService.exportMultipleReportsToWord(reports, "October 2025 Reports");
            System.out.println("✅ Created: " + multiWordFile + "\n");
            
            System.out.println("============================================================");
            System.out.println("🎉 Export Demo Completed Successfully!");
            System.out.println("📁 Check the 'exports' folder for generated files");
            System.out.println("💡 HTML files include the DFR logo and can be printed to PDF");
            System.out.println("============================================================");
            
        } catch (Exception e) {
            System.err.println("❌ Error during export demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static FieldReport createSampleReport1() {
        FieldReport report = new FieldReport();
        report.setReporterName("John Smith");
        report.setLocation("Construction Site Downtown");
        report.setProjectName("Bridge Renovation Project");
        report.setProjectNumber("BR-2025-001");
        report.setWeatherAM("Sunny, Clear skies");
        report.setWeatherPM("Partly cloudy");
        report.setTemperatureHigh("77°F");
        report.setTemperatureLow("65°F");
        report.setWorkDescription("Completed foundation inspection and began steel beam reinforcement. All safety protocols were followed and equipment checks were performed. Work progress is on schedule.");
        report.setNotes("Minor delay due to material delivery, resolved by end of day.");
        
        // Add sample pictures
        report.addPicturePath("/Users/johnsmith/Pictures/bridge_foundation.jpg");
        report.addPicturePath("/Users/johnsmith/Pictures/steel_beam_work.jpg");
        report.addPicturePath("/Users/johnsmith/Pictures/safety_equipment_check.jpg");
        
        return report;
    }
    
    private static FieldReport createSampleReport2() {
        FieldReport report = new FieldReport();
        report.setReporterName("Jane Doe");
        report.setLocation("Manufacturing Plant B");
        report.setProjectName("Factory Upgrade Project");
        report.setProjectNumber("FU-2025-002");
        report.setWeatherAM("Overcast, Light rain");
        report.setWeatherPM("Clearing up");
        report.setTemperatureHigh("68°F");
        report.setTemperatureLow("57°F");
        report.setWorkDescription("Installed new electrical systems and tested safety circuits. Coordinated with electrical contractors for final inspections.");
        report.setNotes("All systems operational, ready for next phase.");
        
        // Add sample pictures
        report.addPicturePath("/Users/janedoe/Pictures/electrical_installation.jpg");
        report.addPicturePath("/Users/janedoe/Pictures/safety_circuit_test.jpg");
        
        return report;
    }
}