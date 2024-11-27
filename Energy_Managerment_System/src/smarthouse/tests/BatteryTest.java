package smarthouse.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smarthouse.engergy.Battery;

class BatteryTest {

	private Battery battery;

    @BeforeEach
    void setUp() {
        // Initialize a new battery with 100 kWh capacity and 50% energy level
        battery = new Battery(100, 50);
    }

    @Test
    void testBatteryCreation() {
        assertEquals(100, battery.getCapacity(), "Capacity should be correctly set.");
        assertEquals(50, battery.getEnergyLevel(), "Initial energy level should be 50 kWh.");
        assertEquals("50%", battery.getBatteryPercentage(), "Initial energy percentage should be 50%.");
    }

    @Test
    void testChargeWithinCapacity() {
        battery.charge(20); // Add 20 kWh
        assertEquals(70, battery.getEnergyLevel(), "Energy level should be 70 kWh after charging 20 kWh.");
        assertEquals("70%", battery.getBatteryPercentage(), "Energy percentage should be 70%.");
    }

    @Test
    void testChargeBeyondCapacity() {
        battery.charge(60); // Attempt to charge beyond capacity
        assertEquals(100, battery.getEnergyLevel(), "Energy level should not exceed capacity.");
        assertEquals("100%", battery.getBatteryPercentage(), "Energy percentage should not exceed 100%.");
    }

    @Test
    void testDischargeWithinLimit() {
        boolean result = battery.discharge(20); // Consume 20 kWh
        assertTrue(result, "Discharge should succeed when energy is sufficient.");
        assertEquals(30, battery.getEnergyLevel(), "Energy level should be 30 kWh after discharging 20 kWh.");
        assertEquals("30%", battery.getBatteryPercentage(), "Energy percentage should be 30%.");
    }

    @Test
    void testDischargeBeyondLimit() {
        boolean result = battery.discharge(60); // Attempt to consume more than available
        assertTrue(result, "Discharge should succeed but energy level should be 0 when overdrawn.");
        assertEquals(0, battery.getEnergyLevel(), "Energy level should not drop below 0.");
        assertEquals("0%", battery.getBatteryPercentage(), "Energy percentage should be 0%.");
    }

    @Test
    void testDischargeFromEmptyBattery() {
        battery.discharge(50); // Fully discharge the battery
        boolean result = battery.discharge(10); // Try discharging again
        assertFalse(result, "Discharge should fail when the battery is empty.");
        assertEquals(0, battery.getEnergyLevel(), "Energy level should remain at 0.");
        assertEquals("0%", battery.getBatteryPercentage(), "Energy percentage should remain at 0%.");
    }

    @Test
    void testInvalidCharge() {
        assertThrows(IllegalArgumentException.class, () -> battery.charge(-10),
                "Charging with a negative amount should throw an exception.");
    }

    @Test
    void testInvalidDischarge() {
        assertThrows(IllegalArgumentException.class, () -> battery.discharge(-10),
                "Discharging with a negative amount should throw an exception.");
    }


}
