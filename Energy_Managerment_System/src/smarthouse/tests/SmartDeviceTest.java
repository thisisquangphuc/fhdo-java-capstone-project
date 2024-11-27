package smarthouse.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smarthouse.devices.SmartDevice;
import smarthouse.engergy.Battery;
import smarthouse.engergy.EnergyManager;
import smarthouse.engergy.EnergySource;

class SmartDeviceTest {

	private SmartDevice deviceWithBattery;
    private SmartDevice deviceWithoutBattery;
    private Battery battery;

    @BeforeEach
    void setUp() {
        // Initialize a battery and devices
        battery = new Battery(100, 50); // 100 kWh capacity, 50% initial energy
        deviceWithBattery = new SmartDevice("BatteryDevice", SmartDevice.EnergyType.DC, battery);
        deviceWithoutBattery = new SmartDevice("GridDevice", SmartDevice.EnergyType.AC);
    }

    @Test
    void testDeviceInitialization() {
        assertNotNull(deviceWithBattery.getDeviceId(), "Device ID should be initialized.");
        assertEquals("BatteryDevice", deviceWithBattery.getDeviceName(), "Device name should match.");
        assertEquals("DC", deviceWithBattery.getDeviceType(), "Energy type should match.");
    }

    @Test
    void testBatteryIntegration() {
        assertNotNull(deviceWithBattery.getBattery(), "Device should have an integrated battery.");
        assertNull(deviceWithoutBattery.getBattery(), "Device should not have an integrated battery.");
    }

    @Test
    void testSetEnergySource() {
        
        EnergySource energySource = new EnergySource("Grid", EnergySource.EnergyType.GRID, battery);
        EnergyManager.getInstance().addEnergySource(energySource);

        deviceWithoutBattery.setEnergySource(energySource);

        assertEquals(energySource.getSourceID(), deviceWithoutBattery.getEnergySourceID(),
                "Energy source ID should match.");
    }

    @Test
    void testTurnOnAndOff() {
        deviceWithBattery.turnOn();
        assertTrue(deviceWithBattery.isOn(), "Device should be on after turning on.");
        
        deviceWithBattery.turnOff();
        assertFalse(deviceWithBattery.isOn(), "Device should be off after turning off.");
    }

    @Test
    void testBatteryDischarge() {
        deviceWithBattery.setUseIntegratedBattery(true);
        deviceWithBattery.turnOn();

        double initialEnergy = battery.getEnergyLevel();

        try {
            Thread.sleep(3000); // Let the device consume some energy
        } catch (InterruptedException e) {
            fail("Thread interrupted during test.");
        }

        assertTrue(battery.getEnergyLevel() < initialEnergy, 
                   "Battery energy level should decrease after device operation.");
        
        deviceWithBattery.turnOff();
    }

    @Test
    void testEnergyConsumptionFromManager() {
        EnergySource grid = new EnergySource("Grid", EnergySource.EnergyType.GRID);
        EnergyManager.getInstance().addEnergySource(grid);
        deviceWithoutBattery.setEnergySource(grid);

        deviceWithoutBattery.turnOn();

        try {
            Thread.sleep(3000); // Let the device consume some energy
        } catch (InterruptedException e) {
            fail("Thread interrupted during test.");
        }

        assertTrue(deviceWithoutBattery.getConsumedEnergy() > 0,
                   "Device should consume energy from the assigned energy source.");
        
        deviceWithoutBattery.turnOff();
    }

    @Test
    void testStatusOutput() {
        String status = deviceWithBattery.getStatus();
        assertTrue(status.contains("deviceId"), "Status should contain device ID.");
        assertTrue(status.contains("deviceName"), "Status should contain device name.");
        assertTrue(status.contains("isActive"), "Status should indicate whether the device is active.");
        assertTrue(status.contains("battery"), "Status should include battery details.");
    }


}
