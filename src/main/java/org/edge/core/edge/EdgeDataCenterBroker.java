package org.edge.core.edge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.edge.core.feature.EdgeLet;
import org.edge.core.feature.EdgeState;
import org.edge.core.feature.Mobility.Location;
import org.edge.core.iot.IoTDevice;
import org.edge.entity.ConnectionHeader;
import org.edge.entity.ConnectionHeader.Direction;
import org.edge.entity.DevicesInfo;
import org.edge.exception.MicroElementNotFoundException;
import org.edge.utils.LogUtil;

/**
 * this a network manager and decision maker for setting up connection and event
 * transferring.
 * 
 * @author cody
 *
 */
public class EdgeDataCenterBroker extends DatacenterBroker {

	private List<ConnectionHeader> connectionInfos;
	private boolean init = false;

	public List<ConnectionHeader> getConnectionInfos() {
		return this.connectionInfos;
	}

	public void setConnectionInfos(List<ConnectionHeader> connectionInfos) {
		this.connectionInfos = connectionInfos;
	}

	public EdgeDataCenterBroker(String name) throws Exception {
		super(name);
		this.connectionInfos = new ArrayList<>();
	}

	@Override
	public void processEvent(SimEvent ev) {
		switch (ev.getTag()) {
		// Resource characteristics request
		case CloudSimTags.RESOURCE_CHARACTERISTICS_REQUEST:
			this.processResourceCharacteristicsRequest(ev);
			break;
		// Resource characteristics answer
		case CloudSimTags.RESOURCE_CHARACTERISTICS:
			this.processResourceCharacteristics(ev);
			break;
		// VM Creation answer
		case CloudSimTags.VM_CREATE_ACK:
			this.processVmCreate(ev);
			this.initializeNetWorkConnection(ev);
			break;
		// A finished cloudlet returned
		case CloudSimTags.CLOUDLET_RETURN:
			this.processCloudletReturn(ev);
			// LogUtil.info("actuing");
			dataShrinkAndSendToDownLink(ev);
			//this.actuating(ev);
			break;
		// if the simulation finishes
		case CloudSimTags.END_OF_SIMULATION:
			this.shutdownEntity();
			break;
		// other unknown tags are processed by this method
		default:
			this.processOtherEvent(ev);
			break;
		}
	}

	/**
	 * submit edgelets to its downlink
	 * 
	 * @param ev
	 */
	private void dataShrinkAndSendToDownLink(SimEvent ev) {
		// TODO transfer Data To the VMs children

		EdgeLet data = (EdgeLet) ev.getData();
		int vmId = data.getVmId();
		List<Vm> vmList2 = getVmList();
		MicroELement findFirst = (MicroELement) vmList2.stream().filter(t -> t.getId() == vmId).findFirst()
				.orElseThrow(MicroElementNotFoundException::new);
		List<MicroELement> downLink = findFirst.getDownLink();
		double shrinkingFactor = findFirst.getEdgeOperation().getShinkingFactor();
		for (MicroELement microELement : downLink) {
			EdgeLet newInstance = data.newInstance(IoTDevice.cloudLetId++, shrinkingFactor);
			
			// added by Areeb
			LogUtil.info("microELement.getId() " + microELement.getId());
			
			
			data.getConnectionHeader().vmId=microELement.getId();
			newInstance.setConnectionHeader(data.getConnectionHeader());
			newInstance.setVmId(microELement.getId());
			newInstance.setUserId(this.getId());
			
			LogUtil.info("shrinked Edgelet " + newInstance.getCloudletId() + " sent from microELement "
					+ findFirst.getId() + " to microELement " + microELement.getId());
			this.send(getId(), 0, EdgeState.SENDING_TO_EDGE, newInstance);

		}
	}

