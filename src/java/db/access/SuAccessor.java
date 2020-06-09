/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access;

import entities.Supplier;
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
public class SuAccessor {

    private static Connection conn = null;
    private static PreparedStatement selectAllStatement = null;
    private static PreparedStatement selectAllShort = null;

    private static void init() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bullseye?useSSL=false", "root", "root");
            selectAllStatement = conn.prepareStatement("select * from supplier");
            selectAllShort = conn.prepareStatement("select supplierID, companyName from supplier");
        }
    }

    public static List<Supplier> getAllShort() {
        List<Supplier> cList = new ArrayList();
        try {
            init();
            ResultSet rs = selectAllStatement.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("supplierID");
                String name = rs.getString("companyName");
                
                Supplier loc = new Supplier(id, name);
                cList.add(loc);
            }
        } catch (SQLException ex) {
            cList = new ArrayList();
        }

        return cList;
    }
}
