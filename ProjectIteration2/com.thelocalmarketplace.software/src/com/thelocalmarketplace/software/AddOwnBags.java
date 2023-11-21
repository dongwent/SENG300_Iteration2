// Author : Mohammad Soomro

package com.thelocalmarketplace.software;

import com.jjjwelectronics.*;
import com.thelocalmarketplace.hardware.*;
import com.tdc.*;
import com.thelocalmarketplace.software.StartSession;
import com.jjjwelectronics.scale.*;
import com.jjjwelectronics.*;

/**
 * A class that allows the user to add a bag object
 * 
 * @author Dongwen Tian
 *
 */
public class AddOwnBags extends Item {

    private static Mass maxBagMass;
    
    private long price;

    public AddOwnBags(Mass mass) {
        super(mass);
        price = 0;
    }
    
    public long getPrice() {
    	return price;
    }
    
    public static void setMaxBagMass(Mass max) {
    	maxBagMass = max;
    }

    public void addOwnBag() {
        if (maxBagMass.compareTo(this.getMass()) >= 0) {
            StartSession.getShoppingCart().add(this);
            StartSession.updateExpectedMass(StartSession.getExpectedMass().sum(this.getMass()));

        }
    }
}
