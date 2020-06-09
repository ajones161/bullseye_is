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
public class Supplier {
    private int supplierID;
    private String companyName;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String country; 
    private String email;
    private String phone;
    private String contactPerson;
    private String notes;
    private Boolean active;
    
    public Supplier(int supplierID, String companyName) {
        this.supplierID = supplierID;
        this.companyName = companyName;
    }
    
    public int getSupplierID(){
        return supplierID;
    }
    
    public String getCompanyName() {
        return companyName;
    }
}