	@Override
	protected void processCloudletReturn(SimEvent ev) {
		EdgeLet cloudlet = (EdgeLet) ev.getData();
		this.getCloudletReceivedList().add(cloudlet);
		Log.printLine(CloudSim.clock() + ": " + getName() + ": Cloudlet " + cloudlet.getCloudletId() + " received");
		this.cloudletsSubmitted--;

		/*
		 * if (getCloudletList().size() == 0 && cloudletsSubmitted == 0) { // all
		 * cloudlets executed Log.printConcatLine(CloudSim.clock(), ": ", getName(),
		 * ": All Cloudlets executed. Finishing..."); clearDatacenters();
		 * finishExecution(); } else { // some cloudlets haven't finished yet if
		 * (getCloudletList().size() > 0 && cloudletsSubmitted == 0) { // all the
		 * cloudlets sent finished. It means that some bount // cloudlet is waiting its
		 * VM be created clearDatacenters(); createVmsInDatacenter(0); }
		 *
		 * }
		 */
	}

	private void actuating(SimEvent ev) {

		EdgeLet let = (EdgeLet) ev.getData();
		ConnectionHeader connectionHeader = let.getConnectionHeader();

		double networkDelay = this.getNetworkDelay(new DevicesInfo(connectionHeader.ioTId, connectionHeader.vmId));
		boolean availible = this.checkAvailiability(connectionHeader);
		if (availible) {
			LogUtil.info(CloudSim.clock() + " the edgelet " + let.getCloudletId() + " has been processed");
			this.send(connectionHeader.ioTId, networkDelay, EdgeState.REQUEST_ACTUATING, let);
		} else {
			LogUtil.info(CloudSim.clock() + " the edgelet " + let.getCloudletId()
					+ " has been processed but cannot find the ioT device, so try to pass edgelet to a near edge device");
			Direction direction = connectionHeader.direction;
			EdgeDevice availiableDevice = this.getNearByEdgeDevice(direction, connectionHeader);
			// should not enter this step unless there are not available edge devices at
			// all;
			if (availiableDevice == null) {
				LogUtil.info("lost connection with iot " + connectionHeader.ioTId + " when actuating");
			} else {
				LogUtil.info("found a near edge " + availiableDevice.getId() + " and give  edgelet "
						+ let.getCloudletId() + " to it");

				availiableDevice.addPendingResponse(let);

			
			}
			// send(connectionHeader.ioTId, networkDelay, EdgeState.REQUEST_ACTUATING, let);
		}

	}

	private EdgeDevice getNearByEdgeDevice(Direction direction, ConnectionHeader connectionHeader) {
		EdgeDataCenter entity = (EdgeDataCenter) CloudSim
				.getEntity(this.getVmsToDatacentersMap().get(connectionHeader.vmId));
		List<Host> hostList = entity.getHostList();
		EdgeDevice device = null;
		EdgeDevice previousDevice = null;
		for (Host host : hostList) {
			List<Vm> vmList2 = host.getVmList();
			for (Vm vm : vmList2) {
				if (vm.getId() == connectionHeader.vmId) {
					previousDevice = (EdgeDevice) host;
				}
			}
		}
		// theoretically it cannot be null;
		if (previousDevice == null)
			return null;
		EdgeDevice availiableDevice = null;
		for (Host host : hostList) {

			device = (EdgeDevice) host;
			// left direction
			if (direction == Direction.LEFT) {
				// if this device is on the left side of the previous device
				if (device.getLocation().location.x < previousDevice.getLocation().location.x) {
					// if there are enabled vm in this edgedevice
					if (host.getVmList().size() > 0 && device.isEnabled()) {
						availiableDevice = device;
					}

				}
			} else if (direction == Direction.RIGHT) {
				// if this device is on the right side of the previous device
				if (device.getLocation().location.x > previousDevice.getLocation().location.x) {
					// if there are enabled vm in this edgedevice
					if (host.getVmList().size() > 0 && device.isEnabled()) {
						availiableDevice = device;
					}
				}
			}

		}

		return availiableDevice;
	}

