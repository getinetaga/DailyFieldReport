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
 * Comprehensive unit tests for the MaterialDelivered model class.
 * 
 * Tests cover:
 * - Constructor functionality
 * - Getter and setter methods
 * - Validation methods
 * - Material status tracking
 * - Edge cases and error conditions
 * 
 * @author DailyFieldReport System
 * @version 1.0
 */
@DisplayName("MaterialDelivered Model Tests")
class MaterialDeliveredTest {

    private MaterialDelivered material;

    @BeforeEach
    void setUp() {
        material = new MaterialDelivered();
    }

    // ================= CONSTRUCTOR TESTS =================

    @Test
    @DisplayName("Default constructor should initialize all fields")
    void testDefaultConstructor() {
        // Default constructor sets all fields to null
        // This is the actual behavior based on the model
        assertNull(material.getMaterial());
        assertNull(material.getSupplier());
        assertNull(material.getQuantity());
        assertNull(material.getLocationStored());
        assertNull(material.getInspectionStatus());
    }

    @Test
    @DisplayName("Parameterized constructor should set all fields correctly")
    void testParameterizedConstructor() {
        String materialName = "Concrete";
        String supplier = "ABC Supply";
        String quantity = "10 cubic yards";
        String locationStored = "Area A";
        String inspectionStatus = "Passed";

        MaterialDelivered testMaterial = new MaterialDelivered(materialName, supplier, quantity, locationStored, inspectionStatus);

        assertEquals(materialName, testMaterial.getMaterial());
        assertEquals(supplier, testMaterial.getSupplier());
        assertEquals(quantity, testMaterial.getQuantity());
        assertEquals(locationStored, testMaterial.getLocationStored());
        assertEquals(inspectionStatus, testMaterial.getInspectionStatus());
    }

    @Test
    @DisplayName("Constructor should handle null values gracefully")
    void testConstructorWithNullValues() {
        MaterialDelivered testMaterial = new MaterialDelivered(null, null, null, null, null);

        assertNull(testMaterial.getMaterial());
        assertNull(testMaterial.getSupplier());
        assertNull(testMaterial.getQuantity());
        assertNull(testMaterial.getLocationStored());
        assertNull(testMaterial.getInspectionStatus());
    }

    // ================= GETTER AND SETTER TESTS =================

    @Test
    @DisplayName("Material name getter and setter should work correctly")
    void testMaterial() {
        String materialName = "Steel Rebar";
        material.setMaterial(materialName);
        assertEquals(materialName, material.getMaterial());

        material.setMaterial(null);
        assertNull(material.getMaterial());
    }

    @Test
    @DisplayName("Supplier getter and setter should work correctly")
    void testSupplier() {
        String supplier = "XYZ Materials";
        material.setSupplier(supplier);
        assertEquals(supplier, material.getSupplier());

        material.setSupplier(null);
        assertNull(material.getSupplier());
    }

    @Test
    @DisplayName("Quantity getter and setter should work correctly")
    void testQuantity() {
        String quantity = "50 tons";
        material.setQuantity(quantity);
        assertEquals(quantity, material.getQuantity());

        material.setQuantity(null);
        assertNull(material.getQuantity());
    }

    @Test
    @DisplayName("LocationStored getter and setter should work correctly")
    void testLocationStored() {
        String locationStored = "Storage Yard B";
        material.setLocationStored(locationStored);
        assertEquals(locationStored, material.getLocationStored());

        material.setLocationStored(null);
        assertNull(material.getLocationStored());
    }

    @Test
    @DisplayName("InspectionStatus getter and setter should work correctly")
    void testInspectionStatus() {
        String inspectionStatus = "Passed inspection";
        material.setInspectionStatus(inspectionStatus);
        assertEquals(inspectionStatus, material.getInspectionStatus());

        material.setInspectionStatus(null);
        assertNull(material.getInspectionStatus());
    }

    // ================= VALIDATION TESTS =================

    @Test
    @DisplayName("isValid should return true for properly configured material")
    void testIsValidTrue() {
        material.setMaterial("Concrete");
        material.setSupplier("ABC Supply");
        material.setQuantity("10 cu yd");

        assertTrue(material.isValid());
    }

    @Test
    @DisplayName("isValid should return false for incomplete material")
    void testIsValidFalse() {
        // Empty material name
        assertFalse(material.isValid());

        material.setMaterial("Concrete");
        // Empty supplier
        assertFalse(material.isValid());

        material.setSupplier("ABC Supply");
        // Empty quantity
        assertFalse(material.isValid());

        material.setQuantity("10 cu yd");
        // Now should be valid (locationStored and inspectionStatus are optional)
        assertTrue(material.isValid());
    }

    @Test
    @DisplayName("isValid should handle null values")
    void testIsValidWithNulls() {
        material.setMaterial(null);
        material.setSupplier("Supplier");
        material.setQuantity("10");
        assertFalse(material.isValid());

        material.setMaterial("Material");
        material.setSupplier(null);
        assertFalse(material.isValid());

        material.setSupplier("Supplier");
        material.setQuantity(null);
        assertFalse(material.isValid());
    }

