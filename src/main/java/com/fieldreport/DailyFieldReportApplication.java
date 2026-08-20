package com.fieldreport;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fieldreport.model.EquipmentOnSite;
import com.fieldreport.model.FieldReport;
import com.fieldreport.model.InspectionTesting;
import com.fieldreport.model.MaterialDelivered;
import com.fieldreport.model.PersonnelOnSite;
import com.fieldreport.service.FieldReportService;
import com.fieldreport.service.ReportExportService;

/**
 * Daily Field Report Management Application
 * 
 * A comprehensive console-based application for managing daily field reports
 * in construction, maintenance, and field operations environments. This application
 * provides a complete suite of features for creating, viewing, searching, updating,
 * and exporting field reports with an intuitive menu-driven interface.
 * 
 * Key Features:
 * - Interactive console interface with professional formatting
 * - Complete CRUD operations for field reports
 * - Advanced search and filtering capabilities
 * - Professional document export (HTML and text formats)
 * - Data validation and error handling
 * - User-friendly input prompts and confirmations
 * 
 * Navigation Menu Options:
 * 1. Create New Field Report - Add a new daily report with all required fields
 * 2. View All Reports - Display comprehensive list of all stored reports
 * 3. Search Reports by Date - Find reports for specific dates or date ranges
 * 4. Search Reports by Reporter - Filter reports by submitter name
 * 5. Search Reports by Project - Find all reports for a specific project
 * 6. Update Existing Report - Modify details of previously created reports
 * 7. Delete Report - Remove reports from the system with confirmation
 * 8. Export Reports - Generate professional documents in HTML/text format
 * 9. Exit Application - Safe shutdown with optional data summary
 * 
 * Technical Details:
 * - Built with Java 23 for modern language features
 * - Uses in-memory storage for simplicity and performance
 * - Implements service layer architecture for maintainability
 * - Follows object-oriented design principles
 * - Includes comprehensive input validation and error handling
 * 
 * Usage:
 * Run the application from command line or IDE. The system will display
 * a welcome message and present the main menu. Follow the numbered options
 * to navigate through different features. Each operation provides clear
 * instructions and feedback messages.
 * 
 * @author Daily Field Report System Development Team
 * @version 1.0
 * @since 2025-10-24
 */
public class DailyFieldReportApplication {
    
    // ================= APPLICATION CONSTANTS =================
    
    /** Application title displayed in headers and welcome messages */
    private static final String APP_TITLE = "Daily Field Report Management System";
    
    /** Current version of the application */
    private static final String APP_VERSION = "v1.0";
    
    /** Main separator for visual formatting of output */
    private static final String SEPARATOR = "=".repeat(60);
    
    /** Secondary separator for menu sections */
    private static final String MENU_SEPARATOR = "-".repeat(40);
    
    // ================= CORE SERVICES =================
    
    /** Service for managing field report data and operations */
    private final FieldReportService dailyFieldReportService;
    
    /** Service for exporting reports to various document formats */
    private final ReportExportService reportExportService;
    
    /** Scanner for reading user input from console */
    private final Scanner inputScanner;
    
    /**
     * Constructs a new Daily Field Report Application instance.
     * 
     * Initializes all core services and sets up the input scanner
     * for user interaction. The application is ready to use immediately
     * after construction.
     */
    public DailyFieldReportApplication() {
        this.dailyFieldReportService = new FieldReportService();
        this.reportExportService = new ReportExportService();
        this.inputScanner = new Scanner(System.in);
    }
    
    /**
     * Application entry point - starts the Daily Field Report Management System.
     * 
     * Creates a new application instance and launches either the console interface
     * or the web interface based on command line arguments.
     * 
     * Command line options:
     * - No arguments: Console interface (interactive menu)
     * - "web": Web interface (browser-based)
     * - "web [port]": Web interface on specified port
     * 
     * @param args command line arguments:
     *            no args = console mode
     *            "web" = web mode on port 8080
     *            "web [port]" = web mode on specified port
     */
    public static void main(String[] args) {
        displayWelcomeHeader();
        
        // Check for web mode
        if (args.length > 0 && "web".equals(args[0])) {
            startWebMode(args);
        } else {
            startConsoleMode();
        }
        
        displayExitMessage();
    }
    
