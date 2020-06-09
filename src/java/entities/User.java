/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author ajone
 */
public class User {
    private String userID;
    private String password;
    private String locationID;
    private String roleID;
    private Boolean active;
    
    public User(String userID, String locationID, String roleID, Boolean active) {
        this.userID = userID;
        this.locationID = locationID;
        this.roleID = roleID;
        this.active = active;
    }
    
    public User(String userID, String password, String locationID, String roleID, Boolean active) throws NoSuchAlgorithmException {
        this.userID = userID;
        this.password = password;
        this.locationID = locationID;
        this.roleID = roleID;
        this.active = active;
    }
    
    public String getUserID() {
        return userID;
    }
    
    public String getPassword() throws NoSuchAlgorithmException {
        return setPassword(password);
    }
    
    private String setPassword(String pass) throws NoSuchAlgorithmException {
        String hashed;
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(pass.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        hashed = sb.toString();
        return hashed;
    }
    
    public String getLocationID() {
        return locationID;
    }
    
    public String getRoleID() {
        return roleID;
    }
    
    public Boolean getActive() {
        return active;
    }
}
