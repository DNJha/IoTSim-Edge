package org.edge.entity;

public class UIEntity {

	
	private boolean trace;
	private int numberOfUser;
	private int numberOfIotDevice;
	private int numberOfEdgeDevice;
	private int numberOfDataCenter;
	private String brokerName;
	private ConfiguationEntity configuationEntity;
	private int numberOfVms;
	
	public int getNumberOfVms() {
		return numberOfVms;
	}
	public ConfiguationEntity getConfiguationEntity() {
		return configuationEntity;
	}
	public void setConfiguationEntity(ConfiguationEntity configuationEntity) {
		this.configuationEntity = configuationEntity;
	}
	public String getBrokerName() {
		return brokerName;
	}
	@Override
	public String toString() {
		return "UIEntity [trace=" + trace + ", numberOfUser=" + numberOfUser + ", numberOfIotDevice="
				+ numberOfIotDevice + ", numberOfEdgeDevice=" + numberOfEdgeDevice + ", numberOfDataCenter="
				+ numberOfDataCenter + ", brokerName=" + brokerName + "]";
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public boolean isTrace() {
		return trace;
	}
	public void setTrace(boolean trace) {
		this.trace = trace;
	}
	public int getNumberOfUser() {
		if(numberOfUser<=0)
			return 1;
		return numberOfUser;
	}
	public void setNumberOfUser(int numberOfUser) {
		
		this.numberOfUser = numberOfUser;
	}
	public int getNumberOfIotDevice() {
		if(numberOfIotDevice<=0)
			return 1;
		return numberOfIotDevice;
	}
	public void setNumberOfIotDevice(int numberOfIotDevice) {
		this.numberOfIotDevice = numberOfIotDevice;
	}
	public int getNumberOfEdgeDevice() {
		if(numberOfEdgeDevice<=0)
			return 1;
		return numberOfEdgeDevice;
	}
	public void setNumberOfEdgeDevice(int numberOfEdgeDevice) {
		this.numberOfEdgeDevice = numberOfEdgeDevice;
	}
	public int getNumberOfDataCenter() {
		if(numberOfDataCenter<=0)
			return 1;
		return numberOfDataCenter;
	}
	public void setNumberOfDataCenter(int numberOfDataCenter) {
		this.numberOfDataCenter = numberOfDataCenter;
	}
	public void setNumberOfVms(int numberOfVms) {
		this.numberOfVms = numberOfVms;
		
	}
	
}
