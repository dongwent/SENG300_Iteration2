/**
 * The DebitCardPayment class represents a payment system that uses a debit card for transactions.
 * It extends the AbstractCardReader class and implements the necessary card reader functionality.
 * The class is designed to interact with a CardIssuer to authorize and process debit card transactions.
 *
 * Names:
 * Henry Pham
 * Dongwen Tian
 * Kyuyop Park
 * Mohammad Soomro
 * Mohammad Mustafa Mehtab
 * MD SAIF ALDEEN 
 * 
 * UCID:
 * 30147233
 * 30181813
 * 10046592
 * 30130440
 * 30189394
 * 30197566
 */
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

public class DebitCardPayment extends AbstractCardReader {

    /**
     * Constructs a new DebitCardPayment object with the specified CardIssuer.
     *
     * @param bank The CardIssuer used for authorizing and processing debit card transactions.
     */
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

                if (bank.authorizeHold(data.getNumber(), StartSession.getExpectedPrice()) == -1)
                    throw new SecurityException("Transaction unauthorized!");

                if (!bank.postTransaction(data.getNumber(), 0, StartSession.getExpectedPrice()))
                    throw new SecurityException("Unsuccessful transaction.");
                StartSession.updateExpectedPrice(0);
            }
        };

        StartSession.getStation().cardReader.register(cardReaderListener);
    }

    /**
     * Swipes the specified card, triggering the card reader to process the card's data.
     *
     * @param card The Card object to be swiped.
     * @throws IOException If an I/O error occurs during the card swiping process.
     */
    public void swipeCard(Card card) throws IOException {
        try {
            StartSession.getStation().cardReader.swipe(card);
        } catch (MagneticStripeFailureException msfe) {
            swipeCard(card);
        }
    }
}