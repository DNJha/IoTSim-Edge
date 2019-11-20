package org.edge.entity;

import org.cloudbus.cloudsim.core.CloudSim;

/***
 * connection information
 * this is a network header like a http header, in which
 * it contains all the information needed to set up connection.
 * for example, it has the vmID and IoT Device ID.
 * what kind of communication protocols the iot device used, and what kind of
 * communication protocols the edge devices support.
 *
 * @author cody
 *
 */
public final class ConnectionHeader {

	public int vmId;
	public int ioTId;
	public int brokeId;
	public Class communicationProtocolForIoT;
	public Class ioTDeviceType;
	public int state;
	/**
	 * direction that the iot Device will move to
	 */
	public  Direction direction;

	//where is this header sent from;
	public int sourceId;

	public static enum Direction{
		LEFT,
		RIGHT
	}
	/**
	 *
	 * @return true if this header is from ioT
	 * otherwise return false
	 */
	public boolean getSourceType() {
		if(this.sourceId==this.ioTId) {
			return true;
		}
		return false;

	}


	public ConnectionHeader(int vmId, int ioTId,int brokeId,Class communicationProtocolForIoT) {
		super();
		this.vmId = vmId;
		this.ioTId = ioTId;
		this.ioTDeviceType=CloudSim.getEntity(ioTId).getClass();
		this.brokeId=brokeId;
		this.communicationProtocolForIoT=communicationProtocolForIoT;
	}


}
