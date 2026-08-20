package com.fieldreport.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive unit tests for the PersonnelOnSite model class.
 * 
 * Tests cover:
 * - Constructor functionality
 * - Getter and setter methods
 * - Validation methods
 * - Business logic calculations
 * - Edge cases and error conditions
 * 
 * @author DailyFieldReport System
 * @version 1.0
 */
@DisplayName("PersonnelOnSite Model Tests")
class PersonnelOnSiteTest {

    private PersonnelOnSite personnel;

    @BeforeEach
    void setUp() {
        personnel = new PersonnelOnSite();
    }

    // ================= CONSTRUCTOR TESTS =================

    @Test
    @DisplayName("Default constructor should initialize all fields")
    void testDefaultConstructor() {
        assertNotNull(personnel.getCompany());
        assertNotNull(personnel.getTradeRole());
        assertNotNull(personnel.getForemanSupervisor());
        assertEquals(0, personnel.getNumberOfWorkers());
        assertEquals(0.0, personnel.getHoursWorked());
    }

    @Test
    @DisplayName("Parameterized constructor should set all fields correctly")
    void testParameterizedConstructor() {
        String company = "ABC Construction";
        String tradeRole = "Steel Workers";
        int numberOfWorkers = 5;
        double hoursWorked = 8.5;
        String foremanSupervisor = "John Smith";

        PersonnelOnSite testPersonnel = new PersonnelOnSite(company, tradeRole, numberOfWorkers, hoursWorked, foremanSupervisor);

        assertEquals(company, testPersonnel.getCompany());
        assertEquals(tradeRole, testPersonnel.getTradeRole());
        assertEquals(numberOfWorkers, testPersonnel.getNumberOfWorkers());
        assertEquals(hoursWorked, testPersonnel.getHoursWorked());
        assertEquals(foremanSupervisor, testPersonnel.getForemanSupervisor());
    }

    @Test
    @DisplayName("Constructor should handle null values gracefully")
    void testConstructorWithNullValues() {
        PersonnelOnSite testPersonnel = new PersonnelOnSite(null, null, -1, -5.0, null);

        assertEquals("", testPersonnel.getCompany());
        assertEquals("", testPersonnel.getTradeRole());
        assertEquals(0, testPersonnel.getNumberOfWorkers());
        assertEquals(0.0, testPersonnel.getHoursWorked());
        assertEquals("", testPersonnel.getForemanSupervisor());
    }

    // ================= GETTER AND SETTER TESTS =================

    @Test
    @DisplayName("Company getter and setter should work correctly")
    void testCompany() {
        String company = "XYZ Corporation";
        personnel.setCompany(company);
        assertEquals(company, personnel.getCompany());

        personnel.setCompany(null);
        assertEquals("", personnel.getCompany());
    }

    @Test
    @DisplayName("Trade/Role getter and setter should work correctly")
    void testTradeRole() {
        String tradeRole = "Electrician";
        personnel.setTradeRole(tradeRole);
        assertEquals(tradeRole, personnel.getTradeRole());

        personnel.setTradeRole(null);
        assertEquals("", personnel.getTradeRole());
    }

    @Test
    @DisplayName("Number of workers getter and setter should work correctly")
    void testNumberOfWorkers() {
        int numberOfWorkers = 10;
        personnel.setNumberOfWorkers(numberOfWorkers);
        assertEquals(numberOfWorkers, personnel.getNumberOfWorkers());

        // Test negative values are converted to 0
        personnel.setNumberOfWorkers(-5);
        assertEquals(0, personnel.getNumberOfWorkers());
    }

    @Test
    @DisplayName("Hours worked getter and setter should work correctly")
    void testHoursWorked() {
        double hoursWorked = 7.5;
        personnel.setHoursWorked(hoursWorked);
        assertEquals(hoursWorked, personnel.getHoursWorked());

        // Test negative values are converted to 0
        personnel.setHoursWorked(-3.0);
        assertEquals(0.0, personnel.getHoursWorked());
    }

    @Test
    @DisplayName("Foreman/Supervisor getter and setter should work correctly")
    void testForemanSupervisor() {
        String foremanSupervisor = "Jane Doe";
        personnel.setForemanSupervisor(foremanSupervisor);
        assertEquals(foremanSupervisor, personnel.getForemanSupervisor());

        personnel.setForemanSupervisor(null);
        assertEquals("", personnel.getForemanSupervisor());
    }

