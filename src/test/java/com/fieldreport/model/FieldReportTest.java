package com.fieldreport.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive unit tests for the FieldReport model class.
 * 
 * Tests cover:
 * - Constructor functionality (all 3 constructors)
 * - Getter and setter methods for all 20+ fields
 * - Validation methods
 * - List management (personnel, equipment, materials, inspections)
 * - Picture attachment functionality
 * - Safety sections
 * - Weather tracking (AM/PM and temperature)
 * - Edge cases and error conditions
 * 
 * @author DailyFieldReport System
 * @version 1.0
 */
@DisplayName("FieldReport Model Tests")
class FieldReportTest {
    
    private FieldReport fieldReport;
    
    @BeforeEach
    void setUp() {
        fieldReport = new FieldReport(
            "John Doe",
            "Construction Site A",
            "Building Project",
            "BR-2024-001",
            "Sunny, 60°F",
            "Partly cloudy, 72°F",
            "75°F",
            "58°F",
            "Completed foundation work and started framing",
            "Weather conditions were favorable for concrete work"
        );
    }
    
    // ================= CONSTRUCTOR TESTS =================
    
    @Test
    @DisplayName("Default constructor should initialize all fields correctly")
    void testDefaultConstructor() {
        FieldReport report = new FieldReport();
        
        assertNotNull(report.getId());
        assertNotNull(report.getDate());
        assertNotNull(report.getCreatedAt());
        assertNotNull(report.getPersonnelOnSite());
        assertNotNull(report.getEquipmentOnSite());
        assertNotNull(report.getMaterialsDelivered());
        assertNotNull(report.getInspectionsTesting());
        assertNotNull(report.getPicturePaths());
        
        assertEquals(LocalDate.now(), report.getDate());
        assertTrue(report.getPersonnelOnSite().isEmpty());
        assertTrue(report.getEquipmentOnSite().isEmpty());
        assertTrue(report.getMaterialsDelivered().isEmpty());
        assertTrue(report.getInspectionsTesting().isEmpty());
        assertTrue(report.getPicturePaths().isEmpty());
    }
    
    @Test
    @DisplayName("Comprehensive constructor should set all fields correctly")
    void testComprehensiveConstructor() {
        assertNotNull(fieldReport.getId());
        assertEquals("John Doe", fieldReport.getReporterName());
        assertEquals("Construction Site A", fieldReport.getLocation());
        assertEquals("Building Project", fieldReport.getProjectName());
        assertEquals("BR-2024-001", fieldReport.getProjectNumber());
        assertEquals("Sunny, 60°F", fieldReport.getWeatherAM());
        assertEquals("Partly cloudy, 72°F", fieldReport.getWeatherPM());
        assertEquals("75°F", fieldReport.getTemperatureHigh());
        assertEquals("58°F", fieldReport.getTemperatureLow());
        assertEquals("Completed foundation work and started framing", fieldReport.getWorkDescription());
        assertEquals("Weather conditions were favorable for concrete work", fieldReport.getNotes());
        assertEquals(LocalDate.now(), fieldReport.getDate());
    }
    
    @Test
    @DisplayName("Constructor should handle null notes gracefully")
    void testConstructorWithNullNotes() {
        FieldReport report = new FieldReport(
            "Jane Smith", "Site B", "Road Project", "RD-2024-002",
            "Cloudy", "Rainy", "65°F", "55°F", "Grading work", null
        );
        
        assertEquals("", report.getNotes());
    }
    
    // ================= GETTER AND SETTER TESTS =================
    
    @Test
    @DisplayName("Basic field getters and setters should work correctly")
    void testBasicFieldAccessors() {
        fieldReport.setReporterName("Jane Smith");
        assertEquals("Jane Smith", fieldReport.getReporterName());
        
        fieldReport.setLocation("New Construction Site");
        assertEquals("New Construction Site", fieldReport.getLocation());
        
        fieldReport.setProjectName("Highway Extension");
        assertEquals("Highway Extension", fieldReport.getProjectName());
        
        fieldReport.setProjectNumber("HW-2024-003");
        assertEquals("HW-2024-003", fieldReport.getProjectNumber());
    }
    
