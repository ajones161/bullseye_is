/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author ajone
 */
public class Perms {
    private String actionID;
    private String roleID;
    
    public Perms(String actionID){
        this.actionID = actionID;
    }
    
    public Perms(String perms, String roleID){
        this.actionID = perms;
        this.roleID = roleID;
    }
    
    public String getPerms() {
        return actionID;
    }
    
    public String getRoleID() {
        return roleID;
    }
}