    /**
     * Start the application in web mode with browser interface.
     * 
     * @param args command line arguments for web configuration
     */
    private static void startWebMode(String[] args) {
        try {
            System.out.println("🌐 Starting web interface...");
            
            // Parse port if provided
            if (args.length > 1) {
                try {
                    int port = Integer.parseInt(args[1]);
                    com.fieldreport.web.ReportWebApplication webApp = new com.fieldreport.web.ReportWebApplication(port);
                    webApp.start();
                } catch (NumberFormatException e) {
                    System.out.println("⚠️  Invalid port number: " + args[1] + ". Using default port 8080.");
                    com.fieldreport.web.ReportWebApplication webApp = new com.fieldreport.web.ReportWebApplication();
                    webApp.start();
                }
            } else {
                com.fieldreport.web.ReportWebApplication webApp = new com.fieldreport.web.ReportWebApplication();
                webApp.start();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Failed to start web interface: " + e.getMessage());
            System.out.println("🔄 Falling back to console mode...");
            startConsoleMode();
        }
    }
    
    /**
     * Start the application in console mode with interactive menu.
     */
    private static void startConsoleMode() {
        System.out.println("💻 Starting console interface...");
        DailyFieldReportApplication dailyReportApp = new DailyFieldReportApplication();
        dailyReportApp.startApplication();
    }
    
    // ================= DISPLAY METHODS =================
    
    /**
     * Displays the welcome header with application branding.
     * 
     * Shows the application title, version, and startup message
     * with professional formatting to welcome users to the system.
     */
    private static void displayWelcomeHeader() {
        System.out.println(SEPARATOR);
        System.out.println("🗂️  " + APP_TITLE + " " + APP_VERSION);
        System.out.println("Starting application...");
        System.out.println(SEPARATOR);
    }
    
    /**
     * Displays the exit message when application shuts down.
     * 
     * Provides a professional farewell message and confirms
     * successful application termination for user assurance.
     */
    private static void displayExitMessage() {
        System.out.println(SEPARATOR);
        System.out.println("Thank you for using " + APP_TITLE + "!");
        System.out.println("Application terminated successfully.");
        System.out.println(SEPARATOR);
    }
    
    /**
     * Main application execution loop
     */
    public void startApplication() {
        displayApplicationWelcome();
        
        boolean applicationRunning = true;
        while (applicationRunning) {
            try {
                displayMainMenu();
                int userChoice = getUserMenuChoice();
                
                applicationRunning = processUserChoice(userChoice);
                
            } catch (Exception e) {
                System.out.println("⚠️  An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again or contact support if the problem persists.\n");
            }
        }
        
        cleanupResources();
    }
    
    /**
     * Display application welcome message
     */
    private void displayApplicationWelcome() {
        System.out.println("\n🏗️  Welcome to the Daily Field Report System!");
        System.out.println("Manage your field reports efficiently and professionally.");
        System.out.println("Current Date: " + LocalDate.now());
        System.out.println();
    }
    
    /**
     * Process user menu choice and return whether application should continue
     * @param choice User's menu selection
     * @return true if application should continue, false to exit
     */
    private boolean processUserChoice(int choice) {
        switch (choice) {
            case 1:
                createNewDailyFieldReport();
                break;
            case 2:
                viewAllDailyFieldReports();
                break;
            case 3:
                viewDailyFieldReportsByDate();
                break;
            case 4:
                viewDailyFieldReportsByReporter();
                break;
            case 5:
                viewDailyFieldReportsByProject();
                break;
            case 6:
                showReportStatistics();
                break;
            case 7:
                showExportMenu();
                break;
            case 0:
            case 8:
                System.out.println("\n✅ Exiting Daily Field Report Application...");
                return false;
            default:
                System.out.println("❌ Invalid choice. Please select a number between 1-8 or 0 to exit.\n");
        }
        return true;
    }
    
    /**
     * Cleanup application resources
     */
    private void cleanupResources() {
        if (inputScanner != null) {
            inputScanner.close();
        }
    }
    
    /**
     * Display the main application menu
     */
    private void displayMainMenu() {
        System.out.println(MENU_SEPARATOR);
        System.out.println("📋 FIELD REPORT - MAIN MENU");
        System.out.println(MENU_SEPARATOR);
        System.out.println("1. 📝 Create New Field Report");
        System.out.println("2. 📄 View All Field Reports");
        System.out.println("3. 📅 View Reports by Date");
        System.out.println("4. 👤 View Reports by Reporter");
        System.out.println("5. 🏗️  View Reports by Project");
        System.out.println("6. 📊 Show Report Statistics");
        System.out.println("7. � Export Reports (PDF/Word)");
        System.out.println("8. �🚪 Exit Application");
        System.out.println(MENU_SEPARATOR);
        System.out.print("➤ Please select an option (1-8): ");
    }
    
    /**
     * Get user menu choice with input validation
     * @return User's menu selection
     */
    private int getUserMenuChoice() {
        try {
            String input = inputScanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1; // Invalid choice indicator
        }
    }
    
    /**
     * Get validated string input from user
     * @param prompt The prompt to display to the user
     * @param fieldName The name of the field being collected
     * @return Trimmed user input
     */
    private String getValidatedStringInput(String prompt, String fieldName) {
        String input;
        do {
            System.out.print(prompt);
            input = inputScanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("❌ " + fieldName + " cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }
    
    /**
     * Get optional string input from user
     * @param prompt The prompt to display to the user
     * @return Trimmed user input (can be empty)
     */
    private String getOptionalStringInput(String prompt) {
        System.out.print(prompt);
        return inputScanner.nextLine().trim();
    }
    
    /**
     * Create a new daily field report with comprehensive input validation
     */
    private void createNewDailyFieldReport() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("📝 CREATE NEW FIELD REPORT");
        System.out.println(SEPARATOR);
        
        try {
            // Collect required information with validation
            String reporterName = getValidatedStringInput("👤 Enter reporter name: ", "Reporter name");
            String location = getValidatedStringInput("📍 Enter work location: ", "Location");
            String projectName = getValidatedStringInput("🏗️  Enter project name: ", "Project name");
            String projectNumber = getValidatedStringInput("🔢 Enter project number: ", "Project number");
            String weatherAM = getValidatedStringInput("� Enter AM weather conditions: ", "AM weather");
            String weatherPM = getValidatedStringInput("🌇 Enter PM weather conditions: ", "PM weather");
            String temperatureHigh = getValidatedStringInput("🌡️  Enter high temperature (°F): ", "High temperature");
            String temperatureLow = getValidatedStringInput("🌡️  Enter low temperature (°F): ", "Low temperature");
            String workDescription = getValidatedStringInput("📋 Enter work description: ", "Work description");
            
            // Optional notes
            String notes = getOptionalStringInput("📝 Enter any notes or issues (optional): ");
            
            // Create and save the report
            FieldReport dailyReport = new FieldReport(
                reporterName, 
                location, 
                projectName, 
                projectNumber,
                weatherAM,
                weatherPM,
                temperatureHigh,
                temperatureLow,
                workDescription, 
                notes
            );
            
            // Add personnel, equipment, materials, and inspections
            addPersonnelToReport(dailyReport);
            addEquipmentToReport(dailyReport);
            addMaterialsToReport(dailyReport);
            addInspectionsToReport(dailyReport);
            addSafetyInfoToReport(dailyReport);
            
            // Add pictures if user wants to
            addPicturesToReport(dailyReport);
            
            dailyFieldReportService.saveReport(dailyReport);
            
            // Display success message
            System.out.println("\n✅ Daily field report created successfully!");
            System.out.println("📄 Report ID: " + dailyReport.getId());
            System.out.println("📅 Report Date: " + dailyReport.getDate());
            System.out.println("⏰ Created At: " + dailyReport.getCreatedAt());
            
        } catch (Exception e) {
            System.out.println("❌ Error creating field report: " + e.getMessage());
        }
        
        waitForUserInput();
    }
    
    /**
     * View all daily field reports with enhanced formatting
     */
    private void viewAllDailyFieldReports() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("📄 ALL FIELD REPORTS");
        System.out.println(SEPARATOR);
        
        List<FieldReport> allReports = dailyFieldReportService.getAllReports();
        
        if (allReports.isEmpty()) {
            System.out.println("📭 No field reports found.");
            System.out.println("💡 Create your first report using option 1 from the main menu.");
        } else {
            System.out.println("📊 Total Reports: " + allReports.size());
            System.out.println();
            
            for (int i = 0; i < allReports.size(); i++) {
                System.out.println("📋 Report #" + (i + 1));
                displayDetailedReport(allReports.get(i));
                System.out.println();
            }
        }
        
        waitForUserInput();
    }
    
    /**
     * View daily field reports by specific date
     */
    private void viewDailyFieldReportsByDate() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("📅 VIEW REPORTS BY DATE");
        System.out.println(SEPARATOR);
        
        System.out.print("📅 Enter date (YYYY-MM-DD): ");
        String dateInput = inputScanner.nextLine().trim();
        
        try {
            LocalDate searchDate = LocalDate.parse(dateInput);
            List<FieldReport> reportsForDate = dailyFieldReportService.getReportsByDate(searchDate);
            
            if (reportsForDate.isEmpty()) {
                System.out.println("📭 No reports found for " + searchDate);
            } else {
                System.out.println("📋 Reports for " + searchDate + " (" + reportsForDate.size() + " found):");
                System.out.println();
                
                for (int i = 0; i < reportsForDate.size(); i++) {
                    System.out.println("📋 Report #" + (i + 1));
                    displayDetailedReport(reportsForDate.get(i));
                    System.out.println();
                }
            }
            
        } catch (DateTimeParseException e) {
            System.out.println("❌ Invalid date format. Please use YYYY-MM-DD format (e.g., 2024-03-15).");
        }
        
        waitForUserInput();
    }
    
    /**
     * View daily field reports by reporter name
     */
    private void viewDailyFieldReportsByReporter() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("👤 VIEW REPORTS BY REPORTER");
        System.out.println(SEPARATOR);
        
        String reporterName = getValidatedStringInput("👤 Enter reporter name: ", "Reporter name");
        List<FieldReport> reportsByReporter = dailyFieldReportService.getReportsByReporter(reporterName);
        
        if (reportsByReporter.isEmpty()) {
            System.out.println("📭 No reports found for reporter: " + reporterName);
        } else {
            System.out.println("📋 Reports by " + reporterName + " (" + reportsByReporter.size() + " found):");
            System.out.println();
            
            for (int i = 0; i < reportsByReporter.size(); i++) {
                System.out.println("📋 Report #" + (i + 1));
                displayDetailedReport(reportsByReporter.get(i));
                System.out.println();
            }
        }
        
        waitForUserInput();
    }
    
