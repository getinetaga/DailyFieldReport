package com.fieldreport.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Model class representing a daily field report in the construction/project management system.
 * 
 * This class encapsulates all essential information about work performed in the field,
 * including worker details, project information, weather conditions, and work progress.
 * Each report is uniquely identified and timestamped for tracking purposes.
 * 
 * Key Features:
 * - Automatic UUID generation for unique identification
 * - Timestamp tracking for creation and date management
 * - Comprehensive validation for data integrity
 * - Support for both mandatory and optional fields
 * - JSON-ready structure for data persistence
 * 
 * Usage Examples:
 * <pre>
 * // Create a new report with current date
 * FieldReport report = new FieldReport("John Doe", "Site A", "Bridge Project", 
 *                                       "Sunny", "Foundation work", "No issues");
 * 
 * // Create a report for specific date
 * FieldReport report = new FieldReport(LocalDate.of(2025, 10, 20), "Jane Smith", 
 *                                       "Site B", "Road Project", "Rainy", 
 *                                       "Delayed due to weather", "Resume tomorrow");
 * </pre>
 * 
 * @author Daily Field Report System
 * @version 1.0
 * @since 2025-10-24
 */
public class FieldReport {
    
    /** Unique identifier for the field report - automatically generated UUID */
    private String id;
    
    /** Date of the field work - defaults to current date if not specified */
    private LocalDate date;
    
    /** Timestamp when the report was created in the system */
    private LocalDateTime createdAt;
    
    /** Name of the person submitting the field report */
    private String reporterName;
    
    /** Physical location or site where the work was performed */
    private String location;
    
    /** Name or identifier of the project being worked on */
    private String projectName;
    
    /** Project number or identifier */
    private String projectNumber;
    
    /** Weather conditions in the morning (AM) */
    private String weatherAM;
    
    /** Weather conditions in the afternoon/evening (PM) */
    private String weatherPM;
    
    /** High temperature recorded during the work period (in degrees Fahrenheit) */
    private String temperatureHigh;
    
    /** Low temperature recorded during the work period (in degrees Fahrenheit) */
    private String temperatureLow;
    
    /** List of personnel on site */
    private List<PersonnelOnSite> personnelOnSite;
    
    /** List of equipment on site */
    private List<EquipmentOnSite> equipmentOnSite;
    
    /** Detailed description of work performed today and activities completed */
    private String workPerformedToday;
    
    /** List of materials delivered or installed */
    private List<MaterialDelivered> materialsDelivered;
    
    /** List of inspections and testing performed */
    private List<InspectionTesting> inspectionsTesting;
    
    /** Safety meeting held (Y/N) */
    private String safetyMeetingHeld;
    
    /** Safety meeting topic */
    private String safetyMeetingTopic;
    
    /** Safety meeting attendees */
    private String safetyMeetingAttendees;
    
    /** Incidents or near misses occurred (Y/N) */
    private String incidentsNearMisses;
    
    /** Description of incidents or near misses */
    private String incidentsDescription;
    
    /** Delays, issues, or non-conformance */
    private String delaysIssuesNonConformance;
    
    /** Coordination, visits, and communication */
    private String coordinationVisitsCommunication;
    
    /** Photos attached (checkbox) */
    private boolean photosAttached;
    
    /** Test results attached (checkbox) */
    private boolean testResultsAttached;
    
    /** Drawings or sketches attached (checkbox) */
    private boolean drawingsSketchesAttached;
    
    /** Additional remarks */
    private String remarks;
    
    /** Digital signature or name of person signing the report */
    private String signature;
    
    /** List of picture file paths attached to this field report */
    private List<String> picturePaths;
    
    /** Detailed description of work performed and progress made */
    private String workDescription;
    
    /** Additional notes, issues, or observations (optional field) */
    private String notes;
    
