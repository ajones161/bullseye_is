/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.sql.Date;

/**
 *
 * @author ajone
 */
public class Delivery {
    private int deliveryID;
    private String courierName;
    private String address;
    private String routeID;
    private double distance;
    private String vehicleID;
    private Date placementDay;
    private Date deliveryDay;
    private String orderStatus;
    
    public Delivery(int deliveryID, String courierName, String address, String routeID, double distance, String vehicleID, Date placementDay, Date deliveryDay, String orderStatus) {
        this.deliveryID = deliveryID;
        this.courierName = courierName;
        this.address = address;
        this.routeID = routeID;
        this.distance = distance;
        this.vehicleID = vehicleID;
        this.placementDay = placementDay;
        this.deliveryDay = deliveryDay;
        this.orderStatus = orderStatus;
    }
    
    public int getDeliveryID() {
        return deliveryID;
    }
    
    public String getCourierName() {
        return courierName;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getRouteID() {
        return routeID;
    }
    
    public double getDistance() {
        return distance;
    }
    
    public String getVehicleID() {
        return vehicleID;
    }
    
    public Date getPlacementDay() {
        return placementDay;
    }
    
    public Date getDeliveryDay() {
        return deliveryDay;
    }
    
    public String getOrderStatus() {
        return orderStatus;
    }
}
