package smarthouse.devices;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import smarthouse.engergy.Battery;
import smarthouse.engergy.EnergyManager;
import smarthouse.engergy.EnergySource;
import smarthouse.log.CustomLogger;

public class SmartDevice implements Runnable {
    // declare logger
    public static Logger logger = CustomLogger.getLogger();
    
    private String deviceName;
    private final String deviceId;
    private final String engergyType;       // AC, DC, or other
    private Battery battery;                // Optional if device is battery powered
    private boolean isOn;
    private boolean useIntegratedBattery = false;
    private Thread deviceThread;
    private double consumedEnergy;
    private String energySourceID;          // EnergyManager instance
    private String sourceName;
    private double simulationRate = 1.0;    // Default simulation rate

    // Enum of energy types 
    public enum EnergyType {
        AC, DC
    }

    public SmartDevice(String name, EnergyType engergyType, Battery battery) {
        this.deviceName = name;
        this.engergyType = engergyType.name();
        this.battery = battery;
        this.deviceId = UUID.randomUUID().toString();   //Assign a unique ID
    }

    public SmartDevice(String name, EnergyType engergyType) {
        this.deviceName = name;
        this.engergyType = engergyType.name();
        this.deviceId = UUID.randomUUID().toString();   //Assign a unique ID
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
    }

    public double getSimulationRate() {
        return simulationRate;
    }

    // get consumedEnergy 
    public synchronized double getConsumedEnergy() {
        return consumedEnergy;
    }

    public String getDeviceType() {
        return engergyType;
    }

    public synchronized void setEnergySource(EnergySource energySource) {
        if (energySource == null) {
            throw new IllegalArgumentException("Energy source cannot be null.");
        }
        // get ID of the energy source
        this.energySourceID = energySource.getSourceID();
        // get sourceName from id 
        this.sourceName = EnergyManager.getInstance().getEnergySourceByID(energySourceID).getSourceName();
    }

    public String getEnergySourceID() {
        return energySourceID;
    }

    public synchronized void setEnergySourceID(String energySourceID) {
        this.energySourceID = energySourceID;
        // get sourceName from id 
        this.sourceName = EnergyManager.getInstance().getEnergySourceByID(energySourceID).getSourceName();
    }

    // Set the useIntegratedBattery
    public void setUseIntegratedBattery(boolean useIntegratedBattery) {
        this.useIntegratedBattery = useIntegratedBattery;
    }

    public void turnOn() {
        if (!isOn) {
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
                        }
                    }
                } else {
                    boolean success = EnergyManager.getInstance().distributeEnergy(this, randomConsumption);
                    if (success) {
                        consumedEnergy += randomConsumption;
                        logger.info(String.format("Device [%s] consumed %.2f kWh from [%s]", deviceName, randomConsumption, sourceName));
                    } else {
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
                "\"energyType\":\"" + engergyType + "\"," +
                "\"batteryStatus\":" + (battery != null ? battery.getStatus() : "\"No Battery\"") + "," +
                // "\"useIntegratedBattery\":" + useIntegratedBattery + "," +
                // "\"energySourceID\":\"" + (energySourceID != null ? energySourceID : "None") + "\"," +
                "\"energySourceName\":\"" + (sourceName != null ? sourceName : "None") + "\"," +
                "\"consumedEnergy\":" + consumedEnergy + "," +
                "\"simulationRate\":" + simulationRate +
                "}";
    }
}