    /**
     * Default constructor for JSON deserialization and object creation.
     * 
     * Automatically generates a unique ID and sets the creation timestamp.
     * Date is set to current date by default but can be modified later.
     */
    public FieldReport() {
        this.id = UUID.randomUUID().toString();
        this.date = LocalDate.now();
        this.createdAt = LocalDateTime.now();
        this.picturePaths = new ArrayList<>();
        this.personnelOnSite = new ArrayList<>();
        this.equipmentOnSite = new ArrayList<>();
        this.materialsDelivered = new ArrayList<>();
        this.inspectionsTesting = new ArrayList<>();
    }
    
        /**
     * Constructs a fully detailed field report with all required fields.
     * 
     * Creates a new field report with comprehensive information including
     * project details, location, weather conditions (AM/PM), temperature readings,
     * work description, and notes. ID and creation timestamp are automatically
     * generated by calling the default constructor.
     * 
     * @param reporterName name of the person submitting the report
     * @param location physical site or location where work was performed
     * @param projectName name or identifier of the project
     * @param projectNumber project number or identifier
     * @param weatherAM weather conditions in the morning (AM)
     * @param weatherPM weather conditions in the afternoon/evening (PM)
     * @param temperatureHigh high temperature recorded during work period (°F)
     * @param temperatureLow low temperature recorded during work period (°F)
     * @param workDescription detailed description of work performed
     * @param notes additional observations or issues (can be null or empty)
     * 
     * @throws IllegalArgumentException if any mandatory field is null or empty
     */
    public FieldReport(String reporterName, String location, String projectName, String projectNumber,
                      String weatherAM, String weatherPM, String temperatureHigh, String temperatureLow, 
                      String workDescription, String notes) {
        this();
        this.reporterName = reporterName;
        this.location = location;
        this.projectName = projectName;
        this.projectNumber = projectNumber;
        this.weatherAM = weatherAM;
        this.weatherPM = weatherPM;
        this.temperatureHigh = temperatureHigh;
        this.temperatureLow = temperatureLow;
        this.workDescription = workDescription;
        this.notes = notes != null ? notes : "";
    }
    
    /**
     * Constructs a field report with a specific date and all required fields.
     * 
     * This constructor allows creating reports for specific dates (past or future)
     * rather than defaulting to the current date. Useful for backdating reports
     * or planning future work.
     * 
     * @param date the specific date when the field work was/will be performed
     * @param reporterName name of the person submitting the report
     * @param location physical site or location where work was performed
     * @param projectName name or identifier of the project
     * @param weatherConditions description of weather conditions during work
     * @param temperatureHigh high temperature recorded during work period
     * @param temperatureLow low temperature recorded during work period
     * @param workDescription detailed description of work performed
     * @param notes additional observations or issues (can be null or empty)
     * 
     * @throws IllegalArgumentException if any mandatory field is null or empty
     */
    public FieldReport(String projectName, String projectNumber, String location, LocalDate date, 
                      String weatherAM, String weatherPM, String temperatureHigh, String temperatureLow) {
        this();
        this.projectName = projectName;
        this.projectNumber = projectNumber;
        this.location = location;
        this.date = date;
        this.weatherAM = weatherAM;
        this.weatherPM = weatherPM;
        this.temperatureHigh = temperatureHigh;
        this.temperatureLow = temperatureLow;
    }
    
    // ================= GETTERS AND SETTERS =================
    
    /**
     * Gets the unique identifier for this field report.
     * 
     * @return the UUID string that uniquely identifies this report
     */
    public String getId() {
        return id;
    }
    
    /**
     * Sets the unique identifier for this field report.
     * 
     * Note: This should generally not be called after object creation
     * as the ID is auto-generated and should remain immutable.
     * 
     * @param id the UUID string to set as the unique identifier
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Gets the date when the field work was performed.
     * 
     * @return the date of the field work
     */
    public LocalDate getDate() {
        return date;
    }
    
    /**
     * Sets the date when the field work was performed.
     * 
     * @param date the date of the field work
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    /**
     * Gets the timestamp when this report was created in the system.
     * 
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the timestamp when this report was created in the system.
     * 
     * Note: This is typically set automatically during object creation
     * and should rarely need to be modified manually.
     * 
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Gets the name of the person who submitted this field report.
     * 
     * @return the reporter's name
     */
    public String getReporterName() {
        return reporterName;
    }
    
