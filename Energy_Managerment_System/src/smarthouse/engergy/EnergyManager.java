package smarthouse.engergy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import smarthouse.devices.SmartDevice;
import smarthouse.log.CustomLogger;

public class EnergyManager {
    // Static variable to hold the single instance
    private static EnergyManager instance;

    private final Map<String, EnergySource> energySources = new HashMap<>();
    private static final Logger logger = CustomLogger.getLogger();
    
    // Private constructor
    private EnergyManager() {}
    
    public static synchronized EnergyManager getInstance() {
        if (instance == null) {
            instance = new EnergyManager();
        }
        return instance;
    }

    // Add a new energy source
    public synchronized void addEnergySource(EnergySource source) {
        if (source == null) {
            throw new IllegalArgumentException("Energy source cannot be null.");
        }
        energySources.put(source.getSourceID(), source);
        logger.info(String.format("Energy source status: %s", source.getStatus()));
    }

    // Remove an energy source by ID
    public synchronized boolean removeEnergySource(String sourceID) {
        if (energySources.containsKey(sourceID)) {
            energySources.remove(sourceID);
            logger.info(String.format("Energy source removed: %s", sourceID));
            return true;
        }
        logger.warning(String.format("Energy source not found: %s", sourceID));
        return false;
    }

    // Get an energy source by ID
    public synchronized EnergySource getEnergySourceByID(String sourceID) {
        return energySources.get(sourceID);
    }
    
    // Check if the energy source has sufficient energy to meet the required amount
    public synchronized boolean hasSufficientEnergy(EnergySource source, double requiredEnergy) {
        return source.getAvailableEnergy() > 0;
    }

    // Distribute energy to a device
    public synchronized boolean distributeEnergy(SmartDevice device, double energyRequired) {
        String sourceID = device.getEnergySourceID();
        EnergySource source = energySources.get(sourceID);
        if (source == null) {
            logger.warning(String.format("No energy source found for device %s with source ID %s", device.getDeviceId(), sourceID));
            return false;
        }
        // Check if the energy source has enough energy for Battery or Solar
        if (source.getSourceType().equals(EnergySource.EnergyType.BATTERY.name()) ||
            source.getSourceType().equals(EnergySource.EnergyType.SOLAR.name())) {
            // Consume energy from Battery or Solar
            if (hasSufficientEnergy(source, energyRequired)) {
                source.consumeEnergy(energyRequired);
                logger.info(String.format("Energy source status: %s", getEnergySourceStatus(sourceID)));
                return true;
            } else {
                logger.warning(String.format("Energy source %s does not have sufficient energy.", source.getSourceType()));
                return false;
            }
        } else {
            // For GRID, just record consumption without affecting available energy
            source.consumeEnergy(energyRequired);
            // logger.info(String.format("Energy source %s consumed: %s", source.getSourceType(), energyRequired));
            return true;
        }     
    }

    // Recharge the energy source
    public synchronized void manageRecharging(String sourceID, boolean start) {
        EnergySource source = energySources.get(sourceID);
        if (source == null) {
            logger.warning(String.format("Energy source not found: %s", sourceID));
            return;
        }
    
        if (start) {
            logger.info(String.format("Recharging energy source: %s --> ON", source.getSourceName()));
            source.startRecharging();
        } else {
            source.stopRecharging();
        }
    }

    // Get the current status of the energy source
    public synchronized String getEnergySourceStatus(String sourceID) {
        EnergySource source = energySources.get(sourceID);
        return source.getStatus();
    }

    // Get all energy sources names in a list
    public synchronized List<String> getEnergySourceNames() {
        List<String> sourceNames = new ArrayList<>();
        for (EnergySource source : energySources.values()) {
            sourceNames.add(source.getSourceName());
        }
        return sourceNames;
    }

    // Get list of all energy source IDs
    public synchronized List<String> getEnergySourceIDs() {
        List<String> sourceIDs = new ArrayList<>();
        for (EnergySource source : energySources.values()) {
            sourceIDs.add(source.getSourceID());
        }
        return sourceIDs;
    }
}