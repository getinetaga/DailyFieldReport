package com.fieldreport.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive unit tests for the EquipmentOnSite model class.
 * 
 * Tests cover:
 * - Constructor functionality
 * - Getter and setter methods
 * - Validation methods
 * - Operating status logic
 * - Edge cases and error conditions
 * 
 * @author DailyFieldReport System
 * @version 1.0
 */
@DisplayName("EquipmentOnSite Model Tests")
class EquipmentOnSiteTest {

    private EquipmentOnSite equipment;

    @BeforeEach
    void setUp() {
        equipment = new EquipmentOnSite();
    }

    // ================= CONSTRUCTOR TESTS =================

    @Test
    @DisplayName("Default constructor should initialize all fields")
    void testDefaultConstructor() {
        assertNotNull(equipment.getEquipment());
        assertNotNull(equipment.getTypeSize());
        assertNotNull(equipment.getIdleReason());
        assertEquals(0, equipment.getQuantity());
        assertTrue(equipment.isOperating());  // Default should be operating
    }

    @Test
    @DisplayName("Parameterized constructor should set all fields correctly")
    void testParameterizedConstructor() {
        String equipmentName = "Crane";
        String typeSize = "50-ton";
        int quantity = 2;
        boolean operating = false;
        String idleReason = "Maintenance";

        EquipmentOnSite testEquipment = new EquipmentOnSite(equipmentName, typeSize, quantity, operating, idleReason);

        assertEquals(equipmentName, testEquipment.getEquipment());
        assertEquals(typeSize, testEquipment.getTypeSize());
        assertEquals(quantity, testEquipment.getQuantity());
        assertEquals(operating, testEquipment.isOperating());
        assertEquals(idleReason, testEquipment.getIdleReason());
    }

    @Test
    @DisplayName("Constructor should handle null values gracefully")
    void testConstructorWithNullValues() {
        EquipmentOnSite testEquipment = new EquipmentOnSite(null, null, -1, false, null);

        assertEquals("", testEquipment.getEquipment());
        assertEquals("", testEquipment.getTypeSize());
        assertEquals(0, testEquipment.getQuantity());
        assertFalse(testEquipment.isOperating());
        assertEquals("", testEquipment.getIdleReason());
    }

    // ================= GETTER AND SETTER TESTS =================

    @Test
    @DisplayName("Equipment name getter and setter should work correctly")
    void testEquipment() {
        String equipmentName = "Excavator";
        equipment.setEquipment(equipmentName);
        assertEquals(equipmentName, equipment.getEquipment());

        equipment.setEquipment(null);
        assertEquals("", equipment.getEquipment());
    }

    @Test
    @DisplayName("Type/Size getter and setter should work correctly")
    void testTypeSize() {
        String typeSize = "CAT 320D";
        equipment.setTypeSize(typeSize);
        assertEquals(typeSize, equipment.getTypeSize());

        equipment.setTypeSize(null);
        assertEquals("", equipment.getTypeSize());
    }

    @Test
    @DisplayName("Quantity getter and setter should work correctly")
    void testQuantity() {
        int quantity = 5;
        equipment.setQuantity(quantity);
        assertEquals(quantity, equipment.getQuantity());

        // Test negative values are converted to 0
        equipment.setQuantity(-3);
        assertEquals(0, equipment.getQuantity());
    }

    @Test
    @DisplayName("Operating status getter and setter should work correctly")
    void testOperating() {
        equipment.setOperating(false);
        assertFalse(equipment.isOperating());

        equipment.setOperating(true);
        assertTrue(equipment.isOperating());
    }

    @Test
    @DisplayName("Idle reason getter and setter should work correctly")
    void testIdleReason() {
        String idleReason = "Hydraulic leak";
        equipment.setIdleReason(idleReason);
        assertEquals(idleReason, equipment.getIdleReason());

        equipment.setIdleReason(null);
        assertEquals("", equipment.getIdleReason());
    }

    // ================= VALIDATION TESTS =================

    @Test
    @DisplayName("isValid should return true for properly configured equipment")
    void testIsValidTrue() {
        equipment.setEquipment("Crane");
        equipment.setTypeSize("50-ton");
        equipment.setQuantity(1);

        assertTrue(equipment.isValid());
    }

    @Test
    @DisplayName("isValid should return false for incomplete equipment")
    void testIsValidFalse() {
        // Empty equipment name
        assertFalse(equipment.isValid());

        equipment.setEquipment("Crane");
        // Empty type/size
        assertFalse(equipment.isValid());

        equipment.setTypeSize("50-ton");
        // Zero quantity
        assertFalse(equipment.isValid());

        equipment.setQuantity(1);
        // Now should be valid
        assertTrue(equipment.isValid());
    }

    @Test
    @DisplayName("isValid should handle edge cases")
    void testIsValidEdgeCases() {
        equipment.setEquipment("   ");  // Whitespace only
        equipment.setTypeSize("Type");
        equipment.setQuantity(1);
        assertFalse(equipment.isValid());

        equipment.setEquipment("Equipment");
        equipment.setTypeSize("   ");  // Whitespace only
        assertFalse(equipment.isValid());
    }

    // ================= OPERATING STATUS LOGIC TESTS =================

    @Test
    @DisplayName("needsIdleReason should work correctly")
    void testNeedsIdleReason() {
        // Operating equipment doesn't need idle reason
        equipment.setOperating(true);
        assertFalse(equipment.needsIdleReason());

        // Non-operating without idle reason needs one
        equipment.setOperating(false);
        equipment.setIdleReason("");
        assertTrue(equipment.needsIdleReason());

        // Non-operating with whitespace idle reason needs one
        equipment.setIdleReason("   ");
        assertTrue(equipment.needsIdleReason());

        // Non-operating with proper idle reason doesn't need one
        equipment.setIdleReason("Maintenance required");
        assertFalse(equipment.needsIdleReason());
    }