    @Test
    @DisplayName("Weather and temperature fields should work correctly")
    void testWeatherAndTemperatureFields() {
        fieldReport.setWeatherAM("Foggy, 55°F");
        assertEquals("Foggy, 55°F", fieldReport.getWeatherAM());
        
        fieldReport.setWeatherPM("Clear, 78°F");
        assertEquals("Clear, 78°F", fieldReport.getWeatherPM());
        
        fieldReport.setTemperatureHigh("82°F");
        assertEquals("82°F", fieldReport.getTemperatureHigh());
        
        fieldReport.setTemperatureLow("52°F");
        assertEquals("52°F", fieldReport.getTemperatureLow());
    }
    
    @Test
    @DisplayName("Work description and notes should work correctly")
    void testWorkDescriptionAndNotes() {
        fieldReport.setWorkDescription("Excavation and utility installation");
        assertEquals("Excavation and utility installation", fieldReport.getWorkDescription());
        
        fieldReport.setWorkPerformedToday("Detailed daily activities and progress");
        assertEquals("Detailed daily activities and progress", fieldReport.getWorkPerformedToday());
        
        fieldReport.setNotes("Equipment delayed due to traffic");
        assertEquals("Equipment delayed due to traffic", fieldReport.getNotes());
        
        fieldReport.setNotes(null);
        assertEquals("", fieldReport.getNotes());
    }
    
    @Test
    @DisplayName("Safety fields should work correctly")
    void testSafetyFields() {
        fieldReport.setSafetyMeetingHeld("Y");
        assertEquals("Y", fieldReport.getSafetyMeetingHeld());
        
        fieldReport.setSafetyMeetingTopic("Fall Protection");
        assertEquals("Fall Protection", fieldReport.getSafetyMeetingTopic());
        
        fieldReport.setSafetyMeetingAttendees("All crew members");
        assertEquals("All crew members", fieldReport.getSafetyMeetingAttendees());
        
        fieldReport.setIncidentsNearMisses("N");
        assertEquals("N", fieldReport.getIncidentsNearMisses());
        
        fieldReport.setIncidentsDescription("No incidents occurred");
        assertEquals("No incidents occurred", fieldReport.getIncidentsDescription());
    }
    
    @Test
    @DisplayName("Project coordination fields should work correctly")
    void testProjectCoordinationFields() {
        fieldReport.setDelaysIssuesNonConformance("Minor material delivery delay");
        assertEquals("Minor material delivery delay", fieldReport.getDelaysIssuesNonConformance());
        
        fieldReport.setCoordinationVisitsCommunication("Inspector visit scheduled for tomorrow");
        assertEquals("Inspector visit scheduled for tomorrow", fieldReport.getCoordinationVisitsCommunication());
        
        fieldReport.setRemarks("Project ahead of schedule");
        assertEquals("Project ahead of schedule", fieldReport.getRemarks());
        
        fieldReport.setSignature("John Doe, Project Manager");
        assertEquals("John Doe, Project Manager", fieldReport.getSignature());
    }
    
    @Test
    @DisplayName("Attachment flags should work correctly")
    void testAttachmentFlags() {
        fieldReport.setPhotosAttached(true);
        assertTrue(fieldReport.isPhotosAttached());
        
        fieldReport.setTestResultsAttached(true);
        assertTrue(fieldReport.isTestResultsAttached());
        
        fieldReport.setDrawingsSketchesAttached(false);
        assertFalse(fieldReport.isDrawingsSketchesAttached());
    }
    
    // ================= LIST MANAGEMENT TESTS =================
    
    @Test
    @DisplayName("Personnel list management should work correctly")
    void testPersonnelListManagement() {
        assertTrue(fieldReport.getPersonnelOnSite().isEmpty());
        
        PersonnelOnSite person1 = new PersonnelOnSite("ABC Construction", "Foreman", 1, 8, "John Smith");
        PersonnelOnSite person2 = new PersonnelOnSite("XYZ Electric", "Electrician", 2, 8, "Jane Doe");
        
        fieldReport.addPersonnelOnSite(person1);
        fieldReport.addPersonnelOnSite(person2);
        
        assertEquals(2, fieldReport.getPersonnelOnSite().size());
        assertTrue(fieldReport.getPersonnelOnSite().contains(person1));
        assertTrue(fieldReport.getPersonnelOnSite().contains(person2));
        
        fieldReport.removePersonnelOnSite(person1);
        assertEquals(1, fieldReport.getPersonnelOnSite().size());
        assertFalse(fieldReport.getPersonnelOnSite().contains(person1));
    }
    
