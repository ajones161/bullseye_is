/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author ajone
 */
public class Item {
    private int itemID;
    private String itemName;
    private String sku;
    private String description;
    private String categoryName;
    private double weight;
    private double costPrice;
    private double retailPrice;
    private int supplierID;
    private int caseSize;
    private String notes;
    private Boolean active;
    
    public Item(int itemID, String itemName, String sku, String description, String categoryName, double weight, 
            double costPrice, double retailPrice, int supplierID, int caseSize, String notes, Boolean active) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.sku = sku;
        this.description = description;
        this.categoryName = categoryName;
        this.weight = weight;
        this.costPrice = costPrice;
        this.retailPrice = retailPrice;
        this.supplierID = supplierID;
        this.caseSize = caseSize;
        this.notes = notes;
        this.active = active;
    }
    
    public int getItemID() {
        return itemID;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public String getSku() {
        return sku;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public double getCostPrice() {
        return costPrice;
    }
    
    public double getRetailPrice() {
        return retailPrice;
    }
    
    public int getSupplierID() {
        return supplierID;
    }
    
    public int getCaseSize() {
        return caseSize;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public Boolean getActive() {
        return active;
    }
}
