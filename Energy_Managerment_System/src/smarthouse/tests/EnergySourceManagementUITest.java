package smarthouse.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smarthouse.devices.DeviceManager;
import smarthouse.devices.SmartDevice;
import smarthouse.engergy.Battery;
import smarthouse.engergy.EnergyManager;
import smarthouse.engergy.EnergySource;
import smarthouse.log.CustomLogger;
import smarthouse.ui.EnergySourceManagementUI;

class EnergySourceManagementUITest {
    private EnergyManager energyManager;
    private DeviceManager deviceManager;
    private EnergySource powerBank, solar, grid;
    
    @BeforeEach
    void setUp() {
		/* Initialize EnergyManager */
		this.energyManager = EnergyManager.getInstance();
		/* Device Manager */
		this.deviceManager = new DeviceManager();
		
		// Create Energy Sources
		Battery battery1 = new Battery(100, 70);
		powerBank = new EnergySource("Grid Power", EnergySource.EnergyType.BATTERY, battery1);

		Battery battery2 = new Battery(1000, 1);
		solar = new EnergySource("Home Solar", EnergySource.EnergyType.SOLAR, battery2);

		grid = new EnergySource("Grid Power", EnergySource.EnergyType.GRID);
		
		// Add energy sources to the EnergyManager
		this.energyManager.addEnergySource(powerBank);
		this.energyManager.addEnergySource(solar);
    }
    
	@Test
	void testInitPanelForEachEnergySource() {	
		EnergySourceManagementUI energySourceManagementUI = new EnergySourceManagementUI(energyManager, deviceManager, false);
		
		assertTrue(energySourceManagementUI.getComponentCount() == this.energyManager.getEnergySourceIDs().size(),
				"Number of components for energy source in UI should equal to number of energy sources.");
		
		List<String> energySourceIDs = this.energyManager.getEnergySourceIDs();
		for (int i=0; i<energySourceIDs.size(); i++) {
			assertEquals(energySourceManagementUI.getEnergySourcePanel().get(energySourceIDs.get(i)).getName(), energySourceIDs.get(i), 
					"Name of corresponding energy panel should be the energy source ID.");
		}
		
	}
	
	@Test
	void testDisplayNameForEachEnergySource() {
		EnergySourceManagementUI energySourceManagementUI = new EnergySourceManagementUI(energyManager, deviceManager, false);
			
		assertTrue(energySourceManagementUI.getComponentCount() == this.energyManager.getEnergySourceIDs().size(),
				"Number of components for energy source in UI should equal to number of energy sources.");
		
		List<String> energySourceIDs = this.energyManager.getEnergySourceIDs();
		List<String> energySourceNames = this.energyManager.getEnergySourceNames();
		for (int i=0; i<energySourceNames.size(); i++) {
			assertEquals(energySourceManagementUI.getEnergySourceLabel().get(energySourceIDs.get(i)).getText(), energySourceNames.get(i), 
					"Display name of corresponding energy source should be the energy name.");
		}
	}
	
	@Test
	void testChargeBtnPressed() {	
		EnergySourceManagementUI energySourceManagementUI = new EnergySourceManagementUI(energyManager, deviceManager, false);
		
		List<String> energySourceIDs = this.energyManager.getEnergySourceIDs();
		for (int i=0; i<energySourceIDs.size(); i++) { 
			if (this.energyManager.getEnergySourceByID(energySourceIDs.get(i)).isInRechargeTimeRange()) {
				energySourceManagementUI.getChargeSourceBtn().get(energySourceIDs.get(i)).doClick();
				JSONObject jsonStatus = new JSONObject(this.energyManager.getEnergySourceByID(energySourceIDs.get(i)).getStatus());
				Boolean status = Boolean.parseBoolean(jsonStatus.getString("isRecharging"));
				assertTrue(status, "Status should be recharging after pressing charge button.");
				
				energySourceManagementUI.getChargeSourceBtn().get(energySourceIDs.get(i)).doClick();
				jsonStatus = new JSONObject(this.energyManager.getEnergySourceByID(energySourceIDs.get(i)).getStatus());
				status = Boolean.parseBoolean(jsonStatus.getString("isRecharging"));
				assertFalse(status, "Status should not be recharging after pressing again charge button to stop charging.");
			} else {
				energySourceManagementUI.getChargeSourceBtn().get(energySourceIDs.get(i)).doClick();
				JSONObject jsonStatus = new JSONObject(this.energyManager.getEnergySourceByID(energySourceIDs.get(i)).getStatus());
				Boolean status = Boolean.parseBoolean(jsonStatus.getString("isRecharging"));
				assertFalse(status, "Status should not be recharging when out of recharging time.");
			}
		}
	}
	