    @Test
    @DisplayName("Equipment list management should work correctly")
    void testEquipmentListManagement() {
        assertTrue(fieldReport.getEquipmentOnSite().isEmpty());
        
        EquipmentOnSite equipment1 = new EquipmentOnSite("Excavator", "CAT 320", 1, true, "");
        EquipmentOnSite equipment2 = new EquipmentOnSite("Crane", "50-ton", 1, false, "Maintenance");
        
        fieldReport.addEquipmentOnSite(equipment1);
        fieldReport.addEquipmentOnSite(equipment2);
        
        assertEquals(2, fieldReport.getEquipmentOnSite().size());
        assertTrue(fieldReport.getEquipmentOnSite().contains(equipment1));
        assertTrue(fieldReport.getEquipmentOnSite().contains(equipment2));
        
        fieldReport.removeEquipmentOnSite(equipment1);
        assertEquals(1, fieldReport.getEquipmentOnSite().size());
        assertFalse(fieldReport.getEquipmentOnSite().contains(equipment1));
    }
    
    @Test
    @DisplayName("Materials list management should work correctly")
    void testMaterialsListManagement() {
        assertTrue(fieldReport.getMaterialsDelivered().isEmpty());
        
        MaterialDelivered material1 = new MaterialDelivered("Concrete", "ABC Supply", "10 cu yd", "Area A", "Passed");
        MaterialDelivered material2 = new MaterialDelivered("Steel Rebar", "XYZ Materials", "2 tons", "Storage", "Pending");
        
        List<MaterialDelivered> materials = new ArrayList<>();
        materials.add(material1);
        materials.add(material2);
        fieldReport.setMaterialsDelivered(materials);
        
        assertEquals(2, fieldReport.getMaterialsDelivered().size());
        assertTrue(fieldReport.getMaterialsDelivered().contains(material1));
        assertTrue(fieldReport.getMaterialsDelivered().contains(material2));
        
        // Test removing by creating new list without first material
        List<MaterialDelivered> updatedMaterials = new ArrayList<>();
        updatedMaterials.add(material2);
        fieldReport.setMaterialsDelivered(updatedMaterials);
        
        assertEquals(1, fieldReport.getMaterialsDelivered().size());
        assertFalse(fieldReport.getMaterialsDelivered().contains(material1));
        assertTrue(fieldReport.getMaterialsDelivered().contains(material2));
    }
    
    @Test
    @DisplayName("Inspections list management should work correctly")
    void testInspectionsListManagement() {
        assertTrue(fieldReport.getInspectionsTesting().isEmpty());
        
        InspectionTesting inspection1 = new InspectionTesting("Foundation", "John Smith", "City", "Passed");
        InspectionTesting inspection2 = new InspectionTesting("Concrete", "Jane Doe", "State", "Pending");
        
        List<InspectionTesting> inspections = new ArrayList<>();
        inspections.add(inspection1);
        inspections.add(inspection2);
        fieldReport.setInspectionsTesting(inspections);
        
        assertEquals(2, fieldReport.getInspectionsTesting().size());
        assertTrue(fieldReport.getInspectionsTesting().contains(inspection1));
        assertTrue(fieldReport.getInspectionsTesting().contains(inspection2));
        
        // Test removing by creating new list without first inspection
        List<InspectionTesting> updatedInspections = new ArrayList<>();
        updatedInspections.add(inspection2);
        fieldReport.setInspectionsTesting(updatedInspections);
        
        assertEquals(1, fieldReport.getInspectionsTesting().size());
        assertFalse(fieldReport.getInspectionsTesting().contains(inspection1));
        assertTrue(fieldReport.getInspectionsTesting().contains(inspection2));
    }
    
    // ================= PICTURE ATTACHMENT TESTS =================
    
