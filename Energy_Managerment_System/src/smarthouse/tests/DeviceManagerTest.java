package smarthouse.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smarthouse.devices.DeviceManager;
import smarthouse.devices.SmartDevice;
import smarthouse.engergy.Battery;

class DeviceManagerTest {
    
    private DeviceManager deviceManager;
    private SmartDevice deviceWithBattery;
    private SmartDevice deviceWithoutBattery;
    private Battery battery;

    @BeforeEach
    void setUp() {
        // Initialize the DeviceManager and devices
        deviceManager = new DeviceManager();
        battery = new Battery(100, 50); // 100 kWh capacity, 50% initial energy
        deviceWithBattery = new SmartDevice("BatteryDevice", SmartDevice.EnergyType.DC, battery);
        deviceWithoutBattery = new SmartDevice("GridDevice", SmartDevice.EnergyType.AC);
    }

    @Test
    void testAddDevice() {
        deviceManager.addDevice(deviceWithBattery);
        deviceManager.addDevice(deviceWithoutBattery);

        assertEquals(deviceWithBattery, deviceManager.getDeviceByID(deviceWithBattery.getDeviceId()),
                "Device with battery should be added successfully.");
        assertEquals(deviceWithoutBattery, deviceManager.getDeviceByID(deviceWithoutBattery.getDeviceId()),
                "Device without battery should be added successfully.");
    }

    @Test
    void testAddDuplicateDevice() {
        deviceManager.addDevice(deviceWithBattery);

        assertThrows(IllegalArgumentException.class, 
            () -> deviceManager.addDevice(deviceWithBattery),
            "Adding a duplicate device should throw an exception.");
    }

    @Test
    void testRemoveDevice() {
        deviceManager.addDevice(deviceWithBattery);
        deviceManager.removeDevice(deviceWithBattery.getDeviceId());

        assertNull(deviceManager.getDeviceByID(deviceWithBattery.getDeviceId()),
                "Device should be removed successfully.");
    }

    @Test
    void testRemoveNonexistentDevice() {
        assertThrows(IllegalArgumentException.class, 
            () -> deviceManager.removeDevice("nonexistentID"),
            "Removing a nonexistent device should throw an exception.");
    }

    @Test
    void testTurnOnDevice() {
        deviceManager.addDevice(deviceWithBattery);

        deviceManager.turnOnDevice(deviceWithBattery, true);
        assertTrue(deviceWithBattery.isOn(), "Device should be turned on.");
    }

    @Test
    void testTurnOffDevice() {
        deviceManager.addDevice(deviceWithBattery);

        deviceManager.turnOnDevice(deviceWithBattery, true);
        deviceManager.turnOffDevice(deviceWithBattery);
        assertFalse(deviceWithBattery.isOn(), "Device should be turned off.");
    }

    @Test
    void testTurnOnAlreadyOnDevice() {
        deviceManager.addDevice(deviceWithBattery);

        deviceManager.turnOnDevice(deviceWithBattery, true);
        deviceManager.turnOnDevice(deviceWithBattery, true); // Call turnOn again

        assertTrue(deviceWithBattery.isOn(), "Device should remain on.");
    }

    @Test
    void testTurnOffAlreadyOffDevice() {
        deviceManager.addDevice(deviceWithBattery);

        deviceManager.turnOffDevice(deviceWithBattery); // Call turnOff without turning on
        assertFalse(deviceWithBattery.isOn(), "Device should remain off.");
    }

    @Test
    void testGetAllDeviceStatus() {
        deviceManager.addDevice(deviceWithBattery);
        deviceManager.addDevice(deviceWithoutBattery);

        String status = deviceManager.getAllDeviceStatus();
        assertTrue(status.contains(deviceWithBattery.getDeviceId()), 
                "Status should include the ID of the device with a battery.");
        assertTrue(status.contains(deviceWithoutBattery.getDeviceId()), 
                "Status should include the ID of the device without a battery.");
    }

    @Test
    void testGetAllDevicesNames() {
        deviceManager.addDevice(deviceWithBattery);
        deviceManager.addDevice(deviceWithoutBattery);

        List<String> deviceNames = deviceManager.getAllDevicesNames();
        assertTrue(deviceNames.contains("BatteryDevice"), 
                "Device names list should include the device with a battery.");
        assertTrue(deviceNames.contains("GridDevice"), 
                "Device names list should include the device without a battery.");
    }

    @Test
    void testEmptyDeviceManagerStatus() {
        String status = deviceManager.getAllDeviceStatus();
        assertEquals("Device Manager Status:\n", status, 
                "Status should indicate no devices when the manager is empty.");
    }

    @Test
    void testEmptyDeviceNamesList() {
        List<String> deviceNames = deviceManager.getAllDevicesNames();
        assertTrue(deviceNames.isEmpty(), "Device names list should be empty when no devices are added.");
    }
}
