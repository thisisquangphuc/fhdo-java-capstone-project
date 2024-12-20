package smarthouse.engergy;

import java.time.LocalTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import smarthouse.log.CustomLogger;
import smarthouse.util.ConfigManager;

public class EnergySource {
    //declare logger
    private static final Logger logger = CustomLogger.getSysLogger();
    private final CustomLogger logManager = new CustomLogger();

	private final String sourceID;          // Unique identifier for the energy source
    private final String sourceName;        // Name of the energy source
    private final String sourceType;        // Type of energy source (e.g., Solar, Grid, Battery)
    // private double capacity;                // Total energy capacity in kWh
    // private double availableEnergy;         // Current available energy in kWh

    private double energyConsumed;          // For tracking energy consumed for GRID sources

    // Energy Battery
    private Battery battery;
    private boolean isRecharging = false;
    private Thread rechargeThread;

    // Time setting
    private int charging_start_time = ConfigManager.getInstance().getInt("charging.start_time", 10);
    private int charging_end_time = ConfigManager.getInstance().getInt("charging.end_time", 17);

    private int simulationPeriod = ConfigManager.getInstance().getInt("charging.simulation.period", 1);
    private double simulationAmount = ConfigManager.getInstance().getDouble("charging.simulation.amount", 10);
    // define Enger Type
    public enum EnergyType {
        SOLAR, GRID, BATTERY
    }

    // Constructor - use for power source having battery
    public EnergySource(String sourceName, EnergyType sourceType, Battery battery) {
        if (battery.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        this.sourceID = UUID.randomUUID().toString();
        this.sourceName = sourceName;
        this.sourceType = sourceType.name();
        this.battery = battery;
        // this.availableEnergy = battery.getEnergyLevel();
        logManager.log(sourceName,Level.INFO, String.format("Energy source created with name: %s, energy type: %s, battery: %s", sourceName, sourceType, battery.getStatus()));
    }

    // Constructor - use for GRID power
    public EnergySource(String sourceName, EnergyType sourceType) {
        this.sourceName = sourceName;
        this.sourceType = sourceType.name();
        this.sourceID = UUID.randomUUID().toString();
        logManager.log(sourceName,Level.INFO, String.format("Energy source created with name: %s, energy type: %s", sourceName, sourceType));
    }

    // Getters
    public String getSourceID() {
        return sourceID;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public synchronized double getAvailableEnergy() {
        if (sourceType.equals(EnergyType.BATTERY.name()) || sourceType.equals(EnergyType.SOLAR.name())) {
            return battery.getEnergyLevel();
        }
        throw new IllegalArgumentException("Cannot get available energy for " + sourceType + " energy source.");
    }

    public synchronized double getCapacity() {
        if (sourceType.equals(EnergyType.BATTERY.name()) || sourceType.equals(EnergyType.SOLAR.name())) {
            return battery.getCapacity();
        }
        throw new IllegalArgumentException("Cannot get capacity for " + sourceType + " energy source.");
    }

    // Consume energy
    public synchronized boolean consumeEnergy(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        if (sourceType.equals(EnergyType.BATTERY.name()) || sourceType.equals(EnergyType.SOLAR.name())) {
            /*if (amount > battery.getEnergyLevel()) {
             *   return false; // Insufficient energy for BATTERY or SOLAR
             *}
             */ 
        	boolean result = battery.discharge(amount);
        	if (!result) {
        	    logger.warning(String.format("Battery of [%s] is empty!", sourceName));
        	} else {
                // logManager.log(sourceName, Level.INFO, String.format("Energy battery has been consumed: %s", amount));
            }
        	return result;
        }

        // For GRID, just record the energy consumption
        if (sourceType.equals(EnergyType.GRID.name())) {
            energyConsumed += amount;
            // logManager.log(sourceName, Level.INFO, String.format("Energy battery has been consumed: %s", amount));
            return true; // Always succeed for GRID, since it doesn't have a limit
        }

        return false; // If it's an unsupported type
    }

    // Recharge energy (only for BATTERY sources)
    public synchronized void recharge(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        if (sourceType.equals(EnergyType.BATTERY.name())) {
            battery.charge(amount);
        } else {
            throw new UnsupportedOperationException("Recharging is only supported for BATTERY/SOLARsources.");
        }
    }

    public synchronized void startRecharging() {
        if (rechargeThread != null && rechargeThread.isAlive()) {
            logger.info(String.format("Recharging thread for %s is already running.", sourceID));
            return;
        }
    
        isRecharging = true;
        logger.info(String.format("Source [%s] is recharging...", sourceName));
        rechargeThread = new Thread(() -> {
            while (isRecharging && isInRechargeTimeRange()) {
                if (battery != null) {
                    double amount = simulationAmount;
                    // Use the Battery's charge method
                    battery.charge(amount); // Recharge by 1 kWh (or any other unit)
                    logger.info(String.format("Battery of source [%s] recharged by %.2f kWh. Current level: %.2f kWh", sourceName, amount, battery.getEnergyLevel()));
                } else {
                    logger.warning(String.format("No battery available for source %s. Skipping recharge.", sourceID));
                }
    
                try {
                    int sleepTime = 1000 * simulationPeriod;
                    Thread.sleep(sleepTime); // Simulate a delay in recharging (1 second)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return; // Exit the loop if interrupted
                }
            }

            stopRecharging();

            if (!isInRechargeTimeRange()) {
                logger.info(String.format("Recharging thread for %s stopped: OUT OF RECHARGE TIME RANGE", sourceID));
            } else {
                logger.info(String.format("Recharging thread for %s stopped: RECIEVED STOP SIGNAL", sourceID));
            }
        });
    
        rechargeThread.start();
    }

    public synchronized void stopRecharging() {
        isRecharging = false;
        logger.info(String.format("Source [%s] recharging stopped.", sourceName ));
        if (rechargeThread != null) {
            rechargeThread.interrupt();
        }
    }

    public boolean isInRechargeTimeRange() {
        LocalTime now = LocalTime.now();
        return now.isAfter(LocalTime.of(charging_start_time, 0)) && now.isBefore(LocalTime.of(charging_end_time, 0));
    }

    // Get energy consumed (for GRID source)
    public synchronized double getEnergyConsumed() {
        if (sourceType.equals(EnergyType.GRID.name())) {
            return energyConsumed;
        }
        throw new IllegalArgumentException("Energy consumption tracking is only applicable for GRID source.");
    }

    // Status string
    public synchronized String getStatus() {
        return "{" +
                "\"sourceName\":\"" + sourceName + 		"\"," +
                "\"sourceType\":\"" + sourceType + 		"\"," +
                "\"isRecharging\":\"" + isRecharging + 	"\"," +
                // "\"rechargeRate_kWh\":" + rechargeRate + "," +
                "\"batteryStatus\":\"" + (battery != null ? battery.getStatus() : "No Battery") + "\"," +
                "}";
    }
}
