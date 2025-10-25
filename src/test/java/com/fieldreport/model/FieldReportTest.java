package com.fieldreport.model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the FieldReport model class
 */
class FieldReportTest {
    
    private FieldReport fieldReport;
    
    @BeforeEach
    void setUp() {
        fieldReport = new FieldReport(
            "John Doe",
            "Construction Site A",
            "Building Project",
            "Sunny, 25°C",
            "Completed foundation work",
            "No issues encountered"
        );
    }
    
    @Test
    void testFieldReportCreation() {
        assertNotNull(fieldReport);
        assertNotNull(fieldReport.getId());
        assertEquals("John Doe", fieldReport.getReporterName());
        assertEquals("Construction Site A", fieldReport.getLocation());
        assertEquals("Building Project", fieldReport.getProjectName());
        assertEquals("Sunny, 25°C", fieldReport.getWeatherConditions());
        assertEquals("Completed foundation work", fieldReport.getWorkDescription());
        assertEquals("No issues encountered", fieldReport.getNotes());
        assertEquals(LocalDate.now(), fieldReport.getDate());
    }
    
    @Test
    void testFieldReportWithSpecificDate() {
        LocalDate specificDate = LocalDate.of(2024, 1, 15);
        FieldReport reportWithDate = new FieldReport(
            specificDate,
            "Jane Smith",
            "Site B",
            "Renovation Project",
            "Cloudy",
            "Electrical work",
            "Power outage for 2 hours"
        );
        
        assertEquals(specificDate, reportWithDate.getDate());
        assertEquals("Jane Smith", reportWithDate.getReporterName());
    }
    
    @Test
    void testFieldReportValidation() {
        assertTrue(fieldReport.isValid());
        
        // Test invalid reports
        FieldReport invalidReport1 = new FieldReport("", "Location", "Project", "Weather", "Work", "Notes");
        assertFalse(invalidReport1.isValid());
        
        FieldReport invalidReport2 = new FieldReport("Name", "", "Project", "Weather", "Work", "Notes");
        assertFalse(invalidReport2.isValid());
        
        FieldReport invalidReport3 = new FieldReport("Name", "Location", "", "Weather", "Work", "Notes");
        assertFalse(invalidReport3.isValid());
        
        FieldReport invalidReport4 = new FieldReport("Name", "Location", "Project", "Weather", "", "Notes");
        assertFalse(invalidReport4.isValid());
    }
    
    @Test
    void testFieldReportEquality() {
        String id = fieldReport.getId();
        
        FieldReport sameReport = new FieldReport("Different Name", "Different Location", "Different Project", "Different Weather", "Different Work", "Different Notes");
        sameReport.setId(id);
        
        assertEquals(fieldReport, sameReport);
        assertEquals(fieldReport.hashCode(), sameReport.hashCode());
    }
    
    @Test
    void testFieldReportToString() {
        String toString = fieldReport.toString();
        assertTrue(toString.contains("John Doe"));
        assertTrue(toString.contains("Construction Site A"));
        assertTrue(toString.contains("Building Project"));
    }
    
    @Test
    void testNotesHandling() {
        FieldReport reportWithNullNotes = new FieldReport("Name", "Location", "Project", "Weather", "Work", null);
        assertEquals("", reportWithNullNotes.getNotes());
        
        fieldReport.setNotes(null);
        assertEquals("", fieldReport.getNotes());
    }
}