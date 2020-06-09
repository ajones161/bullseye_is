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
public class tItem extends InvItem{
    
    public tItem(int itemID, String locationID, int quantity, int reorderThreshold, int reorderLevel) {
        super(itemID, locationID, quantity, reorderThreshold, reorderLevel);
    }
    
}
