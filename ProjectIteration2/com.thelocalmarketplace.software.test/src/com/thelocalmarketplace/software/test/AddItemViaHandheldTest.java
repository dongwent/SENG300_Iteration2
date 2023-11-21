package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertTrue;

/**
 * Test class for the AddItemViaHandheld use case.
 */
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
import com.thelocalmarketplace.software.AddItemViaHandheld;
import com.thelocalmarketplace.software.StartSession;

import powerutility.PowerGrid;

/**
 * Test class for the AddItemViaHandheld use case.
 */
public class AddItemViaHandheldTest {

    // Test data
    private static Barcode barcodeOne;
    private static BarcodedProduct productOne;
    private static BarcodedItem itemOne;

    // Test objects
    private static AbstractSelfCheckoutStation checkoutStation;
    private static AddItemViaHandheld handheldScannerControl;

    /**
     * Set up initial conditions for the test class.
     */
    @BeforeClass
    public static void initialSetUp() {
        // Initialize test data
        barcodeOne = new Barcode(new Numeral[] { Numeral.one });
        productOne = new BarcodedProduct(barcodeOne, "The first test product", 1, 72.0);
        itemOne = new BarcodedItem(barcodeOne, new Mass(72.0));

        // Set up the self-checkout station
        AbstractSelfCheckoutStation.resetConfigurationToDefaults();
        checkoutStation = new SelfCheckoutStationGold();
        handheldScannerControl = new AddItemViaHandheld();
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeOne, productOne);

        // Plug in and turn on the station
        checkoutStation.plugIn(PowerGrid.instance());
        checkoutStation.turnOn();
        checkoutStation.handheldScanner.register(handheldScannerControl);
    }

    /**
     * Set up before each test.
     */
    @Before
    public void setUp() throws Exception {
        // Reset the state before each test
        StartSession.updateExpectedMass(Mass.ZERO);
        StartSession.startSession(checkoutStation);
        checkoutStation.handheldScanner.enable();  // Ensure the scanner is enabled
    }

    /**
     * Clean up after each test.
     */
    @After
    public void tearDown() {
        // Clean up after each test
        StartSession.endSession(checkoutStation);
    }

    /**
     * Test whether the expected mass is updated when an item is scanned.
     */
    @Test
    public void addProductTest() {
        checkoutStation.handheldScanner.scan(itemOne);
        assertTrue(StartSession.getExpectedMass().compareTo(itemOne.getMass()) == 0);
    }

    /**
     * Test whether the expected mass is not updated when the scanner is disabled.
     */
    @Test
    public void disabledTest() {
        checkoutStation.handheldScanner.disable();
        checkoutStation.handheldScanner.scan(itemOne);
        assertTrue(StartSession.getExpectedMass().compareTo(Mass.ZERO) == 0);
    }
}