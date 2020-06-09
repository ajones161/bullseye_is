/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access;

import entities.InvItem;
import entities.tItem;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ajone
 */
public class InvAccessor {

    private static Connection conn = null;
    private static PreparedStatement selectAllStatement = null;
    private static PreparedStatement updateQuantities = null;
    private static PreparedStatement sortByName = null;
    private static PreparedStatement sortByCat = null;
    private static PreparedStatement sortBySKU = null;
    private static PreparedStatement sortByDesc = null;
    private static PreparedStatement grabOrder = null;
    private static PreparedStatement updateQuant = null;
    private static PreparedStatement pointOfSale = null;
    private static CallableStatement createSale = null;
    private static CallableStatement fillSale = null;

    private InvAccessor() {
    }

    private static void init() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bullseye?useSSL=false", "root", "root");

            updateQuantities = conn.prepareStatement("update inventory "
                    + "set quantity = ?, reorderThreshold = ?, "
                    + "reorderLevel = ? "
                    + "where itemID = ? AND locationID = ?");

            selectAllStatement = conn.prepareStatement("SELECT inventory.itemID, inventory.locationID, item.itemName, item.sku, item.categoryName, item.description,"
                    + "supplier.companyName, "
                    + "item.weight, item.caseSize, inventory.quantity, "
                    + "inventory.reorderThreshold, inventory.reorderLevel "
                    + "FROM inventory, item, supplier  WHERE  item.itemID = inventory.itemID "
                    + "AND item.supplierID = supplier.supplierID AND inventory.locationID = ? "
            );

            sortByName = conn.prepareStatement("SELECT inventory.itemID, inventory.locationID, item.itemName, item.sku, item.categoryName, item.description,"
                    + "supplier.companyName, "
                    + "item.weight, item.caseSize, inventory.quantity, "
                    + "inventory.reorderThreshold, inventory.reorderLevel "
                    + "FROM inventory, item, supplier  WHERE  item.itemID = inventory.itemID "
                    + "AND item.supplierID = supplier.supplierID AND inventory.locationID = ? "
                    + "ORDER BY item.itemName"
            );

            sortByCat = conn.prepareStatement("SELECT inventory.itemID, inventory.locationID, item.itemName, item.sku, item.categoryName, item.description,"
                    + "supplier.companyName, "
                    + "item.weight, item.caseSize, inventory.quantity, "
                    + "inventory.reorderThreshold, inventory.reorderLevel "
                    + "FROM inventory, item, supplier  WHERE  item.itemID = inventory.itemID "
                    + "AND item.supplierID = supplier.supplierID AND inventory.locationID = ? "
                    + "ORDER BY item.categoryName"
            );

            sortBySKU = conn.prepareStatement("SELECT inventory.itemID, inventory.locationID, item.itemName, item.sku, item.categoryName, item.description,"
                    + "supplier.companyName, "
                    + "item.weight, item.caseSize, inventory.quantity, "
                    + "inventory.reorderThreshold, inventory.reorderLevel "
                    + "FROM inventory, item, supplier  WHERE  item.itemID = inventory.itemID "
                    + "AND item.supplierID = supplier.supplierID AND inventory.locationID = ? "
                    + "ORDER BY item.sku"
            );

            sortByDesc = conn.prepareStatement("SELECT inventory.itemID, inventory.locationID, item.itemName, item.sku, item.categoryName, item.description,"
                    + "supplier.companyName, "
                    + "item.weight, item.caseSize, inventory.quantity, "
                    + "inventory.reorderThreshold, inventory.reorderLevel "
                    + "FROM inventory, item, supplier  WHERE  item.itemID = inventory.itemID "
                    + "AND item.supplierID = supplier.supplierID AND inventory.locationID = ? "
                    + "ORDER BY item.description"
            );

            grabOrder = conn.prepareStatement("Select itemID, itemName, SKU, categoryName, weight, caseSize, "
                    + "(select quantity from transactionline where itemID = i.itemID and transactionID = ?) as quantity "
                    + "from item i "
                    + "where itemID in (select itemID from transactionline where transactionID = ?)");
            
            updateQuant = conn.prepareStatement("update transactionline set quantity = ? where itemID = ? and transactionID = ?");
            
            pointOfSale = conn.prepareStatement("SELECT i.itemID, i.locationID, it.itemName, it.description, it.categoryName, i.quantity, it.retailPrice\n" +
            "FROM bullseye.inventory i INNER JOIN item it ON i.itemID = it.itemID " +
            "WHERE i.locationID = ?");
            
