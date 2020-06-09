/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access;

import entities.Courier;
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
public class CourierAccessor {

    private static Connection conn = null;
    private static PreparedStatement selectAllStatement = null;
    private static PreparedStatement sortByName = null;
    private static PreparedStatement sortByAddress = null;
    private static PreparedStatement sortByEmail = null;
    private static PreparedStatement sortByPhone = null;
     private static PreparedStatement insertStatement = null;
    private static PreparedStatement updateStatement = null;

    private static void init() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bullseye?useSSL=false", "root", "root");
            
            selectAllStatement = conn.prepareStatement("select * from courier");
            
            sortByName = conn.prepareStatement("select * from courier ORDER BY courier.courierName");
            sortByAddress = conn.prepareStatement("select * from courier ORDER BY courier.address");
            sortByEmail = conn.prepareStatement("select * from courier ORDER BY courier.courierEmail");
            sortByPhone = conn.prepareStatement("select * from courier ORDER BY courier.courierPhone");
            insertStatement = conn.prepareStatement("insert INTO courier values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            updateStatement = conn.prepareStatement("update courier "
                    + "set courierID = ?, courierName = ?, "
                    + "address = ?, city = ?, "
                    + "provinceID = ?, postalCode = ?,"
                    + "country = ?, courierEmail = ?,"
                    + "courierPhone = ?, notes = ?, "
                    + "active = ? "
                    + "where courierID = ?");
        }
    }
    
    public static List<Courier> getAllCouriers() {
        List<Courier> cList = new ArrayList();
        try {
            init();

            ResultSet rs = selectAllStatement.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("courierID");
                String name = rs.getString("courierName");
                String add = rs.getString("address");
                String city = rs.getString("city");
                String prov = rs.getString("provinceID");
                String post = rs.getString("postalCode");
                String coun = rs.getString("country");
                String email = rs.getString("courierEmail");
                String phone = rs.getString("courierPhone");
                String note = rs.getString("notes");
                Boolean act = rs.getBoolean("active");

                Courier cour = new Courier(id, name, add, city, prov, post, coun, email, phone, note, act);

                cList.add(cour);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }
    
    public static List<Courier> sortCour(String sortBy) {
        List<Courier> cList = new ArrayList();
        try {
            init();

            ResultSet rs = null;
            switch (sortBy) {
                case "Company":
                    rs = sortByName.executeQuery();
                    break;
                case "Address":
                    rs = sortByAddress.executeQuery();
                    break;
                case "Email":
                    rs = sortByEmail.executeQuery();
                    break;
                case "Phone":
                    rs = sortByPhone.executeQuery();
                    break;
                default:
                    rs = selectAllStatement.executeQuery();
                    break;
            }
            
            while (rs.next()) {

                int id = rs.getInt("courierID");
                String name = rs.getString("courierName");
                String add = rs.getString("address");
                String city = rs.getString("city");
                String prov = rs.getString("provinceID");
                String post = rs.getString("postalCode");
                String coun = rs.getString("country");
                String email = rs.getString("courierEmail");
                String phone = rs.getString("courierPhone");
                String note = rs.getString("notes");
                Boolean act = rs.getBoolean("active");

                Courier cour = new Courier(id, name, add, city, prov, post, coun, email, phone, note, act);

                cList.add(cour);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }
    
       public static boolean addItm(Courier item) {
        boolean res = true;
        int rowCount;
        try {
            init();

            insertStatement.setInt(1, item.getCourierID());
            insertStatement.setString(2, item.getCourierName());
            insertStatement.setString(3, item.getAddress());
            insertStatement.setString(4, item.getCity());
            insertStatement.setString(5, item.getProvinceID());
            insertStatement.setString(6, item.getPostalCode());
            insertStatement.setString(7, item.getCountry());
            insertStatement.setString(8, item.getCourierEmail());
            insertStatement.setString(9, item.getCourierPhone());
            insertStatement.setString(10, item.getNotes());
            insertStatement.setBoolean(11, item.getActive());
            
            rowCount = insertStatement.executeUpdate();
            out.println(rowCount);

        } catch (SQLException ex) {
            res = false;
        }
        
        return res;
    }

    public static boolean updateItm(Courier item) {
        boolean res = true;

        try {
            init();
            updateStatement.setInt(1, item.getCourierID());
            updateStatement.setString(2, item.getCourierName());
            updateStatement.setString(3, item.getAddress());
            updateStatement.setString(4, item.getCity());
            updateStatement.setString(5, item.getProvinceID());
            updateStatement.setString(6, item.getPostalCode());
            updateStatement.setString(7, item.getCountry());
            updateStatement.setString(8, item.getCourierEmail());
            updateStatement.setString(9, item.getCourierPhone());
            updateStatement.setString(10, item.getNotes());
            updateStatement.setBoolean(11, item.getActive());
            
            updateStatement.setInt(12, item.getCourierID());
            int rowCount = updateStatement.executeUpdate();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }
}