    @Test
    @DisplayName("Picture path management should work correctly")
    void testPicturePathManagement() {
        assertTrue(fieldReport.getPicturePaths().isEmpty());
        
        fieldReport.addPicturePath("/path/to/photo1.jpg");
        fieldReport.addPicturePath("/path/to/photo2.jpg");
        
        assertEquals(2, fieldReport.getPicturePaths().size());
        assertTrue(fieldReport.getPicturePaths().contains("/path/to/photo1.jpg"));
        assertTrue(fieldReport.getPicturePaths().contains("/path/to/photo2.jpg"));
        
        fieldReport.removePicturePath("/path/to/photo1.jpg");
        assertEquals(1, fieldReport.getPicturePaths().size());
        assertFalse(fieldReport.getPicturePaths().contains("/path/to/photo1.jpg"));
    }
    
    @Test
    @DisplayName("Picture path validation should work correctly")
    void testPicturePathValidation() {
        // Test null handling
        fieldReport.addPicturePath(null);
        assertTrue(fieldReport.getPicturePaths().isEmpty());
        
        // Test empty string handling
        fieldReport.addPicturePath("");
        assertTrue(fieldReport.getPicturePaths().isEmpty());
        
        // Test duplicate prevention
        fieldReport.addPicturePath("/path/to/photo.jpg");
        fieldReport.addPicturePath("/path/to/photo.jpg");
        assertEquals(1, fieldReport.getPicturePaths().size());
    }
    
    // ================= VALIDATION TESTS =================
    
    @Test
    @DisplayName("isValid should return true for properly configured report")
    void testIsValidTrue() {
        assertTrue(fieldReport.isValid());
    }
    
    @Test
    @DisplayName("isValid should return false for incomplete reports")
    void testIsValidFalse() {
        // Test empty reporter name
        FieldReport invalidReport1 = new FieldReport("", "Location", "Project", "P001", "AM", "PM", "80", "60", "Work", "Notes");
        assertFalse(invalidReport1.isValid());
        
        // Test empty location
        FieldReport invalidReport2 = new FieldReport("Name", "", "Project", "P001", "AM", "PM", "80", "60", "Work", "Notes");
        assertFalse(invalidReport2.isValid());
        
        // Test empty project name
        FieldReport invalidReport3 = new FieldReport("Name", "Location", "", "P001", "AM", "PM", "80", "60", "Work", "Notes");
        assertFalse(invalidReport3.isValid());
        
        // Test empty work description
        FieldReport invalidReport4 = new FieldReport("Name", "Location", "Project", "P001", "AM", "PM", "80", "60", "", "Notes");
        assertFalse(invalidReport4.isValid());
    }
    
    @Test
    @DisplayName("isValid should handle null values correctly")
    void testIsValidWithNulls() {
        fieldReport.setReporterName(null);
        assertFalse(fieldReport.isValid());
        
        fieldReport.setReporterName("John Doe");
        fieldReport.setLocation(null);
        assertFalse(fieldReport.isValid());
        
        fieldReport.setLocation("Site A");
        fieldReport.setProjectName(null);
        assertFalse(fieldReport.isValid());
    }
    
    // ================= UTILITY TESTS =================
    
    @Test
    @DisplayName("toString should include key fields")
    void testToString() {
        String toString = fieldReport.toString();
        assertTrue(toString.contains("John Doe"));
        assertTrue(toString.contains("Construction Site A"));
        assertTrue(toString.contains("Building Project"));
        assertTrue(toString.contains("BR-2024-001"));
    }
    
    @Test
    @DisplayName("equals and hashCode should work correctly")
    void testEqualsAndHashCode() {
        String id = fieldReport.getId();
        
        FieldReport sameReport = new FieldReport();
        sameReport.setId(id);
        
        assertEquals(fieldReport, sameReport);
        assertEquals(fieldReport.hashCode(), sameReport.hashCode());
        
        FieldReport differentReport = new FieldReport();
        assertNotEquals(fieldReport, differentReport);
    }
    
    // ================= EDGE CASE TESTS =================
    
    @Test
    @DisplayName("Date handling should work correctly")
    void testDateHandling() {
        LocalDate specificDate = LocalDate.of(2024, 12, 25);
        fieldReport.setDate(specificDate);
        assertEquals(specificDate, fieldReport.getDate());
    }
    