	/**
	 * whenever the cloudlet has been processed by a certain VM in Host the host's
	 * battery will be updated.
	 *
	 * @param connectionHeader
	 */
	private void updateHostBattery(ConnectionHeader connectionHeader) {
		
		EdgeDataCenter entity = (EdgeDataCenter) CloudSim
				.getEntity(this.getVmsToDatacentersMap().get(connectionHeader.vmId));
		List<Host> hostList = entity.getHostList();
		for (Host host : hostList) {
			List<Vm> vmList2 = host.getVmList();
			for (Vm vm : vmList2) {
				if (vm.getId() == connectionHeader.vmId) {
					EdgeDevice device = (EdgeDevice) host;
					
					//(double fileSize, double shrinkFactor,double drangeRateForProcess,double drangeRateForSending)
					double shrik=0.1;
					if(vm.getId()==1)
					device.updateBatteryByProcessingCloudLetAndSend(1000, shrik,0.1,0.6);
					else
						device.updateBatteryByProcessingCloudLetAndSend2(1000, shrik,0.1,0.6);
					
				}
			}
		}
	}

	private boolean checkAvailiability(ConnectionHeader connectionInfo) {
		EdgeDataCenter entity = (EdgeDataCenter) CloudSim
				.getEntity(this.getVmsToDatacentersMap().get(connectionInfo.vmId));
		
		//LogUtil.info("connectionInfo.vmId " + connectionInfo.vmId);
		List<Host> hostList = entity.getHostList();
		EdgeDevice device = null;
		for (Host host : hostList) {
			List<Vm> vmList2 = host.getVmList();
			for (Vm vm : vmList2) {
				if (vm.getId() == connectionInfo.vmId) {
					device = (EdgeDevice) host;
					IoTDevice entity2 = (IoTDevice) CloudSim.getEntity(connectionInfo.ioTId);
					//LogUtil.info("connectionInfo.vmId " + connectionInfo.vmId);
					Location location = device.getLocation().location;
					Location location2 = entity2.getMobility().location;
					double singalRange = device.getLocation().signalRange;
					double distance = Math.abs(location2.x - location.x);
					if (singalRange >= distance && device.isEnabled())
						return true;

				}
			}

		}
		if (device != null) {
			this.send(entity.getId(), 0, EdgeState.LOST_CONNECTION, connectionInfo);
		}
		// LogUtil.info("lost connection with ioT "+connectionInfo.ioTId);
		return false;
	}

	/**
	 * Process a cloudlet return event.
	 *
	 * @param ev a SimEvent object
	 * @pre ev != $null
	 * @post $none
	 */
	protected void initializeNetWorkConnection(SimEvent ev) {
		if (this.init)
			return;
		this.init = true;
		LogUtil.info("initialize  network connection");
		this.send(this.getId(), 0, EdgeState.INITILIZE_CONNECTION);
	}

	@Override
	protected void processOtherEvent(SimEvent ev) {
		// super.processOtherEvent(ev);

		switch (ev.getTag()) {
		case EdgeState.INITILIZE_CONNECTION:
			this.setupConnection(ev);
			break;
		case EdgeState.REQUEST_CONNECTION:
			this.requestConnection(ev);

			break;
		case EdgeState.CONNECTING_ACK:

			ConnectionHeader header = (ConnectionHeader) ev.getData();
			if (header.getSourceType()) {
				LogUtil.info("broker received  connection ack  from ioT " + header.ioTId);
				this.processConnectionAckFromIoT(ev);
			} else {
				LogUtil.info("broker received  connection ack  from vm " + header.vmId);
				this.processConnectionAckFromEdge(ev);
			}

			break;
		case EdgeState.REQUEST_DISCONNECTION:
			DevicesInfo info2 = (DevicesInfo) ev.getData();
			this.removeConnection(info2);
			break;

		case EdgeState.BATTERY_DRAINED:
			DevicesInfo info3 = (DevicesInfo) ev.getData();
			LogUtil.info("networkManager: remove iot device " + info3.ioTDeviceId + "'s connection");
			this.removeConnection(info3);
			break;

		case EdgeState.SENDING_TO_EDGE:
			this.sendingDataToEdge(ev);
			break;

		}

	}

