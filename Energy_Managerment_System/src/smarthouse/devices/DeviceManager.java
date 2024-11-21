package smarthouse.devices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import smarthouse.log.CustomLogger;

public class DeviceManager {
    //declare logger object
    private static final Logger logger = CustomLogger.getLogger();

    private final Map<String, SmartDevice> devices; // Map of deviceID to SmartDevice

    public DeviceManager() {
        this.devices = Collections.synchronizedMap(new HashMap<>());
    }

    // Add a new device
    public synchronized void addDevice(SmartDevice device) {
        if (device == null || devices.containsKey(device.getDeviceId())) {
            throw new IllegalArgumentException("Invalid or duplicate device.");
        }
        devices.put(device.getDeviceId(), device);
    }

    // Remove a device
    public synchronized void removeDevice(String deviceID) {
        if (!devices.containsKey(deviceID)) {
            throw new IllegalArgumentException("Device not found.");
        }
        devices.remove(deviceID);
    }

    public void turnOnDevice(SmartDevice device,boolean useIntegratedBattery) {
        if (!device.isOn()) {
            if (useIntegratedBattery) {
                device.setUseIntegratedBattery(true);
            } else {
                device.setUseIntegratedBattery(false);
            }
            device.turnOn();
            logger.info(String.format("Device %s is now ON.", device.getDeviceName()));
        } else {
            logger.info(String.format("Device %s is already ON.", device.getDeviceName()));
        }
    }

    public void turnOffDevice(SmartDevice device) {
        if (device.isOn()) {
            device.turnOff();
            logger.info(String.format("Device %s is now OFF.", device.getDeviceName()));
        } else {
            logger.info(String.format("Device %s is already OFF.", device.getDeviceName()));
        }
    }
    
    // Retrieve a device by ID
    public synchronized SmartDevice getDeviceByID(String deviceID) {
        return devices.get(deviceID);
    }

    // Get status of all devices
    public synchronized String getAllDeviceStatus() {
        StringBuilder status = new StringBuilder("Device Manager Status:\n");
        for (SmartDevice device : devices.values()) {
            status.append(" - ").append(device.getStatus()).append("\n");
        }
        return status.toString();
    }

    // Get all devices return in a list 
    public synchronized List<String> getAllDevicesNames() {
        List<String> devicesList = new ArrayList<>();
        for (SmartDevice device : devices.values()) {
            devicesList.add(device.getDeviceName());
        }
        return devicesList;
    }
}