    @Test
    @DisplayName("Large data handling should work correctly")
    void testLargeDataHandling() {
        // Add many personnel
        for (int i = 0; i < 10; i++) {
            PersonnelOnSite person = new PersonnelOnSite("Company " + i, "Role " + i, 1, 8, "Person " + i);
            fieldReport.addPersonnelOnSite(person);
        }
        assertEquals(10, fieldReport.getPersonnelOnSite().size());
        
        // Add many equipment items
        for (int i = 0; i < 5; i++) {
            EquipmentOnSite equipment = new EquipmentOnSite("Equipment " + i, "Type " + i, 1, true, "");
            fieldReport.addEquipmentOnSite(equipment);
        }
        assertEquals(5, fieldReport.getEquipmentOnSite().size());
        
        // Add many pictures
        for (int i = 0; i < 20; i++) {
            fieldReport.addPicturePath("/photos/photo" + i + ".jpg");
        }
        assertEquals(20, fieldReport.getPicturePaths().size());
    }
    
    @Test
    @DisplayName("Comprehensive workflow simulation")
    void testComprehensiveWorkflow() {
        // Start with a basic report
        FieldReport report = new FieldReport();
        report.setReporterName("Site Supervisor");
        report.setLocation("Highway Construction Zone");
        report.setProjectName("State Route 101 Expansion");
        report.setProjectNumber("SR101-2024-EXPAND");
        report.setWeatherAM("Clear, 45°F");
        report.setWeatherPM("Cloudy, 62°F");
        report.setTemperatureHigh("65°F");
        report.setTemperatureLow("42°F");
        report.setWorkDescription("Pavement marking and signage installation");
        
        // Add personnel
        report.addPersonnelOnSite(new PersonnelOnSite("Main Contractor", "Supervisor", 1, 10, "John Smith"));
        report.addPersonnelOnSite(new PersonnelOnSite("Marking Crew", "Painter", 3, 8, "Jane Doe"));
        
        // Add equipment
        report.addEquipmentOnSite(new EquipmentOnSite("Line Painter", "Truck Mounted", 1, true, ""));
        report.addEquipmentOnSite(new EquipmentOnSite("Traffic Control Vehicle", "Pickup", 2, true, ""));
        
        // Add materials
        List<MaterialDelivered> materials = new ArrayList<>();
        materials.add(new MaterialDelivered("Paint", "Highway Supply Co", "50 gallons", "Truck", "Approved"));
        materials.add(new MaterialDelivered("Signs", "Sign Works Inc", "25 units", "Staging Area", "Inspected"));
        report.setMaterialsDelivered(materials);
        
        // Add inspection
        List<InspectionTesting> inspections = new ArrayList<>();
        inspections.add(new InspectionTesting("Pavement Marking Quality", "State Inspector", "DOT", "Meets specifications"));
        report.setInspectionsTesting(inspections);
        
        // Safety information
        report.setSafetyMeetingHeld("Y");
        report.setSafetyMeetingTopic("Traffic Control and Worker Safety");
        report.setSafetyMeetingAttendees("All crew members");
        report.setIncidentsNearMisses("N");
        
        // Add pictures
        report.addPicturePath("/photos/before_work.jpg");
        report.addPicturePath("/photos/line_painting.jpg");
        report.addPicturePath("/photos/completed_section.jpg");
        
        // Attachments
        report.setPhotosAttached(true);
        report.setTestResultsAttached(false);
        report.setDrawingsSketchesAttached(true);
        
        // Final details
        report.setRemarks("Work completed ahead of schedule. Weather was favorable.");
        report.setSignature("John Smith, Site Supervisor");
        
        // Validate comprehensive report
        assertTrue(report.isValid());
        assertEquals(2, report.getPersonnelOnSite().size());
        assertEquals(2, report.getEquipmentOnSite().size());
        assertEquals(2, report.getMaterialsDelivered().size());
        assertEquals(1, report.getInspectionsTesting().size());
        assertEquals(3, report.getPicturePaths().size());
        assertTrue(report.isPhotosAttached());
        assertTrue(report.isDrawingsSketchesAttached());
        assertFalse(report.isTestResultsAttached());
    }
}