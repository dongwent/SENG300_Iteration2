package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.StartSession;

/**
 * This is a test class for the startSession use case.
 * 
 * @author Dongwen Tian
 *
 */
public class StartSessionTest {
	
	private static AbstractSelfCheckoutStation checkoutStation;
	private static AbstractSelfCheckoutStation checkoutStation2;
	
	@BeforeClass
	public static void initialSetUp() {
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		checkoutStation = new SelfCheckoutStationGold();
		checkoutStation2 = new SelfCheckoutStationBronze();
	}
	
	@After
	public void tearDown() {
		StartSession.endSession(checkoutStation);
		StartSession.endSession(checkoutStation2);
	}
	
	/**
	 * Tests whether a session has been started.	
	 */
	@Test
	public void startTest() {
		try {
			StartSession.startSession(checkoutStation);
		} catch (Exception e) {
			fail();
		}
		
		assertTrue(StartSession.getInSession());
	}
	
	/**
	 * Tests whether the session has been ended.
	 */
	@Test
	public void endTest() {
		try {
			StartSession.startSession(checkoutStation);
		} catch (Exception e) {
			fail();
		}
		
		StartSession.endSession(checkoutStation);
		
		assertTrue(!StartSession.getInSession());
	}
	
	/**
	 * Tests whether the session has been ended when the checkout station that started
	 * the session is different from the on ending it.
	 */
	@Test
	public void differentStationTest() {
		try {
			StartSession.startSession(checkoutStation);
		} catch (Exception e) {
			fail();
		}
		
		StartSession.endSession(checkoutStation2);
		
		assertTrue(StartSession.getInSession());
	}
	
}
