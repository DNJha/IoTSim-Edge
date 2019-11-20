package org.edge.core.iot;

import org.cloudbus.cloudsim.UtilizationModelFull;
import org.edge.core.feature.Battery;
import org.edge.core.feature.EdgeLet;
import org.edge.core.feature.IoTType;
import org.edge.network.NetworkModel;

public class VoiceSensor extends IoTDevice {

	static EdgeLet edgeLet;
	static {
		edgeLet = new EdgeLet(EdgeLet.id++,500, 1, 10, 10, new UtilizationModelFull(), new UtilizationModelFull(),
				new UtilizationModelFull());
	}
	public static final double DATA_GENERATION_TIME = 1;

	public static final double DATA_FREQUENCY = 2;
	public static final int COMPLEXITY_OF_DATAPACKAGE = 1;

	public static final int DATA_SIZE = 2;
	public static final int MAX_BATTERY_CAPACITY = 100;
	public static final double BATTERY_DRAINAGE_RATE = 0.5d;

	public static final int PROCESSING_ABILITY = 1;

	public VoiceSensor(NetworkModel networkModel, long capacityToStore, double transfer_frequency) {
		super(IoTType.ENVIRONMENTAL_SENSOR, "lightSensor", DATA_FREQUENCY, DATA_GENERATION_TIME,
				COMPLEXITY_OF_DATAPACKAGE, DATA_SIZE, networkModel, MAX_BATTERY_CAPACITY, BATTERY_DRAINAGE_RATE,
				PROCESSING_ABILITY, capacityToStore, transfer_frequency, edgeLet);
	}

	public VoiceSensor(NetworkModel networkModel) {
		super(IoTType.ENVIRONMENTAL_SENSOR, "lightSensor", DATA_FREQUENCY, DATA_GENERATION_TIME,
				COMPLEXITY_OF_DATAPACKAGE, DATA_SIZE, networkModel, MAX_BATTERY_CAPACITY, BATTERY_DRAINAGE_RATE,
				PROCESSING_ABILITY, edgeLet);
	}

	@Override
	public double getNetworkDelay() {
		return 0;
	}

	@Override
	public double getTransmissionSpeed() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public EdgeLet processData(EdgeLet generated_data) {
		// TODO Auto-generated method stub
		return generated_data;
	}

	@Override
	public boolean updateBatteryByProcessing(Battery battery) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatteryByTransmission(Battery battery) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean updateBatteryBySensing(Battery battery) {
		battery.setCurrentCapacity(battery.getCurrentCapacity()-battery_drainage_rate);
		if(battery.getCurrentCapacity()<=0)
			return true;
		return false;
	}

	@Override
	public void shutdownEntity() {

	}

	@Override
	public boolean updateBatteryByActuating(Battery battery) {
		// TODO Auto-generated method stub
		return false;
	}

}
