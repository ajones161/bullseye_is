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
public class LType {
    private String locationTypeID;
    
    public LType(String locationTypeID) {
        this.locationTypeID = locationTypeID;
    }
    
    public String getLocationTypeID() {
        return locationTypeID;
    }
}
