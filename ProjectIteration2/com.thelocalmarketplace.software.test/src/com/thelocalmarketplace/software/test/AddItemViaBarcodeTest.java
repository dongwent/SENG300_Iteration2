package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertTrue;

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
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddItemViaBarcode;
import com.thelocalmarketplace.software.StartSession;

import powerutility.PowerGrid;

/**
 * This is the test class for the addItemViaBarcode use case.
 * 
 * @author Dongwen Tian
 *
 */
public class AddItemViaBarcodeTest {
	
	private static Barcode barcodeOne;
	private static BarcodedProduct productOne;
	private static BarcodedItem itemOne;
	
	private static double d1 = 72.0;
	
	private static AbstractSelfCheckoutStation checkoutStation;
	
	private static AddItemViaBarcode mainScannerControl;
	

	/**
	 * Initialize objects required for test
	 */
	@BeforeClass
	public static void initialSetUp() {
		barcodeOne = new Barcode(new Numeral[] {Numeral.one});
		productOne = new BarcodedProduct(barcodeOne, "The first test product", 1, d1);
		itemOne = new BarcodedItem(barcodeOne, new Mass(d1));
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		checkoutStation = new SelfCheckoutStationGold();
		mainScannerControl = new AddItemViaBarcode();
		
		checkoutStation.plugIn(PowerGrid.instance());
		checkoutStation.turnOn();
		checkoutStation.mainScanner.register(mainScannerControl);
	}
	
	/**
	 * Starts the session and resets the variables
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		StartSession.updateExpectedMass(Mass.ZERO);
		StartSession.startSession(checkoutStation);
	}
	
	/**
	 * Ends the current session
	 */
	@After
	public void tearDown() {
		StartSession.endSession(checkoutStation);
	}
	
	/**
	 * Tests whether the expected mass is updated when an item is scanned
	 */
	@Test
	public void AddProductTest() {
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeOne, productOne);
		StartSession.getStation().mainScanner.scan(itemOne);
		assertTrue(StartSession.getExpectedMass().compareTo(itemOne.getMass()) == 0);
	}
	
	/**
	 * Tests whether the expected mass is updated when the scanner is disabled
	 */
	@Test
	public void disabledTest() {
		StartSession.getStation().mainScanner.disable();
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeOne, productOne);
		StartSession.getStation().mainScanner.scan(itemOne);
		assertTrue(StartSession.getExpectedMass().compareTo(Mass.ZERO) == 0);
	}
	
}
