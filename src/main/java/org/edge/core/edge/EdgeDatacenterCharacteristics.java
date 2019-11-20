package org.edge.core.edge;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.edge.core.iot.IoTDevice;
import org.edge.protocol.CommunicationProtocol;


/**
 * 
 * @author cody
 *
 */
public class EdgeDatacenterCharacteristics extends DatacenterCharacteristics{

	private Class<? extends CommunicationProtocol>[] communicationProtocolSupported;
	private Class<? extends IoTDevice>[] ioTDeviceSupported;
	
	
	
	
	public EdgeDatacenterCharacteristics(String architecture, String os, String vmm,
			List<? extends EdgeDevice> hostList,
			double timeZone, double costPerSec, double costPerMem, double costPerStorage, double costPerBw,
			Class<? extends CommunicationProtocol>[] communicationProtocolSupported,
			Class<? extends IoTDevice>[] ioTDeviceSupported
			) {
		super(architecture, os, vmm, hostList, timeZone, costPerSec, costPerMem, costPerStorage, costPerBw);
		this.communicationProtocolSupported=communicationProtocolSupported;
		this.ioTDeviceSupported=ioTDeviceSupported;
		
	}

	public Class<? extends IoTDevice>[] getIoTDeviceSupported() {
		return ioTDeviceSupported;
	}

	public void setIoTDeviceSupported(Class<? extends IoTDevice>[] ioTDeviceSupported) {
		this.ioTDeviceSupported = ioTDeviceSupported;
	}

	public Class<? extends CommunicationProtocol>[] getCommunicationProtocolSupported() {
		return communicationProtocolSupported;
	}

	public void setCommunicationProtocolSupported(Class<? extends CommunicationProtocol>[] communicationProtocolSupported) {
		this.communicationProtocolSupported = communicationProtocolSupported;
	}
	

}
