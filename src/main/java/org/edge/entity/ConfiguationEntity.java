package org.edge.entity;

import java.util.List;

import org.edge.core.feature.Mobility.Location;

import lombok.Data;

@Data
public class ConfiguationEntity {

	private LogEntity logEntity;

	@Data
	public static class LogEntity {
		private String logLevel;
		private boolean saveLogToFile;
		private String logFilePath;
		private boolean append;
	}

	private boolean trace_flag;
	private int numUser;
	private List<IotDeviceEntity> ioTDeviceEntities;
	private List<MELEntities> MELEntities;

	private BrokerEntity broker;
	private List<EdgeDataCenterEntity> edgeDatacenter;

	@Data
	public static class EdgeDataCenterEntity {

		private String name;
		private EdgeDatacenterCharacteristicsEntity characteristics;
		private VmAllcationPolicyEntity vmAllocationPolicy;
		// List<StorageEntity> storageList;
		private double schedulingInterval;

	}

	@Data
	public static class VmAllcationPolicyEntity {

		String className;
		List<HostEntity> hostEntities;
	}

	@Data
	public static class HostEntity {

		int id;
		RamProvisionerEntity ramProvisioner;
		BwProvisionerEntity bwProvisioner;
		long storage;
		List<PeEntity> peEntities;

		VmSchedulerEntity vmScheduler;
		// EdgeType edgeType;
		/**
		 * RASPBERRY_PI, SMART_ROUTER, UDOO_BOARD, MOBILE_PHONE,
		 */
		String edgeType;
		MobilityEntity geo_location;
		NetworkModelEntity networkModel;
		int max_IoTDevice_capacity;
		double max_battery_capacity;
		double battery_drainage_rate;
		double current_battery_capacity;

	}

	@Data
	public static class RamProvisionerEntity {

		String className;
		int ramSize;

	}

	@Data
	public static class BwProvisionerEntity {

		String className;
		int bwSize;

	}

	@Data
	public static class VmSchedulerEntity {

		String className;

	}

	@Data
	public static class PeEntity {

		int id;
		String peProvisionerClassName;
		int mips;
	}

	/**
	 * String arch = "x86"; // system architecture String os = "Linux"; // operating
	 * system String vmm = "Xen"; double time_zone = 10.0; // time zone this
	 * resource located double cost = 3.0; // the cost of using processing in this
	 * resource double costPerMem = 0.05; // the cost of using memory in this
	 * resource double costPerStorage = 0.001; // the cost of using storage in this
	 * // resource double costPerBw = 0.0; // the cost of using bw in this resource
	 * 
	 * @author cody
	 *
	 */

	@Data
	public static class EdgeDatacenterCharacteristicsEntity {

		String architecture;
		String os;
		String vmm;
		List<HostEntity> hostListEntities;
		double cost;

		double timeZone;
		double costPerSec;
		double costPerMem;
		double costPerStorage;
		double costPerBw;
		List<String> communicationProtocolSupported;
		List<String> ioTDeviceClassNameSupported;

	}

	@Data
	public static class BrokerEntity {

		String name;
	}

	@Data
	public static class MELEntities {

		// int brokerAssignmentId;
		int vmid;
		int mips;
		long size; // image size (MB)
		int ram; // vm memory (MB)
		long bw;
		int pesNumber; // number of cpus
		String vmm; // VMM name
		String cloudletSchedulerClassName;
		MicroElementTopologyEntity MELTopology;
		String type;
		String edgeOperationClass;
		float datasizeShrinkFactor;
		
	}

	@Data
	public static class IotDeviceEntity {
		private MobilityEntity mobilityEntity;
		private int numberofEntity;

		public int assignmentId;
		public String ioTClassName;
		String iotType;
		String name;
		double data_frequency;
		double dataGenerationTime;
		int complexityOfDataPackage;
		int dataSize;
		NetworkModelEntity networkModelEntity;
		double max_battery_capacity;
		double battery_drainage_rate;
		double processingAbility;
		EdgeLetEntity dataTemplate;

	}

	@Data
	public static class EdgeLetEntity {

		int cloudletId;
		long cloudletLength;
		int pesNumber;
		long cloudletFileSize;
		long cloudletOutputSize;
		String utilizationModelCpu;
		String utilizationModelRam;
		String utilizationModelBw;

	}

	@Data
	public static class NetworkModelEntity {

		private String networkType;
		private String communicationProtocol;

	}

	private List<ConnectionEntity> connections;

	@Data
	public static class ConnectionEntity {
		private int vmId;
		private int assigmentIoTId;

	}

	@Data
	public static class MobilityEntity {
		private boolean movable;
		private double volecity;
		private MovingRangeEntity range;
		private double signalRange;
		private Location location;
		public MobilityEntity(Location location) {
			super();
			this.location = location;
		}
		public MobilityEntity() {
			super();
		}

	}

	public static class MovingRangeEntity {
		public int beginX;
		public int endX;

		public MovingRangeEntity() {
			super();
		}

		public MovingRangeEntity(int beginX, int endX) {
			super();
			this.beginX = beginX;
			this.endX = endX;
		}

	}
}
