package com.fieldreport.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fieldreport.model.EquipmentOnSite;
import com.fieldreport.model.FieldReport;
import com.fieldreport.model.InspectionTesting;
import com.fieldreport.model.MaterialDelivered;
import com.fieldreport.model.PersonnelOnSite;
import com.fieldreport.service.FieldReportService;
import com.fieldreport.service.ReportExportService;

/**
 * Web controller for handling field report download endpoints.
 * Simplified version using Java's built-in HTTP server.
 * 
 * @author Daily Field Report System
 * @version 1.0.0
 */
public class ReportWebController {
    
    private final FieldReportService fieldReportService;
    private final ReportExportService exportService;
    
    /**
     * Initialize the web controller with required services.
     */
    public ReportWebController(FieldReportService fieldReportService, ReportExportService exportService) {
        this.fieldReportService = fieldReportService;
        this.exportService = exportService;
    }
    
    /**
     * Generate HTML report from form data.
     */
    public String generateHTMLReport(Map<String, String> formData) throws Exception {
        FieldReport report = createReportFromFormData(formData);
        return exportService.exportReportToPDF(report);
    }
    
    /**
     * Generate text report from form data.
     */
    public String generateTextReport(Map<String, String> formData) throws Exception {
        FieldReport report = createReportFromFormData(formData);
        return exportService.exportReportToWord(report);
    }
    
    /**
     * Generate sample HTML report.
     */
    public String generateSampleHTMLReport() throws Exception {
        FieldReport sampleReport = createSampleReport();
        return exportService.exportReportToPDF(sampleReport);
    }
    
    /**
     * Generate sample text report.
     */
    public String generateSampleTextReport() throws Exception {
        FieldReport sampleReport = createSampleReport();
        return exportService.exportReportToWord(sampleReport);
    }
    
