package org.edge.protocol;

import org.edge.network.NetworkType;
import org.edge.protocol.attribute.Architecture;
import org.edge.protocol.attribute.QoS;
import org.edge.protocol.attribute.SecurityProtocol;
import org.edge.protocol.attribute.Synchronism;
import org.edge.protocol.attribute.TransportProtocol;

public  abstract class CommonCommuniProtocol implements CommunicationProtocol {

   
	
    public CommonCommuniProtocol(int runningPort, TransportProtocol[] transportPortProtocol,
			SecurityProtocol[] securityProtocol, int headSize, long maxMessageSize, Architecture[] architecture,
			String encoding, String name, Synchronism[] synchronism, QoS[] qos,
			float batteryConsumptionSpeed, float transmissionSpeed) {
		super();
		this.runningPort = runningPort;
		this.transportPortProtocol = transportPortProtocol;
		this.securityProtocol = securityProtocol;
		this.headSize = headSize;
		this.maxMessageSize = maxMessageSize;
		this.architecture = architecture;
		this.encoding = encoding;
		this.name = name;
		this.synchronism = synchronism;
		this.qos = qos;
		this.batteryConsumptionSpeed = batteryConsumptionSpeed;
		this.transmissionSpeed = transmissionSpeed;
	}


	public CommonCommuniProtocol(int runningPort, TransportProtocol[] transportPortProtocol,
			SecurityProtocol[] securityProtocol, int headSize, long maxMessageSize, Architecture[] architecture,
			String name, Synchronism[] synchronism, QoS[] qos, float batteryConsumptionSpeed,
			float transmissionSpeed) {
		super();
		this.runningPort = runningPort;
		this.transportPortProtocol = transportPortProtocol;
		this.securityProtocol = securityProtocol;
		this.headSize = headSize;
		this.maxMessageSize = maxMessageSize;
		this.architecture = architecture;
		this.name = name;
		this.synchronism = synchronism;
		this.qos = qos;
		this.batteryConsumptionSpeed = batteryConsumptionSpeed;
		this.transmissionSpeed = transmissionSpeed;
	}

	protected int runningPort;
    protected TransportProtocol[] transportPortProtocol;
    protected SecurityProtocol[] securityProtocol;
    protected int headSize;
    protected long maxMessageSize;
    protected Architecture[] architecture;
    protected String encoding;
    protected String name;
    protected Synchronism[] synchronism;
    protected QoS[] qos;
    protected float batteryConsumptionSpeed;
    protected float transmissionSpeed;
    


/*
    public void CommonCommuniProtocol(void all  parameters except encoding) {
        // TODO implement here
    }

    public void CommonCommunicationProtocol(void all the parameter) {
        // TODO implement here
    }
*/
    public TransportProtocol[] getSupportedTransPortProtocol() {
        // TODO implement here
        return transportPortProtocol;
    }

  

    public Architecture[] getSupportedArchitecture() {
        // TODO implement here
        return architecture;
    }

    public String getEncoding() {
        // TODO implement here
        return "utf-8";
    }

   


	@Override
	public SecurityProtocol[] getSupportedSecurityProtocol() {
		// TODO Auto-generated method stub
		return securityProtocol;
	}

	@Override
	public float getTransmissionSpeed() {
		// TODO Auto-generated method stub
		return transmissionSpeed;
	}

	@Override
	public float getBatteryDrainageRate() {
		return batteryConsumptionSpeed;
	}
	@Override
	public int getHeadSize() {
		return headSize;
	}

	@Override
	public long getMaxMessageSize() {
		return maxMessageSize;
	}

	
	@Override
	public String getProtocolName() {

		return this.getClass().getSimpleName().substring(0,this.getClass().getSimpleName().lastIndexOf('P'));
	}

	@Override
	public int getRunningPort() {
		return runningPort;
	}

	@Override
	public Synchronism[] getSupportedSynchronism() {
		return synchronism;
	}

	@Override
	public QoS[] getSupportedQoS() {
		return qos;
	}

  

}