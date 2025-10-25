package com.fieldreport.demo;

import java.time.LocalDateTime;
import java.util.List;

import com.fieldreport.model.Material;
import com.fieldreport.service.MaterialService;

/**
 * Material Management System Demo
 * 
 * Demonstrates the comprehensive material management functionality
 * integrated with the Daily Field Report System. This demo showcases
 * CRUD operations, inventory management, cost tracking, and reporting
 * capabilities for construction and field operation materials.
 * 
 * Key Demonstrations:
 * - Creating and managing material records
 * - Inventory tracking and stock level management
 * - Cost calculations and financial reporting
 * - Supplier and purchase order management
 * - Search and filtering operations
 * - Low stock and overstock monitoring
 * - Material consumption and replenishment
 * - Comprehensive reporting and analytics
 * 
 * Usage:
 * Run this class independently to test material management functionality.
 * The demo creates sample materials, performs various operations, and
 * displays results to demonstrate system capabilities.
 * 
 * @author Daily Field Report System
 * @version 1.0
 * @since 2025-10-24
 */
public class MaterialDemo {
    
    /**
     * Main method to execute the material management demonstration.
     * 
     * Creates sample materials, tests various operations, and provides
     * console output showing the results of different material management
     * scenarios and use cases.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("🏗️  Daily Field Report - Material Management System Demo");
        System.out.println("============================================================\n");
        
        // Initialize the material service
        MaterialService materialService = new MaterialService();
        
        try {
            // Demo 1: Create sample materials
            System.out.println("📦 Demo 1: Creating Sample Materials");
            System.out.println("-----------------------------------");
            createSampleMaterials(materialService);
            
            // Demo 2: Display all materials
            System.out.println("\n📋 Demo 2: Displaying All Materials");
            System.out.println("----------------------------------");
            displayAllMaterials(materialService);
            
            // Demo 3: Search and filtering operations
            System.out.println("\n🔍 Demo 3: Search and Filtering Operations");
            System.out.println("-----------------------------------------");
            demonstrateSearchOperations(materialService);
            
            // Demo 4: Inventory management
            System.out.println("\n📊 Demo 4: Inventory Management");
            System.out.println("------------------------------");
            demonstrateInventoryManagement(materialService);
            
            // Demo 5: Financial calculations
            System.out.println("\n💰 Demo 5: Financial Calculations");
            System.out.println("--------------------------------");
            demonstrateFinancialCalculations(materialService);
            
            // Demo 6: Reporting and analytics
            System.out.println("\n📈 Demo 6: Reporting and Analytics");
            System.out.println("---------------------------------");
            demonstrateReporting(materialService);
            
            System.out.println("\n============================================================");
            System.out.println("🎉 Material Management Demo Completed Successfully!");
            System.out.println("💡 The material system is ready for integration with field reports");
            System.out.println("============================================================");
            
        } catch (Exception e) {
            System.out.println("❌ Error during material demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates sample materials for demonstration purposes.
     * 
     * @param materialService the material service to use
     */
    private static void createSampleMaterials(MaterialService materialService) {
        // Construction materials
        Material concrete = new Material(
            "High-Strength Concrete",
            "Premium concrete mix for structural foundations",
            "Construction",
            500.0,
            "kg",
            0.85,
            "BuildSupply Co."
        );
        concrete.setBrand("ProMix");
        concrete.setMinimumQuantity(100.0);
        concrete.setMaximumQuantity(2000.0);
        concrete.setStatus("Delivered");
        concrete.setStorageLocation("Warehouse A - Section 1");
        concrete.setPurchaseOrderNumber("PO-2025-001");
        concrete.setDeliveryDate(LocalDateTime.now().minusDays(3));
        
        Material steelRebar = new Material(
            "Steel Reinforcement Bars",
            "Grade 60 steel rebar for concrete reinforcement",
            "Construction",
            200.0,
            "pieces",
            25.50,
            "SteelWorks Inc."
        );
        steelRebar.setBrand("SteelPro");
        steelRebar.setModelNumber("SP-R16-6000");
        steelRebar.setMinimumQuantity(50.0);
        steelRebar.setMaximumQuantity(1000.0);
        steelRebar.setStatus("In Use");
        steelRebar.setStorageLocation("Yard - Section B");
        steelRebar.setPurchaseOrderNumber("PO-2025-002");
        
        // Tools and equipment
        Material drill = new Material(
            "Professional Hammer Drill",
            "Heavy-duty rotary hammer drill for concrete work",
            "Tools",
            5.0,
            "pieces",
            450.00,
            "ToolMaster Ltd."
        );
        drill.setBrand("PowerDrill Pro");
        drill.setModelNumber("PDP-2500X");
        drill.setMinimumQuantity(2.0);
        drill.setMaximumQuantity(10.0);
        drill.setStatus("Delivered");
        drill.setStorageLocation("Tool Room - Cabinet 3");
        
        // Safety equipment
        Material helmets = new Material(
            "Safety Helmets",
            "ANSI-compliant hard hats with adjustable suspension",
            "Safety",
            25.0,
            "pieces",
            35.00,
            "SafetyFirst Corp."
        );
        helmets.setBrand("SafeGuard");
        helmets.setModelNumber("SG-H100");
        helmets.setMinimumQuantity(20.0);
        helmets.setMaximumQuantity(100.0);
        helmets.setStatus("Delivered");
        helmets.setStorageLocation("Safety Equipment Room");
        
        // Electrical materials
        Material wire = new Material(
            "Electrical Wire",
            "12 AWG copper electrical wire for interior wiring",
            "Electrical",
            1000.0,
            "meters",
            2.50,
            "ElectroSupply Inc."
        );
        wire.setBrand("CopperMax");
        wire.setModelNumber("CM-12AWG-TW");
        wire.setMinimumQuantity(200.0);
        wire.setMaximumQuantity(5000.0);
        wire.setStatus("Ordered");
        wire.setStorageLocation("Electrical Storage");
        wire.setOrderedDate(LocalDateTime.now().minusDays(1));
        wire.setDeliveryDate(LocalDateTime.now().plusDays(2));
        
        // Low stock item for demonstration
        Material lumber = new Material(
            "Construction Lumber",
            "Pressure-treated 2x4 lumber for framing",
            "Construction",
            15.0,  // Low quantity
            "pieces",
            12.75,
            "Lumber Yard Co."
        );
        lumber.setBrand("TreatWood");
        lumber.setMinimumQuantity(50.0);  // Above current quantity
        lumber.setMaximumQuantity(500.0);
        lumber.setStatus("Low Stock");
        lumber.setStorageLocation("Lumber Yard");
        
        // Save all materials
        materialService.saveMaterial(concrete);
        materialService.saveMaterial(steelRebar);
        materialService.saveMaterial(drill);
        materialService.saveMaterial(helmets);
        materialService.saveMaterial(wire);
        materialService.saveMaterial(lumber);
        
        System.out.println("✅ Created 6 sample materials:");
        System.out.println("   • High-Strength Concrete (Construction)");
        System.out.println("   • Steel Reinforcement Bars (Construction)");
        System.out.println("   • Professional Hammer Drill (Tools)");
        System.out.println("   • Safety Helmets (Safety)");
        System.out.println("   • Electrical Wire (Electrical)");
        System.out.println("   • Construction Lumber (Construction - Low Stock)");
    }
    