	@Test
	void testDisplayDeviceUseEnergySource() {
		// Create SmartDevices
		SmartDevice fan = new SmartDevice("Living Room Fan", SmartDevice.EnergyType.DC);
		SmartDevice cooler = new SmartDevice("Bedroom Cooler", SmartDevice.EnergyType.DC);
		
		// Create device having integrated battery
		Battery ringBatt = new Battery(100, 70);
		SmartDevice ring = new SmartDevice("Door Ring", SmartDevice.EnergyType.DC, ringBatt);
		
		// Assign energy sources to devices
		fan.setEnergySource(solar);
		cooler.setEnergySource(solar);
		ring.setEnergySource(powerBank);

		// Add devices to the DeviceManager
		deviceManager.addDevice(fan);
		deviceManager.addDevice(cooler);
		deviceManager.addDevice(ring);
		
		EnergySourceManagementUI energySourceManagementUI = new EnergySourceManagementUI(energyManager, deviceManager, false);
		 
		List<String> energySourceIDs = this.energyManager.getEnergySourceIDs();
		for (int i=0; i<energySourceIDs.size(); i++) {
			String energyName = energySourceManagementUI.getEnergySourceLabel().get(energySourceIDs.get(i)).getText();
			String deviceComsumingList = energySourceManagementUI.getDeviceComsumingSource().get(energySourceIDs.get(i)).getText();
			if (energyName == "Power Bank") {
				assertFalse(deviceComsumingList.contains("Living Room Fan"), "Living Room Fan should be NOT on the list of Power Bank.");
				assertFalse(deviceComsumingList.contains("Bedroom Cooler"), "Bedroom Cooler should be NOT on the list of Power Bank.");
				assertTrue(deviceComsumingList.contains("Door Ring"), "Door Ring should be on the list of Power Bank.");
			} else if (energyName == "Home Solar") {
				assertTrue(deviceComsumingList.contains("Living Room Fan"), "Living Room Fan should be on the list of Home Solar.");
				assertTrue(deviceComsumingList.contains("Bedroom Cooler"), "Bedroom Cooler should be on the list of Home Solar.");
				assertFalse(deviceComsumingList.contains("Door Ring"), "Door Ring should be NOT on the list of Home Solar.");
			}
		}
	}
	
	@Test
	void testRemoveBtnPressed() {
		EnergySourceManagementUI energySourceManagementUI = new EnergySourceManagementUI(energyManager, deviceManager, false);
		
		List<String> energySourceIDs = this.energyManager.getEnergySourceIDs();
		for (int i=0; i<energySourceIDs.size(); i++) {
			energySourceManagementUI.getRemoveSourceBtn().get(energySourceIDs.get(i)).doClick();
			assertFalse(energySourceManagementUI.getEnergySourcePanel().containsKey(energySourceIDs.get(i)),
					"Corresponding energy source panel must be removed from UI after pressing remove button.");
			assertFalse(this.energyManager.getEnergySourceIDs().contains(energySourceIDs.get(i)), 
					"Corresponding enery source must be removed from the system database.");
		}	
	}
}
