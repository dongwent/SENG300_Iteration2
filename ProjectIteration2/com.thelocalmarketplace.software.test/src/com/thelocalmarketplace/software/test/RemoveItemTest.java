package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.RemoveItem;
import com.thelocalmarketplace.software.StartSession;
import com.thelocalmarketplace.software.WeightDiscrepency;

import powerutility.PowerGrid;

public class RemoveItemTest {
	
	private static Barcode barcodeOne;
	private static BarcodedProduct productOne;
	private static BarcodedItem itemOne;
	
	private static Barcode barcodeTwo;
	private static BarcodedProduct productTwo;
	private static BarcodedItem itemTwo;
	
	private static PriceLookUpCode pluThree;
	private static PLUCodedProduct productThree;
	private static PLUCodedItem itemThree;
	
	private static double d1 = 72.0;
	private static double d2 = 50.0;
	
	private static Mass m3 = Mass.ONE_GRAM;
	
	private static long l1 = 8;
	private static long l2 = 10;
	
	private static long l3 = 1000;
	
	
	private static AbstractSelfCheckoutStation checkoutStation;
	
	/**
	 * Initialize objects required for test
	 */
	@BeforeClass
	public static void initialSetUp(){
		barcodeOne = new Barcode(new Numeral[] {Numeral.one});
		productOne = new BarcodedProduct(barcodeOne, "Barcoded product", l1, d1);
		itemOne = new BarcodedItem(barcodeOne, new Mass(d1));
		
		barcodeTwo = new Barcode(new Numeral[] {Numeral.two});
		productTwo = new BarcodedProduct(barcodeTwo, "Another barcoded product", l2, d2);
		itemTwo = new BarcodedItem(barcodeTwo, new Mass(d2));
		
		pluThree = new PriceLookUpCode("1234");
		productThree = new PLUCodedProduct(pluThree, "PLUCoded product", l3);
		itemThree = new PLUCodedItem(pluThree, m3);
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeOne, productOne);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeTwo, productTwo);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluThree, productThree);
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		checkoutStation = new SelfCheckoutStationGold();
		
		checkoutStation.plugIn(PowerGrid.instance());
		checkoutStation.turnOn();		
		
	}
	
	/**
	 * Reset variables
	 * @throws Exception 
	 */
	@Before
	public void setUp() throws Exception {
		StartSession.getShoppingCart().clear();
		
		StartSession.startSession(checkoutStation);
		StartSession.getStation().baggingArea.enable();
	}
	
	@Test
	public void removeItemTest() {
		Mass tempMass;
		long tempPrice;
		
		StartSession.getStation().baggingArea.addAnItem(itemOne);
		StartSession.getShoppingCart().add(itemOne);
		
		StartSession.updateExpectedMass(StartSession.getExpectedMass().sum(itemOne.getMass()));
		StartSession.updateExpectedPrice(StartSession.getExpectedPrice() + ProductDatabases.BARCODED_PRODUCT_DATABASE.get(itemOne.getBarcode()).getPrice());
		
		tempMass = StartSession.getExpectedMass();
		tempPrice = StartSession.getExpectedPrice();
		
		StartSession.getStation().baggingArea.addAnItem(itemTwo);
		StartSession.getShoppingCart().add(itemTwo);
		
		StartSession.updateExpectedMass(StartSession.getExpectedMass().sum(itemTwo.getMass()));
		StartSession.updateExpectedPrice(StartSession.getExpectedPrice() + ProductDatabases.BARCODED_PRODUCT_DATABASE.get(itemTwo.getBarcode()).getPrice());
		
		RemoveItem.removeItem(itemTwo);
		StartSession.getStation().baggingArea.removeAnItem(itemTwo);
		
		assertTrue(StartSession.getExpectedMass().compareTo(tempMass) == 0 & (StartSession.getExpectedPrice() == tempPrice));		
		
	}
}
