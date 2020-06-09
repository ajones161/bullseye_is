/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access;

import entities.Delivery;
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
public class DelAccessor {

    private static Connection conn = null;
    private static PreparedStatement selectAllStatement = null;
    private static CallableStatement deliveryDate = null;
    private static CallableStatement loadOrder = null;

    private static void init() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bullseye?useSSL=false", "root", "root");
            selectAllStatement = conn.prepareStatement("SELECT dt.deliveryID, c.courierName, \n"
                    + "concat(l.description, '<br>', l.address, '<br>', l.city, ', ', l.province) AS destination, \n"
                    + "dt.routeID, r.distance, d.vehicleID, d.dateTime as deliveryDay, tr.creationDate,\n"
                    + "tr.transactionStatus as orderStatus\n"
                    + "FROM deliverytransaction dt "
                    + "JOIN delivery d ON dt.deliveryID = d.deliveryID\n"
                    + "JOIN courier c ON c.courierID = d.courierID\n"
                    + "JOIN location l ON l.locationID IN (select originalLocationID from transaction where transactionID = dt.transactionID)\n"
                    + "JOIN route r ON r.routeID IN (select routeID from deliverytransaction where dt.transactionID = transactionID)\n"
                    + "JOIN transaction tr ON tr.transactionID = dt.transactionID\n"
                    + "ORDER BY deliveryDay desc");
            deliveryDate = conn.prepareCall("call setArrival(?)");
            loadOrder = conn.prepareCall("call loadOrder(?)");
        }
    }

    public static List<Delivery> getAll() {
        List<Delivery> cList = new ArrayList();
        try {
            init();
            ResultSet rs = selectAllStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("deliveryID");
                Date deliveryDay = rs.getDate("deliveryDay");
                String courier = rs.getString("courierName");
                String address = rs.getString("destination");
                String route = rs.getString("routeID");
                double distance = rs.getDouble("distance");
                String vehicle = rs.getString("vehicleID");
                Date placementDay = rs.getDate("creationDate");

                String orderStatus = rs.getString("orderStatus");

                Delivery loc = new Delivery(id, courier, address, route, distance, vehicle, placementDay, deliveryDay, orderStatus);
                cList.add(loc);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }

    public static Boolean loadOrder(int delID) {
        Boolean resp = false;
        try {
            init();
            loadOrder.setInt(1, delID);
            loadOrder.execute();
            resp = true;
        } catch (SQLException ex) {
            resp = false;
        }
        return resp;
    }
}