            createSale = conn.prepareCall("call createSale(?)");
            fillSale = conn.prepareCall("call doSale(?, ?, ?)");
        }
    }

    public static List<InvItem> getOrderItems(String transactionID) throws SQLException {
        List<InvItem> inv = new ArrayList();
        try {
            init();
            grabOrder.setString(1, transactionID);
            grabOrder.setString(2, transactionID);
            ResultSet rs = grabOrder.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("itemID");
                String name = rs.getString("itemName");
                String sku = rs.getString("SKU");
                String catName = rs.getString("categoryName");
                double weight = rs.getDouble("weight");
                int cSize = rs.getInt("caseSize");
                int quant = rs.getInt("quantity");

                InvItem item = new InvItem(id, name, sku, catName, weight, cSize, quant);
                inv.add(item);
            }
        } catch (SQLException ex) {
            inv = new ArrayList();
        }
        return inv;
    }

    public static List<InvItem> getAllInv(String locationID) {
        List<InvItem> inv = new ArrayList();
        try {
            init();

            selectAllStatement.setString(1, locationID);

            ResultSet rs = selectAllStatement.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("itemID");
                String location = rs.getString("locationID");
                String name = rs.getString("itemName");
                String sku = rs.getString("SKU");
                String catName = rs.getString("categoryName");
                String desc = rs.getString("description");
                String supp = rs.getString("companyName");
                double weight = rs.getDouble("weight");
                int cSize = rs.getInt("caseSize");
                int quant = rs.getInt("quantity");
                int thresh = rs.getInt("reorderThreshold");
                int level = rs.getInt("reorderLevel");

                InvItem item = new InvItem(id, location, name, catName, sku, desc, supp, weight, cSize, quant, thresh, level);

                inv.add(item);
            }
        } catch (SQLException ex) {
            inv = new ArrayList();
        }

        return inv;
    }

    public static List<InvItem> sortInv(String locationID, String sortBy) {
        List<InvItem> inv = new ArrayList();

        try {
            //"Item Name", "Category", "SKU", "Description", "Supplier", "Wgt.", "Case Size", "In Stock", "Threshold", "Level"
            init();
            ResultSet rs = null;
            switch (sortBy) {
                case "Item_Name":
                    sortByName.setString(1, locationID);
                    rs = sortByName.executeQuery();
                    break;
                case "Category":
                    sortByCat.setString(1, locationID);
                    rs = sortByCat.executeQuery();
                    break;
                case "SKU":
                    sortBySKU.setString(1, locationID);
                    rs = sortBySKU.executeQuery();
                    break;
                case "Description":
                    sortByDesc.setString(1, locationID);
                    rs = sortByDesc.executeQuery();
                    break;
                default:
                    selectAllStatement.setString(1, locationID);
                    rs = selectAllStatement.executeQuery();
                    break;
            }

            while (rs.next()) {

                int id = rs.getInt("itemID");
                String location = rs.getString("locationID");
                String name = rs.getString("itemName");
                String sku = rs.getString("SKU");
                String catName = rs.getString("categoryName");
                String desc = rs.getString("description");
                String supp = rs.getString("companyName");
                double weight = rs.getDouble("weight");
                int cSize = rs.getInt("caseSize");
                int quant = rs.getInt("quantity");
                int thresh = rs.getInt("reorderThreshold");
                int level = rs.getInt("reorderLevel");

                InvItem item = new InvItem(id, location, name, catName, sku, desc, supp, weight, cSize, quant, thresh, level);

                inv.add(item);
            }
        } catch (SQLException ex) {
            inv = new ArrayList();
        }
        return inv;
    }

    public static boolean createSale(String locationID) {
        boolean res = true;
        try {
            init();
            createSale.setString(1, locationID);
            createSale.executeUpdate();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }
    
    public static boolean fillSale(String locationID, int item, int quant) {
        boolean res = true;
        try {
            init();
            fillSale.setString(1, locationID);
            fillSale.setInt(2, item);
            fillSale.setInt(3, quant);
            fillSale.executeUpdate();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }
    
    
    public static boolean updateInv(InvItem item) {
        boolean res = true;
        try {
            init();
            updateQuantities.setInt(1, item.getQuantity());
            updateQuantities.setInt(2, item.getReorderThreshold());
            updateQuantities.setInt(3, item.getReorderLevel());
            updateQuantities.setInt(4, item.getItemID());
            updateQuantities.setString(5, item.getLocationID());

            int rowCount = updateQuantities.executeUpdate();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }
    
    
    public static List<InvItem> pointOfSale(String locationID) {
        List<InvItem> inv = new ArrayList();
        try {
            init();

            pointOfSale.setString(1, locationID);

            ResultSet rs = pointOfSale.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("itemID");
                String location = rs.getString("locationID");
                String name = rs.getString("itemName");
                String description = rs.getString("description");
                String catName = rs.getString("categoryName");
                int quant = rs.getInt("quantity");
                double price = rs.getDouble("retailPrice");
                
                InvItem item = new InvItem(id, location, name, description, catName, quant, price);
                inv.add(item);
            }
        } catch (SQLException ex) {
            inv = new ArrayList();
        }

        return inv;
        
    }
}


