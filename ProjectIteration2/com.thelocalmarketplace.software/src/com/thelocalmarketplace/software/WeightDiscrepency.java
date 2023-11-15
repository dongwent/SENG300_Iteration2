package com.thelocalmarketplace.software;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;

/**
 * This is the control software for the bagging area. 
 * When a difference between the expected weight and current weight is detected,
 * other functions are blocked until the discrepancy is resolved.
 * 
 * @author Dongwen Tian
 * 
 */

public class WeightDiscrepency implements ElectronicScaleListener{
	
	/**
	 * This event is triggered when the mass on the scale changes.
	 * If there is a discrepancy between the expected mass and the current
	 * mass on the scale, the payment input devices and item adding devices are disabled.
	 * 
	 */
	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		if (!scale.isDisabled() & StartSession.getInSession()) {
			
			if (mass.difference(StartSession.getExpectedMass()).abs().compareTo(scale.getSensitivityLimit()) >= 0) {
				StartSession.getStation().scanningArea.disable();
				StartSession.getStation().cardReader.disable();
				StartSession.getStation().mainScanner.disable();
				StartSession.getStation().handheldScanner.disable();
				StartSession.getStation().banknoteInput.disable();
				StartSession.getStation().coinSlot.disable();
			} else {
				StartSession.getStation().scanningArea.enable();
				StartSession.getStation().cardReader.enable();
				StartSession.getStation().mainScanner.enable();
				StartSession.getStation().handheldScanner.enable();
				StartSession.getStation().banknoteInput.enable();
				StartSession.getStation().coinSlot.enable();
			}
		}
	}
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}
	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {}
	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {}
	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}
	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {}
	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {}
}
