/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access;

import entities.User;
import java.security.NoSuchAlgorithmException;
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
public class UserAccessor {

    private static Connection conn = null;
    private static PreparedStatement selectAllStatement = null;
    private static PreparedStatement sortByLocation = null;
    private static PreparedStatement sortByRole = null;
    private static PreparedStatement insertStatement = null;
    private static PreparedStatement updateStatement = null;
    private static PreparedStatement updateNoPassStatement = null;

    private static void init() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bullseye?useSSL=false", "root", "root");

            selectAllStatement = conn.prepareStatement("select * from user");

            sortByLocation = conn.prepareStatement("select * from user ORDER BY user.locationID");
            sortByRole = conn.prepareStatement("select * from user ORDER BY user.roleID");

            insertStatement = conn.prepareStatement("insert INTO user values (?, ?, ?, ?, ?)");
            updateStatement = conn.prepareStatement("update user "
                    + "set userID = ?, password = ?, "
                    + "locationID = ?, roleID = ?, "
                    + "active = ? "
                    + "where userID = ?");
            updateNoPassStatement = conn.prepareStatement("update user "
                    + "set userID = ?, "
                    + "locationID = ?, roleID = ?, "
                    + "active = ? "
                    + "where userID = ?");
        }
    }

    public static List<User> getAllUsers() {
        List<User> cList = new ArrayList();
        try {
            init();

            ResultSet rs = selectAllStatement.executeQuery();
            while (rs.next()) {

                String id = rs.getString("userID");
                String loc = rs.getString("locationID");
                String role = rs.getString("roleID");
                Boolean active = rs.getBoolean("active");

                User user = new User(id, loc, role, active);
                cList.add(user);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }

    public static List<User> sortUsers(String sortBy) {
        List<User> cList = new ArrayList();
        try {
            init();

            ResultSet rs = null;
            switch (sortBy) {
                case "Location":
                    rs = sortByLocation.executeQuery();
                    break;
                case "Role":
                    rs = sortByRole.executeQuery();
                    break;
                default:
                    rs = selectAllStatement.executeQuery();
                    break;
            }

            while (rs.next()) {

                String id = rs.getString("userID");
                String loc = rs.getString("locationID");
                String role = rs.getString("roleID");
                Boolean active = rs.getBoolean("active");

                User user = new User(id, loc, role, active);
                cList.add(user);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }
    
       public static boolean addUser(User item) throws NoSuchAlgorithmException {
        boolean res = true;
        try {
            init();

            insertStatement.setString(1, item.getUserID());
            insertStatement.setString(2, item.getPassword());
            insertStatement.setString(3, item.getLocationID());
            insertStatement.setString(4, item.getRoleID());
            insertStatement.setBoolean(5, item.getActive());
            int rowCount = insertStatement.executeUpdate();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }

    public static boolean updateUser(User item) throws NoSuchAlgorithmException {
        boolean res = true;

        try {
            init();
            updateStatement.setString(1, item.getUserID());
            updateStatement.setString(2, item.getPassword());
            updateStatement.setString(3, item.getLocationID());
            updateStatement.setString(4, item.getRoleID());
            updateStatement.setBoolean(5, item.getActive());
            updateStatement.setString(6, item.getUserID());
            int rowCount = updateStatement.executeUpdate();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }
    
    public static boolean updateNoPassUser(User item) {
        boolean res = true;

        try {
            init();
            updateNoPassStatement.setString(1, item.getUserID());
            updateNoPassStatement.setString(2, item.getLocationID());
            updateNoPassStatement.setString(3, item.getRoleID());
            updateNoPassStatement.setBoolean(4, item.getActive());
            updateNoPassStatement.setString(5, item.getUserID());
            int rowCount = updateNoPassStatement.executeUpdate();

        } catch (SQLException ex) {
            res = false;
        }
        return res;
    }
}
