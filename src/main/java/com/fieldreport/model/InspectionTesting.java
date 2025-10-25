package com.fieldreport.model;

/**
 * Represents inspection and testing activities performed on site.
 * 
 * This class captures information about various inspections and tests
 * conducted, including inspector details, testing agencies, and test results.
 * 
 * @author DailyFieldReport System
 * @version 1.0
 */
public class InspectionTesting {
    
    /** Type or description of inspection performed */
    private String inspection;
    
    /** Name of the test inspector */
    private String testInspector;
    
    /** Testing agency or organization */
    private String agency;
    
    /** Test remarks or results */
    private String testRemarks;
    
    /**
     * Default constructor for InspectionTesting.
     */
    public InspectionTesting() {
    }
    
    /**
     * Constructs an InspectionTesting with all required information.
     * 
     * @param inspection type or description of inspection performed
     * @param testInspector name of the test inspector
     * @param agency testing agency or organization
     * @param testRemarks test remarks or results
     */
    public InspectionTesting(String inspection, String testInspector, 
                           String agency, String testRemarks) {
        this.inspection = inspection;
        this.testInspector = testInspector;
        this.agency = agency;
        this.testRemarks = testRemarks;
    }
    
    // ================= GETTER AND SETTER METHODS =================
    
    /**
     * Gets the inspection type or description.
     * @return the inspection type
     */
    public String getInspection() {
        return inspection;
    }
    
    /**
     * Sets the inspection type or description.
     * @param inspection the inspection type
     */
    public void setInspection(String inspection) {
        this.inspection = inspection;
    }
    
    /**
     * Gets the test inspector name.
     * @return the test inspector name
     */
    public String getTestInspector() {
        return testInspector;
    }
    
    /**
     * Sets the test inspector name.
     * @param testInspector the test inspector name
     */
    public void setTestInspector(String testInspector) {
        this.testInspector = testInspector;
    }
    
    /**
     * Gets the testing agency or organization.
     * @return the agency name
     */
    public String getAgency() {
        return agency;
    }
    
    /**
     * Sets the testing agency or organization.
     * @param agency the agency name
     */
    public void setAgency(String agency) {
        this.agency = agency;
    }
    
    /**
     * Gets the test remarks or results.
     * @return the test remarks
     */
    public String getTestRemarks() {
        return testRemarks;
    }
    
    /**
     * Sets the test remarks or results.
     * @param testRemarks the test remarks
     */
    public void setTestRemarks(String testRemarks) {
        this.testRemarks = testRemarks;
    }
    
    // ================= VALIDATION METHODS =================
    
    /**
     * Validates that all required fields are populated.
     * @return true if all fields are valid, false otherwise
     */
    public boolean isValid() {
        return inspection != null && !inspection.trim().isEmpty() &&
               testInspector != null && !testInspector.trim().isEmpty();
    }
    
    /**
     * Gets a summary description of this inspection entry.
     * @return formatted summary string
     */
    public String getSummary() {
        return String.format("%s by %s (%s)", 
                           inspection, testInspector, 
                           agency != null ? agency : "Internal");
    }
    
    /**
     * Checks if the inspection has been completed with remarks.
     * @return true if test remarks are provided, false otherwise
     */
    public boolean isCompleted() {
        return testRemarks != null && !testRemarks.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "InspectionTesting{" +
                "inspection='" + inspection + '\'' +
                ", testInspector='" + testInspector + '\'' +
                ", agency='" + agency + '\'' +
                ", testRemarks='" + testRemarks + '\'' +
                '}';
    }
}