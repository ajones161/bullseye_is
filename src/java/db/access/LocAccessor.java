/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access;

import entities.Location;
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
public class LocAccessor {

    private static Connection conn = null;
    private static PreparedStatement selectAllStatement = null;
    private static PreparedStatement sortByAddress = null;
    private static PreparedStatement sortByCity = null;
    private static PreparedStatement sortByLocationTypeID = null;
    private static PreparedStatement selectLocationID = null;
    private static PreparedStatement insertStatement = null;
    private static PreparedStatement updateStatement = null;

    private static void init() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bullseye?useSSL=false", "root", "root");

            selectAllStatement = conn.prepareStatement("select * from location");

            sortByAddress = conn.prepareStatement("select * from location ORDER BY location.address");
            sortByCity = conn.prepareStatement("select * from location ORDER BY location.city");
            sortByLocationTypeID = conn.prepareStatement("select * from location ORDER BY location.locationTypeID");

            selectLocationID = conn.prepareStatement("select * from location where locationID = ?");

            insertStatement = conn.prepareStatement("insert INTO location values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            updateStatement = conn.prepareStatement("update location "
                    + "set locationID = ?, description = ?, "
                    + "address = ?, city = ?, "
                    + "province = ?, postalCode = ?,"
                    + "country = ?, locationTypeID = ?,"
                    + "deliveryDay = ?, active = ? "
                    + "where locationID = ?");

        }
    }

    public static List<Location> getAllLoc() {
        List<Location> cList = new ArrayList();
        try {
            init();

            ResultSet rs = selectAllStatement.executeQuery();
            while (rs.next()) {

                String id = rs.getString("locationID");
                String description = rs.getString("description");
                String add = rs.getString("address");
                String city = rs.getString("city");
                String prov = rs.getString("province");
                String post = rs.getString("postalCode");
                String coun = rs.getString("country");
                String email = rs.getString("locationTypeID");
                String deliveryDay = rs.getString("deliveryDay");
                Boolean act = rs.getBoolean("active");

                if (!(deliveryDay == null || "".equals(deliveryDay))) {
                } else {
                    deliveryDay = " ";
                }

                Location loc = new Location(id, description, add, city, prov, post, coun, email, deliveryDay, act);

                cList.add(loc);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }

    public static Location getSpecific(String locID) throws SQLException {
        Location loc = null;
        try {
            selectLocationID.setString(1, locID);
            ResultSet rs = selectLocationID.executeQuery();
            while (rs.next()) {
            String id = rs.getString("locationID");
            String description = rs.getString("description");
            String add = rs.getString("address");
            String city = rs.getString("city");
            String prov = rs.getString("province");
            String post = rs.getString("postalCode");
            String coun = rs.getString("country");
            String email = rs.getString("locationTypeID");
            String deliveryDay = rs.getString("deliveryDay");
            Boolean act = rs.getBoolean("active");

            loc = new Location(id, description, add, city, prov, post, coun, email, deliveryDay, act);
            }

        } catch (SQLException ex) {
            loc = null;
        }
        return loc;
    }

    public static List<Location> sortLoc(String sortBy) {
        List<Location> cList = new ArrayList();
        try {
            init();

            ResultSet rs = null;
            switch (sortBy) {
                case "Address":
                    rs = sortByAddress.executeQuery();
                    break;
                case "City":
                    rs = sortByCity.executeQuery();
                    break;
                case "Type":
                    rs = sortByLocationTypeID.executeQuery();
                    break;
                default:
                    rs = selectAllStatement.executeQuery();
                    break;
            }

            while (rs.next()) {
                String id = rs.getString("locationID");
                String description = rs.getString("description");
                String add = rs.getString("address");
                String city = rs.getString("city");
                String prov = rs.getString("province");
                String post = rs.getString("postalCode");
                String coun = rs.getString("country");
                String email = rs.getString("locationTypeID");
                String deliveryDay = rs.getString("deliveryDay");
                Boolean act = rs.getBoolean("active");

                if (!(deliveryDay == null || "".equals(deliveryDay))) {
                } else {
                    deliveryDay = " ";
                }

                Location loc = new Location(id, description, add, city, prov, post, coun, email, deliveryDay, act);

                cList.add(loc);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }

    public static List<Location> getID() {
        List<Location> cList = new ArrayList();
        try {
            init();
            ResultSet rs = selectAllStatement.executeQuery();
            while (rs.next()) {

                String id = rs.getString("locationID");
                Location loc = new Location(id);
                cList.add(loc);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }

    public static boolean addLoc(Location item) {
        boolean res = true;
        try {
            init();

            String deliveryDay = null;
            if (!"".equals(item.getDeliveryDay())) {
                deliveryDay = item.getDeliveryDay();
            }
            insertStatement.setString(1, item.getLocationID());
            insertStatement.setString(2, item.getDescription());
            insertStatement.setString(3, item.getAddress());
            insertStatement.setString(4, item.getCity());
            insertStatement.setString(5, item.getProvince());
            insertStatement.setString(6, item.getPostalCode());
            insertStatement.setString(7, item.getCountry());
            insertStatement.setString(8, item.getLocationtypeID());
            insertStatement.setString(9, deliveryDay);
            insertStatement.setBoolean(10, item.getActive());
            int rowCount = insertStatement.executeUpdate();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }

    public static boolean updateLoc(Location item) {
        boolean res = true;

        try {
            init();
            updateStatement.setString(1, item.getLocationID());
            updateStatement.setString(2, item.getDescription());
            updateStatement.setString(3, item.getAddress());
            updateStatement.setString(4, item.getCity());
            updateStatement.setString(5, item.getProvince());
            updateStatement.setString(6, item.getPostalCode());
            updateStatement.setString(7, item.getCountry());
            updateStatement.setString(8, item.getLocationtypeID());
            updateStatement.setString(9, item.getDeliveryDay());
            updateStatement.setBoolean(10, item.getActive());
            updateStatement.setString(11, item.getLocationID());
            int rowCount = updateStatement.executeUpdate();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }
}
