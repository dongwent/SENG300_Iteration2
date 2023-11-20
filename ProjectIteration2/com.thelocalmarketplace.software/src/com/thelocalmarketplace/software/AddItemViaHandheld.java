package com.thelocalmarketplace.software;

import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.IBarcodeScanner;

/*
 * This class extends AddItemViaBarcode to utilize its functionality.
 */
public class AddItemViaHandheld extends AddItemViaBarcode {

    public AddItemViaHandheld() {
        // Constructor for HandheldBarcodeScanner
    }

    @Override
    public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
        // Call superclass method for generic barcode scanning behavior
        super.aBarcodeHasBeenScanned(barcodeScanner, barcode);

        // Additional behavior specific to handheld barcode scanning
        // For example, disable/enable devices based on weight discrepancy
        if (weightDiscrepancyDetected()) {
            disableDevices();
        } else {
            enableDevices();
        }
        
     // Signal to the customer to place the scanned item in the bagging area
        System.out.println("System: Place the scanned item in the bagging area.");
        
     // Additional signaling to simulate the system's response
        if (weightDiscrepancyDetected()) {
            System.out.println("System: Weight discrepancy detected. Station blocked.");
        } else {
            System.out.println("System: Weight OK. Station unblocked.");
        }
    }

    private boolean weightDiscrepancyDetected() {
        // This is just a placeholder, WeightDiscrepancy Class is available
    	// to be fully implemented with GUI
        return false;
    }



    private void disableDevices() {
        // Disable relevant devices when there is a weight discrepancy
        StartSession.getStation().scanningArea.disable();
        StartSession.getStation().cardReader.disable();
        StartSession.getStation().mainScanner.disable();
        StartSession.getStation().handheldScanner.disable();
        StartSession.getStation().banknoteInput.disable();
        StartSession.getStation().coinSlot.disable();
    }

    private void enableDevices() {
        // Enable relevant devices when there is no weight discrepancy
        StartSession.getStation().scanningArea.enable();
        StartSession.getStation().cardReader.enable();
        StartSession.getStation().mainScanner.enable();
        StartSession.getStation().handheldScanner.enable();
        StartSession.getStation().banknoteInput.enable();
        StartSession.getStation().coinSlot.enable();
    }

	public void scan(Barcode sampleBarcode) {
		
	}

	
}
