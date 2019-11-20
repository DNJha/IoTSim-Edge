package org.edge.core.iot;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.edge.core.feature.Battery;
import org.edge.core.feature.EdgeLet;
import org.edge.core.feature.EdgeState;
import org.edge.core.feature.IoTType;
import org.edge.core.feature.Mobility;
import org.edge.core.feature.policy.MovingPolicy;
import org.edge.core.feature.policy.NetworkDelayCalculationPolicy;
import org.edge.core.feature.policy.SimpleMovingPolicy;
import org.edge.entity.ConnectionHeader;
import org.edge.entity.ConnectionHeader.Direction;
import org.edge.entity.DevicesInfo;
import org.edge.exception.NullConnectionException;
import org.edge.network.NetworkModel;
import org.edge.utils.LogUtil;

/**
 * can LoT devices store sensed data if the data cannot be sent at a time? can
 * those states happen simultaneously?
 *
 * @author cody
 *
 */
public abstract class IoTDevice extends SimEntity {

	private static final double BATTERY_DRAIN_BY_MOVING = 0.000001;
	private static final int NULL_DEVICE = -1;
	private static final int pesNumber = 1;
	public static int cloudLetId = 0;
	private static final double MOVE_INTERVAL = 0.5d;
	private boolean logPrinted=false;
	/**
	 * size of the data package
	 */
	private int brokerId;
	private int assigmentIoTId;
	public NetworkDelayCalculationPolicy getNetworkDelayCalculationPolicy() {
		return this.networkDelayCalculationPolicy;
	}

	public void setNetworkDelayCalculationPolicy(NetworkDelayCalculationPolicy networkDelayCalculationPolicy) {
		this.networkDelayCalculationPolicy = networkDelayCalculationPolicy;
	}

	private NetworkDelayCalculationPolicy networkDelayCalculationPolicy;
	public int getAssigmentIoTId() {
		return this.assigmentIoTId;
	}

	public void setAssigmentIoTId(int assigmentIoTId) {
		this.assigmentIoTId = assigmentIoTId;
	}

	/**
	 * used to show how fast can this device process data
	 */
	private double processingAbility;
	private int complexityOfDataPackage;

	public double getRunningTime() {
		return this.runningTime;
	}

	public void setRunningTime(double runningTime) {
		this.runningTime = runningTime;
	}

	private Mobility mobility;

	public Mobility getMobility() {
		return this.mobility;
	}

	private IoTType ioTType;

	public void setMobility(Mobility location) {
		this.mobility = location;
	}

	private double runningTime = 0;
	public String sensed_data_type;

	private double data_frequency;
	private List<EdgeLet> dataPackagesSent;
	private List<EdgeLet> storedPackages;
	private List<EdgeLet> dataPackagesUnderTransmission;

	private boolean isMoving = false;
	private Battery battery;

	public Battery getBattery() {
		return this.battery;
	}

	protected double transfer_frequency;
	protected double send_data_size;
	protected double battery_drainage_rate;
	private int attachedEdgeDeviceVMId;
	public void setAttachedEdgeDeviceVMId(int attachedEdgeDeviceVMId) {
		this.attachedEdgeDeviceVMId = attachedEdgeDeviceVMId;
	}
	private MovingPolicy movingPolicy;

	public MovingPolicy getMovingPolicy() {
		return this.movingPolicy;
	}

	public void setMovingPolicy(MovingPolicy movingPolicy) {
		this.movingPolicy = movingPolicy;
	}

	private double timeRemainingTosendData = 0;

	public int getAttachedEdgeDeviceId() {
		return this.attachedEdgeDeviceVMId;
	}

	/**
	 * whether this device can store data or not
	 */
	private long capacityToStore = 0;
	private NetworkModel networkModel;
	private EdgeLet dataTemplate;

	public IoTDevice(IoTType type, String name, double data_frequency, double dataGenerationTime,
			int complexityOfDataPackage, int dataSize, NetworkModel networkModel, double max_battery_capacity,
			double battery_drainage_rate, double processingAbility, EdgeLet dataTemplate) {
		super(name);
		this.ioTType = type;
		this.dataTemplate = dataTemplate;
		this.battery = new Battery(max_battery_capacity, max_battery_capacity);
		this.processingAbility = processingAbility;
		this.dataGenerationTime = dataGenerationTime;

		this.attachedEdgeDeviceVMId = NULL_DEVICE;
		this.data_frequency = data_frequency;
		this.complexityOfDataPackage = complexityOfDataPackage;
		if (networkModel == null || networkModel.getCommunicationProtocol() == null)
			throw new NullConnectionException(
					"null networkmodel or null communication is not supported for IOT Device");
		this.networkModel = networkModel;
		this.battery_drainage_rate = battery_drainage_rate;
		this.dataPackagesSent = new ArrayList<>();
		this.dataPackagesUnderTransmission = new ArrayList<>();
		this.enabled = true;
		this.capacityToStore = 0;

	}

