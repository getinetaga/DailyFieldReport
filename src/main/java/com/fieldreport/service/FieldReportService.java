package com.fieldreport.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.fieldreport.model.FieldReport;

/**
 * Service class for managing field reports in the Daily Field Report system.
 * 
 * This service provides comprehensive CRUD (Create, Read, Update, Delete) operations
 * for field reports, along with search and filtering capabilities. It maintains
 * an in-memory collection of reports and provides business logic for report management.
 * 
 * Key Features:
 * - Create, read, update, and delete field reports
 * - Search reports by date ranges and keywords
 * - List all reports with optional filtering
 * - Data validation and error handling
 * - In-memory storage for simplified standalone operation
 * 
 * Usage Examples:
 * <pre>
 * FieldReportService service = new FieldReportService();
 * 
 * // Create and save a new report
 * FieldReport report = new FieldReport("John Doe", "Site A", "Bridge Project", 
 *                                       "Sunny", "Foundation work", "Good progress");
 * service.saveReport(report);
 * 
 * // Find reports by date range
 * List&lt;FieldReport&gt; recentReports = service.getReportsByDateRange(
 *     LocalDate.now().minusDays(7), LocalDate.now());
 * </pre>
 * 
 * @author Daily Field Report System
 * @version 1.0
 * @since 2025-10-24
 */
public class FieldReportService {
    
    /** Logger for this service */
    private static final Logger logger = Logger.getLogger(FieldReportService.class.getName());
    
    /** In-memory storage for field reports - maintains all reports in the system */
    private final List<FieldReport> reports;
    
    /**
     * Constructs a new FieldReportService with empty report collection.
     * 
     * Initializes the internal storage structure for managing field reports.
     * All reports will be stored in memory for the duration of the application session.
     */
    public FieldReportService() {
        this.reports = new ArrayList<>();
    }
    
    // ================= CREATE OPERATIONS =================
    
    /**
     * Saves a new field report to the system.
     * 
     * Validates the report data before saving and adds it to the internal
     * collection. The report must have all required fields populated to be saved.
     * 
     * @param report the field report to save
     * @throws IllegalArgumentException if the report is invalid or missing required fields
     * @throws NullPointerException if the report is null
     */
    public void saveReport(FieldReport report) {
        if (report == null) {
            logger.warning("Attempted to save null report");
            return;
        }
        
        if (!report.isValid()) {
            logger.warning("Attempted to save invalid report: " + report);
            throw new IllegalArgumentException("Report is missing required fields");
        }
        
        reports.add(report);
        logger.info("Saved field report with ID: " + report.getId());
    }
    
    // ================= READ OPERATIONS =================
    
    /**
     * Retrieves all field reports in the system.
     * 
     * Returns a defensive copy of the internal report collection to prevent
     * external modification of the internal data structure.
     * 
     * @return a new list containing all field reports (may be empty)
     */
    public List<FieldReport> getAllReports() {
        return new ArrayList<>(reports);
    }
    
    /**
     * Retrieves field reports for a specific date.
     * 
     * Filters all reports to find those created on the specified date.
     * Returns an empty list if no reports are found for that date.
     * 
     * @param date the date to search for reports
     * @return a list of field reports for the specified date
     */
    public List<FieldReport> getReportsByDate(LocalDate date) {
        return reports.stream()
                .filter(report -> report.getDate().equals(date))
                .toList();
    }
    
    /**
     * Retrieves field reports submitted by a specific reporter.
     * 
     * Performs case-insensitive matching on the reporter name.
     * Returns an empty list if no reports are found for that reporter.
     * 
     * @param reporterName the name of the reporter to search for
     * @return a list of field reports submitted by the specified reporter
     */
    public List<FieldReport> getReportsByReporter(String reporterName) {
        return reports.stream()
                .filter(report -> report.getReporterName().equalsIgnoreCase(reporterName))
                .toList();
    }
    
