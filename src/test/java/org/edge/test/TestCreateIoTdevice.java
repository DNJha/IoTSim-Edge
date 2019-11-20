package org.edge.test;

import org.edge.core.iot.IoTDevice;
import org.edge.core.iot.TemperatureSensor;
import org.edge.network.NetworkModel;
import org.edge.network.NetworkType;
import org.edge.protocol.CommunicationProtocol;
import org.edge.protocol.XMPPProtocol;
import org.junit.Test;

public class TestCreateIoTdevice {

	
	@Test
	public void create(){
		NetworkModel networkModel=new NetworkModel(NetworkType.WIFI);
		CommunicationProtocol communicationProtocol=new XMPPProtocol();
		networkModel.setCommunicationProtocol(communicationProtocol);
		int id=1;
		//
		int data_frequency=1;
		int complexityOfDataPackage=1;
		int dataSize=1;
		//
		int maxBatteryCapacity=100;
		//
		int batteryDrainageRateForSensing=1;
		
		
		long capacityToStore=10;
		double transerFrequency=10d;
		
		int processingAbility=1;
		IoTDevice device=createLoTDevice(networkModel, id, data_frequency, complexityOfDataPackage, dataSize,
				maxBatteryCapacity, batteryDrainageRateForSensing, processingAbility,capacityToStore,transerFrequency);
		
		
	}
	private  IoTDevice createLoTDevice(NetworkModel networkModel, int id, int data_frequency,
			int complexityOfDataPackage, int dataSize, int maxBatteryCapacity, int batteryDrainageRateForSensing,
			int processingAbility, long capacityToStore, double transerFrequency) {
		// TODO Auto-generated method stub
		IoTDevice device2=new TemperatureSensor(
				networkModel,capacityToStore,transerFrequency);
		return device2;
	}

}
