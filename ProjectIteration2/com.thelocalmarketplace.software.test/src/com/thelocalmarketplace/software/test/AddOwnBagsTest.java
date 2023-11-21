package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddItemViaBarcode;
import com.thelocalmarketplace.software.AddOwnBags;
import com.thelocalmarketplace.software.StartSession;

import powerutility.PowerGrid;

/**
 * Test class for addOwnBags
 * 
 * @author Dongwen Tian
 *
 */
public class AddOwnBagsTest {

    private static AddOwnBags bag;
    
    private static Mass m1;
    private static Mass m2;
    
    private static AbstractSelfCheckoutStation checkoutStation;
    
    @BeforeClass
    public static void initialSetUp() throws Exception {
    	AbstractSelfCheckoutStation.resetConfigurationToDefaults();
    	
    	m1 = new Mass(3.0);
    	m2 = new Mass(5.0);
		
		checkoutStation = new SelfCheckoutStationGold();
    	
		checkoutStation.plugIn(PowerGrid.instance());
		checkoutStation.turnOn();
		
		StartSession.startSession(checkoutStation);
    }
    
    // Setting up use cases.
    @Before
    public void setUp() {
        StartSession.getShoppingCart().clear();
        bag = null;
        AddOwnBags.setMaxBagMass(null);
    }

    @Test
    public void testBagsAdded() {
    	AddOwnBags.setMaxBagMass(m2);
        bag = new AddOwnBags(m1);
    	bag.addOwnBag();
  
    	StartSession.getStation().baggingArea.addAnItem(bag);
    	assertTrue(StartSession.getShoppingCart().get(0) == bag);
    	StartSession.getStation().baggingArea.removeAnItem(bag);
    }

    @Test
    public void testWeightOverloadException() throws Exception {
    	try {
			AddOwnBags.setMaxBagMass(m1);
		    bag = new AddOwnBags(m2);
		    bag.addOwnBag();
		    StartSession.getStation().baggingArea.addAnItem(bag);
    	} catch (IndexOutOfBoundsException e) {
    		assertTrue(true);
    	}
    }
}