	private void sendingDataToEdge(SimEvent ev) {

		EdgeLet data = (EdgeLet) ev.getData();
		
		
		
		
		// added by areeb to fix the problem if the header that is not fixed in sending from ML to ML
		LogUtil.info("VMiD "+data.getVmId());
		LogUtil.info("VMiD c "+data.getConnectionHeader().vmId);
		
		data.getConnectionHeader().vmId=data.getVmId();
		boolean available = this.checkAvailiability(data.getConnectionHeader());
		
		
		if (available) {
			LogUtil.info(CloudSim.clock() + " broker received edgeLet " + data.getCloudletId() + " from iot "
					+ data.getConnectionHeader().ioTId + " and send it to VM " + data.getConnectionHeader().vmId);
			List<EdgeLet> list = new ArrayList<>();
			list.add(data);
			this.submitCloudletList(list);
			this.submitCloudlets(data.getConnectionHeader());

		} 
		
		

		else {
			
			LogUtil.info("Not Aval 1");
			EdgeDevice availableDevice = this.findAvailiableDevice(data.getConnectionHeader());
			
			
			
			if (availableDevice != null) {
				// EdgeDataCenter entity = (EdgeDataCenter)
				// CloudSim.getEntity(getVmsToDatacentersMap().get(data.getConnectionHeader().vmId));
				// assume that it can get connected
				// send(entity.getId(),0,EdgeState.REQUEST_CONNECTION,data.getConnectionHeader());

				LogUtil.info("broker received edgeLet " + data.getCloudletId() + " from iot "

						+ data.getConnectionHeader().ioTId + " and its connection has switched to edge "
						+ availableDevice.getId());

				data.setVmId(availableDevice.getVmList().get(0).getId());
				List<EdgeLet> list = new ArrayList<>();
				list.add(data);

				int vmId = data.getVmId();
				// increase the edgelet size by a factor of "increasingFactor"
				MicroELement findFirst = (MicroELement) getVmList().stream().filter(vm -> vm.getId() == vmId)
						.findFirst().orElseThrow(MicroElementNotFoundException::new);
				double increasingFactor = findFirst.getEdgeOperation().getIncreasingFactor();

				data.setCloudletLength(data.getCloudletLength() * increasingFactor);
				this.submitCloudletList(list);
				this.submitCloudlets(data.getConnectionHeader());
				return;
			} 
			 
			else {
				LogUtil.info("Not Aval 2");
				EdgeDataCenter entity = (EdgeDataCenter) CloudSim
						.getEntity(this.getVmsToDatacentersMap().get(data.getConnectionHeader().vmId));
				List<Host> hostList = entity.getHostList();
				boolean allBatteriesEmpty = true;
				inner: for (Host host : hostList) {
					EdgeDevice device = (EdgeDevice) host;
					double current_battery_capacity = device.getCurrentBatteryCapacity();
					if (current_battery_capacity > 0) {
						allBatteriesEmpty = false;
						break inner;
					}

				}
				LogUtil.info("no available devices");
				if (allBatteriesEmpty) {
					LogUtil.info(
							"there are no available edge devices in the environment so the simuation will be shutting down");
					// CloudSim.abruptallyTerminate();
					CloudSim.terminateSimulation();
				}
			}
		}

	}

	/**
	 *
	 * 1.it will check whether there are pending responses from other edge devices
	 * 2.after submit the cloudlets to vm, the corresponding edge device's battery
	 * will be updated.
	 *
	 * @param connectionHeader
	 */
	protected void submitCloudlets(ConnectionHeader connectionHeader) {
		super.submitCloudlets();
		this.checkAndSendCorrespondingReponse(connectionHeader);
		this.updateHostBattery(connectionHeader);
	}

