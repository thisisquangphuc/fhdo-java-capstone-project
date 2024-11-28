package smarthouse.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
	BatteryTest.class,
	SmartDeviceTest.class,
	EnergySourceTest.class,
	DeviceManagerTest.class,
	EnergyManagerTest.class,
	EnergySourceManagementUITest.class,
	UIManagingSmartObjectsTest.class} )
public class AllTests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}

}