    /**
     * Displays all materials in the system.
     * 
     * @param materialService the material service to use
     */
    private static void displayAllMaterials(MaterialService materialService) {
        List<Material> allMaterials = materialService.getAllMaterials();
        
        System.out.println("📦 Total Materials in System: " + allMaterials.size());
        System.out.println();
        
        for (int i = 0; i < allMaterials.size(); i++) {
            Material material = allMaterials.get(i);
            System.out.println("Material #" + (i + 1) + ":");
            System.out.println("  Name: " + material.getMaterialName());
            System.out.println("  Category: " + material.getCategory());
            System.out.println("  Quantity: " + material.getQuantity() + " " + material.getUnit());
            System.out.println("  Unit Cost: $" + String.format("%.2f", material.getUnitCost()));
            System.out.println("  Total Cost: $" + String.format("%.2f", material.getTotalCost()));
            System.out.println("  Supplier: " + material.getSupplierName());
            System.out.println("  Status: " + material.getStatus());
            
            if (material.isLowStock()) {
                System.out.println("  ⚠️  LOW STOCK ALERT!");
            }
            
            System.out.println();
        }
    }
    
    /**
     * Demonstrates search and filtering operations.
     * 
     * @param materialService the material service to use
     */
    private static void demonstrateSearchOperations(MaterialService materialService) {
        // Search by name
        System.out.println("🔍 Searching for materials containing 'Steel':");
        List<Material> steelMaterials = materialService.searchMaterialsByName("Steel");
        steelMaterials.forEach(material -> 
            System.out.println("  • " + material.getMaterialName() + " (" + material.getCategory() + ")"));
        
        System.out.println();
        
        // Filter by category
        System.out.println("🏗️  Construction materials:");
        List<Material> constructionMaterials = materialService.getMaterialsByCategory("Construction");
        constructionMaterials.forEach(material -> 
            System.out.println("  • " + material.getMaterialName() + " - " + 
                             material.getQuantity() + " " + material.getUnit()));
        
        System.out.println();
        
        // Filter by supplier
        System.out.println("🏢 Materials from 'BuildSupply Co.':");
        List<Material> buildSupplyMaterials = materialService.getMaterialsBySupplier("BuildSupply Co.");
        buildSupplyMaterials.forEach(material -> 
            System.out.println("  • " + material.getMaterialName() + " - $" + 
                             String.format("%.2f", material.getTotalCost())));
        
        System.out.println();
        
        // Filter by status
        System.out.println("✅ Delivered materials:");
        List<Material> deliveredMaterials = materialService.getMaterialsByStatus("Delivered");
        deliveredMaterials.forEach(material -> 
            System.out.println("  • " + material.getMaterialName() + " (" + material.getCategory() + ")"));
    }
    