    /**
     * View daily field reports by project name
     */
    private void viewDailyFieldReportsByProject() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("🏗️ VIEW REPORTS BY PROJECT");
        System.out.println(SEPARATOR);
        
        String projectName = getValidatedStringInput("🏗️ Enter project name: ", "Project name");
        List<FieldReport> reportsByProject = dailyFieldReportService.getReportsByProject(projectName);
        
        if (reportsByProject.isEmpty()) {
            System.out.println("📭 No reports found for project: " + projectName);
        } else {
            System.out.println("📋 Reports for project '" + projectName + "' (" + reportsByProject.size() + " found):");
            System.out.println();
            
            for (int i = 0; i < reportsByProject.size(); i++) {
                System.out.println("📋 Report #" + (i + 1));
                displayDetailedReport(reportsByProject.get(i));
                System.out.println();
            }
        }
        
        waitForUserInput();
    }
    
    /**
     * Show comprehensive report statistics
     */
    private void showReportStatistics() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("📊 FIELD REPORT STATISTICS");
        System.out.println(SEPARATOR);
        
        List<FieldReport> allReports = dailyFieldReportService.getAllReports();
        
        if (allReports.isEmpty()) {
            System.out.println("📭 No reports available for statistics.");
        } else {
            System.out.println("📊 Total Reports: " + allReports.size());
            System.out.println("📅 Date Range: " + getDateRange(allReports));
            System.out.println("👥 Unique Reporters: " + getUniqueReporters(allReports).size());
            System.out.println("🏗️ Unique Projects: " + getUniqueProjects(allReports).size());
            System.out.println("📍 Unique Locations: " + getUniqueLocations(allReports).size());
        }
        
        waitForUserInput();
    }
    
    /**
     * Display detailed information for a single field report
     * @param report The field report to display
     */
    private void displayDetailedReport(FieldReport report) {
        System.out.println("┌" + "─".repeat(58) + "┐");
        System.out.println("│ 📄 Report ID: " + String.format("%-42s", report.getId()) + "│");
        System.out.println("│ 📅 Date: " + String.format("%-47s", report.getDate()) + "│");
        System.out.println("│ ⏰ Created: " + String.format("%-44s", report.getCreatedAt()) + "│");
        System.out.println("│ 👤 Reporter: " + String.format("%-43s", report.getReporterName()) + "│");
        System.out.println("│ 📍 Location: " + String.format("%-43s", report.getLocation()) + "│");
        System.out.println("│ 🏗️ Project: " + String.format("%-44s", report.getProjectName()) + "│");
        System.out.println("│ 🌤️ Weather AM: " + String.format("%-42s", report.getWeatherAM()) + "│");
        System.out.println("│ 🌇 Weather PM: " + String.format("%-42s", report.getWeatherPM()) + "│");
        System.out.println("│ 🌡️ High Temp: " + String.format("%-42s", report.getTemperatureHigh()) + "│");
        System.out.println("│ 🌡️ Low Temp: " + String.format("%-43s", report.getTemperatureLow()) + "│");
        System.out.println("├" + "─".repeat(58) + "┤");
        
        // Work description (may need wrapping)
        String workDesc = report.getWorkDescription();
        System.out.println("│ 📋 Work Description:");
        wrapAndDisplayText(workDesc, "│    ");
        
        // Notes (if present)
        if (report.getNotes() != null && !report.getNotes().trim().isEmpty()) {
            System.out.println("├" + "─".repeat(58) + "┤");
            System.out.println("│ 📝 Notes:");
            wrapAndDisplayText(report.getNotes(), "│    ");
        }
        
        // Personnel On Site Table
        if (report.getPersonnelOnSite() != null && !report.getPersonnelOnSite().isEmpty()) {
            System.out.println("├" + "─".repeat(58) + "┤");
            System.out.println("│ 👥 PERSONNEL ON SITE (" + report.getPersonnelOnSite().size() + ")");
            System.out.println("├" + "─".repeat(58) + "┤");
            for (int i = 0; i < report.getPersonnelOnSite().size(); i++) {
                var person = report.getPersonnelOnSite().get(i);
                System.out.println("│ " + (i+1) + ". Company: " + String.format("%-42s", person.getCompany()) + "│");
                System.out.println("│    Trade/Role: " + String.format("%-39s", person.getTradeRole()) + "│");
                System.out.println("│    Workers: " + person.getNumberOfWorkers() + 
                                 ", Hours: " + person.getHoursWorked() + 
                                 ", Supervisor: " + String.format("%-20s", person.getForemanSupervisor()) + "│");
                if (i < report.getPersonnelOnSite().size() - 1) {
                    System.out.println("│" + " ".repeat(58) + "│");
                }
            }
        }

        // Equipment On Site Table
        if (report.getEquipmentOnSite() != null && !report.getEquipmentOnSite().isEmpty()) {
            System.out.println("├" + "─".repeat(58) + "┤");
            System.out.println("│ 🚜 EQUIPMENT ON SITE (" + report.getEquipmentOnSite().size() + ")");
            System.out.println("├" + "─".repeat(58) + "┤");
            for (int i = 0; i < report.getEquipmentOnSite().size(); i++) {
                var equipment = report.getEquipmentOnSite().get(i);
                System.out.println("│ " + (i+1) + ". Equipment: " + String.format("%-39s", equipment.getEquipment()) + "│");
                System.out.println("│    Type/Size: " + String.format("%-41s", equipment.getTypeSize()) + "│");
                System.out.println("│    Quantity: " + equipment.getQuantity() + 
                                 ", Status: " + String.format("%-35s", equipment.getOperatingStatus()) + "│");
                if (equipment.getIdleReason() != null && !equipment.getIdleReason().trim().isEmpty()) {
                    System.out.println("│    Idle Reason: " + String.format("%-37s", equipment.getIdleReason()) + "│");
                }
                if (i < report.getEquipmentOnSite().size() - 1) {
                    System.out.println("│" + " ".repeat(58) + "│");
                }
            }
        }

        // Materials Delivered Table
        if (report.getMaterialsDelivered() != null && !report.getMaterialsDelivered().isEmpty()) {
            System.out.println("├" + "─".repeat(58) + "┤");
            System.out.println("│ 📦 MATERIALS DELIVERED (" + report.getMaterialsDelivered().size() + ")");
            System.out.println("├" + "─".repeat(58) + "┤");
            for (int i = 0; i < report.getMaterialsDelivered().size(); i++) {
                var material = report.getMaterialsDelivered().get(i);
                System.out.println("│ " + (i+1) + ". Material: " + String.format("%-40s", material.getMaterial()) + "│");
                System.out.println("│    Supplier: " + String.format("%-41s", material.getSupplier()) + "│");
                System.out.println("│    Quantity: " + String.format("%-41s", material.getQuantity()) + "│");
                System.out.println("│    Location: " + material.getLocationStored() + 
                                 ", Status: " + String.format("%-25s", material.getInspectionStatus()) + "│");
                if (i < report.getMaterialsDelivered().size() - 1) {
                    System.out.println("│" + " ".repeat(58) + "│");
                }
            }
        }

        // Inspections and Testing Table
        if (report.getInspectionsTesting() != null && !report.getInspectionsTesting().isEmpty()) {
            System.out.println("├" + "─".repeat(58) + "┤");
            System.out.println("│ 🔍 INSPECTIONS & TESTING (" + report.getInspectionsTesting().size() + ")");
            System.out.println("├" + "─".repeat(58) + "┤");
            for (int i = 0; i < report.getInspectionsTesting().size(); i++) {
                var inspection = report.getInspectionsTesting().get(i);
                System.out.println("│ " + (i+1) + ". Inspection: " + String.format("%-38s", inspection.getInspection()) + "│");
                System.out.println("│    Inspector: " + String.format("%-40s", inspection.getTestInspector()) + "│");
                System.out.println("│    Agency: " + String.format("%-43s", inspection.getAgency()) + "│");
                System.out.println("│    Remarks: " + String.format("%-42s", inspection.getTestRemarks()) + "│");
                if (i < report.getInspectionsTesting().size() - 1) {
                    System.out.println("│" + " ".repeat(58) + "│");
                }
            }
        }

        // Safety Information
        System.out.println("├" + "─".repeat(58) + "┤");
        System.out.println("│ 🦺 SAFETY INFORMATION");
        System.out.println("├" + "─".repeat(58) + "┤");
        System.out.println("│ Safety Meeting: " + String.format("%-37s", 
            report.getSafetyMeetingHeld() != null ? report.getSafetyMeetingHeld() : "N/A") + "│");
        if (report.getSafetyMeetingTopic() != null && !report.getSafetyMeetingTopic().trim().isEmpty()) {
            System.out.println("│ Meeting Topic: " + String.format("%-38s", report.getSafetyMeetingTopic()) + "│");
        }
        System.out.println("│ Incidents/Near Misses: " + String.format("%-30s", 
            report.getIncidentsNearMisses() != null ? report.getIncidentsNearMisses() : "N/A") + "│");

        // Pictures (if present)
        if (report.getPicturePaths() != null && !report.getPicturePaths().isEmpty()) {
            System.out.println("├" + "─".repeat(58) + "┤");
            System.out.println("│ 📸 Attached Pictures (" + report.getPicturePaths().size() + "):");
            for (int i = 0; i < report.getPicturePaths().size(); i++) {
                String picturePath = report.getPicturePaths().get(i);
                String displayText = "   " + (i + 1) + ". " + picturePath;
                wrapAndDisplayText(displayText, "│    ");
            }
        }

        // Signature
        if (report.getSignature() != null && !report.getSignature().trim().isEmpty()) {
            System.out.println("├" + "─".repeat(58) + "┤");
            System.out.println("│ ✍️ Signature: " + String.format("%-40s", report.getSignature()) + "│");
        }

        System.out.println("└" + "─".repeat(58) + "┘");
    }
    
    /**
     * Wrap and display text within the report box format
     * @param text The text to wrap and display
     * @param prefix The prefix for each line
     */
    private void wrapAndDisplayText(String text, String prefix) {
        int maxWidth = 54; // Adjust based on box width
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder(prefix);
        
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxWidth) {
                if (currentLine.length() > prefix.length()) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                // Pad the current line to full width
                while (currentLine.length() < maxWidth) {
                    currentLine.append(" ");
                }
                currentLine.append("│");
                System.out.println(currentLine.toString());
                
                currentLine = new StringBuilder(prefix + word);
            }
        }
        
        // Print the last line
        if (currentLine.length() > prefix.length()) {
            while (currentLine.length() < maxWidth) {
                currentLine.append(" ");
            }
            currentLine.append("│");
            System.out.println(currentLine.toString());
        }
    }
    
    /**
     * Wait for user input before continuing
     */
    private void waitForUserInput() {
        System.out.println("\n" + MENU_SEPARATOR);
        System.out.print("Press Enter to continue...");
        inputScanner.nextLine();
    }
    
    /**
     * Get date range from a list of reports
     * @param reports List of field reports
     * @return String representation of date range
     */
    private String getDateRange(List<FieldReport> reports) {
        if (reports.isEmpty()) {
            return "No reports";
        }
        
        LocalDate earliest = reports.stream()
                .map(FieldReport::getDate)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());
                
        LocalDate latest = reports.stream()
                .map(FieldReport::getDate)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());
                
        if (earliest.equals(latest)) {
            return earliest.toString();
        } else {
            return earliest + " to " + latest;
        }
    }
    
    /**
     * Get list of unique reporters from reports
     * @param reports List of field reports
     * @return List of unique reporter names
     */
    private List<String> getUniqueReporters(List<FieldReport> reports) {
        return reports.stream()
                .map(FieldReport::getReporterName)
                .distinct()
                .sorted()
                .toList();
    }
    
    /**
     * Get list of unique projects from reports
     * @param reports List of field reports
     * @return List of unique project names
     */
    private List<String> getUniqueProjects(List<FieldReport> reports) {
        return reports.stream()
                .map(FieldReport::getProjectName)
                .distinct()
                .sorted()
                .toList();
    }
    
    /**
     * Get list of unique locations from reports
     * @param reports List of field reports
     * @return List of unique location names
     */
    private List<String> getUniqueLocations(List<FieldReport> reports) {
        return reports.stream()
                .map(FieldReport::getLocation)
                .distinct()
                .sorted()
                .toList();
    }
    
    /**
     * Show export menu with PDF and Word options
     */
    private void showExportMenu() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("📥 EXPORT REPORTS");
        System.out.println(SEPARATOR);
        
        List<FieldReport> allReports = dailyFieldReportService.getAllReports();
        
        if (allReports.isEmpty()) {
            System.out.println("📭 No reports available for export.");
            System.out.println("💡 Create some reports first using option 1 from the main menu.");
            waitForUserInput();
            return;
        }
        
        System.out.println("📊 Total Reports Available: " + allReports.size());
        System.out.println("\n" + MENU_SEPARATOR);
        System.out.println("📥 EXPORT OPTIONS");
        System.out.println(MENU_SEPARATOR);
        System.out.println("1. 📄 Export All Reports to PDF");
        System.out.println("2. 📄 Export All Reports to Word");
        System.out.println("3. 📅 Export Reports by Date to PDF");
        System.out.println("4. 📅 Export Reports by Date to Word");
        System.out.println("5. 👤 Export Reports by Reporter to PDF");
        System.out.println("6. 👤 Export Reports by Reporter to Word");
        System.out.println("7. 🏗️ Export Reports by Project to PDF");
        System.out.println("8. 🏗️ Export Reports by Project to Word");
        System.out.println("9. 🔙 Back to Main Menu");
        System.out.println(MENU_SEPARATOR);
        System.out.print("➤ Please select an export option (1-9): ");
        
        int exportChoice = getUserMenuChoice();
        processExportChoice(exportChoice);
    }
    
    /**
     * Process export menu choice
     * @param choice User's export selection
     */
    private void processExportChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    exportAllReportsToPDF();
                    break;
                case 2:
                    exportAllReportsToWord();
                    break;
                case 3:
                    exportReportsByDateToPDF();
                    break;
                case 4:
                    exportReportsByDateToWord();
                    break;
                case 5:
                    exportReportsByReporterToPDF();
                    break;
                case 6:
                    exportReportsByReporterToWord();
                    break;
                case 7:
                    exportReportsByProjectToPDF();
                    break;
                case 8:
                    exportReportsByProjectToWord();
                    break;
                case 9:
                    return; // Back to main menu
                default:
                    System.out.println("❌ Invalid choice. Please select a number between 1-9.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error during export: " + e.getMessage());
            System.out.println("💡 Please check that you have write permissions in the current directory.");
        }
        
        if (choice >= 1 && choice <= 8) {
            waitForUserInput();
        }
    }
    
    /**
     * Export all reports to PDF
     */
    private void exportAllReportsToPDF() {
        try {
            List<FieldReport> allReports = dailyFieldReportService.getAllReports();
            String fileName = reportExportService.exportMultipleReportsToPDF(allReports, "All_Daily_Field_Reports");
            System.out.println("📄 PDF exported successfully!");
            System.out.println("📁 File location: " + fileName);
        } catch (Exception e) {
            System.out.println("❌ Failed to export PDF: " + e.getMessage());
        }
    }
    
    /**
     * Export all reports to Word
     */
    private void exportAllReportsToWord() {
        try {
            List<FieldReport> allReports = dailyFieldReportService.getAllReports();
            String fileName = reportExportService.exportMultipleReportsToWord(allReports, "All_Daily_Field_Reports");
            System.out.println("📄 Word document exported successfully!");
            System.out.println("📁 File location: " + fileName);
        } catch (Exception e) {
            System.out.println("❌ Failed to export Word document: " + e.getMessage());
        }
    }
    
    /**
     * Export reports by date to PDF
     */
    private void exportReportsByDateToPDF() {
        System.out.print("📅 Enter date (YYYY-MM-DD): ");
        String dateInput = inputScanner.nextLine().trim();
        
        try {
            LocalDate searchDate = LocalDate.parse(dateInput);
            List<FieldReport> reportsForDate = dailyFieldReportService.getReportsByDate(searchDate);
            
            if (reportsForDate.isEmpty()) {
                System.out.println("📭 No reports found for " + searchDate);
                return;
            }
            
            String fileName = reportExportService.exportMultipleReportsToPDF(reportsForDate, "Reports_for_" + searchDate);
            System.out.println("📄 PDF exported successfully!");
            System.out.println("📁 File location: " + fileName);
            System.out.println("📊 Exported " + reportsForDate.size() + " report(s)");
            
        } catch (DateTimeParseException e) {
            System.out.println("❌ Invalid date format. Please use YYYY-MM-DD format.");
        } catch (Exception e) {
            System.out.println("❌ Failed to export PDF: " + e.getMessage());
        }
    }
    
    /**
     * Export reports by date to Word
     */
    private void exportReportsByDateToWord() {
        System.out.print("📅 Enter date (YYYY-MM-DD): ");
        String dateInput = inputScanner.nextLine().trim();
        
        try {
            LocalDate searchDate = LocalDate.parse(dateInput);
            List<FieldReport> reportsForDate = dailyFieldReportService.getReportsByDate(searchDate);
            
            if (reportsForDate.isEmpty()) {
                System.out.println("📭 No reports found for " + searchDate);
                return;
            }
            
            String fileName = reportExportService.exportMultipleReportsToWord(reportsForDate, "Reports_for_" + searchDate);
            System.out.println("📄 Word document exported successfully!");
            System.out.println("📁 File location: " + fileName);
            System.out.println("📊 Exported " + reportsForDate.size() + " report(s)");
            
        } catch (DateTimeParseException e) {
            System.out.println("❌ Invalid date format. Please use YYYY-MM-DD format.");
        } catch (Exception e) {
            System.out.println("❌ Failed to export Word document: " + e.getMessage());
        }
    }
    
    /**
     * Export reports by reporter to PDF
     */
    private void exportReportsByReporterToPDF() {
        String reporterName = getValidatedStringInput("👤 Enter reporter name: ", "Reporter name");
        
        try {
            List<FieldReport> reportsByReporter = dailyFieldReportService.getReportsByReporter(reporterName);
            
            if (reportsByReporter.isEmpty()) {
                System.out.println("📭 No reports found for reporter: " + reporterName);
                return;
            }
            
            String fileName = reportExportService.exportMultipleReportsToPDF(reportsByReporter, "Reports_by_" + reporterName.replaceAll("[^a-zA-Z0-9]", "_"));
            System.out.println("📄 PDF exported successfully!");
            System.out.println("📁 File location: " + fileName);
            System.out.println("📊 Exported " + reportsByReporter.size() + " report(s)");
            
        } catch (Exception e) {
            System.out.println("❌ Failed to export PDF: " + e.getMessage());
        }
    }
    
    /**
     * Export reports by reporter to Word
     */
    private void exportReportsByReporterToWord() {
        String reporterName = getValidatedStringInput("👤 Enter reporter name: ", "Reporter name");
        
        try {
            List<FieldReport> reportsByReporter = dailyFieldReportService.getReportsByReporter(reporterName);
            
            if (reportsByReporter.isEmpty()) {
                System.out.println("📭 No reports found for reporter: " + reporterName);
                return;
            }
            
            String fileName = reportExportService.exportMultipleReportsToWord(reportsByReporter, "Reports_by_" + reporterName.replaceAll("[^a-zA-Z0-9]", "_"));
            System.out.println("📄 Word document exported successfully!");
            System.out.println("📁 File location: " + fileName);
            System.out.println("📊 Exported " + reportsByReporter.size() + " report(s)");
            
        } catch (Exception e) {
            System.out.println("❌ Failed to export Word document: " + e.getMessage());
        }
    }
    
    /**
     * Export reports by project to PDF
     */
    private void exportReportsByProjectToPDF() {
        String projectName = getValidatedStringInput("🏗️ Enter project name: ", "Project name");
        
        try {
            List<FieldReport> reportsByProject = dailyFieldReportService.getReportsByProject(projectName);
            
            if (reportsByProject.isEmpty()) {
                System.out.println("📭 No reports found for project: " + projectName);
                return;
            }
            
            String fileName = reportExportService.exportMultipleReportsToPDF(reportsByProject, "Reports_for_" + projectName.replaceAll("[^a-zA-Z0-9]", "_"));
            System.out.println("📄 PDF exported successfully!");
            System.out.println("📁 File location: " + fileName);
            System.out.println("📊 Exported " + reportsByProject.size() + " report(s)");
            
        } catch (Exception e) {
            System.out.println("❌ Failed to export PDF: " + e.getMessage());
        }
    }
    
    /**
     * Export reports by project to Word
     */
    private void exportReportsByProjectToWord() {
        String projectName = getValidatedStringInput("🏗️ Enter project name: ", "Project name");
        
        try {
            List<FieldReport> reportsByProject = dailyFieldReportService.getReportsByProject(projectName);
            
            if (reportsByProject.isEmpty()) {
                System.out.println("📭 No reports found for project: " + projectName);
                return;
            }
            
            String fileName = reportExportService.exportMultipleReportsToWord(reportsByProject, "Reports_for_" + projectName.replaceAll("[^a-zA-Z0-9]", "_"));
            System.out.println("📄 Word document exported successfully!");
            System.out.println("📁 File location: " + fileName);
            System.out.println("📊 Exported " + reportsByProject.size() + " report(s)");
            
        } catch (Exception e) {
            System.out.println("❌ Failed to export Word document: " + e.getMessage());
        }
    }
    
    /**
     * Adds pictures to a field report through user interaction.
     * 
     * Prompts the user to add picture file paths to the field report. 
     * The user can add multiple pictures and will be prompted for each one.
     * Basic file path validation is performed to ensure the path is not empty.
     * 
     * @param report the field report to add pictures to
     */
    private void addPicturesToReport(FieldReport report) {
        System.out.println("\n📸 PICTURE ATTACHMENTS");
        System.out.println("==========================================");
        
        try {
            String response = getOptionalStringInput("📷 Would you like to add pictures to this report? (y/n): ");
            
            if (response.toLowerCase().startsWith("y")) {
                System.out.println("📝 Enter picture file paths (one at a time)");
                System.out.println("💡 Tip: Use full file paths (e.g., /Users/yourname/Pictures/photo.jpg)");
                System.out.println("💡 Press Enter without typing anything to finish adding pictures");
                
                int pictureCount = 0;
                while (true) {
                    String prompt = "📸 Picture " + (pictureCount + 1) + " file path (or press Enter to finish): ";
                    String picturePath = getOptionalStringInput(prompt);
                    
                    if (picturePath.trim().isEmpty()) {
                        break; // User finished adding pictures
                    }
                    
                    // Basic validation - check if path looks reasonable
                    if (picturePath.length() < 3 || (!picturePath.contains("/") && !picturePath.contains("\\"))) {
                        System.out.println("⚠️  Warning: This doesn't look like a valid file path. Adding anyway...");
                    }
                    
                    report.addPicturePath(picturePath.trim());
                    pictureCount++;
                    System.out.println("✅ Picture " + pictureCount + " added: " + picturePath.trim());
                }
                
                if (pictureCount > 0) {
                    System.out.println("📷 Total pictures attached: " + pictureCount);
                } else {
                    System.out.println("📷 No pictures were added to this report.");
                }
            } else {
                System.out.println("📷 No pictures will be added to this report.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error adding pictures: " + e.getMessage());
        }
    }
    
    /**
     * Add personnel entries to the field report
     */
    private void addPersonnelToReport(FieldReport report) {
        System.out.println("\n👥 PERSONNEL ON SITE");
        System.out.println("==========================================");
        System.out.print("📝 Would you like to add personnel information? (y/n): ");
        String addPersonnel = inputScanner.nextLine().trim().toLowerCase();
        
        if (addPersonnel.equals("y") || addPersonnel.equals("yes")) {
            int personnelCount = 0;
            while (true) {
                System.out.println("\n➕ Adding Personnel Entry #" + (personnelCount + 1));
                System.out.println("(Press Enter on Company name to finish)");
                
                String company = getOptionalStringInput("🏢 Company/Contractor: ");
                if (company.trim().isEmpty()) {
                    break;
                }
                
                String tradeRole = getValidatedStringInput("👷 Trade/Role: ", "Trade/Role");
                int workers = getValidatedIntInput("👥 Number of Workers: ", "Number of Workers");
                double hours = getValidatedDoubleInput("⏰ Hours Worked: ", "Hours Worked");
                String supervisor = getValidatedStringInput("👤 Foreman/Supervisor: ", "Foreman/Supervisor");
                
                PersonnelOnSite personnel = new PersonnelOnSite(company, tradeRole, workers, hours, supervisor);
                report.addPersonnelOnSite(personnel);
                personnelCount++;
                
                System.out.println("✅ Personnel entry added successfully!");
            }
            
            if (personnelCount > 0) {
                System.out.println("👥 Total personnel entries: " + personnelCount);
            } else {
                System.out.println("👥 No personnel entries were added.");
            }
        } else {
            System.out.println("👥 No personnel information will be added.");
        }
    }
    
    /**
     * Add equipment entries to the field report
     */
    private void addEquipmentToReport(FieldReport report) {
        System.out.println("\n🚜 EQUIPMENT ON SITE");
        System.out.println("==========================================");
        System.out.print("📝 Would you like to add equipment information? (y/n): ");
        String addEquipment = inputScanner.nextLine().trim().toLowerCase();
        
        if (addEquipment.equals("y") || addEquipment.equals("yes")) {
            int equipmentCount = 0;
            while (true) {
                System.out.println("\n➕ Adding Equipment Entry #" + (equipmentCount + 1));
                System.out.println("(Press Enter on Equipment name to finish)");
                
                String equipment = getOptionalStringInput("🚜 Equipment: ");
                if (equipment.trim().isEmpty()) {
                    break;
                }
                
                String typeSize = getValidatedStringInput("📏 Type/Size: ", "Type/Size");
                int quantity = getValidatedIntInput("🔢 Quantity: ", "Quantity");
                
                System.out.print("⚡ Is equipment operating? (y/n): ");
                String operatingInput = inputScanner.nextLine().trim().toLowerCase();
                boolean operating = operatingInput.equals("y") || operatingInput.equals("yes");
                
                String idleReason = "";
                if (!operating) {
                    idleReason = getValidatedStringInput("📝 Idle Reason: ", "Idle Reason");
                }
                
                EquipmentOnSite equipmentEntry = new EquipmentOnSite(equipment, typeSize, quantity, operating, idleReason);
                report.addEquipmentOnSite(equipmentEntry);
                equipmentCount++;
                
                System.out.println("✅ Equipment entry added successfully!");
            }
            
            if (equipmentCount > 0) {
                System.out.println("🚜 Total equipment entries: " + equipmentCount);
            } else {
                System.out.println("🚜 No equipment entries were added.");
            }
        } else {
            System.out.println("🚜 No equipment information will be added.");
        }
    }
    
    /**
     * Add materials to the field report
     */
    private void addMaterialsToReport(FieldReport report) {
        System.out.println("\n📦 MATERIALS DELIVERED");
        System.out.println("==========================================");
        System.out.print("📝 Would you like to add materials information? (y/n): ");
        String addMaterials = inputScanner.nextLine().trim().toLowerCase();
        
        if (addMaterials.equals("y") || addMaterials.equals("yes")) {
            int materialsCount = 0;
            List<MaterialDelivered> materials = new ArrayList<>();
            
            while (true) {
                System.out.println("\n➕ Adding Material Entry #" + (materialsCount + 1));
                System.out.println("(Press Enter on Material name to finish)");
                
                String material = getOptionalStringInput("📦 Material: ");
                if (material.trim().isEmpty()) {
                    break;
                }
                
                String supplier = getValidatedStringInput("🏢 Supplier: ", "Supplier");
                String quantity = getValidatedStringInput("📏 Quantity: ", "Quantity");
                String location = getValidatedStringInput("📍 Location Stored: ", "Location");
                String status = getValidatedStringInput("✅ Inspection Status: ", "Status");
                
                MaterialDelivered materialEntry = new MaterialDelivered(material, supplier, quantity, location, status);
                materials.add(materialEntry);
                materialsCount++;
                
                System.out.println("✅ Material entry added successfully!");
            }
            
            if (materialsCount > 0) {
                report.setMaterialsDelivered(materials);
                System.out.println("📦 Total material entries: " + materialsCount);
            } else {
                System.out.println("📦 No material entries were added.");
            }
        } else {
            System.out.println("📦 No materials information will be added.");
        }
    }
    
    /**
     * Add inspections to the field report
     */
    private void addInspectionsToReport(FieldReport report) {
        System.out.println("\n🔍 INSPECTIONS & TESTING");
        System.out.println("==========================================");
        System.out.print("📝 Would you like to add inspection information? (y/n): ");
        String addInspections = inputScanner.nextLine().trim().toLowerCase();
        
        if (addInspections.equals("y") || addInspections.equals("yes")) {
            int inspectionsCount = 0;
            List<InspectionTesting> inspections = new ArrayList<>();
            
            while (true) {
                System.out.println("\n➕ Adding Inspection Entry #" + (inspectionsCount + 1));
                System.out.println("(Press Enter on Inspection type to finish)");
                
                String inspection = getOptionalStringInput("🔍 Inspection/Test Type: ");
                if (inspection.trim().isEmpty()) {
                    break;
                }
                
                String inspector = getValidatedStringInput("👤 Inspector: ", "Inspector");
                String agency = getValidatedStringInput("🏢 Agency: ", "Agency");
                String remarks = getValidatedStringInput("📝 Test Remarks: ", "Remarks");
                
                InspectionTesting inspectionEntry = new InspectionTesting(inspection, inspector, agency, remarks);
                inspections.add(inspectionEntry);
                inspectionsCount++;
                
                System.out.println("✅ Inspection entry added successfully!");
            }
            
            if (inspectionsCount > 0) {
                report.setInspectionsTesting(inspections);
                System.out.println("🔍 Total inspection entries: " + inspectionsCount);
            } else {
                System.out.println("🔍 No inspection entries were added.");
            }
        } else {
            System.out.println("🔍 No inspection information will be added.");
        }
    }
    
    /**
     * Add safety information to the field report
     */
    private void addSafetyInfoToReport(FieldReport report) {
        System.out.println("\n🦺 SAFETY INFORMATION");
        System.out.println("==========================================");
        
        System.out.print("📋 Was a safety meeting held? (y/n): ");
        String meetingHeld = inputScanner.nextLine().trim().toLowerCase();
        report.setSafetyMeetingHeld(meetingHeld.equals("y") || meetingHeld.equals("yes") ? "Y" : "N");
        
        if (meetingHeld.equals("y") || meetingHeld.equals("yes")) {
            String topic = getValidatedStringInput("📝 Safety meeting topic: ", "Meeting topic");
            report.setSafetyMeetingTopic(topic);
        }
        
        System.out.print("⚠️ Were there any incidents or near misses? (y/n): ");
        String incidents = inputScanner.nextLine().trim().toLowerCase();
        report.setIncidentsNearMisses(incidents.equals("y") || incidents.equals("yes") ? "Y" : "N");
        
        String signature = getValidatedStringInput("✍️ Digital signature (name and title): ", "Signature");
        report.setSignature(signature);
        
        System.out.println("✅ Safety information added successfully!");
    }
    
    /**
     * Get validated integer input
     */
    private int getValidatedIntInput(String prompt, String fieldName) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = inputScanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("❌ " + fieldName + " cannot be empty. Please try again.");
                    continue;
                }
                
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("❌ " + fieldName + " must be a positive number. Please try again.");
                    continue;
                }
                
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number for " + fieldName + ".");
            }
        }
    }
    
    /**
     * Get validated double input
     */
    private double getValidatedDoubleInput(String prompt, String fieldName) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = inputScanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("❌ " + fieldName + " cannot be empty. Please try again.");
                    continue;
                }
                
                double value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.println("❌ " + fieldName + " must be a positive number. Please try again.");
                    continue;
                }
                
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number for " + fieldName + ".");
            }
        }
    }
}