	/**
	 * find the corresponding edgelet in this VM related to this ioT, and send the
	 * edge let back to the ioT for actuating
	 *
	 * @param connectionHeader
	 */
	private void checkAndSendCorrespondingReponse(ConnectionHeader connectionHeader) {
		int vmId = connectionHeader.vmId;
		EdgeDataCenter entity = (EdgeDataCenter) CloudSim.getEntity(this.getVmsToDatacentersMap().get(vmId));
		List<Host> hostList = entity.getHostList();
		EdgeDevice device = null;
		for (Host host : hostList) {
			List<Vm> vmList2 = host.getVmList();
			for (Vm vm : vmList2) {
				if (vm.getId() == vmId) {
					device = (EdgeDevice) host;
					List<EdgeLet> pendingResponse = device.getPendingResponse();
					Iterator<EdgeLet> iterator = pendingResponse.iterator();
					while (iterator.hasNext()) {
						EdgeLet edgeLet = iterator.next();
						ConnectionHeader connectionHeader2 = edgeLet.getConnectionHeader();
						int ioTId = connectionHeader2.ioTId;

						// found
						if (connectionHeader.ioTId == ioTId) {
							iterator.remove();
							LogUtil.info(CloudSim.clock() + " send pending cloudlet " + edgeLet.getCloudletId()
									+ " from VM " + vmId + " to ioT " + ioTId);
							this.send(connectionHeader.ioTId, this.getNetworkDelay(new DevicesInfo(ioTId, vmId)),
									EdgeState.REQUEST_ACTUATING, edgeLet);
						}
					}

					break;
				}
			}

		}

	}

	private void requestConnection(SimEvent ev) {
		// TODO Auto-generated method stub
		ConnectionHeader connectionInfo = (ConnectionHeader) ev.getData();
		EdgeDevice device = this.findAvailiableDevice(connectionInfo);
		if (device != null) {
			this.send(connectionInfo.ioTId,
					this.getNetworkDelay(new DevicesInfo(connectionInfo.ioTId, connectionInfo.vmId)),
					EdgeState.REQUEST_CONNECTION, connectionInfo);
		} else {
			// LogUtil.info("ioT device "+connectionInfo.ioTId+" cannot find an
			// availible edgeDevice");
			this.send(connectionInfo.ioTId, 0, EdgeState.NO_AVAILIBLE_DEVICE, connectionInfo);
		}

	}

	/**
	 * remove connection between vm and iot
	 *
	 * @param info3
	 */
	private void removeConnection(DevicesInfo info3) {
		for (ConnectionHeader header2 : this.connectionInfos) {
			if (header2.vmId == info3.vmId && header2.ioTId == info3.ioTDeviceId) {
				this.connectionInfos.remove(header2);
				break;
			}

		}
		if (this.connectionInfos.isEmpty()) {
			LogUtil.info("all sensors are offline");
			this.clearDatacenters();
			this.finishExecution();
		}
	}

	private void processConnectionAckFromEdge(SimEvent ev) {
		ConnectionHeader connectionInfo = (ConnectionHeader) ev.getData();
		if (connectionInfo.state == EdgeState.SUCCESS) {

			LogUtil.info("send ack to ioT " + connectionInfo.ioTId
					+ " that the connection has been successfully established");
			this.send(connectionInfo.ioTId,
					this.getNetworkDelay(new DevicesInfo(connectionInfo.ioTId, connectionInfo.vmId)),
					EdgeState.CONNECTION_ESTABLISHED, connectionInfo);
		} else {
			LogUtil.info(
					"vm " + connectionInfo.vmId + " doesn't accept this connection from ioT " + connectionInfo.ioTId);
		}

	}

	private void processConnectionAckFromIoT(SimEvent ev) {
		ConnectionHeader connectionInfo = (ConnectionHeader) ev.getData();
		if (connectionInfo.state == EdgeState.SUCCESS) {
			// request
			this.send(this.getVmsToDatacentersMap().get(connectionInfo.vmId),
					this.getNetworkDelay(new DevicesInfo(connectionInfo.ioTId, connectionInfo.vmId)),
					EdgeState.REQUEST_CONNECTION, connectionInfo);
		} else {
			LogUtil.info("connection failure from ioT " + connectionInfo.ioTId);
		}

	}

	private void setupConnection(SimEvent ev) {
		List<ConnectionHeader> connectionInfos2 = this.getConnectionInfos();
		for (ConnectionHeader connectionInfo : connectionInfos2) {
			// request iot connection
			if (!Log.isDisabled()) {
				LogUtil.info("request connection with ioT " + connectionInfo.ioTId);
			}
			EdgeDevice device = this.findAvailiableDevice(connectionInfo);
			if (device != null) {
				this.send(connectionInfo.ioTId,
						this.getNetworkDelay(new DevicesInfo(connectionInfo.ioTId, connectionInfo.vmId)),
						EdgeState.REQUEST_CONNECTION, connectionInfo);
			} else {
				// LogUtil.info("ioT device "+connectionInfo.ioTId+" cannot find an
				// availible edgeDevice");
				this.send(connectionInfo.ioTId, 0, EdgeState.NO_AVAILIBLE_DEVICE, connectionInfo);

			}

		} 

	}

