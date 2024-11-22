/**
 * 
 */
package smarthouse;

import java.util.List;
import java.util.logging.Logger;
import smarthouse.devices.DeviceManager;
import smarthouse.devices.SmartDevice;
import smarthouse.engergy.Battery;
import smarthouse.engergy.EnergyManager;
import smarthouse.engergy.EnergySource;
import smarthouse.log.CustomLogger;
import smarthouse.ui.UIManagingSmartObjects;
import smarthouse.util.AsciiArtPrinter;

/**
 * 
 */
public class Main {
	public static final Logger logger = CustomLogger.getLogger();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AsciiArtPrinter.printGroupName();

		/* How to use the logger
		 * Declare the logger object in each class and use it to log messages
		 * 
		 * logger.info("Hello this is INFO message from logger!!!");
		 * logger.warning("This is WARNING message from logger.");
		 * logger.severe("Error message -> use the keyword SEVERE");
		 * logger.fine("Debug messagea -> use the keyword FINE");
		 * 
		 * Log message with format 
		 * logger.info(String.format("Hello this is INFO message from logger!! %s", someValue));
		*/ 
		logger.info("JAVA CAPSTONE PROJECT - SMART HOUSE: ENERGY MANAGERMENT SYSTEM");

		try {
			
//			/* Initialize data */ 
//
//			/* Initialize EnergyManager */
//			EnergyManager energyManager = EnergyManager.getInstance();
//	
//			// Create Engery Sources
//			Battery battery1 = new Battery(100, 70);
//			EnergySource powerBank = new EnergySource("Power Bank", EnergySource.EnergyType.BATTERY, battery1);
//	
//			Battery battery2 = new Battery(1000, 1);
//			EnergySource solar = new EnergySource("Home Solar", EnergySource.EnergyType.SOLAR, battery2);
//	
//			EnergySource grid = new EnergySource("Grid Power", EnergySource.EnergyType.GRID);
//			
//			// Add energy sources to the EnergyManager
//			energyManager.addEnergySource(powerBank);
//			energyManager.addEnergySource(solar);
//			energyManager.addEnergySource(grid);
//			
//			// Print all energy sources ID
//			List<String> energySourceIDs = energyManager.getEnergySourceIDs();
//			for (String id : energySourceIDs) {
//				System.out.println(id);
//				//get name of the energy source
//				EnergySource energySource = energyManager.getEnergySourceByID(id);
//				System.out.println(energySource.getSourceName());
//			}
//			/* Device Manager */
//			DeviceManager deviceManager = new DeviceManager();
//	
//			// Create SmartDevices
//			SmartDevice fan = new SmartDevice("Living Room Fan", SmartDevice.EnergyType.DC);
//			SmartDevice cooler = new SmartDevice("Bedroom Cooler", SmartDevice.EnergyType.DC);
//			SmartDevice heater = new SmartDevice("Living Room Heater", SmartDevice.EnergyType.AC);
//
//			// Create device having integrated battery
//			Battery ringBatt = new Battery(100, 70);
//			SmartDevice ring = new SmartDevice("Door Ring", SmartDevice.EnergyType.DC, ringBatt);
//	
//			// Assign energy sources to devices
//			fan.setEnergySource(solar);
//			cooler.setEnergySource(solar);
//			heater.setEnergySource(grid);
//			ring.setEnergySource(powerBank);
//
//			// Set simulation rate to devices (default is 1.0), see detail in README.md
//			fan.setSimulationRate(0.5);
//			cooler.setSimulationRate(2.0);
//			// heater.setSimulationRate(1.0);
//			// ring.setSimulationRate(1.0);
//	
//			// Add devices to the DeviceManager
//			deviceManager.addDevice(fan);
//			deviceManager.addDevice(cooler);
//			deviceManager.addDevice(heater);
//			deviceManager.addDevice(ring);
//	
//			// get energy sources name from manager and print a list
//			// Get and print all source names
//			List<String> sourceNames = energyManager.getEnergySourceNames();
//			for (String name : sourceNames) {
//				logger.info(name);
//				// print source id for each source name
//				String sourceID = energyManager.getEnergySourceIDByName(name);
//				logger.info(sourceID);
//			}
//			// Get all devices names
//			List<String> deviceNames = deviceManager.getAllDevicesNames();
//			for (String name : deviceNames) {
//				logger.info(name);
//			}
//			// Get all device IDs
//			List<String> deviceIDs = deviceManager.getAllDevicesIDs();
//			for (String id : deviceIDs) {
//				logger.info(id);
//				// Print device status 
//				SmartDevice device = deviceManager.getDeviceByID(id);
//				logger.info(device.getStatus());
//			}

			new UIManagingSmartObjects().setVisible(true);
			/* Turn on devices though the DeviceManager */
			// deviceManager.turnOnDevice(heater);
			// deviceManager.turnOnDevice(fan);
//			deviceManager.turnOnDevice(cooler, false);
			
			/* Get battery percentage of devices */
			// String perRing = ring.getBattery().getBatteryPercentage();
			// logger.info(String.format("Device %s battery percentage: %s", ring.getDeviceName(), perRing));
//			if (fan.getBattery() != null)
//			{
//				String fanPer = fan.getBattery().getBatteryPercentage();
//				logger.info(String.format("Device %s battery percentage: %s", fan.getDeviceName(), fanPer));
//			}
//			
//			/* Recharge Energy source */
//			energyManager.manageRecharging(solar.getSourceID(), true);
//
//			/* Device use its own battery */
//			deviceManager.turnOnDevice(ring,true);
//
//			try {
//				/* Simulation time */
//				Thread.sleep(10000);
//				
//				/* Turn off devices */
//				deviceManager.turnOffDevice(cooler);
//				// deviceManager.turnOffDevice(heater);
//				// deviceManager.turnOffDevice(fan);
//				
//				/* Turn off source charging */
//				energyManager.manageRecharging(solar.getSourceID(), false);
//
//				/* Print consumed energy from devices */
//				// double consumedEnergy = heater.getConsumedEnergy();
//				// logger.info(String.format("Device %s consumed total %.2f kWh.", heater.getDeviceName(), consumedEnergy));
//			
//			} catch (InterruptedException e) {
//				Thread.currentThread().interrupt(); // Restore interrupted status
//				energyManager.manageRecharging(solar.getSourceID(), false);
//				deviceManager.turnOffDevice(cooler);
//				// Handle the exception, e.g., log the error or perform some other action
//			}
		} catch (Exception e) {
			logger.severe(e.getMessage());	
		}

	}

}
