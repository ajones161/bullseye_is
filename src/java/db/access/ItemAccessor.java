/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access;

import entities.Item;
import static java.lang.System.out;
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
public class ItemAccessor {

    private static Connection conn = null;
    private static PreparedStatement selectAllStatement = null;
    private static PreparedStatement sortByName = null;
    private static PreparedStatement sortBySupplier = null;
    private static PreparedStatement sortByCategory = null;
    private static PreparedStatement insertStatement = null;
    private static PreparedStatement updateStatement = null;

    private static void init() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bullseye?useSSL=false", "root", "root");

            selectAllStatement = conn.prepareStatement("select * from item");
            sortByName = conn.prepareStatement("select * from item ORDER BY item.itemName");
            sortBySupplier = conn.prepareStatement("select * from item ORDER BY item.supplierID");
            sortByCategory = conn.prepareStatement("select * from item ORDER BY item.categoryName");

            insertStatement = conn.prepareStatement("insert INTO item values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            updateStatement = conn.prepareStatement("update item "
                    + "set itemID = ?, itemName = ?, "
                    + "sku = ?, description = ?, "
                    + "categoryName = ?, weight = ?,"
                    + "costPrice = ?, retailPrice = ?,"
                    + "supplierID = ?, caseSize = ?, "
                    + "notes = ?, active = ? "
                    + "where itemID = ?");
        }
    }

    public static List<Item> getAllItems() {
        List<Item> cList = new ArrayList();
        try {
            init();

            ResultSet rs = selectAllStatement.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("itemID");
                String name = rs.getString("itemName");
                String sku = rs.getString("sku");
                String desc = rs.getString("description");
                String cat = rs.getString("categoryName");
                double wgt = rs.getDouble("weight");
                double cos = rs.getDouble("costPrice");
                double ret = rs.getDouble("retailPrice");
                int sup = rs.getInt("supplierID");
                int cas = rs.getInt("caseSize");
                String not = rs.getString("notes");
                Boolean act = rs.getBoolean("active");

                Item item = new Item(id, name, sku, desc, cat, wgt, cos, ret, sup, cas, not, act);

                cList.add(item);

            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }
    
    public static List<Item> sortItem(String sortBy) {
        List<Item> cList = new ArrayList();
        try {
            init();

            ResultSet rs = null;
            switch (sortBy) {
                case "Name":
                    rs = sortByName.executeQuery();
                    break;
                case "Supplier":
                    rs = sortBySupplier.executeQuery();
                    break;
                case "Category":
                    rs = sortByCategory.executeQuery();
                    break;
                default:
                    rs = selectAllStatement.executeQuery();
                    break;
            }

            while (rs.next()) {

                int id = rs.getInt("itemID");
                String name = rs.getString("itemName");
                String sku = rs.getString("sku");
                String desc = rs.getString("description");
                String cat = rs.getString("categoryName");
                double wgt = rs.getDouble("weight");
                double cos = rs.getDouble("costPrice");
                double ret = rs.getDouble("retailPrice");
                int sup = rs.getInt("supplierID");
                int cas = rs.getInt("caseSize");
                String not = rs.getString("notes");
                Boolean act = rs.getBoolean("active");

                Item item = new Item(id, name, sku, desc, cat, wgt, cos, ret, sup, cas, not, act);

                cList.add(item);

                cList.add(item);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }

    public static boolean addItm(Item item) {
        boolean res = true;
        int rowCount;
        try {
            init();

            insertStatement.setInt(1, item.getItemID());
            insertStatement.setString(2, item.getItemName());
            insertStatement.setString(3, item.getSku());
            insertStatement.setString(4, item.getDescription());
            insertStatement.setString(5, item.getCategoryName());
            insertStatement.setDouble(6, item.getWeight());
            insertStatement.setDouble(7, item.getCostPrice());
            insertStatement.setDouble(8, item.getRetailPrice());
            insertStatement.setInt(9, item.getSupplierID());
            insertStatement.setInt(10, item.getCaseSize());
            insertStatement.setString(11, item.getNotes());
            insertStatement.setBoolean(12, item.getActive());
            
            rowCount = insertStatement.executeUpdate();
            out.println(rowCount);

        } catch (SQLException ex) {
            res = false;
        }
        
        return res;
    }

    public static boolean updateItm(Item item) {
        boolean res = true;

        try {
            init();
            updateStatement.setInt(1, item.getItemID());
            updateStatement.setString(2, item.getItemName());
            updateStatement.setString(3, item.getSku());
            updateStatement.setString(4, item.getDescription());
            updateStatement.setString(5, item.getCategoryName());
            updateStatement.setDouble(6, item.getWeight());
            updateStatement.setDouble(7, item.getCostPrice());
            updateStatement.setDouble(8, item.getRetailPrice());
            updateStatement.setInt(9, item.getSupplierID());
            updateStatement.setInt(10, item.getCaseSize());
            updateStatement.setString(11, item.getNotes());
            updateStatement.setBoolean(12, item.getActive());
            
            updateStatement.setInt(13, item.getItemID());
            int rowCount = updateStatement.executeUpdate();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }
}
