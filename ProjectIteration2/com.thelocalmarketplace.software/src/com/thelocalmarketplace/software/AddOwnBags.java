package com.thelocalmarketplace.software;

import com.jjjwelectronics.*;
import com.thelocalmarketplace.hardware.*;
import com.tdc.*;
import com.thelocalmarketplace.software.StartSession;
import com.jjjwelectronics.*;

public class AddOwnBags extends Item {

    Mass expectedbagWeight;

    protected AddOwnBags(Mass mass) {
        super(mass);
        // TODO Auto-generated constructor stub
    }

    public void addownbags(AddOwnBags bag) {
        StartSession.getExpectedMass().sum(expectedbagWeight);
        StartSession.updateExpectedMass(StartSession.getExpectedMass().sum(expectedbagWeight));
        if (StartSession.getStation().baggingArea.getMassLimit().compareTo(StartSession.getExpectedMass()) > 0) {
            getMass();
        }
    }
}
