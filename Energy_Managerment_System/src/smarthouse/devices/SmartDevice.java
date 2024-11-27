package smarthouse.devices;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import smarthouse.engergy.Battery;
import smarthouse.engergy.EnergyManager;
import smarthouse.engergy.EnergySource;
import smarthouse.log.CustomLogger;

public class SmartDevice implements Runnable {
    // declare logger
    public static Logger logger = CustomLogger.getSysLogger();
    private final CustomLogger logManager = new CustomLogger();

    private String deviceName;
    private final String deviceId;
    private final String energyType;       // AC, DC, or other
    private Battery battery;                // Optional if device is battery powered
    private boolean isOn;
    private boolean useIntegratedBattery = false;
    private Thread deviceThread;
    private double consumedEnergy=0.0;
    private String energySourceID;          // EnergyManager instance
    private String sourceName;
    private double simulationRate = 1.0;    // Default simulation rate

    // Enum of energy types 
    public enum EnergyType {
        AC, DC
    }

    public SmartDevice(String name, EnergyType energyType, Battery battery) {
        this.deviceName = name;
        this.energyType = energyType.name();
        this.battery = battery;
        this.deviceId = UUID.randomUUID().toString();   //Assign a unique ID
        logManager.log(name,Level.INFO, String.format("Device created with name: %s, energy type: %s, battery: %s", name, energyType, battery));
    }

    public SmartDevice(String name, EnergyType energyType) {
        this.deviceName = name;
        this.energyType = energyType.name();
        this.deviceId = UUID.randomUUID().toString();   //Assign a unique ID
        logManager.log(name,Level.INFO, String.format("Device created with name: %s, energy type: %s", name, energyType));
    }
    
    // Getter for deviceId
    public String getDeviceId() {
        return deviceId;
    }

    // Getter and Setter for deviceName
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    // Getter and Setter for isActive
    public boolean isOn() {
        return isOn;
    }

    // Getter and Setter for Battery
    public Battery getBattery() {
        return battery;
    }
    
    public void setBattery(Battery battery) {
        this.battery = battery;
    }
    
    public synchronized void setSimulationRate(double rate) {
        if (rate < 0 || rate > 1) {
            throw new IllegalArgumentException("Simulation rate must be between 0 and 1.");
        }
        this.simulationRate = rate;
        logManager.log(deviceName,Level.INFO, String.format("Simulation rate set to: %s", rate));
    }

    public double getSimulationRate() {
        return simulationRate;
    }

    // get consumedEnergy 
    public synchronized double getConsumedEnergy() {
        return consumedEnergy;
    }

    public String getDeviceType() {
        return energyType;
    }

    private void validateEnergySource(EnergySource energySource) {
        if (energySource != null) {
            // Get sourceType
            String sourceType = energySource.getSourceType();
            // Apply validation rules
            if ("AC".equals(energyType) && !"GRID".equals(sourceType)) {
                throw new IllegalArgumentException(
                    "Devices with AC energy type can only use GRID power."
                );
            }
            if ("DC".equals(energyType) && !("SOLAR".equals(sourceType) || "BATTERY".equals(sourceType))) {
                throw new IllegalArgumentException(
                    "Devices with DC energy type can only use SOLAR or BATTERY power."
                );
            }
        } else {
            throw new IllegalArgumentException("Energy source cannot be null.");
        }
    }

    public synchronized void setEnergySource(EnergySource energySource) {
        validateEnergySource(energySource);
        // get ID of the energy source
        this.energySourceID = energySource.getSourceID();
        // get sourceName from id 
        this.sourceName = EnergyManager.getInstance().getEnergySourceByID(energySourceID).getSourceName();
        logManager.log(deviceName,Level.INFO, String.format("Energy source set to: %s", sourceName));
    }

    public String getEnergySourceID() {
        return energySourceID;
    }

