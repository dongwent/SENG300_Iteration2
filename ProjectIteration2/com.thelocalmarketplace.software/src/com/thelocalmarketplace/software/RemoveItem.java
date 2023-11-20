package com.thelocalmarketplace.software;

import java.math.BigDecimal;
import java.util.Scanner;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;


public class RemoveItem {
	
	public static void removeProduct() {
		if (!StartSession.getStation().baggingArea.isDisabled() & StartSession.getInSession()) {
			
			Scanner remover = new Scanner(System.in);
			int i = 1;
			for (Item listItem: StartSession.getShoppingCart()) {
				if (listItem instanceof BarcodedItem) {
					System.out.println(i + ". " + ProductDatabases.BARCODED_PRODUCT_DATABASE.get(
							((BarcodedItem)listItem).getBarcode()).getDescription());
				} else if (listItem instanceof PLUCodedItem) {
					System.out.println(i + ". " + ProductDatabases.PLU_PRODUCT_DATABASE.get(
							((PLUCodedItem)listItem).getPLUCode()).getDescription());
				}
				i += 1;
			}
			
			System.out.println("List the number cooresponding to the item you would like to remove.");
			int removedIndex = remover.nextInt();
			
			StartSession.updateExpectedMass(StartSession.getExpectedMass().difference(
					StartSession.getShoppingCart().get(removedIndex - 1).getMass()).abs());
			
			if (StartSession.getShoppingCart().get(removedIndex) instanceof BarcodedItem) {
				
				StartSession.updateExpectedPrice(StartSession.getExpectedPrice() - 
						ProductDatabases.BARCODED_PRODUCT_DATABASE.get(
						((BarcodedItem)StartSession.getShoppingCart().get(removedIndex))
						.getBarcode()).getPrice());
				
			} else if (StartSession.getShoppingCart().get(removedIndex) instanceof PLUCodedItem) {
				
				StartSession.updateExpectedPrice(StartSession.getExpectedPrice() - 
						StartSession.getShoppingCart().get(removedIndex - 1).getMass().inGrams()
						.divide(BigDecimal.valueOf(1000)).multiply(
						BigDecimal.valueOf(ProductDatabases.PLU_PRODUCT_DATABASE.get(
						((PLUCodedItem)StartSession.getShoppingCart().get(removedIndex))
						.getPLUCode()).getPrice())).longValue());
				
			}

			System.out.println("Please remove the item from the bagging area.");
		}	
	}
	
}