	public IoTDevice(IoTType type, String name, double data_frequency, double dataGenerationTime,
			int complexityOfDataPackage, int dataSize, NetworkModel networkModel, double max_battery_capacity,
			double battery_drainage_rate, double processingAbility, long capacityToStore, double transfer_frequency,
			EdgeLet dataTemplate) {

		this(type, name, data_frequency, dataGenerationTime, complexityOfDataPackage, dataSize, networkModel,
				max_battery_capacity, battery_drainage_rate, processingAbility, dataTemplate);
		this.transfer_frequency = transfer_frequency;
		this.timeRemainingTosendData = transfer_frequency;
		this.capacityToStore = capacityToStore;
		this.storedPackages = new ArrayList<>();
	}

	public double getProcessingAbility() {
		return this.processingAbility;
	}

	public void setProcessingAbility(double processingAbility) {
		this.processingAbility = processingAbility;
	}

	public IoTType getType() {
		return this.ioTType;
	}

	int connectingEdgeDeviceId = -1;

	public void setEdgeDeviceId(int id) {
		this.connectingEdgeDeviceId = id;
	}

	public double getBatteryDrainageRate() {
		return this.battery_drainage_rate;
	}

	public void setBatteryDrainageRate(double battery_drainage_rate) {
		this.battery_drainage_rate = battery_drainage_rate;
	}

	public NetworkModel getNetworkModel() {
		return this.networkModel;
	}

	/**
	 * if there is not networkModel or battery drained
	 */
	private boolean enabled;

	private double dataGenerationTime = 1;

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {

		this.enabled = enabled;
	}

	/**
	 * consume time
	 *
	 * @return
	 */
	private void generateData() {
		//		LogUtil.info("iot " + getId() + " is generating data");
		this.schedule(this.getId(), this.dataGenerationTime, EdgeState.GENERATING);
	}

	/**
	 * send a batch of data at a time
	 *
	 * @param dataPackages
	 */
	private void sendDataBundle(List<EdgeLet> dataPackages) {
		boolean transerable = this.checkTranserable();
		if (!transerable)
			return;
		this.dataPackagesSent.addAll(dataPackages);
		List<EdgeLet> sendingPackage = new ArrayList<>();
		sendingPackage.addAll(dataPackages);

		this.send(this.attachedEdgeDeviceVMId, this.getNetworkDelay(), EdgeState.SENDING_TO_EDGE, sendingPackage);

	}

	/**
	 * for now, it can only send one edge let at a time
	 * @param dataPackage
	 */
	private void sendData(EdgeLet dataPackage) {
		boolean transerable = this.checkTranserable();
		if (!transerable)
			return;

		if (this.capacityToStore == 0) {
			// if previous data is under transmitting then stop sensing
			this.sending(dataPackage);
		}
		// TODO to be capable of storing
		else {
			this.timeRemainingTosendData -= this.data_frequency;
			this.storedPackages.add(dataPackage);
			if (this.timeRemainingTosendData <= 0) {
				this.timeRemainingTosendData = this.transfer_frequency;
				this.sendDataBundle(this.storedPackages);
				this.storedPackages.clear();
			}
		}
		// send(getId(), data_frequency, EdgeState.SENSING);
	}

	/**
	 * send data,  no matter  it has connection or does not have due to range problem
	 * @param dataPackage
	 */
	private void sending(EdgeLet dataPackage) {
		LogUtil.info(CloudSim.clock()+ " "+this.getClass().getSimpleName()+" " + this.getId() + " is sending data");
		ConnectionHeader connectionHeader = new ConnectionHeader(this.attachedEdgeDeviceVMId, this.getId(), this.brokerId,
				this.getNetworkModel().getCommunicationProtocol().getClass());
		if(this.getMobility().movable) {
			connectionHeader.direction=this.getMobility().volecity>0?Direction.RIGHT:Direction.LEFT;
			
		}
		dataPackage.setConnectionHeader(connectionHeader);
//		double speed = this.getTransmissionSpeed();

		this.send(this.brokerId, this.getNetworkDelay(), EdgeState.SENDING_TO_EDGE, dataPackage);
		this.dataPackagesSent.add(dataPackage);
		this.send(this.getId(),this.data_frequency , EdgeState.SENSING);
	}

	public abstract double getNetworkDelay();

