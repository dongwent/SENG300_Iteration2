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

public class CreditCardPayment extends AbstractCardReader {

    private static final double CREDIT_LIMIT = 1000.0;

    public CreditCardPayment(CardIssuer bank) {

        CardReaderListener cardReaderListener = new CardReaderListener() {

            @Override
            public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
                System.out.println("Device has been enabled.");
            }

            @Override
            public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
                throw new NoPowerException();
            }

            @Override
            public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
                System.out.println("Device has been turned on.");
            }

            @Override
            public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
                throw new NoPowerException();
            }

            @Override
            public void aCardHasBeenSwiped() {
                System.out.println("Card has been swiped.");
            }

            @Override
            public void theDataFromACardHasBeenRead(CardData data) {
                if (!data.getType().toLowerCase().equals("credit"))
                    throw new SecurityException("Invalid card type!");

                double transactionAmount = StartSession.getExpectedPrice();

                if (transactionAmount > CREDIT_LIMIT)
                    throw new SecurityException("Transaction exceeds credit limit!");

                if (bank.authorizeHold(data.getNumber(), transactionAmount) == -1)
                    throw new SecurityException("Transaction unauthorized!");

                if (!bank.postTransaction(data.getNumber(), 0, transactionAmount))
                    throw new SecurityException("Unsuccessful transaction.");

                StartSession.updateExpectedPrice(0);
            }
        };

        StartSession.getStation().cardReader.register(cardReaderListener);
    }

    public void swipeCard(Card card) throws IOException {
        try {
            StartSession.getStation().cardReader.swipe(card);
        } catch (MagneticStripeFailureException msfe) {
            swipeCard(card);
        }
    }
}