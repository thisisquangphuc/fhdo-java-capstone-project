package smarthouse.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smarthouse.devices.SmartDevice;
import smarthouse.engergy.Battery;
import smarthouse.engergy.EnergyManager;
import smarthouse.engergy.EnergySource;

class EnergyManagerTest {
    
    private EnergyManager energyManager;
    private EnergySource batterySource;
    private EnergySource solarSource;
    private EnergySource gridSource;

    @BeforeEach
    void setUp() {
        energyManager = EnergyManager.getInstance(); // Singleton instance
        Battery battery = new Battery(100.0, 50); // 100 kWh capacity, 50% energy level
        batterySource = new EnergySource("Battery Source", EnergySource.EnergyType.BATTERY, battery);
        solarSource = new EnergySource("Solar Source", EnergySource.EnergyType.SOLAR, battery);
        gridSource = new EnergySource("Grid Source", EnergySource.EnergyType.GRID);

        energyManager.addEnergySource(batterySource);
        energyManager.addEnergySource(solarSource);
        energyManager.addEnergySource(gridSource);
    }

    @AfterEach
    void tearDown() {
        // Reset singleton state for isolated tests
        energyManager = null;
    }

    @Test
    void testAddEnergySource() {
        EnergySource newSource = new EnergySource("New Battery Source", EnergySource.EnergyType.BATTERY, new Battery(50.0, 60));
        energyManager.addEnergySource(newSource);

        assertNotNull(energyManager.getEnergySourceByID(newSource.getSourceID()));
        assertEquals("New Battery Source", newSource.getSourceName());
        energyManager.removeEnergySource(newSource.getSourceID());
    }

    @Test
    void testRemoveEnergySource() {
        String sourceID = batterySource.getSourceID();
        assertTrue(energyManager.removeEnergySource(sourceID));
        assertNull(energyManager.getEnergySourceByID(sourceID));
    }

    @Test
    void testRemoveNonExistentEnergySource() {
        assertFalse(energyManager.removeEnergySource("nonexistent-id"));
    }

    @Test
    void testGetEnergySourceByID() {
        EnergySource source = energyManager.getEnergySourceByID(batterySource.getSourceID());
        assertEquals("Battery Source", source.getSourceName());
    }

    @Test
    void testGetEnergySourceStatus() {
        String status = energyManager.getEnergySourceStatus(batterySource.getSourceID());
        assertNotNull(status);
        assertTrue(status.contains("\"sourceName\":\"Battery Source\""));
    }

//    @Test
//    void testDistributeEnergySuccess() {
//        SmartDevice device = new SmartDevice("Device1", SmartDevice.EnergyType.DC, new Battery(50.0, 100));
//        boolean distributed = energyManager.distributeEnergy(device, 10.0); // Consuming 10 kWh
//        assertTrue(distributed);
//        assertEquals(40.0, energyManager.get
//        energyManager.removeEnergySource(newSource.getSourceID());
//    }

    @Test
    void testDistributeEnergyInsufficientBattery() {
        SmartDevice device = new SmartDevice("Device2", SmartDevice.EnergyType.DC, new Battery(50.0, 30));
        boolean distributed = energyManager.distributeEnergy(device, 100.0); // Attempting to consume more than available
        assertFalse(distributed);
    }

    @Test
    void testGetEnergySourceNames() {
        List<String> names = energyManager.getEnergySourceNames();
        assertTrue(names.contains("Battery Source"));
        assertTrue(names.contains("Solar Source"));
        assertTrue(names.contains("Grid Source"));
    }

}
