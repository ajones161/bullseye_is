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
public class Order {
    private int transactionID;
    private String transactionType;
    private String originalLocationID;
    private Date creationDate;
    private Date estimatedArrival;
    private String transactionStatus;
    private int sourceTransactionID;
    private String notes;

    public Order(int transactionID, String transactionType, String originalLocationID, Date creationDate, Date estimatedArrival, String transactionStatus, int sourceTransactionID, String notes) {
        this.transactionID = transactionID;
        this.transactionType = transactionType;
        this.originalLocationID = originalLocationID;
        this.creationDate = creationDate;
        this.estimatedArrival = estimatedArrival;
        this.transactionStatus = transactionStatus;
        this.sourceTransactionID = sourceTransactionID;
        this.notes = notes;
    }

    public Order(int transactionID, String notes) {
        this.transactionID = transactionID;
        this.notes = notes;
    }
    
    public Order(int transactionID, String originalLocationID, String transactionStatus, String notes) {
        this.transactionID = transactionID;
        this.originalLocationID = originalLocationID;
        this.transactionStatus = transactionStatus;
        this.notes = notes;
    }
    
    public int getTransactionID() {
        return transactionID;
    }
    
    public String getTransactionType() {
        return transactionType;
    }
    
    public String getOriginalLocationID() {
        return originalLocationID;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public Date getEstimatedArrival() {
        return estimatedArrival;
    }
    
    public String getTransactionStatus() {
        return transactionStatus;
    }
    
    public int getSourceTransactionID() {
        return sourceTransactionID;
    }
    
    public String getNotes() {
        return notes;
    }
}
