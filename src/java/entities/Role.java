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
public class Role {
    private String roleID;
    
    public Role(String roleID) {
        this.roleID = roleID;
    }
    
    public String getRole() {
        return roleID;
    }
}