    @Test
    @DisplayName("isValid should handle whitespace-only values")
    void testIsValidWithWhitespace() {
        material.setMaterial("   ");  // Whitespace only
        material.setSupplier("Supplier");
        material.setQuantity("10");
        assertFalse(material.isValid());

        material.setMaterial("Material");
        material.setSupplier("   ");  // Whitespace only
        assertFalse(material.isValid());

        material.setSupplier("Supplier");
        material.setQuantity("   ");  // Whitespace only
        assertFalse(material.isValid());
    }

    // ================= UTILITY TESTS =================

    @Test
    @DisplayName("toString should include all fields")
    void testToString() {
        material.setMaterial("Test Material");
        material.setSupplier("Test Supplier");
        material.setQuantity("Test Quantity");
        material.setLocationStored("Test Location");
        material.setInspectionStatus("Test Status");

        String toString = material.toString();
        
        assertTrue(toString.contains("MaterialDelivered{"));
        assertTrue(toString.contains("material='Test Material'"));
        assertTrue(toString.contains("supplier='Test Supplier'"));
        assertTrue(toString.contains("quantity='Test Quantity'"));
        assertTrue(toString.contains("locationStored='Test Location'"));
        assertTrue(toString.contains("inspectionStatus='Test Status'"));
    }

    @Test
    @DisplayName("getSummary should format material information correctly")
    void testGetSummary() {
        material.setMaterial("Portland Cement");
        material.setSupplier("Construction Materials Inc.");
        material.setQuantity("200 bags");
        material.setInspectionStatus("Passed");

        String summary = material.getSummary();
        
        assertTrue(summary.contains("Portland Cement"));
        assertTrue(summary.contains("200 bags"));
        assertTrue(summary.contains("Passed"));
        assertTrue(summary.contains("Construction Materials Inc."));
    }

    @Test
    @DisplayName("getSummary should handle null values gracefully")
    void testGetSummaryWithNulls() {
        // Test with null values - should not throw exception
        assertDoesNotThrow(() -> {
            String summary = material.getSummary();
            assertNotNull(summary);
        });
    }

    // ================= EDGE CASE TESTS =================

    @Test
    @DisplayName("Setting empty strings should work correctly")
    void testEmptyStrings() {
        material.setMaterial("");
        material.setSupplier("");
        material.setQuantity("");
        material.setLocationStored("");
        material.setInspectionStatus("");
        
        assertEquals("", material.getMaterial());
        assertEquals("", material.getSupplier());
        assertEquals("", material.getQuantity());
        assertEquals("", material.getLocationStored());
        assertEquals("", material.getInspectionStatus());
        
        assertFalse(material.isValid());
    }

    @Test
    @DisplayName("Material with all fields should be valid")
    void testCompleteValidMaterial() {
        material.setMaterial("Portland Cement");
        material.setSupplier("Construction Materials Inc.");
        material.setQuantity("200 bags (94 lb each)");
        material.setLocationStored("Material Storage Area - North Side");
        material.setInspectionStatus("Passed - Quality checked on delivery");

        assertTrue(material.isValid());
        
        String summary = material.getSummary();
        assertNotNull(summary);
        assertFalse(summary.isEmpty());
    }

    @Test
    @DisplayName("Material with special characters in fields")
    void testSpecialCharacters() {
        material.setMaterial("Steel Rebar #5 @ 12\" O.C.");
        material.setSupplier("ABC Supply & Materials Co.");
        material.setQuantity("1,500 linear feet");
        material.setLocationStored("Area B-2 (West Side)");
        material.setInspectionStatus("Passed - Cert #12345");

        assertTrue(material.isValid());
        
        String toString = material.toString();
        assertTrue(toString.contains("Steel Rebar #5 @ 12\" O.C."));
        assertTrue(toString.contains("ABC Supply & Materials Co."));
        assertTrue(toString.contains("1,500 linear feet"));
        assertTrue(toString.contains("Area B-2 (West Side)"));
        assertTrue(toString.contains("Passed - Cert #12345"));
    }

    @Test
    @DisplayName("Material delivery workflow simulation")
    void testMaterialDeliveryWorkflow() {
        // Material arrives
        material.setMaterial("Ready-Mix Concrete");
        material.setSupplier("City Concrete");
        material.setQuantity("15 cubic yards, 3000 PSI");
        material.setLocationStored("Foundation Area");
        material.setInspectionStatus("Pending");

        assertTrue(material.isValid());
        assertEquals("Pending", material.getInspectionStatus());

        // Material gets inspected
        material.setInspectionStatus("Passed - Temperature: 75°F, Slump: 4\"");
        assertEquals("Passed - Temperature: 75°F, Slump: 4\"", material.getInspectionStatus());
        
        String summary = material.getSummary();
        assertTrue(summary.contains("Passed - Temperature: 75°F, Slump: 4\""));
    }

    @Test
    @DisplayName("Minimal valid material should work")
    void testMinimalValidMaterial() {
        material.setMaterial("Concrete");
        material.setSupplier("ABC");
        material.setQuantity("1");
        
        assertTrue(material.isValid());
        
        // Optional fields can be null
        assertNull(material.getLocationStored());
        assertNull(material.getInspectionStatus());
    }
}