	private boolean checkTranserable() {
		// if the battery is drained,
		if (this.updateBatteryByTransmission(this.battery)) {
			this.runningTime = CloudSim.clock();
			LogUtil.info(this.getClass().getSimpleName()+" "+this.getId()+"'s battery has ran out when transmitting");
			if(this.getMobility().movable) {
				LogUtil.info("and it has moved "+this.mobility.totalMovingDistance+ " units");
			}
			this.setEnabled(false);
			return false;
		}

		if (this.attachedEdgeDeviceVMId == -1) {
			this.setEnabled(false);
			LogUtil.info("there is no target edge device connected to this loT: " + this.getId());
			return false;
		}
		return true;
	}

	@Override
	public void startEntity() {
		this.isMoving = true;
		// plus 10 make sure eveything has started
		this.send(this.getId(), MOVE_INTERVAL + 2, EdgeState.MOVING);
	}

	/**
	 * calculate network speed by considering different communication protocols and
	 * target's network speed;
	 *
	 * @return
	 */
	public abstract double getTransmissionSpeed();

	public void process_data(EdgeLet generated_data) {
		//		LogUtil.info("iot " + getId() + " is processing data");
		EdgeLet processedData = this.processData(generated_data);
		this.schedule(this.getId(), 0, EdgeState.PROCESS_COMPLETED, processedData);
	}

	public abstract EdgeLet processData(EdgeLet generated_data);

	/**
	 * calculate battery consumption by using the processing ability
	 *
	 * @return
	 */
	public abstract boolean updateBatteryByProcessing(Battery battery);

	public abstract boolean updateBatteryByTransmission(Battery battery);

	public void check_failure() {
	}

	public void check_sending_successful() {
		// TODO implement here
	}

	public void resend_data(int edge_device_id, int dataType) {
		// TODO implement here
	}

	protected void updateGeolocation() {

		if(this.movingPolicy!=null) {
			//this.getMobility().location.x=this.getMobility().location.x+this.getMobility().volecity;
			LogUtil.info(this.getClass().getSimpleName()+" " + this.getId() +" Location is:"+this.getMobility().location.x);
			this.movingPolicy.updateLocation(this.getMobility());
		}else {
			this.movingPolicy=new SimpleMovingPolicy();
			//this.getMobility().location.x=this.getMobility().location.x+this.getMobility().volecity;
			LogUtil.info(this.getClass().getSimpleName()+" " + this.getId() +" Location is:"+this.getMobility().location.x);
			this.movingPolicy.updateLocation(this.mobility);
		}
	}

	@Override
	public void processEvent(SimEvent ev) {
		int tag = ev.getTag();
		switch (tag) {

		case EdgeState.REQUEST_DISCONNECTION:
			this.attachedEdgeDeviceVMId = -1;
			this.setEnabled(false);
			LogUtil.info(this.getClass().getSimpleName()+" " + this.getId() + " gets disconnected with edge device " + this.attachedEdgeDeviceVMId);
			if (ev.getData() != null && ev.getData() instanceof String) {
				String data = (String) ev.getData();
				LogUtil.info(data);
			}
			break;
		case EdgeState.REQUEST_CONNECTION:

			ConnectionHeader info = (ConnectionHeader) ev.getData();
			LogUtil.info("received request for connection from broker" + info.brokeId);
			info.sourceId = info.ioTId;
			if (this.attachedEdgeDeviceVMId == NULL_DEVICE) {
				this.brokerId = info.brokeId;
				info.state = EdgeState.SUCCESS;
				LogUtil.info("ack request to broker " + info.brokeId);
				this.send(info.brokeId, this.getNetworkDelay(), EdgeState.CONNECTING_ACK, info);
			} else {
				info.state = EdgeState.FAILURE;
				this.send(info.brokeId, this.getNetworkDelay(), EdgeState.CONNECTING_ACK, info);
			}

			break;
		case EdgeState.CONNECTION_ESTABLISHED:

			ConnectionHeader info2 = (ConnectionHeader) ev.getData();
			this.attachedEdgeDeviceVMId = info2.vmId;
			LogUtil.info(CloudSim.clock()+ " "+
					this.getClass().getSimpleName()+" " + this.getId() + " has established connection with vm " + info2.vmId + " and start to sense");
			this.setEnabled(true);
			this.send(this.getId(), this.data_frequency, EdgeState.SENSING);
			break;
		case EdgeState.DISCONNECTED:
			Object data = ev.getData();
			if(data instanceof String) {
				String result=(String) data;
				if(result.equals(EdgeState.UNSUPPORTED_COMMUNICATION_PROTOCOL) || result.equals(EdgeState.UNSUPPORTED_IOT_DEVICE)) {
					shutDownDevice();
				}

			}else {
				this.attachedEdgeDeviceVMId = NULL_DEVICE;
				this.setEnabled(false);
			}
			break;

		case EdgeState.SENSING:
			//			LogUtil.info("ioT " + getId() + " start to sense");
			this.sensing();
			break;
		case EdgeState.GENERATING:
			EdgeLet edgeLet = this.generateEdgeLet();

			this.process_data(edgeLet);
			break;
		case EdgeState.PROCESS_COMPLETED:

			EdgeLet processedData = (EdgeLet) ev.getData();

			this.sendData(processedData);

			break;
		case EdgeState.SENDING_TO_EDGE:

			break;
		case EdgeState.REQUEST_ACTUATING:

			this.actuating(ev);
			break;

		case EdgeState.NO_AVAILIBLE_DEVICE:
			ConnectionHeader connectionHeader = (ConnectionHeader) ev.getData();
			this.brokerId = connectionHeader.brokeId;
			this.attachedEdgeDeviceVMId = -1;
			this.enabled = false;
			break;

		case EdgeState.MOVING:

			if (this.attachedEdgeDeviceVMId == NULL_DEVICE) {
				ConnectionHeader connectionHeader2 =new ConnectionHeader(attachedEdgeDeviceVMId,getId(),brokerId,getNetworkModel().getCommunicationProtocol().getClass());
				connectionHeader2.brokeId=brokerId;
				connectionHeader2.ioTId=getId();

				this.send(this.brokerId, 0, EdgeState.REQUEST_CONNECTION, connectionHeader2);
			}
			if(!this.getMobility().movable)
				return ;
			if (this.updateBatteryByMoving())
				return;
			this.updateGeolocation();
			this.send(this.getId(), MOVE_INTERVAL, EdgeState.MOVING, ev.getData());
			break;
		}

	}