    // ================= BUSINESS LOGIC TESTS =================

    @Test
    @DisplayName("Total worker hours calculation should be correct")
    void testGetTotalWorkerHours() {
        personnel.setNumberOfWorkers(5);
        personnel.setHoursWorked(8.0);
        
        assertEquals(40.0, personnel.getTotalWorkerHours());

        personnel.setNumberOfWorkers(0);
        assertEquals(0.0, personnel.getTotalWorkerHours());

        personnel.setNumberOfWorkers(3);
        personnel.setHoursWorked(0.0);
        assertEquals(0.0, personnel.getTotalWorkerHours());
    }

    @Test
    @DisplayName("Total worker hours with decimal precision")
    void testGetTotalWorkerHoursDecimal() {
        personnel.setNumberOfWorkers(4);
        personnel.setHoursWorked(7.5);
        
        assertEquals(30.0, personnel.getTotalWorkerHours());
    }

    // ================= VALIDATION TESTS =================

    @Test
    @DisplayName("isValid should return true for properly configured personnel")
    void testIsValidTrue() {
        personnel.setCompany("ABC Corp");
        personnel.setTradeRole("Supervisor");
        personnel.setNumberOfWorkers(1);
        personnel.setHoursWorked(8.0);
        personnel.setForemanSupervisor("John Smith");

        assertTrue(personnel.isValid());
    }

    @Test
    @DisplayName("isValid should return false for incomplete personnel")
    void testIsValidFalse() {
        // Empty company
        assertFalse(personnel.isValid());

        personnel.setCompany("ABC Corp");
        // Empty trade/role
        assertFalse(personnel.isValid());

        personnel.setTradeRole("Supervisor");
        // Zero workers
        assertFalse(personnel.isValid());

        personnel.setNumberOfWorkers(1);
        // Now should be valid
        assertTrue(personnel.isValid());
    }

    @Test
    @DisplayName("isValid should handle edge cases")
    void testIsValidEdgeCases() {
        personnel.setCompany("   ");  // Whitespace only
        personnel.setTradeRole("Role");
        personnel.setNumberOfWorkers(1);
        assertFalse(personnel.isValid());

        personnel.setCompany("Company");
        personnel.setTradeRole("   ");  // Whitespace only
        assertFalse(personnel.isValid());
    }

    // ================= UTILITY TESTS =================

    @Test
    @DisplayName("toString should include all fields")
    void testToString() {
        personnel.setCompany("Test Company");
        personnel.setTradeRole("Test Role");
        personnel.setNumberOfWorkers(2);
        personnel.setHoursWorked(6.0);
        personnel.setForemanSupervisor("Test Supervisor");

        String toString = personnel.toString();
        
        assertTrue(toString.contains("PersonnelOnSite{"));
        assertTrue(toString.contains("company='Test Company'"));
        assertTrue(toString.contains("tradeRole='Test Role'"));
        assertTrue(toString.contains("numberOfWorkers=2"));
        assertTrue(toString.contains("hoursWorked=6.0"));
        assertTrue(toString.contains("foremanSupervisor='Test Supervisor'"));
    }

    // ================= EDGE CASE TESTS =================

    @Test
    @DisplayName("Large numbers should be handled correctly")
    void testLargeNumbers() {
        personnel.setNumberOfWorkers(1000);
        personnel.setHoursWorked(24.0);
        
        assertEquals(24000.0, personnel.getTotalWorkerHours());
    }

    @Test
    @DisplayName("Very small decimal hours should be handled correctly")
    void testSmallDecimalHours() {
        personnel.setNumberOfWorkers(1);
        personnel.setHoursWorked(0.1);
        
        assertEquals(0.1, personnel.getTotalWorkerHours());
    }

    @Test
    @DisplayName("Setting empty strings should not cause issues")
    void testEmptyStrings() {
        personnel.setCompany("");
        personnel.setTradeRole("");
        personnel.setForemanSupervisor("");
        
        assertEquals("", personnel.getCompany());
        assertEquals("", personnel.getTradeRole());
        assertEquals("", personnel.getForemanSupervisor());
        
        assertFalse(personnel.isValid());
    }
}