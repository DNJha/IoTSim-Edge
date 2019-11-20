package org.edge.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.edge.core.feature.Mobility.Location;
import org.edge.entity.ConfiguationEntity;
import org.edge.entity.ConfiguationEntity.BrokerEntity;
import org.edge.entity.ConfiguationEntity.BwProvisionerEntity;
import org.edge.entity.ConfiguationEntity.ConnectionEntity;
import org.edge.entity.ConfiguationEntity.EdgeDataCenterEntity;
import org.edge.entity.ConfiguationEntity.EdgeDatacenterCharacteristicsEntity;
import org.edge.entity.ConfiguationEntity.HostEntity;
import org.edge.entity.ConfiguationEntity.IotDeviceEntity;
import org.edge.entity.ConfiguationEntity.MELEntities;
import org.edge.entity.ConfiguationEntity.MobilityEntity;
import org.edge.entity.ConfiguationEntity.MovingRangeEntity;
import org.edge.entity.ConfiguationEntity.NetworkModelEntity;
import org.edge.entity.ConfiguationEntity.PeEntity;
import org.edge.entity.ConfiguationEntity.RamProvisionerEntity;
import org.edge.entity.ConfiguationEntity.VmAllcationPolicyEntity;
import org.edge.entity.ConfiguationEntity.VmSchedulerEntity;
import org.junit.Test;

import com.google.gson.Gson;

public class ConfigurationTest {

	@Test
	public void testConfigration() {
		Gson gson=new Gson();
		ConfiguationEntity configuationEntity=new ConfiguationEntity();
		configuationEntity.setTrace_flag(false);
		configuationEntity.setNumUser(1);
		initilizeBroker(configuationEntity);
		initilizeDataCenter(configuationEntity);
		initilizeIoT(configuationEntity);
		initializeVM(configuationEntity);
		initializeConnection(configuationEntity);
		String json = gson.toJson(configuationEntity);
		System.out.println(json);
		
	}

	private void initializeConnection(ConfiguationEntity configuationEntity) {
		List<ConnectionEntity> connections=new ArrayList<>();
		ConnectionEntity connection=new ConnectionEntity();
		connection.setAssigmentIoTId(1);
		connection.setVmId(1);
		connections.add(connection);
		configuationEntity.setConnections(connections);
	}

	private void initializeVM(ConfiguationEntity configuationEntity) {
		List<MELEntities> vmEntities=new ArrayList<>();
		MELEntities vm=new MELEntities();
		vm.setBw(1111);
		vm.setMips(1111);
		vm.setPesNumber(1);
		vm.setVmid(1);
		vm.setSize(1111);
		vm.setRam(1222);
		vm.setVmm("xxx");
		vm.setCloudletSchedulerClassName("org.cloudbus.cloudsim.CloudletSchedulerTimeShared");
		vmEntities.add(vm);
		configuationEntity.setMELEntities(vmEntities);
	}

	private void initilizeIoT(ConfiguationEntity configuationEntity) {
		List<IotDeviceEntity> ioTDeviceEntities=new ArrayList<>();
		IotDeviceEntity iot=new IotDeviceEntity();
		iot.setIoTClassName("org.edge.device.iot.TemperatureSensor");
		/*IoTType.ENVIRONMENTAL_SENSOR,
		"temperatureSensor",
		DATA_FREQUENCY,
		DATA_GENERATION_TIME, 
		COMPLEXITY_OF_DATAPACKAGE,
		DATA_SIZE,
		networkModel,
		MAX_BATTERY_CAPACITY,
		BATTERY_DRAINAGE_RATE,
		PROCESSING_ABILITY,
		capacityToStore, 
		transfer_frequency,
		edgeLet
		*/
		iot.setBattery_drainage_rate(1);
		iot.setComplexityOfDataPackage(1);
		iot.setData_frequency(1);
		iot.setDataGenerationTime(1);
		iot.setDataSize(1);
		iot.setIotType("environmental");
		iot.setMax_battery_capacity(100);
		iot.setName("temperature");
		iot.setNetworkModelEntity(getDefaultNetworkModel());
		iot.setProcessingAbility(1);
		iot.setAssignmentId(1);
		MobilityEntity mobilityEntity=new MobilityEntity();
		mobilityEntity.setLocation(new Location(0,0,0));
		mobilityEntity.setMovable(true);
		mobilityEntity.setRange(new MovingRangeEntity(0,range-100));
		mobilityEntity.setVolecity(2);
		mobilityEntity.setSignalRange(0);
		
		iot.setMobilityEntity(mobilityEntity);
		ioTDeviceEntities.add(iot);
		configuationEntity.setIoTDeviceEntities(ioTDeviceEntities);
	}

