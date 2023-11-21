package com.thelocalmarketplace.software;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

/**
 * This class handles situations where the customer decides not to place an item
 * in the bagging area.
 */
public class HandleBulkyItem {

    public static void handleNoBaggingRequest(AbstractSelfCheckoutStation station) {
        if (isSessionActive() && customerSignalsNoBagging()) {
            blockStation(station);
            notifyAttendant();
            approveByAttendant();
            unblockStation(station);
        }
    }

    private static boolean isSessionActive() {
        // Simulate session being active
        return StartSession.getInSession();
    }

    private static boolean customerSignalsNoBagging() {
        // Simulate customer signaling not to bag
        return true;
    }

    private static void blockStation(AbstractSelfCheckoutStation station) {
        station.scanningArea.disable();
        station.cardReader.disable();
        station.mainScanner.disable();
        station.handheldScanner.disable();
        station.banknoteInput.disable();
        station.coinSlot.disable();
    }

    private static void notifyAttendant() {
        System.out.println("System: Requesting Attendant Assistance. Customer avoids bagging.");
    }

    private static void approveByAttendant() {
        System.out.println("Attendant: Approval granted. Customer avoids bagging.");
    }

    private static void unblockStation(AbstractSelfCheckoutStation station) {
        station.scanningArea.enable();
        station.cardReader.enable();
        station.mainScanner.enable();
        station.handheldScanner.enable();
        station.banknoteInput.enable();
        station.coinSlot.enable();

        System.out.println("System: Station unblocked. Customer can continue.");
    }
}