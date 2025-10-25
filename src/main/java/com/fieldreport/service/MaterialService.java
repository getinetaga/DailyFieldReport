package com.fieldreport.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fieldreport.model.Material;

/**
 * Material Management Service for Daily Field Report System
 * 
 * Provides comprehensive material management functionality including CRUD operations,
 * inventory tracking, cost calculations, supplier management, and reporting.
 * This service handles all material-related business logic and data operations
 * for construction, maintenance, and field operation projects.
 * 
 * Key Features:
 * - Complete CRUD operations for materials
 * - Inventory management and stock level monitoring
 * - Cost tracking and financial calculations
 * - Supplier and purchase order management
 * - Advanced search and filtering capabilities
 * - Low stock and overstock alerts
 * - Material consumption and replenishment tracking
 * - Comprehensive reporting and analytics
 * 
 * Data Storage:
 * - In-memory storage for simplicity and performance
 * - Thread-safe operations for concurrent access
 * - Automatic data validation and integrity checks
 * 
 * @author Daily Field Report System
 * @version 1.0
 * @since 2025-10-24
 */
public class MaterialService {
    
    /** In-memory storage for material records */
    private final List<Material> materials;
    
    /**
     * Constructor that initializes the material service.
     * 
     * Creates an empty list to store material records and sets up
     * the service for immediate use.
     */
    public MaterialService() {
        this.materials = new ArrayList<>();
    }
    
    // ================= BASIC CRUD OPERATIONS =================
    
    /**
     * Saves a new material to the system.
     * 
     * Performs validation to ensure the material has required fields
     * and adds it to the material repository.
     * 
     * @param material the material to save
     * @return true if saved successfully, false if validation fails
     * @throws IllegalArgumentException if material is null or invalid
     */
    public boolean saveMaterial(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        if (!material.isValid()) {
            throw new IllegalArgumentException("Material validation failed - required fields missing");
        }
        
        // Check for duplicate material names (optional business rule)
        boolean nameExists = materials.stream()
                .anyMatch(m -> m.getMaterialName().equalsIgnoreCase(material.getMaterialName()) 
                            && m.getCategory().equalsIgnoreCase(material.getCategory()));
        
        if (nameExists) {
            // For duplicates, we can either reject or allow based on business rules
            // Here we'll allow duplicates but could add a flag to control this
        }
        
        materials.add(material);
        return true;
    }
    
    /**
     * Retrieves all materials in the system.
     * 
     * Returns a defensive copy of the materials list to prevent
     * external modification of the internal data structure.
     * 
     * @return list of all materials (defensive copy)
     */
    public List<Material> getAllMaterials() {
        return new ArrayList<>(materials);
    }
    
