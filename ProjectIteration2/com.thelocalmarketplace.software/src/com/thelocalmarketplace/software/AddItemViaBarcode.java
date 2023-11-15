package com.thelocalmarketplace.software;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

/**
 * This class acts as the control software for the barcode scanner.
 * When a barcode has been scanned, updates the expected weight 
 * and prices of products in the order
 * 
 * @author Dongwen Tian
 *
 */
public class AddItemViaBarcode implements BarcodeScannerListener {
	
	/**
	 * This method is called when a barcode has been scanned.
	 * Updates the expected mass and price of items in the order according to information
	 * obtained from the barcoded product database.
	 */
	@Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
		if (!barcodeScanner.isDisabled() & StartSession.getInSession()) {
			StartSession.updateExpectedMass(StartSession.getExpectedMass().sum(new Mass(
					ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode).getExpectedWeight())));
			StartSession.updateExpectedPrice(StartSession.getExpectedPrice() + 
					ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode).getPrice());
		}
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}
	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {}
	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {}
	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}

}