    /**
     * Retrieves field reports associated with a specific project.
     * 
     * Performs case-insensitive matching on the project name.
     * Returns an empty list if no reports are found for that project.
     * 
     * @param projectName the name of the project to search for
     * @return a list of field reports for the specified project
     */
    public List<FieldReport> getReportsByProject(String projectName) {
        return reports.stream()
                .filter(report -> report.getProjectName().equalsIgnoreCase(projectName))
                .toList();
    }
    
    /**
     * Retrieves field reports from a specific location.
     * 
     * Performs case-insensitive matching on the location field.
     * Returns an empty list if no reports are found for that location.
     * 
     * @param location the location to search for reports
     * @return a list of field reports from the specified location
     */
    public List<FieldReport> getReportsByLocation(String location) {
        return reports.stream()
                .filter(report -> report.getLocation().equalsIgnoreCase(location))
                .toList();
    }
    
    /**
     * Finds a specific field report by its unique identifier.
     * 
     * Searches through all reports to find the one with the matching ID.
     * Returns an Optional to handle cases where the report might not exist.
     * 
     * @param id the unique identifier of the report to find
     * @return an Optional containing the report if found, empty otherwise
     */
    public Optional<FieldReport> findReportById(String id) {
        return reports.stream()
                .filter(report -> report.getId().equals(id))
                .findFirst();
    }
    
    // ================= UPDATE OPERATIONS =================
    
    /**
     * Updates an existing field report with new information.
     * 
     * Locates the report by ID and replaces it with the updated version.
     * The updated report must be valid before the update can proceed.
     * 
     * @param updatedReport the field report with updated information
     * @return true if the report was successfully updated, false otherwise
     */
    public boolean updateReport(FieldReport updatedReport) {
        if (updatedReport == null || !updatedReport.isValid()) {
            return false;
        }
        
        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getId().equals(updatedReport.getId())) {
                reports.set(i, updatedReport);
                logger.info("Updated field report with ID: " + updatedReport.getId());
                return true;
            }
        }
        
        logger.warning("Attempted to update non-existent report with ID: " + updatedReport.getId());
        return false;
    }
    
    // ================= DELETE OPERATIONS =================
    
    /**
     * Deletes a field report by its unique identifier.
     * 
     * Removes the report from the internal collection if found.
     * Provides console feedback about the operation result.
     * 
     * @param id the unique identifier of the report to delete
     * @return true if the report was successfully deleted, false if not found
     */
    public boolean deleteReport(String id) {
        boolean removed = reports.removeIf(report -> report.getId().equals(id));
        if (removed) {
            logger.info("Deleted field report with ID: " + id);
        } else {
            logger.warning("Attempted to delete non-existent report with ID: " + id);
        }
        return removed;
    }
    
    // ================= SEARCH AND FILTER OPERATIONS =================
    
    /**
     * Retrieves field reports within a specified date range.
     * 
     * Finds all reports where the report date falls between the start
     * and end dates (inclusive). Returns an empty list if no reports
     * are found in the specified range.
     * 
     * @param startDate the beginning of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @return a list of field reports within the specified date range
     */
    public List<FieldReport> getReportsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return reports.stream()
                .filter(report -> {
                    LocalDate reportDate = report.getDate();
                    return (reportDate.equals(startDate) || reportDate.isAfter(startDate)) &&
                           (reportDate.equals(endDate) || reportDate.isBefore(endDate));
                })
                .toList();
    }
    
    // ================= UTILITY OPERATIONS =================
    
    /**
     * Gets the total number of field reports in the system.
     * 
     * Provides a quick count of all reports currently stored,
     * useful for statistics and display purposes.
     * 
     * @return the total number of field reports
     */
    public int getReportCount() {
        return reports.size();
    }
    
    /**
     * Clears all field reports from the system.
     * 
     * WARNING: This operation is irreversible and will permanently
     * remove all stored field reports. Use with extreme caution.
     * Primarily intended for testing or system reset scenarios.
     */
    public void clearAllReports() {
        reports.clear();
        logger.warning("All field reports have been cleared");
    }
}