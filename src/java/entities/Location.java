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
public class Location {
    private String locationID;
    private String description;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String locationTypeID;
    private String deliveryDay;
    private Boolean active;
    
    public Location(String locationID, String description, String address, String city, String province, String postalCode,
            String country, String locationTypeID, String deliveryDay, Boolean active) {
        this.locationID = locationID;
        this.description = description;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
        this.locationTypeID = locationTypeID;
        this.deliveryDay = deliveryDay;
        this.active = active;
    }
    
    public Location(String locationID) {
        this.locationID = locationID;
    }
    
    public String getLocationID() {
        return locationID;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getProvince() {
        return province;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public String getLocationtypeID() {
        return locationTypeID;
    }
    
    public String getDeliveryDay() {
        return deliveryDay;
    }
    
    public Boolean getActive() {
        return active;
    }
}
