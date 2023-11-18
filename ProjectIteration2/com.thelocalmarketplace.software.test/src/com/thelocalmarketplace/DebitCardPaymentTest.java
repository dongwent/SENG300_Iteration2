package com.thelocalmarketplace;

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
    public void setUp() {
        SelfCheckoutStationBronze.configureCurrency(Currency.getInstance(Locale.CANADA));
        SelfCheckoutStationBronze.configureBanknoteDenominations(denominations);
        SelfCheckoutStationBronze.configureCoinDenominations(denominations);
        SelfCheckoutStationBronze.configureBanknoteStorageUnitCapacity(100);
        SelfCheckoutStationBronze.configureCoinStorageUnitCapacity(100);
        SelfCheckoutStationBronze.configureCoinTrayCapacity(100);
        SelfCheckoutStationBronze.configureCoinDispenserCapacity(100);

        station = new SelfCheckoutStationBronze();

        bank = new CardIssuer("CIBC", 1);
        cardNumber = "8437";
        cardHolder = "Bob Bee";
        cardCVV = "247";
        
        cardExpiryDate = Calendar.getInstance();
        cardExpiryDate.set(Calendar.YEAR, 2030);
        cardExpiryDate.set(Calendar.MONTH, 10);

        card = new Card("debit", cardNumber, cardHolder, cardCVV);
        
        bank.addCardData(cardNumber, cardHolder, cardExpiryDate, cardCVV, 500);

        debitCard = new DebitCardPayment(station, bank);
        debitCard.setAmountAvailable(500);
    }

    @Test
    public void testListenerDeviceEnabled() throws IOException {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        station.plugIn(PowerGrid.instance());
        station.turnOn();
        station.cardReader.enable();

        String[] lines = outputStreamCaptor.toString().split(System.lineSeparator());

        String expectedOutput = "Device has been enabled.";

        System.setOut(System.out);

        // Assert the expected output
        assertEquals(expectedOutput, lines[1]);
    }
}