    /**
     * Create a FieldReport object from form data.
     */
    private FieldReport createReportFromFormData(Map<String, String> formData) {
        FieldReport report = new FieldReport();
        
        // Basic information
        String reportedBy = formData.getOrDefault("reportedBy", "Unknown");
        report.setReporterName(reportedBy);
        
        String location = formData.getOrDefault("location", "");
        report.setLocation(location);
        
        String projectName = formData.getOrDefault("projectName", "");
        report.setProjectName(projectName);
        
        String dateStr = formData.get("date");
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                report.setDate(LocalDate.parse(dateStr));
            } catch (Exception e) {
                report.setDate(LocalDate.now());
            }
        }
        
        // Weather information
        String weatherAM = formData.getOrDefault("weatherAM", "");
        report.setWeatherAM(weatherAM);
        
        String temperatureAM = formData.getOrDefault("temperatureAM", "");
        report.setTemperatureHigh(temperatureAM);
        
        String weatherPM = formData.getOrDefault("weatherPM", "");
        report.setWeatherPM(weatherPM);
        
        String temperaturePM = formData.getOrDefault("temperaturePM", "");
        report.setTemperatureLow(temperaturePM);
        
        // Work performed
        String workPerformed = formData.getOrDefault("workPerformed", "");
        report.setWorkPerformedToday(workPerformed);
        report.setWorkDescription(workPerformed);  // Set both fields
        
        String safetyNotes = formData.getOrDefault("safetyNotes", "");
        report.setRemarks(safetyNotes);
        report.setNotes(safetyNotes);  // Set both fields
        
        // Add default personnel
        List<PersonnelOnSite> personnel = new ArrayList<>();
        personnel.add(new PersonnelOnSite(
            reportedBy,
            "General",
            1,
            8.0,
            reportedBy
        ));
        report.setPersonnelOnSite(personnel);
        
        // Add default equipment
        List<EquipmentOnSite> equipment = new ArrayList<>();
        equipment.add(new EquipmentOnSite(
            "General Equipment",
            "Standard",
            1,
            true,
            ""
        ));
        report.setEquipmentOnSite(equipment);
        
        // Add default materials
        List<MaterialDelivered> materials = new ArrayList<>();
        materials.add(new MaterialDelivered(
            "Construction Materials",
            "Local Supplier",
            "As needed",
            "Site Storage",
            "Standard delivery"
        ));
        report.setMaterialsDelivered(materials);
        
        // Add default inspections
        List<InspectionTesting> inspections = new ArrayList<>();
        inspections.add(new InspectionTesting(
            "General Inspection",
            "Passed",
            reportedBy,
            "Standard inspection completed"
        ));
        report.setInspectionsTesting(inspections);
        
        return report;
    }
    
    /**
     * Create a sample field report for demonstration purposes.
     */
    private FieldReport createSampleReport() {
        FieldReport report = new FieldReport();
        report.setReporterName("John Smith");
        report.setLocation("Construction Site A");
        report.setProjectName("Sample Bridge Project");
        report.setDate(LocalDate.now());
        report.setWeatherAM("Sunny");
        report.setTemperatureHigh("22°C");
        report.setWeatherPM("Partly Cloudy");
        report.setTemperatureLow("25°C");
        report.setWorkPerformedToday("Foundation work and steel beam installation");
        report.setWorkDescription("Completed excavation for foundation. Installed steel reinforcement bars according to specifications. Poured concrete for foundation walls.");
        report.setNotes("Quality checks completed successfully. All work meets project specifications.");
        report.setRemarks("All safety protocols followed. No incidents reported.");
        
        // Sample personnel
        List<PersonnelOnSite> personnel = new ArrayList<>();
        personnel.add(new PersonnelOnSite("Smith Construction", "Site Manager", 
                                         1, 9.5, "John Smith"));
        personnel.add(new PersonnelOnSite("ABC Contractors", "Foreman", 
                                         1, 8.5, "Mike Johnson"));
        report.setPersonnelOnSite(personnel);
        
        // Sample equipment
        List<EquipmentOnSite> equipment = new ArrayList<>();
        equipment.add(new EquipmentOnSite("Excavator", "CAT 320", 1, true, ""));
        equipment.add(new EquipmentOnSite("Crane", "50-ton", 1, true, ""));
        report.setEquipmentOnSite(equipment);
        
        // Sample materials
        List<MaterialDelivered> materials = new ArrayList<>();
        materials.add(new MaterialDelivered("Concrete", "Ready Mix Inc", "25 cubic yards", 
                                          "Site Storage", "Delivered on time"));
        materials.add(new MaterialDelivered("Steel Beams", "Steel Works Ltd", "12 pieces", 
                                          "Staging Area", "Grade A quality"));
        report.setMaterialsDelivered(materials);
        
        // Sample inspections
        List<InspectionTesting> inspections = new ArrayList<>();
        inspections.add(new InspectionTesting("Foundation Inspection", "Passed", 
                                             "City Inspector", "Foundation meets specifications"));
        inspections.add(new InspectionTesting("Steel Quality Check", "Passed", 
                                             "QC Engineer", "All welds approved"));
        report.setInspectionsTesting(inspections);
        
        return report;
    }
    
    /**
     * Get the main web interface HTML page.
     */
    public String getMainPageHTML() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Daily Field Report - Download Center</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        max-width: 800px;
                        margin: 0 auto;
                        padding: 20px;
                        background-color: #f5f5f5;
                    }
                    .container {
                        background-color: white;
                        padding: 30px;
                        border-radius: 10px;
                        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                    }
                    h1 {
                        color: #2c3e50;
                        text-align: center;
                        margin-bottom: 30px;
                    }
                    .form-group {
                        margin-bottom: 20px;
                    }
                    label {
                        display: block;
                        margin-bottom: 5px;
                        font-weight: bold;
                        color: #34495e;
                    }
                    input, textarea {
                        width: 100%;
                        padding: 10px;
                        border: 1px solid #ddd;
                        border-radius: 5px;
                        font-size: 14px;
                        box-sizing: border-box;
                    }
                    textarea {
                        height: 100px;
                        resize: vertical;
                    }
                    .button-group {
                        display: flex;
                        gap: 10px;
                        margin-top: 30px;
                        flex-wrap: wrap;
                    }
                    button, .download-btn {
                        padding: 12px 24px;
                        border: none;
                        border-radius: 5px;
                        cursor: pointer;
                        font-size: 16px;
                        text-decoration: none;
                        display: inline-block;
                        text-align: center;
                        transition: background-color 0.3s;
                    }
                    .btn-primary {
                        background-color: #3498db;
                        color: white;
                    }
                    .btn-primary:hover {
                        background-color: #2980b9;
                    }
                    .btn-secondary {
                        background-color: #95a5a6;
                        color: white;
                    }
                    .btn-secondary:hover {
                        background-color: #7f8c8d;
                    }
                    .sample-section {
                        margin-top: 40px;
                        padding-top: 30px;
                        border-top: 2px solid #ecf0f1;
                    }
                    .sample-section h2 {
                        color: #2c3e50;
                        margin-bottom: 15px;
                    }
                    .sample-section p {
                        color: #7f8c8d;
                        margin-bottom: 20px;
                    }
                    .alert {
                        padding: 15px;
                        margin: 20px 0;
                        border: 1px solid transparent;
                        border-radius: 4px;
                        background-color: #d4edda;
                        border-color: #c3e6cb;
                        color: #155724;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>🏗️ Daily Field Report - Download Center</h1>
                    
                    <div class="alert">
                        <strong>Welcome!</strong> Generate and download your field reports in HTML or text format.
                        HTML files can be converted to PDF using your browser's print function.
                    </div>
                    
                    <form id="reportForm">
                        <div class="form-group">
                            <label for="reportedBy">Reported By:</label>
                            <input type="text" id="reportedBy" name="reportedBy" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="location">Location:</label>
                            <input type="text" id="location" name="location" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="projectName">Project Name:</label>
                            <input type="text" id="projectName" name="projectName" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="date">Date:</label>
                            <input type="date" id="date" name="date" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="weatherAM">Weather Conditions (AM):</label>
                            <input type="text" id="weatherAM" name="weatherAM" placeholder="e.g., Sunny, Cloudy, Rainy">
                        </div>
                        
                        <div class="form-group">
                            <label for="temperatureAM">Temperature (AM):</label>
                            <input type="text" id="temperatureAM" name="temperatureAM" placeholder="e.g., 22°C">
                        </div>
                        
                        <div class="form-group">
                            <label for="weatherPM">Weather Conditions (PM):</label>
                            <input type="text" id="weatherPM" name="weatherPM" placeholder="e.g., Partly Cloudy">
                        </div>
                        
                        <div class="form-group">
                            <label for="temperaturePM">Temperature (PM):</label>
                            <input type="text" id="temperaturePM" name="temperaturePM" placeholder="e.g., 25°C">
                        </div>
                        
                        <div class="form-group">
                            <label for="workPerformed">Work Performed:</label>
                            <textarea id="workPerformed" name="workPerformed" placeholder="Describe the work completed today..."></textarea>
                        </div>
                        
                        <div class="form-group">
                            <label for="safetyNotes">Safety Notes:</label>
                            <textarea id="safetyNotes" name="safetyNotes" placeholder="Any safety observations or incidents..."></textarea>
                        </div>
                        
                        <div class="button-group">
                            <button type="button" class="btn-primary" onclick="downloadReport('html')">
                                📄 Download as HTML
                            </button>
                            <button type="button" class="btn-primary" onclick="downloadReport('text')">
                                📝 Download as Text
                            </button>
                        </div>
                    </form>
                    
                    <div class="sample-section">
                        <h2>📋 Sample Reports</h2>
                        <p>Download sample reports to see the format and structure:</p>
                        <div class="button-group">
                            <a href="/api/reports/sample-html" class="download-btn btn-secondary">
                                📄 Sample HTML Report
                            </a>
                            <a href="/api/reports/sample-text" class="download-btn btn-secondary">
                                📝 Sample Text Report
                            </a>
                        </div>
                    </div>
                </div>
                
                <script>
                    // Set today's date as default
                    document.getElementById('date').value = new Date().toISOString().split('T')[0];
                    
                    function downloadReport(format) {
                        const form = document.getElementById('reportForm');
                        const formData = new FormData(form);
                        
                        // Validate required fields
                        const requiredFields = ['reportedBy', 'location', 'projectName', 'date'];
                        for (let field of requiredFields) {
                            if (!formData.get(field)) {
                                alert('Please fill in all required fields.');
                                return;
                            }
                        }
                        
                        const endpoint = format === 'html' ? 
                            '/api/reports/generate-html' : 
                            '/api/reports/generate-text';
                        
                        // Create a temporary form for file download
                        const downloadForm = document.createElement('form');
                        downloadForm.method = 'POST';
                        downloadForm.action = endpoint;
                        downloadForm.style.display = 'none';
                        
                        // Add all form data to the download form
                        for (let [key, value] of formData) {
                            const input = document.createElement('input');
                            input.type = 'hidden';
                            input.name = key;
                            input.value = value;
                            downloadForm.appendChild(input);
                        }
                        
                        document.body.appendChild(downloadForm);
                        downloadForm.submit();
                        document.body.removeChild(downloadForm);
                    }
                </script>
            </body>
            </html>
        """;
    }
}