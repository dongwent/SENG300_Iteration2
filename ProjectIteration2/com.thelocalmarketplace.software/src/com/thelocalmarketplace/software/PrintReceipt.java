package com.thelocalmarketplace.software;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrintReceipt {

    public static boolean isOutOfPaperOrInk;
	public boolean printPaymentRecord;



	public void printReceipt(PaymentRecord paymentRecord) {
        try {
            updatePaymentRecord(paymentRecord);

            boolean printSuccess = printPaymentRecord(paymentRecord);

            if (printSuccess) {
                // 3. System: Thanks the Customer.
                thankCustomer();

                // 4. System: Ends the session, ready for a new one.
                endSession();
            } else {
                throw new ReceiptPrintingException("Receipt printing failed.");
            }
        } catch (OutOfPaperOrInkException e) {
            handleOutOfPaperOrInkException();
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void updatePaymentRecord(PaymentRecord paymentRecord) {
        paymentRecord.updateRecord();
    }

    private boolean printPaymentRecord(PaymentRecord paymentRecord) throws OutOfPaperOrInkException {

        if (isOutOfPaperOrInk()) {
            throw new OutOfPaperOrInkException("Out of paper or ink during printing.");
        }

        System.out.println("Payment Details:");
        List<String> paymentDetails = paymentRecord.getPaymentDetails();
        for (String detail : paymentDetails) {
            System.out.println(detail);
        }

        return true;
    }

    private void thankCustomer() {
        System.out.println("Thank you for your purchase!");
    }

    private void endSession() {
        System.out.println("Session ended. Ready for a new one.");
    }

    private void handleOutOfPaperOrInkException() {
        System.out.println("Error: Out of paper or ink. Printing aborted. Station suspended.");
        System.out.println("Attendant informed to print a duplicate receipt and perform maintenance.");
    }

    private void handleException(Exception e) {
        System.out.println("An unexpected error occurred: " + e.getMessage());
    }

    private boolean isOutOfPaperOrInk() {
        return Math.random() < 0.1; // 10% chance of being out of paper or ink
    }

   

	public class PaymentRecord {
	    private Date timestamp;
	    private String paymentMethod;
	    private double amount;
	    private List<String> paymentDetails; // List to store payment details
	
	    public PaymentRecord(Date timestamp, String paymentMethod, double amount) {
	        this.timestamp = timestamp;
	        this.paymentMethod = paymentMethod;
	        this.amount = amount;
	        this.paymentDetails = new ArrayList<>();
	    }
	
	    public void updateRecord() {
	        paymentDetails.add("Timestamp: " + timestamp);
	        paymentDetails.add("Payment Method: " + paymentMethod);
	        paymentDetails.add("Amount: " + amount);
	        System.out.println("Payment record updated successfully.");
	    }
	
	    public boolean isPaymentInFull() {
	        return amount > 0;
	    }
	
	    public List<String> getPaymentDetails() {
	        return paymentDetails;
	    }
	}
}

class ReceiptPrintingException extends RuntimeException {
    public ReceiptPrintingException(String message) {
        super(message);
    }
}

class OutOfPaperOrInkException extends Exception {
    public OutOfPaperOrInkException(String message) {
        super(message);
    }
}