    public synchronized void setEnergySourceID(String energySourceID) {
        validateEnergySource(EnergyManager.getInstance().getEnergySourceByID(energySourceID));
        this.energySourceID = energySourceID;
        // get sourceName from id 
        this.sourceName = EnergyManager.getInstance().getEnergySourceByID(energySourceID).getSourceName();
        logManager.log(deviceName,Level.INFO, String.format("Energy source set to: %s", sourceName));
    }

    // Set the useIntegratedBattery
    public void setUseIntegratedBattery(boolean useIntegratedBattery) {
        this.useIntegratedBattery = useIntegratedBattery;
    }

    public void turnOn() {
        if (!isOn) {
            logManager.log(deviceName,Level.INFO, "Device TURNED ON");
            // Print status of the device using getStatus()
            logManager.log(deviceName,Level.INFO, String.format("Device status: %s", getStatus()));
            isOn = true;
            deviceThread = new Thread(this);
            deviceThread.start();
        }
    }

    public void turnOff() {
        if (isOn) {
            isOn = false;
            this.useIntegratedBattery = false;
            // deviceThread.interrupt();
            logManager.log(deviceName,Level.INFO, "Device TURNED OFF");
            logManager.log(deviceName,Level.INFO, String.format("Device status: %s", getStatus()));
        }
    }

    @Override
    public void run() {
        while (isOn) {
            try {
                // Simulate random energy consumption rate (scaled by the simulation rate)
                //int randomConsumption = new Random().nextInt(5) + 1;        // Consume between 1 and 5 units of energy per cycle
                double randomConsumption = new Random().nextDouble(5) + 1;        // Consume between 1 and 5 units of energy per cycle
                if (useIntegratedBattery) {
                    //check if battery is null
                    if (battery == null) {
                        logger.warning(String.format("Device [%s] has no battery!", deviceName));
                        turnOff();
                    } else {
                        if (!battery.discharge(randomConsumption)) {
                            logger.warning(String.format("Battery of [%s] is empty!", deviceName));
                            turnOff();
                        } else {
                            logger.info(String.format("Device [%s] consumed %.2f kWh from integrated battery", deviceName, randomConsumption));
                            // logManager.log(deviceName,Level.INFO, String.format("Device [%s] consumed %.2f kWh from integrated battery", deviceName, randomConsumption));
                        }
                    }
                } else {
                    boolean success = EnergyManager.getInstance().distributeEnergy(this, randomConsumption);
                    if (success) {
                        consumedEnergy += randomConsumption;
                        logger.info(String.format("Device [%s] consumed %.2f kWh from [%s]", deviceName, randomConsumption, sourceName));
                        // logManager.log(deviceName,Level.INFO, String.format("Device [%s] consumed %.2f kWh from [%s]", deviceName, randomConsumption, sourceName));
                    } else {
                        turnOff();
                        // logger.warning(String.format("Device %s failed to consume %.2f kWh from %s", deviceName, randomConsumption, sourceName));
                    }
                }
                
                // Adjust the sleep time based on the simulation rate
                int sleepTime = (int) ((new Random().nextInt(2000) + 1000) * simulationRate); // Adjust sleep time based on simulation rate
                
                Thread.sleep(sleepTime); // Sleep for adjusted time

            } catch (InterruptedException e) {
                // Thread was interrupted, exit the loop
                logger.severe(String.format("Device [%s] interrupted: %s", deviceName, e.getMessage()));
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public String getStatus() {
        return "{" +
                "\"deviceId\":\"" + deviceId + "\"," +
                "\"deviceName\":\"" + deviceName + "\"," +
                "\"isActive\":" + isOn + "," +
                "\"energyType\":\"" + energyType + "\"," +
                "\"batteryStatus\":" + (battery != null ? battery.getStatus() : "\"No Battery\"") + "," +
                // "\"useIntegratedBattery\":" + useIntegratedBattery + "," +
                // "\"energySourceID\":\"" + (energySourceID != null ? energySourceID : "None") + "\"," +
                "\"energySourceName\":\"" + (sourceName != null ? sourceName : "None") + "\"," +
                "\"consumedEnergy\":" + String.format("%.2f", consumedEnergy) + "," +
                "\"simulationRate\":" + simulationRate +
                "}";
    }
}