    @Test
    @DisplayName("getEffectiveIdleReason should work correctly")
    void testGetEffectiveIdleReason() {
        // Operating equipment should return "N/A"
        equipment.setOperating(true);
        assertEquals("N/A", equipment.getEffectiveIdleReason());

        // Non-operating without reason should return "Not specified"
        equipment.setOperating(false);
        equipment.setIdleReason("");
        assertEquals("Not specified", equipment.getEffectiveIdleReason());

        // Non-operating with reason should return the reason
        equipment.setIdleReason("Under repair");
        assertEquals("Under repair", equipment.getEffectiveIdleReason());
    }

    @Test
    @DisplayName("getOperatingStatus should return Y/N correctly")
    void testGetOperatingStatus() {
        equipment.setOperating(true);
        assertEquals("Y", equipment.getOperatingStatus());

        equipment.setOperating(false);
        assertEquals("N", equipment.getOperatingStatus());
    }

    @Test
    @DisplayName("setOperatingStatus should accept Y/N strings")
    void testSetOperatingStatus() {
        equipment.setOperatingStatus("Y");
        assertTrue(equipment.isOperating());

        equipment.setOperatingStatus("N");
        assertFalse(equipment.isOperating());

        equipment.setOperatingStatus("y");
        assertTrue(equipment.isOperating());

        equipment.setOperatingStatus("n");
        assertFalse(equipment.isOperating());

        equipment.setOperatingStatus(null);
        // Should not change current state
        assertFalse(equipment.isOperating());
    }

    @Test
    @DisplayName("Operating equipment should not need idle reason")
    void testOperatingEquipmentLogic() {
        equipment.setEquipment("Crane");
        equipment.setTypeSize("50-ton");
        equipment.setQuantity(1);
        equipment.setOperating(true);
        equipment.setIdleReason("");

        assertTrue(equipment.isValid());
        assertTrue(equipment.isOperating());
        assertFalse(equipment.needsIdleReason());
        assertEquals("Y", equipment.getOperatingStatus());
        assertEquals("N/A", equipment.getEffectiveIdleReason());
    }

    @Test
    @DisplayName("Non-operating equipment logic")
    void testNonOperatingEquipmentLogic() {
        equipment.setEquipment("Crane");
        equipment.setTypeSize("50-ton");
        equipment.setQuantity(1);
        equipment.setOperating(false);
        equipment.setIdleReason("Under repair");

        assertTrue(equipment.isValid());
        assertFalse(equipment.isOperating());
        assertFalse(equipment.needsIdleReason());
        assertEquals("N", equipment.getOperatingStatus());
        assertEquals("Under repair", equipment.getEffectiveIdleReason());
    }

    // ================= UTILITY TESTS =================

    @Test
    @DisplayName("toString should include all fields")
    void testToString() {
        equipment.setEquipment("Test Equipment");
        equipment.setTypeSize("Test Type");
        equipment.setQuantity(3);
        equipment.setOperating(false);
        equipment.setIdleReason("Test Reason");

        String toString = equipment.toString();
        
        assertTrue(toString.contains("EquipmentOnSite{"));
        assertTrue(toString.contains("equipment='Test Equipment'"));
        assertTrue(toString.contains("typeSize='Test Type'"));
        assertTrue(toString.contains("quantity=3"));
        assertTrue(toString.contains("operating=false"));
        assertTrue(toString.contains("idleReason='Test Reason'"));
    }

    // ================= EDGE CASE TESTS =================

    @Test
    @DisplayName("Large quantities should be handled correctly")
    void testLargeQuantities() {
        equipment.setQuantity(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, equipment.getQuantity());
    }

    @Test
    @DisplayName("Setting empty strings should not cause issues")
    void testEmptyStrings() {
        equipment.setEquipment("");
        equipment.setTypeSize("");
        equipment.setIdleReason("");
        
        assertEquals("", equipment.getEquipment());
        assertEquals("", equipment.getTypeSize());
        assertEquals("", equipment.getIdleReason());
        
        assertFalse(equipment.isValid());
        // Empty idle reason means it needs one if not operating
        equipment.setOperating(false);
        assertTrue(equipment.needsIdleReason());
    }

    @Test
    @DisplayName("Equipment status consistency")
    void testEquipmentStatusConsistency() {
        // Operating equipment with idle reason - setting idle reason should make it non-operating
        equipment.setEquipment("Crane");
        equipment.setTypeSize("50-ton");
        equipment.setQuantity(1);
        equipment.setOperating(true);
        equipment.setIdleReason("Some reason");

        assertTrue(equipment.isValid());
        assertFalse(equipment.isOperating()); // Should be false because idle reason was set
        assertFalse(equipment.needsIdleReason()); // Has an idle reason
    }

    @Test
    @DisplayName("Non-operating equipment without idle reason")
    void testNonOperatingWithoutReason() {
        equipment.setEquipment("Crane");
        equipment.setTypeSize("50-ton");
        equipment.setQuantity(1);
        equipment.setOperating(false);
        equipment.setIdleReason("");

        assertTrue(equipment.isValid());
        assertFalse(equipment.isOperating());
        assertTrue(equipment.needsIdleReason()); // Needs an idle reason
    }

    @Test
    @DisplayName("setOperating(true) should clear idle reason")
    void testSetOperatingClearsIdleReason() {
        equipment.setIdleReason("Broken");
        equipment.setOperating(true);
        
        assertTrue(equipment.isOperating());
        assertEquals("", equipment.getIdleReason());
        assertEquals("N/A", equipment.getEffectiveIdleReason());
    }
}