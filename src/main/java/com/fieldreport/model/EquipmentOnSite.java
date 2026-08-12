package com.fieldreport.model;

/**
 * Equipment On Site Model Class for Field Report System
 * 
 * Represents equipment present on site for a specific field report.
 * This class tracks equipment information including type, size, quantity,
 * operational status, and reasons for idle equipment.
 * 
 * Key Features:
 * - Equipment identification and classification
 * - Type/size specification (e.g., "Excavator 320", "Crane 50-ton")
 * - Quantity tracking
 * - Operating status monitoring (Y/N)
 * - Idle reason documentation for non-operating equipment
 * - Input validation for data integrity
 * 
 * @author Field Report System
 * @version 1.0
 * @since 2025-10-24
 */
public class EquipmentOnSite {
    
    /** Equipment name or identifier */
    private String equipment;
    
    /** Type, model, or size specification */
    private String typeSize;
    
    /** Quantity of this equipment type */
    private int quantity;
    
    /** Operating status - true if operating, false if idle */
    private boolean operating;
    
    /** Reason for being idle (only relevant when operating = false) */
    private String idleReason;
    
    /**
     * Default constructor.
     */
    public EquipmentOnSite() {
        this.equipment = "";
        this.typeSize = "";
        this.quantity = 0;
        this.operating = true;
        this.idleReason = "";
    }
    
    /**
     * Constructor with all parameters.
     * 
     * @param equipment the equipment name or identifier
     * @param typeSize the type, model, or size specification
     * @param quantity the quantity of this equipment type
     * @param operating the operating status (true if operating, false if idle)
     * @param idleReason the reason for being idle (can be empty if operating)
     */
    public EquipmentOnSite(String equipment, String typeSize, int quantity, 
                          boolean operating, String idleReason) {
        this.equipment = equipment != null ? equipment : "";
        this.typeSize = typeSize != null ? typeSize : "";
        this.quantity = Math.max(0, quantity);
        this.operating = operating;
        this.idleReason = idleReason != null ? idleReason : "";
    }
    
    // Getter methods
    
    /**
     * Gets the equipment name.
     * @return the equipment name or identifier
     */
    public String getEquipment() {
        return equipment;
    }
    
    /**
     * Gets the type/size specification.
     * @return the type, model, or size specification
     */
    public String getTypeSize() {
        return typeSize;
    }
    
    /**
     * Gets the quantity.
     * @return the quantity of this equipment type
     */
    public int getQuantity() {
        return quantity;
    }
    
    /**
     * Gets the operating status.
     * @return true if operating, false if idle
     */
    public boolean isOperating() {
        return operating;
    }
    
    /**
     * Gets the operating status as Y/N string.
     * @return "Y" if operating, "N" if idle
     */
    public String getOperatingStatus() {
        return operating ? "Y" : "N";
    }
    
    /**
     * Gets the idle reason.
     * @return the reason for being idle
     */
    public String getIdleReason() {
        return idleReason;
    }
    
    // Setter methods
    
    /**
     * Sets the equipment name.
     * @param equipment the equipment name or identifier
     */
    public void setEquipment(String equipment) {
        this.equipment = equipment != null ? equipment : "";
    }
    
    /**
     * Sets the type/size specification.
     * @param typeSize the type, model, or size specification
     */
    public void setTypeSize(String typeSize) {
        this.typeSize = typeSize != null ? typeSize : "";
    }
    
    /**
     * Sets the quantity.
     * @param quantity the quantity (must be non-negative)
     */
    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }
    
    /**
     * Sets the operating status.
     * @param operating true if operating, false if idle
     */
    public void setOperating(boolean operating) {
        this.operating = operating;
        // Clear idle reason if equipment becomes operational
        if (operating) {
            this.idleReason = "";
        }
    }
    
    /**
     * Sets the operating status from Y/N string.
     * @param operatingStatus "Y" for operating, "N" for idle
     */
    public void setOperatingStatus(String operatingStatus) {
        if (operatingStatus != null) {
            this.operating = operatingStatus.trim().toUpperCase().equals("Y");
            if (this.operating) {
                this.idleReason = "";
            }
        }
    }
    
    /**
     * Sets the idle reason.
     * @param idleReason the reason for being idle
     */
    public void setIdleReason(String idleReason) {
        this.idleReason = idleReason != null ? idleReason : "";
        // If an idle reason is provided, mark as not operating
        if (!this.idleReason.trim().isEmpty()) {
            this.operating = false;
        }
    }
    
    /**
     * Validates that the equipment record has required fields.
     * 
     * @return true if equipment name and type/size are not empty
     */
    public boolean isValid() {
        return !equipment.trim().isEmpty() && !typeSize.trim().isEmpty();
    }
    
    /**
     * Checks if the equipment requires an idle reason.
     * Equipment should have an idle reason when not operating.
     * 
     * @return true if not operating and no idle reason provided
     */
    public boolean needsIdleReason() {
        return !operating && idleReason.trim().isEmpty();
    }
    
    /**
     * Gets the effective idle reason for display.
     * Returns the idle reason if not operating, or "N/A" if operating.
     * 
     * @return idle reason or "N/A" if operating
     */
    public String getEffectiveIdleReason() {
        if (operating) {
            return "N/A";
        }
        return idleReason.trim().isEmpty() ? "Not specified" : idleReason;
    }
    
    @Override
    public String toString() {
        return "EquipmentOnSite{" +
                "equipment='" + equipment + '\'' +
                ", typeSize='" + typeSize + '\'' +
                ", quantity=" + quantity +
                ", operating=" + operating +
                ", idleReason='" + idleReason + '\'' +
                '}';
    }
}