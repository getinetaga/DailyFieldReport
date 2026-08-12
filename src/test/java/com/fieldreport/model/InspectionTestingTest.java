package com.fieldreport.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive unit tests for the InspectionTesting model class.
 * 
 * Tests cover:
 * - Constructor functionality
 * - Getter and setter methods
 * - Validation methods
 * - Test result tracking
 * - Edge cases and error conditions
 * 
 * @author DailyFieldReport System
 * @version 1.0
 */
@DisplayName("InspectionTesting Model Tests")
class InspectionTestingTest {

    private InspectionTesting inspection;

    @BeforeEach
    void setUp() {
        inspection = new InspectionTesting();
    }

    // ================= CONSTRUCTOR TESTS =================

    @Test
    @DisplayName("Default constructor should initialize all fields")
    void testDefaultConstructor() {
        // Default constructor sets all fields to null based on the model
        assertNull(inspection.getInspection());
        assertNull(inspection.getTestInspector());
        assertNull(inspection.getAgency());
        assertNull(inspection.getTestRemarks());
    }

    @Test
    @DisplayName("Parameterized constructor should set all fields correctly")
    void testParameterizedConstructor() {
        String inspectionType = "Concrete Slump Test";
        String testInspector = "John Smith";
        String agency = "City Building Dept";
        String testRemarks = "Passed - 4 inch slump";

        InspectionTesting testInspection = new InspectionTesting(inspectionType, testInspector, agency, testRemarks);

        assertEquals(inspectionType, testInspection.getInspection());
        assertEquals(testInspector, testInspection.getTestInspector());
        assertEquals(agency, testInspection.getAgency());
        assertEquals(testRemarks, testInspection.getTestRemarks());
    }

    @Test
    @DisplayName("Constructor should handle null values gracefully")
    void testConstructorWithNullValues() {
        InspectionTesting testInspection = new InspectionTesting(null, null, null, null);

        assertNull(testInspection.getInspection());
        assertNull(testInspection.getTestInspector());
        assertNull(testInspection.getAgency());
        assertNull(testInspection.getTestRemarks());
    }

    // ================= GETTER AND SETTER TESTS =================

    @Test
    @DisplayName("Inspection type getter and setter should work correctly")
    void testInspection() {
        String inspectionType = "Steel Reinforcement Inspection";
        inspection.setInspection(inspectionType);
        assertEquals(inspectionType, inspection.getInspection());

        inspection.setInspection(null);
        assertNull(inspection.getInspection());
    }

    @Test
    @DisplayName("Test inspector getter and setter should work correctly")
    void testTestInspector() {
        String testInspector = "Jane Doe, PE";
        inspection.setTestInspector(testInspector);
        assertEquals(testInspector, inspection.getTestInspector());

        inspection.setTestInspector(null);
        assertNull(inspection.getTestInspector());
    }

    @Test
    @DisplayName("Agency getter and setter should work correctly")
    void testAgency() {
        String agency = "State Department of Transportation";
        inspection.setAgency(agency);
        assertEquals(agency, inspection.getAgency());

        inspection.setAgency(null);
        assertNull(inspection.getAgency());
    }

    @Test
    @DisplayName("Test remarks getter and setter should work correctly")
    void testTestRemarks() {
        String testRemarks = "All measurements within acceptable limits";
        inspection.setTestRemarks(testRemarks);
        assertEquals(testRemarks, inspection.getTestRemarks());

        inspection.setTestRemarks(null);
        assertNull(inspection.getTestRemarks());
    }

    // ================= VALIDATION TESTS =================

    @Test
    @DisplayName("isValid should return true for properly configured inspection")
    void testIsValidTrue() {
        inspection.setInspection("Concrete Test");
        inspection.setTestInspector("John Smith");
        inspection.setAgency("City Dept");

        assertTrue(inspection.isValid());
    }

    @Test
    @DisplayName("isValid should return false for incomplete inspection")
    void testIsValidFalse() {
        // Empty inspection type
        assertFalse(inspection.isValid());

        inspection.setInspection("Concrete Test");
        // Empty test inspector
        assertFalse(inspection.isValid());

        inspection.setTestInspector("John Smith");
        // Now should be valid (agency and testRemarks are optional according to isValid method)
        assertTrue(inspection.isValid());
    }

