package com.thelocalmarketplace.software;

import java.io.IOException;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.AbstractCardReader;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderListener;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import powerutility.NoPowerException;

public class DebitCardPayment extends AbstractCardReader{
    private SelfCheckoutStationBronze station;
    
    private double amountAvailable;

    public DebitCardPayment(SelfCheckoutStationBronze station, CardIssuer bank) {
        this.station = station;

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
            public void aCardHasBeenSwiped() {}

            @Override
            public void theDataFromACardHasBeenRead(CardData data) {
                if (!data.getType().toLowerCase().equals("debit"))
                    throw new SecurityException("Invalid card type!");

                if (bank.authorizeHold(data.getNumber(), amountAvailable) == -1) 
                    throw new SecurityException("Transaction unauthorized!");
            }            
        };

        station.cardReader.register(cardReaderListener);
    }

    public void swipeCard(Card card) throws IOException {
        station.cardReader.swipe(card);
    }

    public void setAmountAvailable(double num) {
        amountAvailable = num;
    }
}
