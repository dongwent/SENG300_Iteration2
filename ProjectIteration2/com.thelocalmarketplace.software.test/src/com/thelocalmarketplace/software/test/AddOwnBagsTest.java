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
import com.thelocalmarketplace.software.StartSession;

import powerutility.PowerGrid;

public class AddOwnBagsTest extends AddItemViaBarcodeTest {

    AddOwnBagsTest useCase;
	Item bag;
	
	// Setting up use cases.
	@Before
	public void setUp() {
		useCase = new AddOwnBagsTest();
	}
	
    @Test
    public void testBagsAdded() {
        bag = new Item(34.0);
        useCase.WeightDiscrepancyTest.baggingAreaControl.add(bag);
        assertTrue("place bag in bagging area.", useCase.bagsAdded);
    }

    @Test 
    public void testWeightOverloadException() throws Exception {
        bag = new Bag(444444);
        useCase.WeightDiscrepancy.baggingAreaControl.add(bag);
        useCase.WeightDiscrepancy.baggingAreaControl.getExpectedWeight();


    }}




    