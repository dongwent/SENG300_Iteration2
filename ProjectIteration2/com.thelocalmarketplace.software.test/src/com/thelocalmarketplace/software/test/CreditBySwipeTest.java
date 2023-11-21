package com.thelocalmarketplace.software;

import java.io.IOException;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.AbstractCardReader;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderListener;
import com.jjjwelectronics.card.MagneticStripeFailureException;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import powerutility.NoPowerException;
public class CreditCardPaymentTest {

    @Test
    public void testCreditLimitExceeded() {
        CardIssuer cardIssuer = new CardIssuer(); // Replace with the actual CardIssuer implementation
        CreditCardPayment creditCardPayment = new CreditCardPayment(cardIssuer);
        creditCardPayment.setCreditLimit(1000.0);

        // Set a card with a transaction amount exceeding the credit limit
        com.jjjwelectronics.card.Card card = new com.jjjwelectronics.card.Card("1234567890123456", "credit");
        StartSession.setExpectedPrice(1000.0);

        try {
            creditCardPayment.swipeCard(card);
        } catch (SecurityException e) {
            System.out.println("Limit Exceeded: " + e.getMessage());
        }
    }

    @Test
    public void testPurchaseSuccessful() {
        CardIssuer cardIssuer = new CardIssuer(); // Replace with the actual CardIssuer implementation
        CreditCardPayment creditCardPayment = new CreditCardPayment(cardIssuer);
        creditCardPayment.setCreditLimit(1000.0);

        // Set a card with a transaction amount within the credit limit
        com.jjjwelectronics.card.Card card = new com.jjjwelectronics.card.Card("9876543210987654", "credit");
        StartSession.setExpectedPrice(500.0);

        try {
            creditCardPayment.swipeCard(card);
            System.out.println("Purchase Successful");
        } catch (SecurityException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}