	private void initilizeDataCenter(ConfiguationEntity configuationEntity) {
		List<EdgeDataCenterEntity> edgeDatacenter=new ArrayList<>();
		EdgeDataCenterEntity e=new EdgeDataCenterEntity();
		e.setName("edgeDatacenter1");
		e.setSchedulingInterval(1);
		EdgeDatacenterCharacteristicsEntity characteristics=new EdgeDatacenterCharacteristicsEntity();
		characteristics.setArchitecture("x86");
		characteristics.setCommunicationProtocolSupported( getDefaultSupportedCommunicationProtocol());
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource
		characteristics.setCost(1.0);
		characteristics.setCostPerBw(costPerBw);
		characteristics.setCostPerMem(costPerMem);
		characteristics.setCostPerSec(costPerBw);
		characteristics.setCostPerStorage(costPerStorage);
		characteristics.setVmm(vmm);
		List<HostEntity> hostList=new ArrayList<>();
		HostEntity host = getHost(1);
		HostEntity host2 = getHost(2);
		
		hostList.add(host);
		hostList.add(host2);
		characteristics.setHostListEntities(hostList);
		
		List<String> ioTDeviceClassNameSupported=Arrays.asList("org.edge.device.iot.TemperatureSensor");	
		characteristics.setIoTDeviceClassNameSupported(ioTDeviceClassNameSupported);
		characteristics.setOs(os);
		characteristics.setTimeZone(time_zone);
		e.setCharacteristics(characteristics);
		
		
		VmAllcationPolicyEntity vmAllocationPolicy=new VmAllcationPolicyEntity();
		vmAllocationPolicy.setClassName("org.cloudbus.cloudsim.VmAllocationPolicySimple");
		vmAllocationPolicy.setHostEntities(hostList);
		e.setVmAllocationPolicy(vmAllocationPolicy);
		
		
		edgeDatacenter.add(e);
		configuationEntity.setEdgeDatacenter(edgeDatacenter);
	}

	private List<String> getDefaultSupportedCommunicationProtocol() {
		List<String> communicationProtocolSupported=Arrays.asList("xmpp");
		return communicationProtocolSupported;
	}

	public static int range=0;
	private HostEntity getHost(int id) {
		HostEntity host=new HostEntity();
		host.setEdgeType("environmental");
		host.setBattery_drainage_rate(1);
		host.setBwProvisioner( getDefaultBwProvisioner());
		host.setCurrent_battery_capacity(100);
		host.setEdgeType("RASPBERRY_PI");
		host.setId(id);
		MobilityEntity mobility = new MobilityEntity(new Location(range,0,0));
		range+=100;
		mobility.setMovable(false);
//		mobility.setRange(new MovingRangeEntity(0, 100));
		mobility.setSignalRange(50);
		mobility.setVolecity(0);
		host.setGeo_location(mobility);
		host.setMax_battery_capacity(100);
		host.setMax_IoTDevice_capacity(10);
		host.setStorage(1000000);
		host.setRamProvisioner( getDefaultRamProvisioner());
		host.setNetworkModel(getDefaultNetworkModel());
		List<PeEntity> peEntities=new ArrayList<>();
		peEntities.add(getDefaultPe());
		host.setPeEntities(peEntities);
		host.setVmScheduler(getDefaultVmScheduler(peEntities));
		return host;
	}

	private VmSchedulerEntity getDefaultVmScheduler(List<PeEntity> peEntities) {
		VmSchedulerEntity vmScheduler=new VmSchedulerEntity();
		vmScheduler.setClassName("org.cloudbus.cloudsim.VmSchedulerTimeShared");
		return vmScheduler;
	}

	private BwProvisionerEntity getDefaultBwProvisioner() {
		BwProvisionerEntity bwProvisioner=new BwProvisionerEntity();
		bwProvisioner.setBwSize(1000000);
		bwProvisioner.setClassName("org.cloudbus.cloudsim.provisioners.BwProvisionerSimple");
		return bwProvisioner;
	}

	private RamProvisionerEntity getDefaultRamProvisioner() {
		RamProvisionerEntity ramProvisioner=new RamProvisionerEntity();
		ramProvisioner.setClassName("org.cloudbus.cloudsim.provisioners.RamProvisionerSimple");
		ramProvisioner.setRamSize(100000);
		return ramProvisioner;
	}

	public static int peId=1;
	private PeEntity getDefaultPe() {
		PeEntity entity=new PeEntity();
		entity.setId(peId);
		peId++;
		entity.setMips(100000);
		entity.setPeProvisionerClassName("org.cloudbus.cloudsim.provisioners.PeProvisionerSimple");
		return entity;
	}

	private NetworkModelEntity getDefaultNetworkModel() {
		NetworkModelEntity networkModel=new NetworkModelEntity();
		networkModel.setCommunicationProtocol("xmpp");
		networkModel.setNetworkType("wifi");
		return networkModel;
	}

	private void initilizeBroker(ConfiguationEntity configuationEntity) {
		BrokerEntity broker=new BrokerEntity();
		broker.setName("broker1");
		
		configuationEntity.setBroker(broker);
	}
}
