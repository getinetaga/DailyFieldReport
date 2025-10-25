package com.fieldreport;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import com.fieldreport.model.FieldReport;
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
     * Creates a new application instance and launches the main menu interface.
     * The application will continue running until the user chooses to exit.
     * All resources are properly cleaned up on shutdown.
     * 
     * @param args command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        displayWelcomeHeader();
        
        DailyFieldReportApplication dailyReportApp = new DailyFieldReportApplication();
        dailyReportApp.startApplication();
        
        displayExitMessage();
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
        System.out.println("📋 DAILY FIELD REPORT - MAIN MENU");
        System.out.println(MENU_SEPARATOR);
        System.out.println("1. 📝 Create New Daily Field Report");
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
        System.out.println("📝 CREATE NEW DAILY FIELD REPORT");
        System.out.println(SEPARATOR);
        
        try {
            // Collect required information with validation
            String reporterName = getValidatedStringInput("👤 Enter reporter name: ", "Reporter name");
            String location = getValidatedStringInput("📍 Enter work location: ", "Location");
            String projectName = getValidatedStringInput("🏗️  Enter project name: ", "Project name");
            String weatherConditions = getValidatedStringInput("🌤️  Enter weather conditions: ", "Weather conditions");
            String workDescription = getValidatedStringInput("📋 Enter work description: ", "Work description");
            
            // Optional notes
            String notes = getOptionalStringInput("📝 Enter any notes or issues (optional): ");
            
            // Create and save the report
            FieldReport dailyReport = new FieldReport(
                reporterName, 
                location, 
                projectName, 
                weatherConditions, 
                workDescription, 
                notes
            );
            
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
        System.out.println("📄 ALL DAILY FIELD REPORTS");
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
        System.out.println("📊 DAILY FIELD REPORT STATISTICS");
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
        System.out.println("│ 🌤️ Weather: " + String.format("%-44s", report.getWeatherConditions()) + "│");
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
}