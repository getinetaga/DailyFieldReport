package com.fieldreport.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Material Model Class for Daily Field Report System
 * 
 * Represents materials used in construction, maintenance, and field operations.
 * This class provides comprehensive material tracking including quantity,
 * cost, supplier information, and usage details for accurate project
 * documentation and inventory management.
 * 
 * Key Features:
 * - Unique material identification with UUID
 * - Comprehensive material details (name, description, category)
 * - Quantity and unit of measurement tracking
 * - Cost and supplier information
 * - Usage status and delivery tracking
 * - Automatic timestamp generation
 * - Input validation and data integrity
 * 
 * Usage Examples:
 * - Track concrete, steel, lumber, and other construction materials
 * - Monitor material consumption and remaining quantities
 * - Document supplier information and delivery dates
 * - Calculate total material costs for projects
 * - Generate material usage reports
 * 
 * @author Daily Field Report System
 * @version 1.0
 * @since 2025-10-24
 */
public class Material {
    
    // ================= CORE IDENTIFICATION =================
    
    /** Unique identifier for the material record */
    private String materialId;
    
    /** Timestamp when the material record was created */
    private LocalDateTime createdAt;
    
    /** Timestamp when the material record was last updated */
    private LocalDateTime updatedAt;
    
    // ================= MATERIAL DETAILS =================
    
    /** Name of the material (e.g., "Steel Rebar", "Concrete Mix", "Wood Planks") */
    private String materialName;
    
    /** Detailed description of the material specifications */
    private String description;
    
    /** Category or type of material (e.g., "Construction", "Tools", "Safety") */
    private String category;
    
    /** Brand or manufacturer of the material */
    private String brand;
    
    /** Model or product code */
    private String modelNumber;
    
    // ================= QUANTITY AND MEASUREMENTS =================
    
    /** Quantity of material */
    private double quantity;
    
    /** Unit of measurement (e.g., "kg", "meters", "pieces", "liters") */
    private String unit;
    
    /** Minimum quantity threshold for reordering */
    private double minimumQuantity;
    
    /** Maximum storage capacity */
    private double maximumQuantity;
    
    // ================= COST AND FINANCIAL =================
    
    /** Cost per unit of the material */
    private double unitCost;
    
    /** Total cost (quantity × unit cost) */
    private double totalCost;
    
    /** Currency code (e.g., "USD", "EUR", "CAD") */
    private String currency;
    
    // ================= SUPPLIER INFORMATION =================
    
    /** Name of the supplier or vendor */
    private String supplierName;
    
    /** Supplier contact information */
    private String supplierContact;
    
    /** Purchase order number */
    private String purchaseOrderNumber;
    
    /** Date when the material was ordered */
    private LocalDateTime orderedDate;
    
    /** Expected or actual delivery date */
    private LocalDateTime deliveryDate;
    
    // ================= STATUS AND TRACKING =================
    
    /** Current status of the material (e.g., "Ordered", "Delivered", "In Use", "Depleted") */
    private String status;
    
    /** Storage location of the material */
    private String storageLocation;
    
    /** Quality control notes */
    private String qualityNotes;
    
    /** General notes and comments */
    private String notes;
    
    // ================= CONSTRUCTORS =================
    
    /**
     * Default constructor that initializes basic material information.
     * 
     * Creates a new material record with:
     * - Auto-generated unique material ID
     * - Current timestamp for creation and update times
     * - Default values for numeric fields
     * - Empty strings for text fields
     */
    public Material() {
        this.materialId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.quantity = 0.0;
        this.minimumQuantity = 0.0;
        this.maximumQuantity = 0.0;
        this.unitCost = 0.0;
        this.totalCost = 0.0;
        this.materialName = "";
        this.description = "";
        this.category = "";
        this.brand = "";
        this.modelNumber = "";
        this.unit = "";
        this.currency = "USD";
        this.supplierName = "";
        this.supplierContact = "";
        this.purchaseOrderNumber = "";
        this.status = "Planned";
        this.storageLocation = "";
        this.qualityNotes = "";
        this.notes = "";
    }
    