	EdgeDataCenter edgeDatacenter = null;

	/**
	 * firstly will try to connect assigned ioT and edge and if it fails, will try
	 * to connect an available edge device. otherwise, return null
	 *
	 * @param connectionInfo
	 * @return
	 */
	@SuppressWarnings("unused")
	private EdgeDevice findAvailiableDevice(ConnectionHeader connectionInfo) {
		Integer integer = this.getVmsToDatacentersMap().get(connectionInfo.vmId);
		if (integer == null) {
			if (edgeDatacenter == null)
				return null;
		} else {
			SimEntity ent = CloudSim.getEntity(integer);
			edgeDatacenter = (EdgeDataCenter) ent;
		}
		@SuppressWarnings("null")
		List<Host> hostList = edgeDatacenter.getHostList();

		fo: for (Host host : hostList) {
			List<Vm> vmList2 = host.getVmList();
			for (Vm vm : vmList2) {
				if (vm.getId() == connectionInfo.vmId) {
					EdgeDevice device = (EdgeDevice) host;
					IoTDevice iot = (IoTDevice) CloudSim.getEntity(connectionInfo.ioTId);
					Location edgeLocation = device.getLocation().location;
					Location iotLocation = iot.getMobility().location;
					double range = device.getLocation().signalRange;
					double distance = Math.abs(iotLocation.x - edgeLocation.x);
					if (range >= distance && device.isEnabled()) {
						LogUtil.info("got the required device!");
						return device;
					}
					break fo;
				}
			}
		}

		for (Host host : hostList) {
			EdgeDevice edgeDevice = (EdgeDevice) host;
			IoTDevice iotDevice = (IoTDevice) CloudSim.getEntity(connectionInfo.ioTId);
			Location edgeLocation = edgeDevice.getLocation().location;
			Location ioTLocation = iotDevice.getMobility().location;
			double range = edgeDevice.getLocation().signalRange;
			double distance = Math.abs(ioTLocation.x - edgeLocation.x);
			if (range >= distance) {
				if (edgeDevice.getAvailability()) {
					LogUtil.info("got the desired device!");
					// TODO send connection request to host
					connectionInfo.vmId = host.getVmList().get(0).getId();
					iotDevice.setAttachedEdgeDeviceVMId(host.getVmList().get(0).getId());
					return edgeDevice;
				}

			}
		}
		LogUtil.info("no device availiable for iot " + connectionInfo.ioTId);
		return null;
	}

	public double getNetworkDelay(DevicesInfo devicesInfo) {
		/*
		 * int ioTDeviceId = devicesInfo.ioTDeviceId; IoTDevice ioTDevice =
		 * ioTDevices.get(ioTDeviceId); Location location = ioTDevice.getLocation(); int
		 * edgeDeviceId = devicesInfo.vmId; EdgeDevice edgeDevice =
		 * edgeDevices.get(edgeDeviceId); Location location2 = edgeDevice.getLocation();
		 * NetworkType netWorkType = ioTDevice.getNetworkModel().getNetWorkType();
		 double speedRate = netWorkType.getSpeedRate();
		 */
		return 0;

		/*
		 * switch (netWorkType) { NetworkType netWorkType =
		 * ioTDevice.getNetworkModel().getNetWorkType(); case WIFI:
		 *
		 * break; case WLAN:
		 *
		 * break; case FourG:
		 *
		 * break; case ThreeG:
		 *
		 * break; case BLUETOOTH:
		 *
		 * break; case LAN:
		 *
		 * break;
		 *
		 * default: break; }
		 */

	}

	public void submitConnection(List<ConnectionHeader> connectionInfos) {

		this.getConnectionInfos().addAll(connectionInfos);

	}

}
