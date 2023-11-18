package com.thelocalmarketplace.software;

import java.io.IOException;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.AbstractCardReader;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderListener;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import powerutility.NoPowerException;

public class DebitCardPayment extends AbstractCardReader{    
    private double holdAmount;

    public DebitCardPayment(CardIssuer bank) {

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
                if (!data.getType().toLowerCase().equals("debit"))
                    throw new SecurityException("Invalid card type!");
                
                if (bank.authorizeHold(data.getNumber(), holdAmount) == -1) 
                    throw new SecurityException("Transaction unauthorized!");
            }            
        };

        StartSession.getStation().cardReader.register(cardReaderListener);
    }

    public void swipeCard(Card card, CardIssuer bank) throws IOException {
        StartSession.getStation().cardReader.swipe(card);

        Card.CardSwipeData data = card.new CardSwipeData();

        bank.postTransaction(data.getNumber(), Double.valueOf(holdAmount).longValue(), StartSession.getExpectedPrice());
    }

    public void setHoldAmount(double num) {
        holdAmount = num;
    }
}
