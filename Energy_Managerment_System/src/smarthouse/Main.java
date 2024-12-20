/**
 * 
 */
package smarthouse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import smarthouse.engergy.Battery;
import smarthouse.engergy.EnergyManager;
import smarthouse.engergy.EnergySource;
import smarthouse.log.CustomLogger;
import smarthouse.ui.UIManagingSmartObjects;
import smarthouse.util.AsciiArtPrinter;
import smarthouse.util.ConfigManager;

/**
 * 
 */
public class Main {
	public static final Logger logger = CustomLogger.getSysLogger();
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
			
			/* Get config */ 
			ConfigManager config = ConfigManager.getInstance();
            config.loadSettings("appconfig.properties");
            
            logger.info("Settings loaded successfully!");
			
			/* Initialize EnergyManager */
			EnergyManager energyManager = EnergyManager.getInstance();
	
			// Create Engery Sources
			Battery battery1 = new Battery(config.getDouble("powerbank.capacity", 100), config.getInt("powerbank.percentage", 1));
			EnergySource powerBank = new EnergySource("Power Bank", EnergySource.EnergyType.BATTERY, battery1);
	
			Battery battery2 = new Battery(config.getDouble("solar.capacity", 100), config.getInt("solar.percentage", 1));
			EnergySource solar = new EnergySource("Home Solar", EnergySource.EnergyType.SOLAR, battery2);
	
			EnergySource grid = new EnergySource("Grid Power", EnergySource.EnergyType.GRID);
			
			// Add energy sources to the EnergyManager
			energyManager.addEnergySource(powerBank);
			energyManager.addEnergySource(solar);
			energyManager.addEnergySource(grid);
			
			// Print all energy sources ID
			List<String> energySourceIDs = energyManager.getEnergySourceIDs();
			for (String id : energySourceIDs) {
				System.out.println(id);
				//get name of the energy source
				EnergySource energySource = energyManager.getEnergySourceByID(id);
				System.out.println(energySource.getSourceName());
			}

			// Start UI
			new UIManagingSmartObjects(energyManager).setVisible(true);
			
		} catch (RuntimeException e) {
			logger.severe(String.format("Unexpected error: " + e.getMessage()));
		} catch (IOException e) {
    		logger.severe(String.format("I/O error: " + e.getMessage()));
		} catch (Exception e) {
			logger.severe(String.format("Error: " + e.getMessage()));
		}

	}

}
