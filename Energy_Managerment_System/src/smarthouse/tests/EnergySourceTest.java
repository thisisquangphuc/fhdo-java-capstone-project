package smarthouse.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smarthouse.engergy.Battery;
import smarthouse.engergy.EnergySource;

class EnergySourceTest {
    
    private Battery battery50Percent; // Battery initialized at 50% capacity
    private Battery battery30Percent; // Battery initialized at 30% capacity
    private Battery battery90Percent; // Battery initialized at 90% capacity
    private EnergySource batterySource;
    private EnergySource gridSource;

    @BeforeEach
    void setUp() {
        battery50Percent = new Battery(100.0, 50); // Capacity: 100 kWh, 50% energy level
        battery30Percent = new Battery(100.0, 30); // Capacity: 100 kWh, 30% energy level
        battery90Percent = new Battery(100.0, 90); // Capacity: 100 kWh, 90% energy level
        batterySource = new EnergySource("Battery Power", EnergySource.EnergyType.BATTERY, battery50Percent);
        gridSource = new EnergySource("Grid Power", EnergySource.EnergyType.GRID);
    }

    @Test
    void testConstructorForBattery() {
        assertEquals("Battery Power", batterySource.getSourceName());
        assertEquals(EnergySource.EnergyType.BATTERY.name(), batterySource.getSourceType());
        assertNotNull(batterySource.getSourceID());
        assertEquals(50.0, batterySource.getAvailableEnergy()); // 50% of 100 kWh
    }

    @Test
    void testConstructorForGrid() {
        assertEquals("Grid Power", gridSource.getSourceName());
        assertEquals(EnergySource.EnergyType.GRID.name(), gridSource.getSourceType());
        assertNotNull(gridSource.getSourceID());
    }

    @Test
    void testConsumeEnergyForBattery() {
        assertTrue(batterySource.consumeEnergy(20.0)); // Consuming 20 kWh
        assertEquals(30.0, batterySource.getAvailableEnergy()); // Remaining: 50 - 20 = 30 kWh
    }

    @Test
    void testRechargeBattery() {
        batterySource.recharge(20.0); // Recharge by 20 kWh
        assertEquals(70.0, batterySource.getAvailableEnergy()); // Remaining: 50 + 20 = 70 kWh
    }

    @Test
    void testRechargeBatteryOverCapacity() {
        EnergySource source = new EnergySource("Battery Power", EnergySource.EnergyType.BATTERY, battery90Percent);
        source.recharge(20.0); // Attempt to recharge by 20 kWh
        assertEquals(100.0, source.getAvailableEnergy()); // Max capacity is 100 kWh
    }

    @Test
    void testEnergyConsumedForGrid() {
        assertTrue(gridSource.consumeEnergy(50.0)); // Always succeeds
        assertEquals(50.0, gridSource.getEnergyConsumed()); // Energy consumed: 50 kWh
    }

    @Test
    void testGetStatus() {
        String status = batterySource.getStatus();
        assertTrue(status.contains("\"sourceName\":\"Battery Power\""));
        assertTrue(status.contains("\"sourceType\":\"BATTERY\""));
        assertTrue(status.contains("\"batteryStatus\":"));
    }
}
