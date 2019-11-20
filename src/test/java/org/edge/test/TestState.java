package org.edge.test;

import org.edge.core.iot.IoTDevice;
import org.edge.core.iot.TemperatureSensor;
import org.junit.Test;

public class TestState {

	
	
	  /**
     * 256 128 64  32 16  8 4 2 1
     * 1    1   1   1  1  1 1 1 1
     *  CONNECT|DISCONNECT 1
		SEND    2
     *  SENSE  4
		ACTUATE 8
		GENERATE 16
		PROCESS 32
		RECEIVE 64
		MOVING  128
		BATTERY_DRAINED 256
     * 
     * 
     * 
     */
	@Test
	public void testState() {
		try {
			Class<?> forName = Class.forName("org.edge.core.iot.TemperatureSensor");
			System.out.println(forName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testClass(){
		Class s=TemperatureSensor.class;
		Class iot=IoTDevice.class;
		System.out.println(iot.isAssignableFrom(s));
		
		
	}
	
	
}
