package com.fieldreport.demo;

import java.util.ArrayList;
import java.util.List;

import com.fieldreport.model.EquipmentOnSite;
import com.fieldreport.model.FieldReport;
import com.fieldreport.model.InspectionTesting;
import com.fieldreport.model.MaterialDelivered;
import com.fieldreport.model.PersonnelOnSite;
import com.fieldreport.service.ReportExportService;

/**
 * Comprehensive demo showcasing the new comprehensive field report structure
 * including all new tables and fields.
 * 
 * This demo creates a sample field report with:
 * - Project details (name, number, location, date)
 * - Weather conditions (AM/PM) and temperatures in Fahrenheit
 * - Personnel on site table
 * - Equipment on site table
 * - Materials delivered table
 * - Inspection/testing table
 * - Safety information
 * - Attachments tracking
 * - Signature
 * 
 * @author DailyFieldReport System
 * @version 2.0
 */
public class ComprehensiveFieldReportDemo {
    
    private static final String SEPARATOR = "============================================================";
    
    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("🏗️  COMPREHENSIVE FIELD REPORT DEMO");
        System.out.println(SEPARATOR);
        
        try {
            // Create comprehensive field report
            FieldReport report = createComprehensiveReport();
            
            // Display the report summary
            displayReportSummary(report);
            
            // Export the comprehensive report
            exportComprehensiveReport(report);
            
            System.out.println(SEPARATOR);
            System.out.println("🎉 Comprehensive Demo Completed Successfully!");
            System.out.println("📁 Check the 'exports' folder for generated comprehensive report");
            System.out.println(SEPARATOR);
            
        } catch (Exception e) {
            System.err.println("❌ Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a comprehensive field report with all new fields and tables populated.
     */
    private static FieldReport createComprehensiveReport() {
        FieldReport report = new FieldReport();
        
        // Basic information
        report.setReporterName("Michael Johnson");
        report.setLocation("Downtown Infrastructure Project Site");
        report.setProjectName("Metropolitan Bridge Reconstruction");
        report.setProjectNumber("MBR-2025-789");
        
        // Weather and temperature
        report.setWeatherAM("Clear skies, light breeze");
        report.setWeatherPM("Partly cloudy, calm");
        report.setTemperatureHigh("78°F");
        report.setTemperatureLow("62°F");
        
        // Work performed today
        report.setWorkPerformedToday("Completed structural steel installation for east span. " +
                "Performed concrete pour for foundation section C-3. " +
                "Conducted safety inspection of scaffolding systems.");
        
        // Work description and notes
        report.setWorkDescription("Major progress on bridge reconstruction including steel beam placement " +
                "and concrete foundation work. All safety protocols followed.");
        report.setNotes("Weather conditions were favorable for concrete work. " +
                "Next phase scheduled for Monday pending material delivery.");
        
        // Personnel on site
        List<PersonnelOnSite> personnel = new ArrayList<>();
        personnel.add(new PersonnelOnSite("ABC Construction", "Site Supervisor", 1, 8.0, "Mike Johnson"));
        personnel.add(new PersonnelOnSite("ABC Construction", "Steel Workers", 4, 8.0, "Tom Wilson"));
        personnel.add(new PersonnelOnSite("XYZ Concrete", "Concrete Crew", 3, 6.0, "Sarah Davis"));
        personnel.add(new PersonnelOnSite("Safety Plus", "Safety Inspector", 1, 4.0, "Bob Smith"));
        report.setPersonnelOnSite(personnel);
        
        // Equipment on site
        List<EquipmentOnSite> equipment = new ArrayList<>();
        equipment.add(new EquipmentOnSite("Tower Crane", "500-ton capacity", 1, true, ""));
        equipment.add(new EquipmentOnSite("Concrete Mixer", "10 cubic yard", 2, true, ""));
        equipment.add(new EquipmentOnSite("Excavator", "CAT 330", 1, false, "Hydraulic leak - under repair"));
        equipment.add(new EquipmentOnSite("Welding Equipment", "Portable units", 3, true, ""));
        report.setEquipmentOnSite(equipment);
        
        // Materials delivered
        List<MaterialDelivered> materials = new ArrayList<>();
        materials.add(new MaterialDelivered("Steel I-Beams", "Steel Supply Co", "12 pieces", 
                "Yard Area A", "Inspected - Approved"));
        materials.add(new MaterialDelivered("Ready-Mix Concrete", "City Concrete", "15 cubic yards", 
                "Foundation Section C-3", "Tested - Meets spec"));
        materials.add(new MaterialDelivered("Rebar", "Iron Works Inc", "2 tons", 
                "Storage Shed B", "Pending inspection"));
        report.setMaterialsDelivered(materials);
        
        // Inspections and testing
        List<InspectionTesting> inspections = new ArrayList<>();
        inspections.add(new InspectionTesting("Steel Weld Inspection", "John Roberts", 
                "City Building Dept", "All welds passed visual and dye penetrant testing"));
        inspections.add(new InspectionTesting("Concrete Slump Test", "Maria Garcia", 
                "Quality Labs", "Slump: 4 inches - Within specification"));
        inspections.add(new InspectionTesting("Safety Equipment Check", "Bob Smith", 
                "Safety Plus", "All harnesses and scaffolding inspected and approved"));
        report.setInspectionsTesting(inspections);
        
        // Safety information
        report.setSafetyMeetingHeld("Y");
        report.setSafetyMeetingTopic("Fall Protection and Scaffolding Safety");
        report.setSafetyMeetingAttendees("All site personnel (9 attendees)");
        report.setIncidentsNearMisses("N");
        report.setIncidentsDescription("");
        
        // Project management sections
        report.setDelaysIssuesNonConformance("Minor delay in concrete delivery (30 minutes) due to traffic. " +
                "No impact on schedule.");
        report.setCoordinationVisitsCommunication("Coordinated with city inspector for steel inspection. " +
                "Conference call with project manager regarding next week's deliveries.");
        
        // Attachments
        report.setPhotosAttached(true);
        report.setTestResultsAttached(true);
        report.setDrawingsSketchesAttached(false);
        
        // Pictures
        List<String> pictures = new ArrayList<>();
        pictures.add("photos/steel_beam_installation_001.jpg");
        pictures.add("photos/concrete_pour_section_c3.jpg");
        pictures.add("photos/safety_inspection_scaffolding.jpg");
        report.setPicturePaths(pictures);
        
        // Final fields
        report.setRemarks("Excellent progress today. Team worked efficiently and safely. " +
                "Project remains on schedule for completion.");
        report.setSignature("Michael Johnson, Site Supervisor");
        
        return report;
    }
    
    /**
     * Displays a summary of the comprehensive report.
     */
    private static void displayReportSummary(FieldReport report) {
        System.out.println("\n📋 COMPREHENSIVE FIELD REPORT SUMMARY");
        System.out.println("=====================================");
        System.out.println("Project: " + report.getProjectName());
        System.out.println("Project Number: " + report.getProjectNumber());
        System.out.println("Location: " + report.getLocation());
        System.out.println("Reporter: " + report.getReporterName());
        System.out.println("Date: " + report.getDate());
        
        System.out.println("\n🌤️ Weather & Temperature:");
        System.out.println("AM: " + report.getWeatherAM());
        System.out.println("PM: " + report.getWeatherPM());
        System.out.println("High: " + report.getTemperatureHigh() + " | Low: " + report.getTemperatureLow());
        
        System.out.println("\n👥 Personnel Summary:");
        System.out.println("Total Personnel: " + report.getPersonnelOnSite().size() + " entries");
        double totalWorkerHours = report.getPersonnelOnSite().stream()
                .mapToDouble(p -> p.getNumberOfWorkers() * p.getHoursWorked())
                .sum();
        System.out.println("Total Worker-Hours: " + totalWorkerHours);
        
        System.out.println("\n🚜 Equipment Summary:");
        System.out.println("Total Equipment: " + report.getEquipmentOnSite().size() + " entries");
        long operatingEquipment = report.getEquipmentOnSite().stream()
                .filter(e -> e.isOperating())
                .count();
        System.out.println("Operating Equipment: " + operatingEquipment + "/" + report.getEquipmentOnSite().size());
        
        System.out.println("\n📦 Materials Summary:");
        System.out.println("Materials Delivered: " + report.getMaterialsDelivered().size() + " entries");
        
        System.out.println("\n🔍 Inspections Summary:");
        System.out.println("Inspections/Tests: " + report.getInspectionsTesting().size() + " entries");
        
        System.out.println("\n🛡️ Safety Summary:");
        System.out.println("Safety Meeting: " + report.getSafetyMeetingHeld());
        System.out.println("Incidents/Near Misses: " + report.getIncidentsNearMisses());
        
        System.out.println("\n📎 Attachments:");
        System.out.println("Photos: " + (report.isPhotosAttached() ? "✅" : "❌"));
        System.out.println("Test Results: " + (report.isTestResultsAttached() ? "✅" : "❌"));
        System.out.println("Drawings/Sketches: " + (report.isDrawingsSketchesAttached() ? "✅" : "❌"));
        
        System.out.println("\n✍️ Signed by: " + report.getSignature());
    }
    
    /**
     * Exports the comprehensive report to both HTML and text formats.
     */
    private static void exportComprehensiveReport(FieldReport report) {
        System.out.println("\n🔄 Exporting Comprehensive Report...");
        
        try {
            ReportExportService exportService = new ReportExportService();
            
            // Export to HTML
            String htmlFile = exportService.exportReportToPDF(report);
            System.out.println("✅ HTML export: " + htmlFile);
            
            // Export to Text
            String textFile = exportService.exportReportToWord(report);
            System.out.println("✅ Text export: " + textFile);
            
        } catch (Exception e) {
            System.err.println("❌ Export error: " + e.getMessage());
        }
    }
}