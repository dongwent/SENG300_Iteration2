package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;

import com.tdc.banknote.Banknote;
import com.thelocalmarketplace.software.PayWithBanknoteBronze;

public class PayWithBanknoteBronzeTest {

	public static void main(String[] args) {
        Currency currency = Currency.getInstance("USD");
        BigDecimal initialAmountDue = BigDecimal.valueOf(100);

        PayWithBanknoteBronze payWithBanknoteBronze = new PayWithBanknoteBronze(null, null, null, null, initialAmountDue, currency);

        // Test with valid banknote
        try {
            Banknote validBanknote = new Banknote(currency, BigDecimal.valueOf(50));
            payWithBanknoteBronze.payWithBanknote(validBanknote);
            System.out.println("Test with valid banknote passed.");
        } catch (Exception e) {
            System.out.println("Test with valid banknote failed: " + e.getMessage());
        }

        // Test with invalid currency banknote
        try {
            Banknote invalidCurrencyBanknote = new Banknote(Currency.getInstance("EUR"), BigDecimal.valueOf(50));
            payWithBanknoteBronze.payWithBanknote(invalidCurrencyBanknote);
            System.out.println("Test with invalid currency banknote should have failed but passed.");
        } catch (IllegalArgumentException e) {
            System.out.println("Test with invalid currency banknote passed.");
        } catch (Exception e) {
            System.out.println("Test with invalid currency banknote failed with unexpected exception: " + e.getMessage());
        }

    }

}
