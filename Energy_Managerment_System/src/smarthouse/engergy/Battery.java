package smarthouse.engergy;

import java.util.UUID;
import java.util.logging.Logger;
import smarthouse.log.CustomLogger;

public class Battery {
    // Declare logger
    private static final Logger logger = CustomLogger.getLogger();

	private final String id;
	private double capacity;      
    private double energyLevel;	// Current energy in kWh
    private double currentPercentage;

    public Battery(double capacity_kWh, int currentPercentage) {
        //!TODO: Should be in percentage
        this.id = UUID.randomUUID().toString();
        if (capacity_kWh <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
    	this.capacity = capacity_kWh;
        this.energyLevel = capacity_kWh*currentPercentage/100;
        logger.info(String.format("Battery %s created with capacity: %.0f, current energy level: %.0f", id, capacity, energyLevel));    
    }

    // Synchronized method for adding energy
    public synchronized void charge(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Energy amount must be positive.");
        }
        energyLevel = Math.min(energyLevel+amount, capacity);
        logger.info(String.format("Battery %s recharged by %.2f, current energy level: %.2f", id, amount, energyLevel));
    }

    // Synchronized method for consuming energy
    public synchronized void discharge(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Energy amount must be positive.");
        }
        /*if (amount > energyLevel) {
        *    throw new IllegalStateException("Insufficient energy to consume " + amount);
        *}
        */
        energyLevel = Math.max(energyLevel - amount, 0);
        // logger.info(String.format("Battery %d discharged by %.0f, current energy level: %.0f", id, amount, energyLevel));
    }
    
    public synchronized double getEnergyLevel() {
        return energyLevel;
    }

    public synchronized double getCapacity() {
        return capacity;
    }

    // Get percentage of energy level in string format
    public synchronized String getBatteryPercentage() {
        currentPercentage = (double) energyLevel / capacity * 100;
        return String.format("%.0f%%", currentPercentage);
    }
    
    // Get Id
    public String getId() {
    	return id;
    }
    
    // Get status
    public synchronized String getStatus() {
        // in json format
        return String.format("{battery_id=%s, capacity_kWh= %.1f, energy_level_kWh= %.1f}", id, capacity, energyLevel);
    }
}