    /**
     * Demonstrates inventory management operations.
     * 
     * @param materialService the material service to use
     */
    private static void demonstrateInventoryManagement(MaterialService materialService) {
        // Check low stock items
        System.out.println("⚠️  Low Stock Materials:");
        List<Material> lowStockItems = materialService.getLowStockMaterials();
        if (lowStockItems.isEmpty()) {
            System.out.println("  No low stock items found.");
        } else {
            lowStockItems.forEach(material -> 
                System.out.println("  • " + material.getMaterialName() + 
                                 " - Current: " + material.getQuantity() + 
                                 ", Minimum: " + material.getMinimumQuantity()));
        }
        
        System.out.println();
        
        // Check overstock items
        System.out.println("📈 Overstock Materials:");
        List<Material> overStockItems = materialService.getOverStockMaterials();
        if (overStockItems.isEmpty()) {
            System.out.println("  No overstock items found.");
        } else {
            overStockItems.forEach(material -> 
                System.out.println("  • " + material.getMaterialName() + 
                                 " - Current: " + material.getQuantity() + 
                                 ", Maximum: " + material.getMaximumQuantity()));
        }
        
        System.out.println();
        
        // Demonstrate material consumption
        System.out.println("🔄 Demonstrating Material Consumption:");
        List<Material> allMaterials = materialService.getAllMaterials();
        if (!allMaterials.isEmpty()) {
            Material firstMaterial = allMaterials.get(0);
            double originalQuantity = firstMaterial.getQuantity();
            
            System.out.println("  Consuming 50 units of: " + firstMaterial.getMaterialName());
            System.out.println("  Original quantity: " + originalQuantity + " " + firstMaterial.getUnit());
            
            boolean success = materialService.consumeMaterial(firstMaterial.getMaterialId(), 50.0);
            if (success) {
                System.out.println("  ✅ Consumption successful!");
                System.out.println("  New quantity: " + firstMaterial.getQuantity() + " " + firstMaterial.getUnit());
            } else {
                System.out.println("  ❌ Consumption failed (insufficient quantity)");
            }
        }
    }
    
    /**
     * Demonstrates financial calculations.
     * 
     * @param materialService the material service to use
     */
    private static void demonstrateFinancialCalculations(MaterialService materialService) {
        // Total inventory value
        double totalValue = materialService.getTotalInventoryValue();
        System.out.println("💰 Total Inventory Value: $" + String.format("%.2f", totalValue));
        
        System.out.println();
        
        // Value by category
        System.out.println("📊 Value by Category:");
        List<String> categories = materialService.getAllCategories();
        for (String category : categories) {
            double categoryValue = materialService.getTotalValueByCategory(category);
            int categoryCount = materialService.getMaterialCountByCategory(category);
            System.out.println("  • " + category + ": $" + String.format("%.2f", categoryValue) + 
                             " (" + categoryCount + " items)");
        }
        
        System.out.println();
        
        // Value by supplier
        System.out.println("🏢 Value by Supplier:");
        List<String> suppliers = materialService.getAllSuppliers();
        for (String supplier : suppliers) {
            double supplierValue = materialService.getTotalValueBySupplier(supplier);
            System.out.println("  • " + supplier + ": $" + String.format("%.2f", supplierValue));
        }
    }
    
    /**
     * Demonstrates reporting and analytics.
     * 
     * @param materialService the material service to use
     */
    private static void demonstrateReporting(MaterialService materialService) {
        // Inventory summary
        System.out.println("📋 Inventory Summary:");
        System.out.println(materialService.getInventorySummary());
        
        // Materials needing reorder
        System.out.println("🛒 Materials Needing Reorder:");
        List<Material> reorderMaterials = materialService.getMaterialsNeedingReorder();
        if (reorderMaterials.isEmpty()) {
            System.out.println("  No materials need reordering at this time.");
        } else {
            reorderMaterials.forEach(material -> 
                System.out.println("  • " + material.getMaterialName() + 
                                 " - Current: " + material.getQuantity() + 
                                 ", Minimum: " + material.getMinimumQuantity() + 
                                 " (" + material.getUnit() + ")"));
        }
        
        System.out.println();
        
        // All categories and suppliers
        System.out.println("📂 All Categories: " + String.join(", ", materialService.getAllCategories()));
        System.out.println("🏢 All Suppliers: " + String.join(", ", materialService.getAllSuppliers()));
        System.out.println("📋 All Statuses: " + String.join(", ", materialService.getAllStatuses()));
    }
}