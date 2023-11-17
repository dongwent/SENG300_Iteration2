package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.StartSession;
import com.thelocalmarketplace.software.WeightDiscrepency;

import powerutility.PowerGrid;

/**
 * This is the test class of the weightDiscrepancy use case.
 * 
 * @author Dongwen Tian
 *
 */
public class WeightDiscrepancyTest {

	private static Barcode barcodeOne;
	private static BarcodedProduct productOne;
	private static BarcodedItem itemOne;
	
	private static Barcode barcodeTwo;
	private static BarcodedProduct productTwo;
	private static BarcodedItem itemTwo;
	
	private static double d1 = 72.0;
	private static double d2 = 50.0;
	private static double d3 = 30.0;
	
	private static AbstractSelfCheckoutStation checkoutStation;
	
	private static WeightDiscrepency baggingAreaControl;
	
	/**
	 * Initialize objects required for test
	 */
	@BeforeClass
	public static void initialSetUp(){
		barcodeOne = new Barcode(new Numeral[] {Numeral.one});
		productOne = new BarcodedProduct(barcodeOne, "The first test product", 1, d1);
		itemOne = new BarcodedItem(barcodeOne, new Mass(d1));
		
		barcodeTwo = new Barcode(new Numeral[] {Numeral.two});
		productTwo = new BarcodedProduct(barcodeTwo, "The second test product", 1, d2);
		itemTwo = new BarcodedItem(barcodeTwo, new Mass(d3));
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		checkoutStation = new SelfCheckoutStationGold();
		baggingAreaControl = new WeightDiscrepency();
		
		checkoutStation.plugIn(PowerGrid.instance());
		checkoutStation.turnOn();
		checkoutStation.baggingArea.register(baggingAreaControl);
		
		
		
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
		StartSession.getStation().scanningArea.enable();
		StartSession.getStation().cardReader.enable();
		StartSession.getStation().mainScanner.enable();
		StartSession.getStation().handheldScanner.enable();
		StartSession.getStation().banknoteInput.enable();
		StartSession.getStation().coinSlot.enable();
	}
	
	/**
	 * Ends Session
	 */
	@After
	public void tearDown() {
		try {
			StartSession.getStation().baggingArea.removeAnItem(itemOne);
		} catch (Exception e) {}
		try {
			StartSession.getStation().baggingArea.removeAnItem(itemTwo);
		} catch (Exception e) {}
		StartSession.endSession(checkoutStation);
	}
	
	/**
	 * Test behavior when the expected mass is the same as the actual mass on scale.
	 * The scanner and coin slot should both remain enabled.
	 */
	@Test
	public void sameMassTest() {
		StartSession.updateExpectedMass(new Mass(productOne.getExpectedWeight()));
		StartSession.getStation().baggingArea.addAnItem(itemOne);
		assertTrue(!StartSession.getStation().coinSlot.isDisabled());
	}
	
	/**
	 * Test behavior when the expected mass is different from the actual mass on scale.
	 * the scanner and coin slot should both be disabled.
	 */
	@Test
	public void differentMassTest() {
		StartSession.updateExpectedMass(new Mass(productTwo.getExpectedWeight()));
		StartSession.getStation().baggingArea.addAnItem(itemTwo);
		assertTrue(StartSession.getStation().coinSlot.isDisabled());
	}
	
	/**
	 * Test behavior when the bagging area is disabled.
	 * The scanner and coin slot should both remain enabled.
	 */
	@Test
	public void disabledTest() {
		StartSession.getStation().baggingArea.disable();
		StartSession.updateExpectedMass(new Mass(productOne.getExpectedWeight()));
		StartSession.getStation().baggingArea.addAnItem(itemOne);
		assertTrue(!StartSession.getStation().coinSlot.isDisabled());
	}

}
