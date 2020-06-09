/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access;

import entities.Order;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
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
public class OrdAccessor {

    private static Connection conn = null;
    private static PreparedStatement selectAllStatement = null;
    private static PreparedStatement selectOutStatement = null;
    private static PreparedStatement sortAllStatement = null;
    private static PreparedStatement sortOutStatement = null;
    private static PreparedStatement updateOrder = null;
    private static PreparedStatement getWarehouse = null;
    private static PreparedStatement getOrderItems = null;
    private static CallableStatement createOrder = null;
    private static CallableStatement createEmergency = null;
    private static CallableStatement doBackorder = null;
    private static CallableStatement doEmptyLoss = null;
    private static CallableStatement fillLoss = null;
    private static CallableStatement processReturns = null;
    private static CallableStatement checkWarehouse = null;

    private static void init() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bullseye?useSSL=false", "root", "root");
            selectAllStatement = conn.prepareStatement("select * from transaction order by estimatedArrival desc");
            sortAllStatement = conn.prepareStatement("select * from transaction where originalLocationID = ? order by estimatedArrival desc");
            selectOutStatement = conn.prepareStatement("select * from transaction where transactionStatus NOT IN ('NEW', 'COMPLETE') order by estimatedArrival desc");
            sortOutStatement = conn.prepareStatement("select * from transaction where originalLocationID = ? and transactionStatus NOT IN ('NEW', 'COMPLETE') order by estimatedArrival desc");
            updateOrder = conn.prepareStatement("update transaction set transactionStatus = ?, notes = concat(notes, ?) where transactionID = ?");
            createOrder = conn.prepareCall("call createOrder(?)");
            getOrderItems = conn.prepareStatement("select * from transactionline where transactionID = ?");
            getWarehouse = conn.prepareStatement("select quantity from inventory where locationID = 'WARE' and itemID = ?");
            createEmergency = conn.prepareCall("call createEmergency(?)");
            doBackorder = conn.prepareCall("call createBackorder(?, ?)");
            doEmptyLoss = conn.prepareCall("call createBlankLoss(?)");
            fillLoss = conn.prepareCall("call fillLoss(?,?,?,?)");
            processReturns = conn.prepareCall("call createReturn(?,?,?,?)");
            checkWarehouse = conn.prepareCall("call checkWarehouse(?)");
            
        }
    }

    public static List<Order> getAll() {
        List<Order> cList = new ArrayList();
        try {
            init();
            ResultSet rs = selectAllStatement.executeQuery();
            while (rs.next()) {

                int transactionID = rs.getInt("transactionID");
                String transactionType = rs.getString("transactionType");
                String originalLocationID = rs.getString("originalLocationID");
                Date creationDate = rs.getDate("creationDate");
                Date estimatedArrival = rs.getDate("estimatedArrival");
                String transactionStatus = rs.getString("transactionStatus");
                int sourceTransactionID = rs.getInt("sourceTransactionID");
                String notes = rs.getString("notes");

                Order loc = new Order(transactionID, transactionType, originalLocationID, creationDate, estimatedArrival, transactionStatus, sourceTransactionID, notes);
                cList.add(loc);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }

    public static List<Order> getOutstanding() {
        List<Order> cList = new ArrayList();
        try {
            init();
            ResultSet rs = selectOutStatement.executeQuery();
            while (rs.next()) {

                int transactionID = rs.getInt("transactionID");
                String transactionType = rs.getString("transactionType");
                String originalLocationID = rs.getString("originalLocationID");
                Date creationDate = rs.getDate("creationDate");
                Date estimatedArrival = rs.getDate("estimatedArrival");
                String transactionStatus = rs.getString("transactionStatus");
                int sourceTransactionID = rs.getInt("sourceTransactionID");
                String notes = rs.getString("notes");

                Order loc = new Order(transactionID, transactionType, originalLocationID, creationDate, estimatedArrival, transactionStatus, sourceTransactionID, notes);
                cList.add(loc);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }

    public static List<Order> getFiltered(String type, String location) throws SQLException {

        List<Order> cList = new ArrayList();
        try {
            init();
            ResultSet rs = null;
            if ("reg".equals(type)) {
                sortAllStatement.setString(1, location);
                rs = sortAllStatement.executeQuery();
            } else {
                sortOutStatement.setString(1, location);
                rs = sortOutStatement.executeQuery();
            }
            while (rs.next()) {

                int transactionID = rs.getInt("transactionID");
                String transactionType = rs.getString("transactionType");
                String originalLocationID = rs.getString("originalLocationID");
                Date creationDate = rs.getDate("creationDate");
                Date estimatedArrival = rs.getDate("estimatedArrival");
                String transactionStatus = rs.getString("transactionStatus");
                int sourceTransactionID = rs.getInt("sourceTransactionID");
                String notes = rs.getString("notes");

                Order loc = new Order(transactionID, transactionType, originalLocationID, creationDate, estimatedArrival, transactionStatus, sourceTransactionID, notes);
                cList.add(loc);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }

    public static boolean updateOrd(Order item) {
        boolean res = true;
        try {
            Boolean needsOrder = false;
            init();
            int id = item.getTransactionID();
            updateOrder.setInt(3, item.getTransactionID());
            updateOrder.setString(1, item.getTransactionStatus());
            updateOrder.setString(2, item.getNotes());

            updateOrder.executeUpdate();

            if (item.getTransactionStatus().equals("COMPLETE")) {
                createOrder.setString(1, item.getOriginalLocationID());
                createOrder.execute();
                needsOrder = false;
            }
            if (item.getTransactionStatus().equals("READY")) {
                getOrderItems.setInt(1, id);
                ResultSet r2 = getOrderItems.executeQuery();

                while (r2.next()) {
                    int itemID = r2.getInt("itemID");
                    int quantity = r2.getInt("quantity");

                    getWarehouse.setInt(1, itemID);
                    ResultSet r3 = getWarehouse.executeQuery();

                    while (r3.next()) {
                        int wareQuant = r3.getInt("quantity");
                        if (wareQuant < quantity) {
                            needsOrder = true;
                            doBackorder.setInt(1, id);
                            doBackorder.setInt(2, itemID);
                            doBackorder.execute();

                        }
                    }
                }
                if (needsOrder = true) {
                    createOrder.setString(1, "WARE");
                    createOrder.execute();
                }
            }

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }

    public static boolean createOrd(String location, String ftn) {
        Boolean res = false;
        try {
            init();
            if (ftn.equals("reg")) {
                createOrder.setString(1, location);
                createOrder.execute();
            } else {
                createEmergency.setString(1, location);
                createEmergency.execute();
            }
        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }

    public static boolean emptyLoss(String locationID) {
        Boolean res = false;
        try {
            init();
            doEmptyLoss.setString(1, locationID);
            res = doEmptyLoss.execute();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }

    public static boolean fillLoss(int itemID, String notes, int amount, String locationID) {
        Boolean res = false;
        try {
            init();
            fillLoss.setInt(1, itemID);
            fillLoss.setString(2, notes);
            fillLoss.setInt(3, amount);
            fillLoss.setString(4, locationID);
            fillLoss.execute();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }
    
    public static boolean createReturn(String location, int item, int amount, String type) {
        Boolean red = false;
        try {
            init();
            processReturns.setString(1, location);
            processReturns.setInt(2, item);
            processReturns.setInt(3, amount);
            processReturns.setString(4, type);
            red = processReturns.execute();

        } catch (SQLException ex) {
            red = false;
        }
        
        return red;
    }
    
    public static boolean checkWarehouse(int orderID) {
        Boolean red = true;
        try {
            init();
            checkWarehouse.setInt(1, orderID);
            red = checkWarehouse.execute();
        } catch(SQLException ex) {
            red = false;
        }
        return red;
    }
}
