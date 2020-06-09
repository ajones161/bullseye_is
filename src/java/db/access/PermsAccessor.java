/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access;

import entities.Perms;
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
public class PermsAccessor {

    private static Connection conn = null;
    private static PreparedStatement selectAllStatement = null;
    private static PreparedStatement selectSpecific = null;

    private static void init() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bullseye?useSSL=false", "root", "root");
            selectAllStatement = conn.prepareStatement("SELECT distinct actionID FROM bullseye.permission");
            selectSpecific = conn.prepareStatement("SELECT * FROM bullseye.permission where roleID = ?");
        }
    }

    public static List<Perms> getAll() {
        List<Perms> cList = new ArrayList();
        try {
            init();
            ResultSet rs = selectAllStatement.executeQuery();
            while (rs.next()) {

                String id = rs.getString("actionID");
                Perms loc = new Perms(id);
                cList.add(loc);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }

    public static List<Perms> getSpecific(String id) {
        List<Perms> perm = new ArrayList();
        try {
            init();
            
            selectSpecific.setString(1, id);
            
            ResultSet rs = selectSpecific.executeQuery();
            
            while (rs.next()) {

                String userID = rs.getString("actionID");
                String role = rs.getString("roleID");
                Perms item = new Perms(userID, role);
                
                perm.add(item);
            }

        } catch (SQLException ex) {
            perm = new ArrayList();
        }

        return perm;
    }
}