    /**
     * Constructor with essential material information.
     * 
     * Creates a material record with the most commonly used fields.
     * Other fields are set to default values and can be updated later.
     * 
     * @param materialName the name of the material
     * @param description detailed description of the material
     * @param category category or type of material
     * @param quantity amount of material
     * @param unit unit of measurement
     * @param unitCost cost per unit
     * @param supplierName name of the supplier
     */
    public Material(String materialName, String description, String category, 
                   double quantity, String unit, double unitCost, String supplierName) {
        this(); // Call default constructor for initialization
        this.materialName = materialName != null ? materialName : "";
        this.description = description != null ? description : "";
        this.category = category != null ? category : "";
        this.quantity = Math.max(0, quantity);
        this.unit = unit != null ? unit : "";
        this.unitCost = Math.max(0, unitCost);
        this.supplierName = supplierName != null ? supplierName : "";
        calculateTotalCost();
    }
    
    // ================= GETTER METHODS =================
    
    /**
     * Gets the unique material identifier.
     * @return the material ID
     */
    public String getMaterialId() {
        return materialId;
    }
    
    /**
     * Gets the creation timestamp.
     * @return when the material record was created
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Gets the last update timestamp.
     * @return when the material record was last updated
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * Gets the material name.
     * @return the name of the material
     */
    public String getMaterialName() {
        return materialName;
    }
    
    /**
     * Gets the material description.
     * @return detailed description of the material
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the material category.
     * @return the category or type of material
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Gets the material brand.
     * @return the brand or manufacturer
     */
    public String getBrand() {
        return brand;
    }
    
    /**
     * Gets the model number.
     * @return the model or product code
     */
    public String getModelNumber() {
        return modelNumber;
    }
    
    /**
     * Gets the material quantity.
     * @return the amount of material
     */
    public double getQuantity() {
        return quantity;
    }
    
    /**
     * Gets the unit of measurement.
     * @return the unit (e.g., "kg", "meters", "pieces")
     */
    public String getUnit() {
        return unit;
    }
    
    /**
     * Gets the minimum quantity threshold.
     * @return the minimum quantity for reordering
     */
    public double getMinimumQuantity() {
        return minimumQuantity;
    }
    
    /**
     * Gets the maximum quantity capacity.
     * @return the maximum storage capacity
     */
    public double getMaximumQuantity() {
        return maximumQuantity;
    }
    
    /**
     * Gets the unit cost.
     * @return the cost per unit
     */
    public double getUnitCost() {
        return unitCost;
    }
    
    /**
     * Gets the total cost.
     * @return the total cost (quantity × unit cost)
     */
    public double getTotalCost() {
        return totalCost;
    }
    
    /**
     * Gets the currency code.
     * @return the currency (e.g., "USD", "EUR")
     */
    public String getCurrency() {
        return currency;
    }
    
    /**
     * Gets the supplier name.
     * @return the name of the supplier
     */
    public String getSupplierName() {
        return supplierName;
    }
    
    /**
     * Gets the supplier contact information.
     * @return the supplier contact details
     */
    public String getSupplierContact() {
        return supplierContact;
    }
    
