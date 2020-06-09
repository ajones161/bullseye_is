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
public class InvItem {

    private int itemID;
    private String locationID;
    private String itemName;
    private String categoryName;
    private String sku;
    private String description;
    private String companyName;
    private double weight;
    private int caseSize;
    private int quantity;
    private int reorderThreshold;
    private int reorderLevel;
    private double retailPrice;

    public InvItem(int itemID, String locationID, String itemName, String categoryName, String sku, String description, String companyName,
            double weight, int caseSize,
            int quantity, int reorderThreshold, int reorderLevel) {
        this.itemID = itemID;
        this.locationID = locationID;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.sku = sku;
        this.description = description;
        this.companyName = companyName;
        this.weight = weight;
        this.caseSize = caseSize;
        this.quantity = quantity;
        this.reorderThreshold = reorderThreshold;
        this.reorderLevel = reorderLevel;
    }

    public InvItem(int itemID, String locationID, int quantity, int reorderThreshold, int reorderLevel) {
        this.itemID = itemID;
        this.locationID = locationID;
        this.quantity = quantity;
        this.reorderThreshold = reorderThreshold;
        this.reorderLevel = reorderLevel;
    }

    public InvItem(int itemID, String itemName, String sku, String categoryName, double weight, int caseSize, int quantity) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.sku = sku;
        this.weight = weight;
        this.caseSize = caseSize;
        this.quantity = quantity;

    }
    
    public InvItem(int itemID, String locationID, String itemName, String description, String categoryName, int quantity, double retailPrice) {
        this.itemID = itemID;
        this.locationID = locationID;
        this.itemName = itemName;
        this.description = description;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.retailPrice = retailPrice;
    }

    public int getItemID() {
        return itemID;
    }

    public String getLocationID() {
        return locationID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getSku() {
        return sku;
    }

    public double getWeight() {
        return weight;
    }

    public String getDesc() {
        return description;
    }

    public String getSupp() {
        return companyName;
    }

    public int getCaseSize() {
        return caseSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getReorderThreshold() {
        return reorderThreshold;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }
    
    public double getRetailPrice() {
        return retailPrice;
    }
}