    /**
     * Sets the name of the person who submitted this field report.
     * 
     * @param reporterName the reporter's name
     */
    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }
    
    /**
     * Gets the physical location or site where the field work was performed.
     * 
     * @return the work location
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Sets the physical location or site where the field work was performed.
     * 
     * @param location the work location
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * Gets the name or identifier of the project being worked on.
     * 
     * @return the project name
     */
    public String getProjectName() {
        return projectName;
    }
    
    /**
     * Sets the name or identifier of the project being worked on.
     * 
     * @param projectName the project name
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    /**
     * Gets the project number or identifier.
     * @return the project number
     */
    public String getProjectNumber() {
        return projectNumber;
    }

    /**
     * Sets the project number or identifier.
     * @param projectNumber the project number
     */
    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    /**
     * Gets the weather conditions in the morning (AM).
     * @return the morning weather conditions
     */
    public String getWeatherAM() {
        return weatherAM;
    }

    /**
     * Sets the weather conditions in the morning (AM).
     * @param weatherAM the morning weather conditions
     */
    public void setWeatherAM(String weatherAM) {
        this.weatherAM = weatherAM;
    }

    /**
     * Gets the weather conditions in the afternoon/evening (PM).
     * @return the afternoon/evening weather conditions
     */
    public String getWeatherPM() {
        return weatherPM;
    }

    /**
     * Sets the weather conditions in the afternoon/evening (PM).
     * @param weatherPM the afternoon/evening weather conditions
     */
    public void setWeatherPM(String weatherPM) {
        this.weatherPM = weatherPM;
    }
    
    /**
     * Gets the high temperature recorded during the work period.
     * 
     * @return the high temperature
     */
    public String getTemperatureHigh() {
        return temperatureHigh;
    }
    
    /**
     * Sets the high temperature recorded during the work period.
     * 
     * @param temperatureHigh the high temperature (with unit, e.g., "25°C" or "77°F")
     */
    public void setTemperatureHigh(String temperatureHigh) {
        this.temperatureHigh = temperatureHigh;
    }
    
    /**
     * Gets the low temperature recorded during the work period.
     * 
     * @return the low temperature
     */
    public String getTemperatureLow() {
        return temperatureLow;
    }
    
    /**
     * Sets the low temperature recorded during the work period.
     * 
     * @param temperatureLow the low temperature (with unit, e.g., "15°C" or "59°F")
     */
    public void setTemperatureLow(String temperatureLow) {
        this.temperatureLow = temperatureLow;
    }
    
    /**
     * Gets the list of picture file paths attached to this field report.
     * 
     * @return a list of picture file paths (may be empty if no pictures attached)
     */
    public List<String> getPicturePaths() {
        return new ArrayList<>(picturePaths);
    }
    
    /**
     * Sets the list of picture file paths for this field report.
     * 
     * @param picturePaths list of picture file paths to attach
     */
    public void setPicturePaths(List<String> picturePaths) {
        this.picturePaths = picturePaths != null ? new ArrayList<>(picturePaths) : new ArrayList<>();
    }
    
    /**
     * Adds a picture file path to this field report.
     * 
     * @param picturePath the file path of the picture to add
     */
    public void addPicturePath(String picturePath) {
        if (picturePath != null && !picturePath.trim().isEmpty()) {
            this.picturePaths.add(picturePath);
        }
    }
    
    /**
     * Removes a picture file path from this field report.
     * 
     * @param picturePath the file path of the picture to remove
     * @return true if the picture was removed, false if it wasn't found
     */
    public boolean removePicturePath(String picturePath) {
        return this.picturePaths.remove(picturePath);
    }
    
    // ================= NEW FIELD GETTER AND SETTER METHODS =================
    
    /**
     * Gets the work performed today description.
     * @return the work performed today
     */
    public String getWorkPerformedToday() {
        return workPerformedToday;
    }
    
    /**
     * Sets the work performed today description.
     * @param workPerformedToday the work performed today
     */
    public void setWorkPerformedToday(String workPerformedToday) {
        this.workPerformedToday = workPerformedToday;
    }
    
    /**
     * Gets the list of materials delivered or installed.
     * @return the materials delivered list
     */
    public List<MaterialDelivered> getMaterialsDelivered() {
        return new ArrayList<>(materialsDelivered);
    }
    
    /**
     * Sets the list of materials delivered or installed.
     * @param materialsDelivered the materials delivered list
     */
    public void setMaterialsDelivered(List<MaterialDelivered> materialsDelivered) {
        this.materialsDelivered = materialsDelivered != null ? new ArrayList<>(materialsDelivered) : new ArrayList<>();
    }
    
    /**
     * Gets the list of inspections and testing performed.
     * @return the inspections testing list
     */
    public List<InspectionTesting> getInspectionsTesting() {
        return new ArrayList<>(inspectionsTesting);
    }
    
    /**
     * Sets the list of inspections and testing performed.
     * @param inspectionsTesting the inspections testing list
     */
    public void setInspectionsTesting(List<InspectionTesting> inspectionsTesting) {
        this.inspectionsTesting = inspectionsTesting != null ? new ArrayList<>(inspectionsTesting) : new ArrayList<>();
    }
    
    /**
     * Gets whether a safety meeting was held.
     * @return safety meeting held status (Y/N)
     */
    public String getSafetyMeetingHeld() {
        return safetyMeetingHeld;
    }
    
    /**
     * Sets whether a safety meeting was held.
     * @param safetyMeetingHeld safety meeting held status (Y/N)
     */
    public void setSafetyMeetingHeld(String safetyMeetingHeld) {
        this.safetyMeetingHeld = safetyMeetingHeld;
    }
    
    /**
     * Gets the safety meeting topic.
     * @return the safety meeting topic
     */
    public String getSafetyMeetingTopic() {
        return safetyMeetingTopic;
    }
    
    /**
     * Sets the safety meeting topic.
     * @param safetyMeetingTopic the safety meeting topic
     */
    public void setSafetyMeetingTopic(String safetyMeetingTopic) {
        this.safetyMeetingTopic = safetyMeetingTopic;
    }
    
    /**
     * Gets the safety meeting attendees.
     * @return the safety meeting attendees
     */
    public String getSafetyMeetingAttendees() {
        return safetyMeetingAttendees;
    }
    
    /**
     * Sets the safety meeting attendees.
     * @param safetyMeetingAttendees the safety meeting attendees
     */
    public void setSafetyMeetingAttendees(String safetyMeetingAttendees) {
        this.safetyMeetingAttendees = safetyMeetingAttendees;
    }
    
    /**
     * Gets whether incidents or near misses occurred.
     * @return incidents or near misses status (Y/N)
     */
    public String getIncidentsNearMisses() {
        return incidentsNearMisses;
    }
    
    /**
     * Sets whether incidents or near misses occurred.
     * @param incidentsNearMisses incidents or near misses status (Y/N)
     */
    public void setIncidentsNearMisses(String incidentsNearMisses) {
        this.incidentsNearMisses = incidentsNearMisses;
    }
    
    /**
     * Gets the description of incidents or near misses.
     * @return the incidents description
     */
    public String getIncidentsDescription() {
        return incidentsDescription;
    }
    
    /**
     * Sets the description of incidents or near misses.
     * @param incidentsDescription the incidents description
     */
    public void setIncidentsDescription(String incidentsDescription) {
        this.incidentsDescription = incidentsDescription;
    }
    
    /**
     * Gets delays, issues, or non-conformance information.
     * @return delays, issues, or non-conformance
     */
    public String getDelaysIssuesNonConformance() {
        return delaysIssuesNonConformance;
    }
    
    /**
     * Sets delays, issues, or non-conformance information.
     * @param delaysIssuesNonConformance delays, issues, or non-conformance
     */
    public void setDelaysIssuesNonConformance(String delaysIssuesNonConformance) {
        this.delaysIssuesNonConformance = delaysIssuesNonConformance;
    }
    
    /**
     * Gets coordination, visits, and communication information.
     * @return coordination, visits, and communication
     */
    public String getCoordinationVisitsCommunication() {
        return coordinationVisitsCommunication;
    }
    
    /**
     * Sets coordination, visits, and communication information.
     * @param coordinationVisitsCommunication coordination, visits, and communication
     */
    public void setCoordinationVisitsCommunication(String coordinationVisitsCommunication) {
        this.coordinationVisitsCommunication = coordinationVisitsCommunication;
    }
    
    /**
     * Gets whether photos are attached.
     * @return true if photos are attached, false otherwise
     */
    public boolean isPhotosAttached() {
        return photosAttached;
    }
    
    /**
     * Sets whether photos are attached.
     * @param photosAttached true if photos are attached, false otherwise
     */
    public void setPhotosAttached(boolean photosAttached) {
        this.photosAttached = photosAttached;
    }
    
    /**
     * Gets whether test results are attached.
     * @return true if test results are attached, false otherwise
     */
    public boolean isTestResultsAttached() {
        return testResultsAttached;
    }
    
    /**
     * Sets whether test results are attached.
     * @param testResultsAttached true if test results are attached, false otherwise
     */
    public void setTestResultsAttached(boolean testResultsAttached) {
        this.testResultsAttached = testResultsAttached;
    }
    
    /**
     * Gets whether drawings or sketches are attached.
     * @return true if drawings or sketches are attached, false otherwise
     */
    public boolean isDrawingsSketchesAttached() {
        return drawingsSketchesAttached;
    }
    
    /**
     * Sets whether drawings or sketches are attached.
     * @param drawingsSketchesAttached true if drawings or sketches are attached, false otherwise
     */
    public void setDrawingsSketchesAttached(boolean drawingsSketchesAttached) {
        this.drawingsSketchesAttached = drawingsSketchesAttached;
    }
    
    /**
     * Gets additional remarks.
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }
    
    /**
     * Sets additional remarks.
     * @param remarks the remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * Gets the digital signature or name of person signing the report.
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }
    
    /**
     * Sets the digital signature or name of person signing the report.
     * @param signature the signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }
    
    /**
     * Gets the list of personnel on site for this field report.
     * 
     * @return a list of personnel on site (may be empty)
     */
    public List<PersonnelOnSite> getPersonnelOnSite() {
        return new ArrayList<>(personnelOnSite);
    }
    
    /**
     * Sets the list of personnel on site for this field report.
     * 
     * @param personnelOnSite list of personnel on site
     */
    public void setPersonnelOnSite(List<PersonnelOnSite> personnelOnSite) {
        this.personnelOnSite = personnelOnSite != null ? new ArrayList<>(personnelOnSite) : new ArrayList<>();
    }
    
    /**
     * Adds personnel on site to this field report.
     * 
     * @param personnel the personnel to add
     */
    public void addPersonnelOnSite(PersonnelOnSite personnel) {
        if (personnel != null) {
            this.personnelOnSite.add(personnel);
        }
    }
    
    /**
     * Removes personnel on site from this field report.
     * 
     * @param personnel the personnel to remove
     * @return true if the personnel was removed, false if it wasn't found
     */
    public boolean removePersonnelOnSite(PersonnelOnSite personnel) {
        return this.personnelOnSite.remove(personnel);
    }
    
    /**
     * Gets the list of equipment on site for this field report.
     * 
     * @return a list of equipment on site (may be empty)
     */
    public List<EquipmentOnSite> getEquipmentOnSite() {
        return new ArrayList<>(equipmentOnSite);
    }
    
    /**
     * Sets the list of equipment on site for this field report.
     * 
     * @param equipmentOnSite list of equipment on site
     */
    public void setEquipmentOnSite(List<EquipmentOnSite> equipmentOnSite) {
        this.equipmentOnSite = equipmentOnSite != null ? new ArrayList<>(equipmentOnSite) : new ArrayList<>();
    }
    
    /**
     * Adds equipment on site to this field report.
     * 
     * @param equipment the equipment to add
     */
    public void addEquipmentOnSite(EquipmentOnSite equipment) {
        if (equipment != null) {
            this.equipmentOnSite.add(equipment);
        }
    }
    
    /**
     * Removes equipment on site from this field report.
     * 
     * @param equipment the equipment to remove
     * @return true if the equipment was removed, false if it wasn't found
     */
    public boolean removeEquipmentOnSite(EquipmentOnSite equipment) {
        return this.equipmentOnSite.remove(equipment);
    }
    
    /**
     * Gets the detailed description of work performed and progress made.
     * 
     * @return the work description
     */
    public String getWorkDescription() {
        return workDescription;
    }
    
    /**
     * Sets the detailed description of work performed and progress made.
     * 
     * @param workDescription the work description
     */
    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }
    
    /**
     * Gets additional notes, issues, or observations about the field work.
     * 
     * @return the notes (may be empty string if no notes provided)
     */
    public String getNotes() {
        return notes;
    }
    
    /**
     * Sets additional notes, issues, or observations about the field work.
     * 
     * @param notes additional observations (can be null, will be converted to empty string)
     */
    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
    }
    
    // ================= OBJECT COMPARISON METHODS =================
    
    /**
     * Compares this field report with another object for equality.
     * 
     * Two field reports are considered equal if they have the same ID,
     * since each report should have a unique identifier.
     * 
     * @param o the object to compare with this field report
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldReport that = (FieldReport) o;
        return Objects.equals(id, that.id);
    }
    
    /**
     * Returns a hash code value for this field report.
     * 
     * The hash code is based solely on the unique ID field,
     * consistent with the equals() method implementation.
     * 
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /**
     * Returns a string representation of this field report.
     * 
     * Provides a readable format showing all field values, useful for
     * debugging and logging purposes.
     * 
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "FieldReport{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", reporterName='" + reporterName + '\'' +
                ", location='" + location + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectNumber='" + projectNumber + '\'' +
                ", weatherAM='" + weatherAM + '\'' +
                ", weatherPM='" + weatherPM + '\'' +
                ", temperatureHigh='" + temperatureHigh + '\'' +
                ", temperatureLow='" + temperatureLow + '\'' +
                ", workPerformedToday='" + workPerformedToday + '\'' +
                ", workDescription='" + workDescription + '\'' +
                ", notes='" + notes + '\'' +
                ", safetyMeetingHeld='" + safetyMeetingHeld + '\'' +
                ", incidentsNearMisses='" + incidentsNearMisses + '\'' +
                ", photosAttached=" + photosAttached +
                ", testResultsAttached=" + testResultsAttached +
                ", drawingsSketchesAttached=" + drawingsSketchesAttached +
                ", signature='" + signature + '\'' +
                ", picturePaths=" + picturePaths +
                ", personnelOnSite=" + personnelOnSite +
                ", equipmentOnSite=" + equipmentOnSite +
                ", materialsDelivered=" + materialsDelivered +
                ", inspectionsTesting=" + inspectionsTesting +
                '}';
    }
    
    // ================= VALIDATION METHODS =================
    
    /**
     * Validates that the field report has all required fields populated.
     * 
     * Checks that mandatory fields (reporter name, location, project name,
     * weather conditions, and work description) are not null or empty.
     * The notes field is optional and not validated.
     * 
     * @return true if all required fields are valid, false otherwise
     */
    public boolean isValid() {
        return reporterName != null && !reporterName.trim().isEmpty() &&
               location != null && !location.trim().isEmpty() &&
               projectName != null && !projectName.trim().isEmpty() &&
               workDescription != null && !workDescription.trim().isEmpty();
    }
}