    @Test
    @DisplayName("isValid should handle null values")
    void testIsValidWithNulls() {
        inspection.setInspection(null);
        inspection.setTestInspector("Inspector");
        inspection.setAgency("Agency");
        assertFalse(inspection.isValid());

        inspection.setInspection("Inspection");
        inspection.setTestInspector(null);
        assertFalse(inspection.isValid());

        inspection.setTestInspector("Inspector");
        inspection.setAgency(null);
        // Should still be valid since agency is optional
        assertTrue(inspection.isValid());
    }

    @Test
    @DisplayName("isValid should handle whitespace-only values")
    void testIsValidWithWhitespace() {
        inspection.setInspection("   ");  // Whitespace only
        inspection.setTestInspector("Inspector");
        inspection.setAgency("Agency");
        assertFalse(inspection.isValid());

        inspection.setInspection("Inspection");
        inspection.setTestInspector("   ");  // Whitespace only
        assertFalse(inspection.isValid());

        inspection.setTestInspector("Inspector");
        inspection.setAgency("   ");  // Whitespace only
        // Should still be valid since agency is optional
        assertTrue(inspection.isValid());
    }

    // ================= UTILITY TESTS =================

    @Test
    @DisplayName("toString should include all fields")
    void testToString() {
        inspection.setInspection("Test Inspection");
        inspection.setTestInspector("Test Inspector");
        inspection.setAgency("Test Agency");
        inspection.setTestRemarks("Test Remarks");

        String toString = inspection.toString();
        
        assertTrue(toString.contains("InspectionTesting{"));
        assertTrue(toString.contains("inspection='Test Inspection'"));
        assertTrue(toString.contains("testInspector='Test Inspector'"));
        assertTrue(toString.contains("agency='Test Agency'"));
        assertTrue(toString.contains("testRemarks='Test Remarks'"));
    }

    @Test
    @DisplayName("getSummary should format inspection information correctly")
    void testGetSummary() {
        inspection.setInspection("Concrete Cylinder Test");
        inspection.setTestInspector("John Smith, PE");
        inspection.setAgency("State DOT");
        inspection.setTestRemarks("28-day strength: 4200 PSI - Passed");

        String summary = inspection.getSummary();
        
        assertTrue(summary.contains("Concrete Cylinder Test"));
        assertTrue(summary.contains("John Smith, PE"));
        assertTrue(summary.contains("State DOT"));
    }

    @Test
    @DisplayName("getSummary should handle null agency gracefully")
    void testGetSummaryWithNullAgency() {
        inspection.setInspection("Concrete Test");
        inspection.setTestInspector("John Smith");
        inspection.setAgency(null);

        String summary = inspection.getSummary();
        assertTrue(summary.contains("Internal"));
    }

    @Test
    @DisplayName("getSummary should handle null values gracefully")
    void testGetSummaryWithNulls() {
        // Test with null values - should not throw exception
        assertDoesNotThrow(() -> {
            String summary = inspection.getSummary();
            assertNotNull(summary);
        });
    }

    @Test
    @DisplayName("isCompleted should work correctly")
    void testIsCompleted() {
        assertFalse(inspection.isCompleted());

        inspection.setTestRemarks("");
        assertFalse(inspection.isCompleted());

        inspection.setTestRemarks("   ");
        assertFalse(inspection.isCompleted());

        inspection.setTestRemarks("Test completed successfully");
        assertTrue(inspection.isCompleted());
    }

    // ================= EDGE CASE TESTS =================

    @Test
    @DisplayName("Setting empty strings should work correctly")
    void testEmptyStrings() {
        inspection.setInspection("");
        inspection.setTestInspector("");
        inspection.setAgency("");
        inspection.setTestRemarks("");
        
        assertEquals("", inspection.getInspection());
        assertEquals("", inspection.getTestInspector());
        assertEquals("", inspection.getAgency());
        assertEquals("", inspection.getTestRemarks());
        
        assertFalse(inspection.isValid());
        assertFalse(inspection.isCompleted());
    }