	private void shutDownDevice() {
		LogUtil.info("iot "+getId()+ " is shuting down");
		getBattery().setCurrentCapacity(0);
		setEnabled(false);

	}

	private boolean updateBatteryByMoving() {
		this.battery.setCurrentCapacity(this.battery.getCurrentCapacity() - BATTERY_DRAIN_BY_MOVING);

		if (this.battery.getCurrentCapacity() <= 0)
			return true;
		return false;
	}

	private EdgeLet generateEdgeLet() {

		EdgeLet newInstance = this.dataTemplate.newInstance(cloudLetId);
		newInstance.setUserId(this.brokerId);
		// LogUtil.info(attachedEdgeDeviceVMId);
		newInstance.setVmId(this.attachedEdgeDeviceVMId);
		cloudLetId++;
		return newInstance;
	}

	private void sensing() {

		// if the battery is drained,
		boolean died = this.updateBatteryBySensing(this.battery);
		if (died) {
			LogUtil.info(this.getClass().getSimpleName() + " running time： " + CloudSim.clock());

			this.setEnabled(false);
			LogUtil.info(this.getClass().getSimpleName()+" " + this.getId() + "'s battery has been drained");
			this.runningTime = CloudSim.clock();
			this.schedule(this.brokerId, 0, EdgeState.BATTERY_DRAINED, new DevicesInfo(this.getId(), this.attachedEdgeDeviceVMId));
			return;
		}

		this.generateData();

	}

	public abstract boolean updateBatteryBySensing(Battery battery);

	/**
	 * if the specific device can actuate, then override this method.
	 *
	 * @param battery
	 * @return
	 */
	public boolean updateBatteryByActuating(Battery battery) {
		return false;
	}

	public void calculate_send_data_size() {
	}

	public void receive_message_from_edge() {
		// TODO implement here
	}

	/**
	 * if this device can actuate, then override this method
	 */
	public void actuating(SimEvent ev) {
		boolean updateBatteryByActuating = this.updateBatteryByActuating(this.battery);
		if (updateBatteryByActuating) {
			this.setEnabled(false);
			this.schedule(this.brokerId, 0, EdgeState.BATTERY_DRAINED, new DevicesInfo(this.getId(), this.attachedEdgeDeviceVMId));
			LogUtil.info(this.getClass().getSimpleName() +" "+this.getId()+ " running time： " + CloudSim.clock());
		} else {
			if(isEnabled()) {
				EdgeLet edgeLet = (EdgeLet) ev.getData();
				LogUtil.info("Insied IoT "+this.assigmentIoTId +"EdgeLet"+edgeLet.getCloudletId() +"edgeLet lenght"+ edgeLet.getCloudletLength());
				LogUtil.info(CloudSim.clock()+ " "+this.getClass().getSimpleName()+" "+this.getId()+" received processed edgelet "+edgeLet.getCloudletId()+" from vm "+edgeLet.getVmId()  + " and start actuating");
			}else {
				if(!logPrinted) {
					LogUtil.info(CloudSim.clock()+" "+this.getClass().getSimpleName()+" "+this.getId()+"  has offline");
					logPrinted=true;
				}

			}

			//			send(getId(), data_frequency, EdgeState.SENSING);
		}

	}

}