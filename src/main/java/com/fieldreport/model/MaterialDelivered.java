package com.fieldreport.model;

/**
 * Represents materials delivered or installed on site.
 * 
 * This class captures information about materials used in the project,
 * including supplier details, quantities, storage locations, and
 * inspection status.
 * 
 * @author DailyFieldReport System
 * @version 1.0
 */
public class MaterialDelivered {
    
    /** Name or description of the material */
    private String material;
    
    /** Supplier or vendor who provided the material */
    private String supplier;
    
    /** Quantity of material delivered or installed */
    private String quantity;
    
    /** Location where material is stored or installed */
    private String locationStored;
    
    /** Inspection status or remarks about the material */
    private String inspectionStatus;
    
    /**
     * Default constructor for MaterialDelivered.
     */
    public MaterialDelivered() {
    }
    
    /**
     * Constructs a MaterialDelivered with all required information.
     * 
     * @param material name or description of the material
     * @param supplier supplier or vendor who provided the material
     * @param quantity quantity of material delivered or installed
     * @param locationStored location where material is stored or installed
     * @param inspectionStatus inspection status or remarks about the material
     */
    public MaterialDelivered(String material, String supplier, String quantity, 
                           String locationStored, String inspectionStatus) {
        this.material = material;
        this.supplier = supplier;
        this.quantity = quantity;
        this.locationStored = locationStored;
        this.inspectionStatus = inspectionStatus;
    }
    
    // ================= GETTER AND SETTER METHODS =================
    
    /**
     * Gets the material name or description.
     * @return the material name
     */
    public String getMaterial() {
        return material;
    }
    
    /**
     * Sets the material name or description.
     * @param material the material name
     */
    public void setMaterial(String material) {
        this.material = material;
    }
    
    /**
     * Gets the supplier or vendor name.
     * @return the supplier name
     */
    public String getSupplier() {
        return supplier;
    }
    
    /**
     * Sets the supplier or vendor name.
     * @param supplier the supplier name
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    /**
     * Gets the quantity of material.
     * @return the quantity
     */
    public String getQuantity() {
        return quantity;
    }
    
    /**
     * Sets the quantity of material.
     * @param quantity the quantity
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    
    /**
     * Gets the location where material is stored or installed.
     * @return the storage/installation location
     */
    public String getLocationStored() {
        return locationStored;
    }
    
    /**
     * Sets the location where material is stored or installed.
     * @param locationStored the storage/installation location
     */
    public void setLocationStored(String locationStored) {
        this.locationStored = locationStored;
    }
    
    /**
     * Gets the inspection status or remarks.
     * @return the inspection status
     */
    public String getInspectionStatus() {
        return inspectionStatus;
    }
    
    /**
     * Sets the inspection status or remarks.
     * @param inspectionStatus the inspection status
     */
    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }
    
    // ================= VALIDATION METHODS =================
    
    /**
     * Validates that all required fields are populated.
     * @return true if all fields are valid, false otherwise
     */
    public boolean isValid() {
        return material != null && !material.trim().isEmpty() &&
               supplier != null && !supplier.trim().isEmpty() &&
               quantity != null && !quantity.trim().isEmpty();
    }
    
    /**
     * Gets a summary description of this material entry.
     * @return formatted summary string
     */
    public String getSummary() {
        return String.format("%s (%s) - %s from %s", 
                           material, quantity, inspectionStatus, supplier);
    }
    
    @Override
    public String toString() {
        return "MaterialDelivered{" +
                "material='" + material + '\'' +
                ", supplier='" + supplier + '\'' +
                ", quantity='" + quantity + '\'' +
                ", locationStored='" + locationStored + '\'' +
                ", inspectionStatus='" + inspectionStatus + '\'' +
                '}';
    }
}