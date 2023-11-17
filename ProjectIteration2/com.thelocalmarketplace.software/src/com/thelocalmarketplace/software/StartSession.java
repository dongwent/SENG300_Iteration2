package com.thelocalmarketplace.software;

import java.util.ArrayList;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

/**
 * This class is used to start a session on a self checkout station.
 * Stores the information given while the session is in progress.
 * 
 * @author Dongwen Tian
 * 
 */
public class StartSession {
	
	private static AbstractSelfCheckoutStation thisStation;
	
	/**
	 * 
	 * @return The station where the session is occurring.
	 */
	public static AbstractSelfCheckoutStation getStation() {
		return thisStation;
	}
	
	private static boolean inSession;
	
	/**
	 * 
	 * @return True if a session has been started, false otherwise.
	 */
	public static boolean getInSession() {
		return inSession;
	}
	private static ArrayList<Item> shoppingCart = new ArrayList<>();
	
	/**	
	 * 
	 * @return A list of items that the customer is ordering.
	 */
	public static ArrayList<Item> getShoppingCart() {
		return shoppingCart;
	}
	
	private static Mass expectedMass;
	
	/**
	 * 
	 * @return The expected mass in the bagging area.
	 */
	public static Mass getExpectedMass() {
		return expectedMass;
	}
	
	/**
	 * Updates the expected mass in the bagging area.
	 * 
	 * @param newMass The new mass expected.
	 */
	public static void updateExpectedMass(Mass newMass) {
		expectedMass = newMass;
	}
	
	private static long expectedPrice;
	
	/**
	 * 
	 * @return The expected price of items in the order.
	 */
	public static long getExpectedPrice() {
		return expectedPrice;
	}
	
	/**
	 * Updates the expected price of items in the order.
	 * 
	 * @param newPrice The new price of all items in the order.
	 */
	public static void updateExpectedPrice(long newPrice) {
		expectedPrice = newPrice;
	}
	
	/**
	 * This method is called to start a session on a self checkout station.
	 * 
	 * @param station The self checkout station where the session was started.
	 * 
	 * @throws Exception
	 */
	public static void startSession(AbstractSelfCheckoutStation station) throws Exception {
		if(inSession) {
			throw new Exception();
		}
		thisStation = station;
		shoppingCart.clear();
		expectedMass = new Mass(0);		
		inSession = true;
	}
	
	/**
	 * This method is called to end the current session.
	 * 
	 * @param station The self checkout station where the session was ended.
	 */
	public static void endSession(AbstractSelfCheckoutStation station) {
		if (thisStation == station) {
			thisStation = null;
			inSession = false;
		}
	}
	
}
