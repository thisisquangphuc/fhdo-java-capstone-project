/**
 * 
 */
package smarthouse;

import java.util.logging.Logger;
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

		// How to use the logger
		logger.info("Hello this is INFO message from logger!!!");
		logger.warning("This is WARNING message from logger.");
		logger.severe("Error message -> use the keyword SEVERE");
		logger.fine("Debug messagea -> use the keyword FINE");
		
		// Code here 
		
		
	}

}