    /**
     * Gets the purchase order number.
     * @return the purchase order number
     */
    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }
    
    /**
     * Gets the order date.
     * @return when the material was ordered
     */
    public LocalDateTime getOrderedDate() {
        return orderedDate;
    }
    
    /**
     * Gets the delivery date.
     * @return the expected or actual delivery date
     */
    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }
    
    /**
     * Gets the material status.
     * @return the current status (e.g., "Ordered", "Delivered")
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Gets the storage location.
     * @return where the material is stored
     */
    public String getStorageLocation() {
        return storageLocation;
    }
    
    /**
     * Gets the quality control notes.
     * @return quality control information
     */
    public String getQualityNotes() {
        return qualityNotes;
    }
    
    /**
     * Gets the general notes.
     * @return additional notes and comments
     */
    public String getNotes() {
        return notes;
    }
    
    // ================= SETTER METHODS =================
    
    /**
     * Sets the material name.
     * @param materialName the name of the material
     */
    public void setMaterialName(String materialName) {
        this.materialName = materialName != null ? materialName : "";
        updateTimestamp();
    }
    
    /**
     * Sets the material description.
     * @param description detailed description of the material
     */
    public void setDescription(String description) {
        this.description = description != null ? description : "";
        updateTimestamp();
    }
    
    /**
     * Sets the material category.
     * @param category the category or type of material
     */
    public void setCategory(String category) {
        this.category = category != null ? category : "";
        updateTimestamp();
    }
    
    /**
     * Sets the material brand.
     * @param brand the brand or manufacturer
     */
    public void setBrand(String brand) {
        this.brand = brand != null ? brand : "";
        updateTimestamp();
    }
    
    /**
     * Sets the model number.
     * @param modelNumber the model or product code
     */
    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber != null ? modelNumber : "";
        updateTimestamp();
    }
    
    /**
     * Sets the material quantity and recalculates total cost.
     * @param quantity the amount of material (must be non-negative)
     */
    public void setQuantity(double quantity) {
        this.quantity = Math.max(0, quantity);
        calculateTotalCost();
        updateTimestamp();
    }
    
    /**
     * Sets the unit of measurement.
     * @param unit the unit (e.g., "kg", "meters", "pieces")
     */
    public void setUnit(String unit) {
        this.unit = unit != null ? unit : "";
        updateTimestamp();
    }
    
    /**
     * Sets the minimum quantity threshold.
     * @param minimumQuantity the minimum quantity for reordering
     */
    public void setMinimumQuantity(double minimumQuantity) {
        this.minimumQuantity = Math.max(0, minimumQuantity);
        updateTimestamp();
    }
    
    /**
     * Sets the maximum quantity capacity.
     * @param maximumQuantity the maximum storage capacity
     */
    public void setMaximumQuantity(double maximumQuantity) {
        this.maximumQuantity = Math.max(0, maximumQuantity);
        updateTimestamp();
    }
    
    /**
     * Sets the unit cost and recalculates total cost.
     * @param unitCost the cost per unit (must be non-negative)
     */
    public void setUnitCost(double unitCost) {
        this.unitCost = Math.max(0, unitCost);
        calculateTotalCost();
        updateTimestamp();
    }
    
    /**
     * Sets the currency code.
     * @param currency the currency (e.g., "USD", "EUR")
     */
    public void setCurrency(String currency) {
        this.currency = currency != null ? currency : "USD";
        updateTimestamp();
    }
    
    /**
     * Sets the supplier name.
     * @param supplierName the name of the supplier
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName != null ? supplierName : "";
        updateTimestamp();
    }
    
    /**
     * Sets the supplier contact information.
     * @param supplierContact the supplier contact details
     */
    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact != null ? supplierContact : "";
        updateTimestamp();
    }
    
    /**
     * Sets the purchase order number.
     * @param purchaseOrderNumber the purchase order number
     */
    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber != null ? purchaseOrderNumber : "";
        updateTimestamp();
    }
    
    /**
     * Sets the order date.
     * @param orderedDate when the material was ordered
     */
    public void setOrderedDate(LocalDateTime orderedDate) {
        this.orderedDate = orderedDate;
        updateTimestamp();
    }
    
    /**
     * Sets the delivery date.
     * @param deliveryDate the expected or actual delivery date
     */
    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
        updateTimestamp();
    }
    
    /**
     * Sets the material status.
     * @param status the current status (e.g., "Ordered", "Delivered", "In Use")
     */
    public void setStatus(String status) {
        this.status = status != null ? status : "Planned";
        updateTimestamp();
    }
    
    /**
     * Sets the storage location.
     * @param storageLocation where the material is stored
     */
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation != null ? storageLocation : "";
        updateTimestamp();
    }
    
    /**
     * Sets the quality control notes.
     * @param qualityNotes quality control information
     */
    public void setQualityNotes(String qualityNotes) {
        this.qualityNotes = qualityNotes != null ? qualityNotes : "";
        updateTimestamp();
    }
    
    /**
     * Sets the general notes.
     * @param notes additional notes and comments
     */
    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
        updateTimestamp();
    }
    
    // ================= UTILITY METHODS =================
    
    /**
     * Calculates the total cost based on quantity and unit cost.
     * 
     * Updates the total cost field automatically when quantity or unit cost changes.
     * Ensures precision by rounding to 2 decimal places for currency calculations.
     */
    private void calculateTotalCost() {
        this.totalCost = Math.round(this.quantity * this.unitCost * 100.0) / 100.0;
    }
    
    /**
     * Updates the last modified timestamp to current time.
     * 
     * Called automatically whenever any field is modified to track
     * when the material record was last updated.
     */
    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Checks if the material quantity is below the minimum threshold.
     * 
     * Useful for inventory management and automatic reordering alerts.
     * 
     * @return true if quantity is below minimum threshold, false otherwise
     */
    public boolean isLowStock() {
        return this.quantity <= this.minimumQuantity;
    }
    
    /**
     * Checks if the material quantity exceeds the maximum capacity.
     * 
     * Useful for storage management and overflow prevention.
     * 
     * @return true if quantity exceeds maximum capacity, false otherwise
     */
    public boolean isOverStock() {
        return this.maximumQuantity > 0 && this.quantity > this.maximumQuantity;
    }
    
    /**
     * Gets the remaining capacity before reaching maximum.
     * 
     * Calculates how much more material can be stored before reaching
     * the maximum capacity limit.
     * 
     * @return remaining capacity, or -1 if no maximum is set
     */
    public double getRemainingCapacity() {
        if (this.maximumQuantity <= 0) {
            return -1; // No maximum set
        }
        return Math.max(0, this.maximumQuantity - this.quantity);
    }
    
    /**
     * Consumes (reduces) material quantity.
     * 
     * Reduces the current quantity by the specified amount.
     * Prevents negative quantities and updates timestamps.
     * 
     * @param amount the amount to consume (must be positive)
     * @return true if consumption was successful, false if insufficient quantity
     */
    public boolean consumeMaterial(double amount) {
        if (amount <= 0 || amount > this.quantity) {
            return false; // Invalid amount or insufficient quantity
        }
        this.quantity -= amount;
        calculateTotalCost();
        updateTimestamp();
        return true;
    }
    
    /**
     * Adds material quantity.
     * 
     * Increases the current quantity by the specified amount.
     * Prevents exceeding maximum capacity if set.
     * 
     * @param amount the amount to add (must be positive)
     * @return true if addition was successful, false if would exceed capacity
     */
    public boolean addMaterial(double amount) {
        if (amount <= 0) {
            return false; // Invalid amount
        }
        
        if (this.maximumQuantity > 0 && (this.quantity + amount) > this.maximumQuantity) {
            return false; // Would exceed maximum capacity
        }
        
        this.quantity += amount;
        calculateTotalCost();
        updateTimestamp();
        return true;
    }
    
    /**
     * Validates that the material has all required fields populated.
     * 
     * Checks that essential fields (material name, category, unit) are not null or empty.
     * 
     * @return true if all required fields are valid, false otherwise
     */
    public boolean isValid() {
        return this.materialName != null && !this.materialName.trim().isEmpty() &&
               this.category != null && !this.category.trim().isEmpty() &&
               this.unit != null && !this.unit.trim().isEmpty() &&
               this.quantity >= 0 && this.unitCost >= 0;
    }
    
    // ================= OBJECT METHODS =================
    
    /**
     * Returns a string representation of this material.
     * 
     * Provides a readable format showing key material information,
     * useful for debugging and logging purposes.
     * 
     * @return a string representation of the material
     */
    @Override
    public String toString() {
        return "Material{" +
                "materialId='" + materialId + '\'' +
                ", materialName='" + materialName + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", unitCost=" + unitCost +
                ", totalCost=" + totalCost +
                ", currency='" + currency + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    
    /**
     * Checks equality with another object.
     * 
     * Two materials are considered equal if they have the same material ID.
     * 
     * @param obj the object to compare with
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Material material = (Material) obj;
        return materialId != null ? materialId.equals(material.materialId) : material.materialId == null;
    }
    
    /**
     * Returns hash code for this material.
     * 
     * Based on the material ID to ensure consistency with equals method.
     * 
     * @return hash code of the material
     */
    @Override
    public int hashCode() {
        return materialId != null ? materialId.hashCode() : 0;
    }
}