package smarthouse.log;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class CustomLogger {
    private static Logger logger;
    public static Logger getLogger() {
        if (logger == null) {
        	// Create the logger
        	logger = Logger.getLogger(CustomLogger.class.getName());
            try {
                
                 // Create directory if it doesn't exist
                 File logDir = new File("log_output");
                 if (!logDir.exists()) {
                     logDir.mkdirs();
                     System.out.println("Log directory created: " + logDir.getAbsolutePath());
                 }
                
                // Create a FileHandler with rotation (max 5MB per file, 3 files to retain)
                FileHandler fileHandler = new FileHandler("log_output/ems_app.log", 1 * 1024 * 1024, 3, true); // 5MB file size, 3 rotations

                fileHandler.setFormatter(new SimpleFormatter() {
                    @Override
                    public String format(LogRecord record) {
                    	return formatLogMessage(record);
                    }
                });
                
                // Reset default configure before applying the custom format
                LogManager.getLogManager().reset();
                
                logger.addHandler(fileHandler);
                
                // Set the log level to DEBUG=FINE
                logger.setLevel(Level.FINE);
                
                // Set console handler as well for console output
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setLevel(Level.FINE);
                consoleHandler.setFormatter(new SimpleFormatter(){
                    @Override
                    public String format(LogRecord record) {
                    	return formatLogMessage(record);
                    }
                });
                logger.addHandler(consoleHandler);
                logger.info("Logger innitialized successfully.");
            } catch (IOException e) {
                System.err.println("Error setting up logger: " + e.getMessage());
            }
        }
        return logger;
    }
    
    private static String formatLogMessage(LogRecord record) {
        return String.format("[%1$tF %1$tT.%1$tL] [%2$s] [%3$s] %4$s%n", 
                record.getMillis(), 
                record.getSourceClassName(),
                record.getLevel(), 
                record.getMessage());
    }
}
