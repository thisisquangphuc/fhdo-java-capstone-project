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
			
			/* Initialize data */ 
			// Initialize EnergyManager
			EnergyManager energyManager = EnergyManager.getInstance();
	
			// Initialize Engery Sources
			Battery battery1 = new Battery(100, 70);
			EnergySource powerBank = new EnergySource("Power Bank", EnergySource.EnergyType.BATTERY.name(), battery1);
	
			Battery battery2 = new Battery(1000, 1);
			EnergySource solar = new EnergySource("Home Solar", EnergySource.EnergyType.SOLAR.name(), battery2);
	
			EnergySource grid = new EnergySource("Grid Power", EnergySource.EnergyType.GRID.name());
			
			// Add energy sources to the EnergyManager
			energyManager.addEnergySource(powerBank);
			energyManager.addEnergySource(solar);
			energyManager.addEnergySource(grid);
			
			// Create a DeviceManager
			DeviceManager deviceManager = new DeviceManager();
	
			// Create SmartDevice instances
			SmartDevice fan = new SmartDevice("Living Room Fan", EnergySource.EnergyType.BATTERY.name(), new Battery(100, 10));
			SmartDevice cooler = new SmartDevice("Bedroom Cooler", EnergySource.EnergyType.SOLAR.name());
			SmartDevice heater = new SmartDevice("Living Room Heater", EnergySource.EnergyType.GRID.name());
			SmartDevice ring = new SmartDevice("Door Ring", EnergySource.EnergyType.BATTERY.name(), new Battery(100, 10));
	
			// Assign energy sources to devices
			fan.setEnergySource(solar);
			cooler.setEnergySource(solar);
			heater.setEnergySource(grid);
			ring.setEnergySource(powerBank);
	
			// Add devices to the DeviceManager
			deviceManager.addDevice(fan);
			deviceManager.addDevice(cooler);
			deviceManager.addDevice(heater);
			deviceManager.addDevice(ring);
	
			// get energy sources name from manager and print a list
			// Get and print all source names
			List<String> sourceNames = energyManager.getEnergySourceNames();
			for (String name : sourceNames) {
				logger.info(name);
			}
			
			// Turn on devices
			// deviceManager.turnOnDevice(heater);
			deviceManager.turnOnDevice(cooler);
			// deviceManager.turnOnDevice(fan);
			
			//get battery percentage of devices
			// Get and print battery percentage of device
			// String perFan = fan.getBattery().getBatteryPercentage();
			// logger.info(String.format("Device %s battery percentage: %s", fan.getDeviceName(), perFan));
			// String perRing = ring.getBattery().getBatteryPercentage();
			// logger.info(String.format("Device %s battery percentage: %s", ring.getDeviceName(), perRing));
			
			/* Recharge source */
			energyManager.manageRecharging(solar.getSourceID(), true);
			
			try {
				Thread.sleep(10000);
				// deviceManager.turnOffDevice(heater);
				// double consumedEnergy = heater.getConsumedEnergy();
				// logger.info(String.format("Device %s consumed total %.2f kWh.", heater.getDeviceName(), consumedEnergy));
				
				deviceManager.turnOffDevice(cooler);
				// deviceManager.turnOffDevice(fan);

				/* Turn off source charging */
				energyManager.manageRecharging(solar.getSourceID(), false);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Restore interrupted status
				// Handle the exception, e.g., log the error or perform some other action
			}
		} catch (Exception e) {
			logger.severe(e.getMessage());	
		}
	}

}