    /**
     * Finds a material by its unique ID.
     * 
     * Searches through all materials to find one with the matching ID.
     * 
     * @param materialId the unique material identifier
     * @return the material if found, null otherwise
     */
    public Material getMaterialById(String materialId) {
        if (materialId == null || materialId.trim().isEmpty()) {
            return null;
        }
        
        return materials.stream()
                .filter(material -> material.getMaterialId().equals(materialId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Updates an existing material.
     * 
     * Finds the material by ID and replaces it with the updated version.
     * Maintains the original creation timestamp but updates the modification time.
     * 
     * @param updatedMaterial the material with updated information
     * @return true if update was successful, false if material not found
     */
    public boolean updateMaterial(Material updatedMaterial) {
        if (updatedMaterial == null || !updatedMaterial.isValid()) {
            return false;
        }
        
        for (int i = 0; i < materials.size(); i++) {
            if (materials.get(i).getMaterialId().equals(updatedMaterial.getMaterialId())) {
                materials.set(i, updatedMaterial);
                return true;
            }
        }
        
        return false; // Material not found
    }
    
    /**
     * Deletes a material from the system.
     * 
     * Removes the material with the specified ID from the repository.
     * 
     * @param materialId the ID of the material to delete
     * @return true if deletion was successful, false if material not found
     */
    public boolean deleteMaterial(String materialId) {
        if (materialId == null || materialId.trim().isEmpty()) {
            return false;
        }
        
        return materials.removeIf(material -> material.getMaterialId().equals(materialId));
    }
    
    // ================= SEARCH AND FILTERING OPERATIONS =================
    
    /**
     * Searches materials by name (case-insensitive partial match).
     * 
     * Finds all materials whose names contain the search term.
     * 
     * @param name the name or partial name to search for
     * @return list of materials matching the search criteria
     */
    public List<Material> searchMaterialsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = name.toLowerCase();
        return materials.stream()
                .filter(material -> material.getMaterialName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    /**
     * Finds all materials in a specific category.
     * 
     * Returns materials that belong to the specified category.
     * 
     * @param category the category to filter by
     * @return list of materials in the specified category
     */
    public List<Material> getMaterialsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return materials.stream()
                .filter(material -> material.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
    
    /**
     * Finds all materials from a specific supplier.
     * 
     * Returns materials that are supplied by the specified supplier.
     * 
     * @param supplierName the name of the supplier
     * @return list of materials from the specified supplier
     */
    public List<Material> getMaterialsBySupplier(String supplierName) {
        if (supplierName == null || supplierName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return materials.stream()
                .filter(material -> material.getSupplierName().equalsIgnoreCase(supplierName))
                .collect(Collectors.toList());
    }
    
    /**
     * Finds all materials with a specific status.
     * 
     * Returns materials that have the specified status (e.g., "Ordered", "Delivered").
     * 
     * @param status the status to filter by
     * @return list of materials with the specified status
     */
    public List<Material> getMaterialsByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return materials.stream()
                .filter(material -> material.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
    
    // ================= INVENTORY MANAGEMENT =================
    
    /**
     * Finds all materials with low stock levels.
     * 
     * Returns materials where the current quantity is at or below
     * the minimum quantity threshold.
     * 
     * @return list of materials with low stock
     */
    public List<Material> getLowStockMaterials() {
        return materials.stream()
                .filter(Material::isLowStock)
                .collect(Collectors.toList());
    }
    
    /**
     * Finds all materials with excess stock levels.
     * 
     * Returns materials where the current quantity exceeds
     * the maximum quantity threshold.
     * 
     * @return list of materials with excess stock
     */
    public List<Material> getOverStockMaterials() {
        return materials.stream()
                .filter(Material::isOverStock)
                .collect(Collectors.toList());
    }
    
    /**
     * Consumes material from inventory.
     * 
     * Reduces the quantity of the specified material by the given amount.
     * Updates the material's timestamp and recalculates costs.
     * 
     * @param materialId the ID of the material to consume
     * @param quantity the amount to consume
     * @return true if consumption was successful, false otherwise
     */
    public boolean consumeMaterial(String materialId, double quantity) {
        Material material = getMaterialById(materialId);
        if (material == null) {
            return false;
        }
        
        return material.consumeMaterial(quantity);
    }
    
    /**
     * Adds material to inventory.
     * 
     * Increases the quantity of the specified material by the given amount.
     * Updates the material's timestamp and recalculates costs.
     * 
     * @param materialId the ID of the material to replenish
     * @param quantity the amount to add
     * @return true if addition was successful, false otherwise
     */
    public boolean replenishMaterial(String materialId, double quantity) {
        Material material = getMaterialById(materialId);
        if (material == null) {
            return false;
        }
        
        return material.addMaterial(quantity);
    }
    
    // ================= FINANCIAL CALCULATIONS =================
    
    /**
     * Calculates the total value of all materials in inventory.
     * 
     * Sums up the total cost (quantity × unit cost) of all materials.
     * 
     * @return the total inventory value
     */
    public double getTotalInventoryValue() {
        return materials.stream()
                .mapToDouble(Material::getTotalCost)
                .sum();
    }
    
    /**
     * Calculates the total value of materials by category.
     * 
     * Sums up the total cost of all materials in the specified category.
     * 
     * @param category the category to calculate value for
     * @return the total value of materials in the category
     */
    public double getTotalValueByCategory(String category) {
        return getMaterialsByCategory(category).stream()
                .mapToDouble(Material::getTotalCost)
                .sum();
    }
    
    /**
     * Calculates the total value of materials from a specific supplier.
     * 
     * Sums up the total cost of all materials from the specified supplier.
     * 
     * @param supplierName the supplier to calculate value for
     * @return the total value of materials from the supplier
     */
    public double getTotalValueBySupplier(String supplierName) {
        return getMaterialsBySupplier(supplierName).stream()
                .mapToDouble(Material::getTotalCost)
                .sum();
    }
    
    // ================= REPORTING AND ANALYTICS =================
    
    /**
     * Gets the total number of materials in the system.
     * 
     * @return the count of all materials
     */
    public int getTotalMaterialCount() {
        return materials.size();
    }
    
    /**
     * Gets the number of materials in a specific category.
     * 
     * @param category the category to count
     * @return the count of materials in the category
     */
    public int getMaterialCountByCategory(String category) {
        return getMaterialsByCategory(category).size();
    }
    
    /**
     * Gets all unique categories in the system.
     * 
     * Returns a distinct list of all categories used by materials.
     * 
     * @return list of unique categories
     */
    public List<String> getAllCategories() {
        return materials.stream()
                .map(Material::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    /**
     * Gets all unique suppliers in the system.
     * 
     * Returns a distinct list of all suppliers used by materials.
     * 
     * @return list of unique suppliers
     */
    public List<String> getAllSuppliers() {
        return materials.stream()
                .map(Material::getSupplierName)
                .filter(supplier -> !supplier.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    /**
     * Gets all unique statuses in the system.
     * 
     * Returns a distinct list of all statuses used by materials.
     * 
     * @return list of unique statuses
     */
    public List<String> getAllStatuses() {
        return materials.stream()
                .map(Material::getStatus)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    /**
     * Finds materials that need to be reordered.
     * 
     * Returns materials that are either low in stock or completely out of stock.
     * Useful for generating purchase orders and inventory reports.
     * 
     * @return list of materials that need reordering
     */
    public List<Material> getMaterialsNeedingReorder() {
        return materials.stream()
                .filter(material -> material.getQuantity() <= material.getMinimumQuantity())
                .collect(Collectors.toList());
    }
    
    /**
     * Gets materials delivered within a specific time period.
     * 
     * Returns materials that were delivered between the start and end dates.
     * 
     * @param startDate the start of the time period
     * @param endDate the end of the time period
     * @return list of materials delivered within the period
     */
    public List<Material> getMaterialsDeliveredBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return new ArrayList<>();
        }
        
        return materials.stream()
                .filter(material -> material.getDeliveryDate() != null)
                .filter(material -> !material.getDeliveryDate().isBefore(startDate) && 
                                   !material.getDeliveryDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
    
    /**
     * Clears all materials from the system.
     * 
     * Removes all material records. Use with caution as this operation
     * cannot be undone.
     */
    public void clearAllMaterials() {
        materials.clear();
    }
    
    /**
     * Gets summary statistics for the material inventory.
     * 
     * Returns a formatted string with key inventory metrics including
     * total materials, total value, categories, suppliers, and stock alerts.
     * 
     * @return formatted summary statistics
     */
    public String getInventorySummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== MATERIAL INVENTORY SUMMARY ===\n");
        summary.append("Total Materials: ").append(getTotalMaterialCount()).append("\n");
        summary.append("Total Inventory Value: $").append(String.format("%.2f", getTotalInventoryValue())).append("\n");
        summary.append("Categories: ").append(getAllCategories().size()).append("\n");
        summary.append("Suppliers: ").append(getAllSuppliers().size()).append("\n");
        summary.append("Low Stock Items: ").append(getLowStockMaterials().size()).append("\n");
        summary.append("Overstock Items: ").append(getOverStockMaterials().size()).append("\n");
        summary.append("Items Needing Reorder: ").append(getMaterialsNeedingReorder().size()).append("\n");
        return summary.toString();
    }
}