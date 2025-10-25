package com.fieldreport.model;

/**
 * Personnel On Site Model Class for Field Report System
 * 
 * Represents personnel working on site for a specific field report.
 * This class tracks workforce information including company affiliation,
 * trade/role, number of workers, hours worked, and supervision details.
 * 
 * Key Features:
 * - Company and contractor tracking
 * - Trade/role specification (e.g., electrician, carpenter, supervisor)
 * - Worker count and hours worked tracking
 * - Foreman/supervisor identification
 * - Input validation for data integrity
 * 
 * @author Field Report System
 * @version 1.0
 * @since 2025-10-24
 */
public class PersonnelOnSite {
    
    /** Company or contractor name */
    private String company;
    
    /** Trade, role, or job classification */
    private String tradeRole;
    
    /** Number of workers */
    private int numberOfWorkers;
    
    /** Hours worked during the reporting period */
    private double hoursWorked;
    
    /** Name of foreman or supervisor */
    private String foremanSupervisor;
    
    /**
     * Default constructor.
     */
    public PersonnelOnSite() {
        this.company = "";
        this.tradeRole = "";
        this.numberOfWorkers = 0;
        this.hoursWorked = 0.0;
        this.foremanSupervisor = "";
    }
    
    /**
     * Constructor with all parameters.
     * 
     * @param company the company or contractor name
     * @param tradeRole the trade, role, or job classification
     * @param numberOfWorkers the number of workers
     * @param hoursWorked the hours worked during the reporting period
     * @param foremanSupervisor the name of foreman or supervisor
     */
    public PersonnelOnSite(String company, String tradeRole, int numberOfWorkers, 
                          double hoursWorked, String foremanSupervisor) {
        this.company = company != null ? company : "";
        this.tradeRole = tradeRole != null ? tradeRole : "";
        this.numberOfWorkers = Math.max(0, numberOfWorkers);
        this.hoursWorked = Math.max(0.0, hoursWorked);
        this.foremanSupervisor = foremanSupervisor != null ? foremanSupervisor : "";
    }
    
    // Getter methods
    
    /**
     * Gets the company name.
     * @return the company or contractor name
     */
    public String getCompany() {
        return company;
    }
    
    /**
     * Gets the trade/role.
     * @return the trade, role, or job classification
     */
    public String getTradeRole() {
        return tradeRole;
    }
    
    /**
     * Gets the number of workers.
     * @return the number of workers
     */
    public int getNumberOfWorkers() {
        return numberOfWorkers;
    }
    
    /**
     * Gets the hours worked.
     * @return the hours worked during the reporting period
     */
    public double getHoursWorked() {
        return hoursWorked;
    }
    
    /**
     * Gets the foreman/supervisor name.
     * @return the name of foreman or supervisor
     */
    public String getForemanSupervisor() {
        return foremanSupervisor;
    }
    
    // Setter methods
    
    /**
     * Sets the company name.
     * @param company the company or contractor name
     */
    public void setCompany(String company) {
        this.company = company != null ? company : "";
    }
    
    /**
     * Sets the trade/role.
     * @param tradeRole the trade, role, or job classification
     */
    public void setTradeRole(String tradeRole) {
        this.tradeRole = tradeRole != null ? tradeRole : "";
    }
    
    /**
     * Sets the number of workers.
     * @param numberOfWorkers the number of workers (must be non-negative)
     */
    public void setNumberOfWorkers(int numberOfWorkers) {
        this.numberOfWorkers = Math.max(0, numberOfWorkers);
    }
    
    /**
     * Sets the hours worked.
     * @param hoursWorked the hours worked (must be non-negative)
     */
    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = Math.max(0.0, hoursWorked);
    }
    
    /**
     * Sets the foreman/supervisor name.
     * @param foremanSupervisor the name of foreman or supervisor
     */
    public void setForemanSupervisor(String foremanSupervisor) {
        this.foremanSupervisor = foremanSupervisor != null ? foremanSupervisor : "";
    }
    
    /**
     * Validates that the personnel record has required fields.
     * 
     * @return true if company and trade/role are not empty
     */
    public boolean isValid() {
        return !company.trim().isEmpty() && !tradeRole.trim().isEmpty();
    }
    
    /**
     * Calculates total worker-hours (numberOfWorkers × hoursWorked).
     * 
     * @return total worker-hours for this personnel entry
     */
    public double getTotalWorkerHours() {
        return numberOfWorkers * hoursWorked;
    }
    
    @Override
    public String toString() {
        return "PersonnelOnSite{" +
                "company='" + company + '\'' +
                ", tradeRole='" + tradeRole + '\'' +
                ", numberOfWorkers=" + numberOfWorkers +
                ", hoursWorked=" + hoursWorked +
                ", foremanSupervisor='" + foremanSupervisor + '\'' +
                '}';
    }
}