    @Test
    @DisplayName("Inspection with all fields should be valid")
    void testCompleteValidInspection() {
        inspection.setInspection("Reinforcing Steel Placement Inspection");
        inspection.setTestInspector("Jane Doe, PE, CWI");
        inspection.setAgency("County Building Department");
        inspection.setTestRemarks("All rebar placement conforms to drawings. Splice lengths verified. Approved for concrete placement.");

        assertTrue(inspection.isValid());
        assertTrue(inspection.isCompleted());
        
        String summary = inspection.getSummary();
        assertNotNull(summary);
        assertFalse(summary.isEmpty());
    }

    @Test
    @DisplayName("Inspection with special characters in fields")
    void testSpecialCharacters() {
        inspection.setInspection("Concrete Test (f'c = 4000 PSI)");
        inspection.setTestInspector("John Smith, P.E. #12345");
        inspection.setAgency("State DOT - Materials & Testing Division");
        inspection.setTestRemarks("7-day strength: 3,200 PSI (80% of f'c) - On track");

        assertTrue(inspection.isValid());
        assertTrue(inspection.isCompleted());
        
        String toString = inspection.toString();
        assertTrue(toString.contains("Concrete Test (f'c = 4000 PSI)"));
        assertTrue(toString.contains("John Smith, P.E. #12345"));
        assertTrue(toString.contains("State DOT - Materials & Testing Division"));
        assertTrue(toString.contains("7-day strength: 3,200 PSI (80% of f'c) - On track"));
    }

    @Test
    @DisplayName("Inspection workflow simulation")
    void testInspectionWorkflow() {
        // Initial inspection setup
        inspection.setInspection("Foundation Inspection");
        inspection.setTestInspector("Mike Johnson");
        inspection.setAgency("City Building Department");
        inspection.setTestRemarks("Pending");

        assertTrue(inspection.isValid());
        assertTrue(inspection.isCompleted());
        assertEquals("Pending", inspection.getTestRemarks());

        // Inspection completed
        inspection.setTestRemarks("Foundation excavation depth verified at 8 feet. Soil bearing capacity adequate. Approved for footing placement.");
        
        String summary = inspection.getSummary();
        assertTrue(summary.contains("Foundation Inspection"));
    }

    @Test
    @DisplayName("Minimal valid inspection should work")
    void testMinimalValidInspection() {
        inspection.setInspection("Basic Test");
        inspection.setTestInspector("Inspector");
        
        assertTrue(inspection.isValid());
        
        // agency and testRemarks can be null
        assertNull(inspection.getAgency());
        assertNull(inspection.getTestRemarks());
        assertFalse(inspection.isCompleted());
    }

    @Test
    @DisplayName("Multiple inspection types should be handled")
    void testMultipleInspectionTypes() {
        String[] inspectionTypes = {
            "Concrete Slump Test",
            "Steel Reinforcement Inspection", 
            "Soil Compaction Test",
            "Welding Inspection",
            "Electrical Rough-in Inspection",
            "Plumbing Pressure Test",
            "Fire Protection System Test"
        };
        
        for (String type : inspectionTypes) {
            inspection.setInspection(type);
            inspection.setTestInspector("Test Inspector");
            inspection.setAgency("Test Agency");
            
            assertTrue(inspection.isValid(), "Inspection type '" + type + "' should be valid");
            assertEquals(type, inspection.getInspection());
        }
    }

    @Test
    @DisplayName("Long text fields should be handled correctly")
    void testLongTextFields() {
        String longInspection = "Very detailed inspection type with lots of technical specifications and requirements that goes on and on";
        String longInspector = "Dr. John Michael Smith, PE, SE, PhD, Professional Engineer License #12345, Structural Engineering Specialist";
        String longAgency = "State Department of Transportation, Materials and Testing Division, Quality Assurance Department";
        String longRemarks = "Comprehensive test results: Multiple samples taken at various locations throughout the project site. " +
                           "All test results fall within acceptable parameters as specified in the project specifications. " +
                           "Temperature conditions were optimal. Humidity levels recorded. All documentation attached.";
        
        inspection.setInspection(longInspection);
        inspection.setTestInspector(longInspector);
        inspection.setAgency(longAgency);
        inspection.setTestRemarks(longRemarks);
        
        assertTrue(inspection.isValid());
        assertTrue(inspection.isCompleted());
        assertEquals(longInspection, inspection.getInspection());
        assertEquals(longInspector, inspection.getTestInspector());
        assertEquals(longAgency, inspection.getAgency());
        assertEquals(longRemarks, inspection.getTestRemarks());
    }
}