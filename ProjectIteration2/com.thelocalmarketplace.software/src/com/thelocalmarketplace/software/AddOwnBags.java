// Author : Mohammad Soomro

package com.thelocalmarketplace.software;

import com.jjjwelectronics.*;
import com.thelocalmarketplace.hardware.*;
import com.tdc.*;
import com.thelocalmarketplace.software.StartSession;
import com.jjjwelectronics.scale.*;
import com.jjjwelectronics.*;

public class AddOwnBags extends Item {

    Mass expectedbagWeight;

    protected AddOwnBags(Mass mass) {
        super(mass);
        // TODO Auto-generated constructor stub
    }

    public void addownbags(AddOwnBags bag) {
        if (expectedbagWeight.compareTo(this.getMass()) >= 0) {
            StartSession.getShoppingCart().add(bag);
            StartSession.updateExpectedWeight(StartSession.getExpectedMass().sum(this.getmass()));

        }
    }
}
