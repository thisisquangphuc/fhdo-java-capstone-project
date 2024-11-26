package smarthouse.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smarthouse.ui.UIManagingSmartObjects;
class UIManagingSmartObjectsTest {
	private UIManagingSmartObjects UI;
	@BeforeEach
	void setUp() throws Exception {
		 UI = new UIManagingSmartObjects();
		 UI.setVisible(true);
	}

	public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
	//==============on button==================
	@Test
	void testFanOnBtn() {
		UI.getOnButton("fan").doClick();
		String sourceID = UI.getDevice("fan").getEnergySourceID();
		if (sourceID != null) {
			String sourceName = UI.getEnergyManager().getEnergySourceByID(sourceID).getSourceName();
			if ("DC".equals(UI.getDevice("fan").getDeviceType()) & ("Power Bank".equals(sourceName) || "Home Solar".equals(sourceName))) {
				assertEquals(true, UI.getDevice("fan").isOn());
	        } else if ("AC".equals(UI.getDevice("fan").getDeviceType()) & ("Grid Power".equals(sourceName))){
	        	assertEquals(true, UI.getDevice("fan").isOn());
			}
        } else {
        	assertEquals(false, UI.getDevice("heater").isOn());
        }
		
	}
	
	@Test
	void testHeaterOnBtn() {
		UI.getOnButton("heater").doClick();
		String sourceID = UI.getDevice("heater").getEnergySourceID();
//		System.out.println(dev);
		if (sourceID != null) {
			String sourceName = UI.getEnergyManager().getEnergySourceByID(sourceID).getSourceName();
			if ("DC".equals(UI.getDevice("heater").getDeviceType()) & ("Power Bank".equals(sourceName) || "Home Solar".equals(sourceName))) {
				assertEquals(true, UI.getDevice("heater").isOn());
			} else if ("AC".equals(UI.getDevice("heater").getDeviceType()) & ("Grid Power".equals(sourceName))){
				assertEquals(true, UI.getDevice("heater").isOn());
			}
        } else {
        	assertEquals(false, UI.getDevice("heater").isOn());
        }
	}

	@Test
	void testCoolerOnBtn() {
		UI.getOnButton("cooler").doClick();
		String sourceID = UI.getDevice("cooler").getEnergySourceID();
//		System.out.println(dev);
		if (sourceID != null) {
			String sourceName = UI.getEnergyManager().getEnergySourceByID(sourceID).getSourceName();
			if ("DC".equals(UI.getDevice("cooler").getDeviceType()) & ("Power Bank".equals(sourceName) || "Home Solar".equals(sourceName))) {
				assertEquals(true, UI.getDevice("cooler").isOn());
			} else if ("AC".equals(UI.getDevice("cooler").getDeviceType()) & ("Grid Power".equals(sourceName))){
				assertEquals(true, UI.getDevice("cooler").isOn());
			}
        } else {
        	assertEquals(false, UI.getDevice("heater").isOn());
        }
	}
	//============off button===================
	@Test
	void testFanOffBtn() {
		UI.getOnButton("fan").doClick();				
		String sourceID = UI.getDevice("fan").getEnergySourceID();
		if (sourceID != null) {
			UI.getOffButton("fan").doClick();		
			String consume = "Fan: " + (Double.toString(round(UI.getDevice("fan").getConsumedEnergy(),2))) + " kWh";
			String sourceName = UI.getEnergyManager().getEnergySourceByID(sourceID).getSourceName();
			if ("DC".equals(UI.getDevice("fan").getDeviceType()) & ("Power Bank".equals(sourceName) || "Home Solar".equals(sourceName))) {
				assertEquals(false, UI.getDevice("fan").isOn());
				assertEquals(consume, UI.getlabelConsume("fan").getText());
	        } else if ("AC".equals(UI.getDevice("fan").getDeviceType()) & ("Grid Power".equals(sourceName))){
	        	assertEquals(false, UI.getDevice("fan").isOn());
	        	assertEquals(consume, UI.getlabelConsume("fan").getText());
			}
        } else {
        	assertEquals(false, UI.getDevice("fan").isOn());
        	assertEquals("Fan:", UI.getlabelConsume("fan").getText());
        }
	}
	
	@Test
	void testHeaterOffBtn() {
		UI.getOnButton("heater").doClick();			
		String sourceID = UI.getDevice("heater").getEnergySourceID();
		if (sourceID != null) {
			UI.getOffButton("heater").doClick();
			String consume = "Heater: " + (Double.toString(round(UI.getDevice("heater").getConsumedEnergy(),2))) + " kWh";
			String sourceName = UI.getEnergyManager().getEnergySourceByID(sourceID).getSourceName();
			if ("DC".equals(UI.getDevice("heater").getDeviceType()) & ("Power Bank".equals(sourceName) || "Home Solar".equals(sourceName))) {
				assertEquals(false, UI.getDevice("heater").isOn());
				assertEquals(consume, UI.getlabelConsume("heater").getText());
	        } else if ("AC".equals(UI.getDevice("heater").getDeviceType()) & ("Grid Power".equals(sourceName))){
	        	assertEquals(false, UI.getDevice("heater").isOn());
	        	assertEquals(consume, UI.getlabelConsume("heater").getText());
			}
        } else {
        	assertEquals(false, UI.getDevice("heater").isOn());
        	assertEquals("Heater:", UI.getlabelConsume("heater").getText());
        }
	}

	@Test
	void testCoolerOffBtn() {
		UI.getOnButton("cooler").doClick();			
		String sourceID = UI.getDevice("cooler").getEnergySourceID();
		if (sourceID != null) {
			UI.getOffButton("cooler").doClick();
			String consume = "Cooler: " + (Double.toString(round(UI.getDevice("cooler").getConsumedEnergy(),2))) + " kWh";
			String sourceName = UI.getEnergyManager().getEnergySourceByID(sourceID).getSourceName();
			if ("DC".equals(UI.getDevice("cooler").getDeviceType()) & ("Power Bank".equals(sourceName) || "Home Solar".equals(sourceName))) {
				assertEquals(false, UI.getDevice("cooler").isOn());
				assertEquals(consume, UI.getlabelConsume("cooler").getText());
	        } else if ("AC".equals(UI.getDevice("cooler").getDeviceType()) & ("Grid Power".equals(sourceName))){
	        	assertEquals(false, UI.getDevice("cooler").isOn());
	        	assertEquals(consume, UI.getlabelConsume("cooler").getText());
			}
        } else {
        	assertEquals(false, UI.getDevice("cooler").isOn());
        	assertEquals("Cooler:", UI.getlabelConsume("cooler").getText());
        }
	}
}
