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
public class Province {
    private String provinceID;
    private String longName;
    
    public Province(String provinceID, String longName) {
        this.provinceID = provinceID;
        this.longName = longName;
    }
    
    public String getProvinceID() {
        return provinceID;
    }
    
    public String getLongName() {
        return longName;
    }
}
