package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.card.Card;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.software.DebitCardPayment;
import com.thelocalmarketplace.software.StartSession;

import powerutility.NoPowerException;
import powerutility.PowerGrid;

public class DebitCardPaymentTest {
    private SelfCheckoutStationBronze station;
    
    private CardIssuer bank;
    private String cardNumber;
    private String cardHolder;
    private Calendar cardExpiryDate;
    private String cardCVV;
    
    private Card card;
    private DebitCardPayment debitCard;

    BigDecimal[] denominations = {BigDecimal.valueOf(5), BigDecimal.valueOf(10), BigDecimal.valueOf(20)};

    @Before
    public void setUp() throws Exception {
        SelfCheckoutStationBronze.configureCurrency(Currency.getInstance(Locale.CANADA));
        SelfCheckoutStationBronze.configureBanknoteDenominations(denominations);
        SelfCheckoutStationBronze.configureCoinDenominations(denominations);
        SelfCheckoutStationBronze.configureBanknoteStorageUnitCapacity(100);
        SelfCheckoutStationBronze.configureCoinStorageUnitCapacity(100);
        SelfCheckoutStationBronze.configureCoinTrayCapacity(100);
        SelfCheckoutStationBronze.configureCoinDispenserCapacity(100);

        station = new SelfCheckoutStationBronze();
        StartSession.startSession(station);

        bank = new CardIssuer("CIBC", 1000);
        cardNumber = "8437";
        cardHolder = "Bob Bee";
        cardCVV = "247";
        
        cardExpiryDate = Calendar.getInstance();
        cardExpiryDate.set(Calendar.YEAR, 2030);
        cardExpiryDate.set(Calendar.MONTH, 10);

        card = new Card("debit", cardNumber, cardHolder, cardCVV);
        
        bank.addCardData(cardNumber, cardHolder, cardExpiryDate, cardCVV, 500);

        debitCard = new DebitCardPayment(bank);
        debitCard.setHoldAmount(500);
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

        debitCard.swipeCard(card, bank);
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

        debitCard.swipeCard(card, bank);
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

        debitCard.swipeCard(card, bank);

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

        debitCard.swipeCard(card, bank);
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

        debitCard.setHoldAmount(0);

        debitCard.swipeCard(card, bank);
    }
}
