
package com.thelocalmarketplace.software;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.banknote.*;
import java.math.BigDecimal;
import java.util.Currency;

public class PayWithBanknoteBronze implements BanknoteValidatorObserver {
    private BanknoteInsertionSlot insertionSlot;
    private BanknoteValidator validator;
    private BanknoteStorageUnit storageUnit;
    private BanknoteDispenserBronze dispenser;
    private BigDecimal amountDue;
    private Currency currency;
    private boolean waitingForValidation;

    public PayWithBanknoteBronze(BanknoteInsertionSlot insertionSlot, BanknoteValidator validator,
                                 BanknoteStorageUnit storageUnit, BanknoteDispenserBronze dispenser,
                                 BigDecimal amountDue, Currency currency) {
        this.insertionSlot = insertionSlot;
        this.validator = validator;
        this.storageUnit = storageUnit;
        this.dispenser = dispenser;
        this.amountDue = amountDue;
        this.currency = currency;
        this.waitingForValidation = false;

       
        validator.attach(this);
    }

    public void payWithBanknote(Banknote banknote) throws Exception {
        if (!banknote.getCurrency().equals(currency)) {
            throw new IllegalArgumentException("Invalid currency.");
        }

        insertionSlot.receive(banknote);
        validator.receive(banknote);
        waitingForValidation = true;
    }

    @Override
    public void goodBanknote(BanknoteValidator validator, Currency currency, BigDecimal denomination) {
        if (!waitingForValidation) return;
        waitingForValidation = false;

        updateTransactionTotal(denomination);
        try {
            dispenseChange();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    @Override
    public void badBanknote(BanknoteValidator validator) {
        if (!waitingForValidation) return;
        waitingForValidation = false;

    }

    private void updateTransactionTotal(BigDecimal banknoteValue) {
        amountDue = amountDue.subtract(banknoteValue);
    }

    

        

    private void dispenseChange() throws Exception {
        if (amountDue.compareTo(BigDecimal.ZERO) >= 0) {
            return; // No change required
        }

        BigDecimal changeAmount = amountDue.negate();

        // Simplified logic to dispense change
        while (changeAmount.compareTo(BigDecimal.ZERO) > 0) {
            Banknote banknoteToDispense = selectBanknoteForChange(changeAmount);
            if (banknoteToDispense != null) {
                dispenser.emit(); // Emit the banknote
                changeAmount = changeAmount.subtract(banknoteToDispense.getDenomination());
            } else {
                // No suitable banknote available for dispensing
                throw new Exception("Unable to dispense the required change.");
            }
        }
    }

        private Banknote selectBanknoteForChange(BigDecimal changeAmount) {
            
            if (changeAmount.compareTo(new BigDecimal("20")) >= 0) {
                return new Banknote(currency, new BigDecimal("20"));
            } else if (changeAmount.compareTo(new BigDecimal("10")) >= 0) {
                return new Banknote(currency, new BigDecimal("10"));
            } // ... add other denominations as needed
            else {
                return null; // No suitable denomination found
            }
        

    }

	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}
}
