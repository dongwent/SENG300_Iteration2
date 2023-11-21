package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jjjwelectronics.card.Card;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.software.DebitCardPayment;
import com.thelocalmarketplace.software.StartSession;

import powerutility.NoPowerException;
import powerutility.PowerGrid;

public class DebitCardPaymentTest {
    private static SelfCheckoutStationBronze station;
    
    private static CardIssuer bank;
    private static String cardNumber;
    private static String cardHolder;
    private static Calendar cardExpiryDate;
    private static String cardCVV;
    
    private static Card card;
    private static DebitCardPayment debitCardPayment;

    private static BigDecimal[] denominations = {BigDecimal.valueOf(5), BigDecimal.valueOf(10), BigDecimal.valueOf(20)};

    @BeforeClass
    public static void setUp() throws Exception {
        SelfCheckoutStationBronze.configureCurrency(Currency.getInstance(Locale.CANADA));
        SelfCheckoutStationBronze.configureBanknoteDenominations(denominations);
        SelfCheckoutStationBronze.configureCoinDenominations(denominations);
        SelfCheckoutStationBronze.configureBanknoteStorageUnitCapacity(100);
        SelfCheckoutStationBronze.configureCoinStorageUnitCapacity(100);
        SelfCheckoutStationBronze.configureCoinTrayCapacity(100);
        SelfCheckoutStationBronze.configureCoinDispenserCapacity(100);

        station = new SelfCheckoutStationBronze();
        StartSession.startSession(station);

        bank = new CardIssuer("CIBC", 1);
        cardNumber = "8437";
        cardHolder = "Bob Bee";
        cardCVV = "247";
        
        cardExpiryDate = Calendar.getInstance();
        cardExpiryDate.set(Calendar.YEAR, 2030);
        cardExpiryDate.set(Calendar.MONTH, 10);

        card = new Card("debit", cardNumber, cardHolder, cardCVV);
        
        bank.addCardData(cardNumber, cardHolder, cardExpiryDate, cardCVV, 500);

        debitCardPayment = new DebitCardPayment(bank);
    }

    /**
     * Test aDeviceHasBeenEnabled() when runs succesfully
     * 
     */
    @Test
    public void testListenerDeviceEnabled() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.enable();

        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());

        String expectedOutput = "Device has been enabled.";

        System.setOut(System.out);

        // Assert the expected output
        assertEquals(expectedOutput, lines[1]);
    }

    /**
     * Test aDeviceHasBeenTurnedOn() when runs succesfully
     * 
     */
    @Test
    public void testListenerDeviceTurnedOn() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.enable();

        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());

        String expectedOutput = "Device has been turned on.";

        System.setOut(System.out);

        // Assert the expected output
        assertEquals(expectedOutput, lines[0]);
    }

    /**
     * Test aDeviceHasBeenDisabled()
     * 
     * @throws IOException
     */
    @Test (expected = NoPowerException.class)
    public void testListenerDeviceDisabled() throws IOException {
        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.disable();

        debitCardPayment.swipeCard(card);
    }

    /**
     * Test aDeviceHasBeenTurnedOff()
     * 
     * @throws IOException
     */
    @Test (expected = NoPowerException.class)
    public void testListenerDeviceTurnedOff() throws IOException {
        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOff();
        StartSession.getStation().cardReader.enable();

        debitCardPayment.swipeCard(card);
    }

    /**
     * Test aCardHasBeenSwiped() when runs successfully
     * 
     * @throws IOException
     */
    @Test
    public void testListenerCardSwipe() throws IOException {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.enable();

        StartSession.updateExpectedPrice(50);
        
        debitCardPayment.swipeCard(card);

        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());

        String expectedOutput = "Card has been swiped.";

        System.setOut(System.out);

        // Assert the expected output
        assertEquals(expectedOutput, lines[2]);
    }

    /**
     * Test theDataFromACardHasBeenRead() with wrong card type
     * 
     * @throws IOException
     */
    @Test (expected = SecurityException.class)
    public void testListenerDataRead1() throws IOException {
        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.enable();

        card = new Card("credit", cardNumber, cardHolder, cardCVV);

        debitCardPayment.swipeCard(card);
    }

    /**
     * Test theDataFromACardHasBeenRead() with invalid amount
     * 
     * @throws IOException
     */
    @Test (expected = SecurityException.class)
    public void testListenerDataRead2() throws IOException {
        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.enable();

        debitCardPayment.swipeCard(card);
    }

    /**
     * Test theDataFromACardHasBeenRead() with null card data
     * 
     * @throws IOException
     */
    @Test (expected = SecurityException.class)
    public void testListenerDataRead3() throws IOException {
        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.enable();

        CardIssuer bank2 = new CardIssuer("TD Bank", 500);

        debitCardPayment = new DebitCardPayment(bank2);

        debitCardPayment.swipeCard(card);
    }

    /**
     * test swipeCard() when runs successfully
     * 
     * @throws IOException
     */
    @Test
    public void testSwipeCard1() throws IOException {
        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.enable();

        StartSession.updateExpectedPrice(50);
        
        debitCardPayment.swipeCard(card);
    }

    /**
     * Test swipeCard() when no power
     * 
     * @throws IOException
     */
    @Test (expected = NoPowerException.class)
    public void testSwipeCard2() throws IOException {
        StartSession.getStation().unplug();

        debitCardPayment.swipeCard(card);
    }

    /**
     * Test swipeCard() when bank's card info does not match card info
     * @throws Exception
     */
    @Test (expected = SecurityException.class)
    public void testSwipeCard3() throws Exception {
        StartSession.endSession(station);
        SelfCheckoutStationBronze station1 = new SelfCheckoutStationBronze();
        StartSession.startSession(station1);
        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.enable();

        StartSession.updateExpectedPrice(50);

        bank = new CardIssuer("TD Bank", 1);
        bank.addCardData("1234", cardHolder, cardExpiryDate, "423", 500);
        debitCardPayment = new DebitCardPayment(bank);
        
        debitCardPayment.swipeCard(card);
    }

    /**
     * Test swipeCard() with unexisting card
     * 
     * @throws IOException
     */
    @Test (expected = SecurityException.class)
    public void testSwipeCard4() throws IOException {
        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.enable();

        StartSession.updateExpectedPrice(50);

        Card card2 = new Card("debit", "1234", cardHolder, cardCVV);
        
        debitCardPayment.swipeCard(card2);
    }

     /**
     * Test swipeCard() when bank has no data of card
     * @throws Exception
     */
    @Test (expected = SecurityException.class)
    public void testSwipeCard5() throws Exception {
        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.enable();

        StartSession.updateExpectedPrice(50);

        bank = new CardIssuer("TD Bank", 1);
        
        debitCardPayment = new DebitCardPayment(bank);
        
        debitCardPayment.swipeCard(card);
    }

    /**
     * Test swipeCard() when card does not have enough money
     * 
     */
    @Test (expected = SecurityException.class)
    public void testSwipeCard6() throws IOException {
        StartSession.getStation().plugIn(PowerGrid.instance());
        StartSession.getStation().turnOn();
        StartSession.getStation().cardReader.enable();

        StartSession.updateExpectedPrice(1000);
        
        debitCardPayment.swipeCard(card);
    }
}
