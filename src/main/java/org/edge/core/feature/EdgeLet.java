package org.edge.core.feature;

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.edge.entity.ConnectionHeader;

public class EdgeLet extends Cloudlet {
	
	public static int id = 0;
	private ConnectionHeader header;
	private boolean edgeRecord;
	public ConnectionHeader getConnectionHeader() {
		return header;
	}

	public void setConnectionHeader(ConnectionHeader header) {
		this.header = header;
	}

	public boolean isEdgeRecord() {
		return edgeRecord;
	}

	public void setEdgeRecord(boolean edgeRecord) {
		this.edgeRecord = edgeRecord;
	}

	public EdgeLet( 
			final int cloudletId,
            final long cloudletLength,
            final int pesNumber,
            final long cloudletFileSize,
            final long cloudletOutputSize,
            final UtilizationModel utilizationModelCpu,
            final UtilizationModel utilizationModelRam,
            final UtilizationModel utilizationModelBw) {
		super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu,
				utilizationModelRam, utilizationModelBw);
	}
	
	public EdgeLet  newInstance(int id) {
		EdgeLet edgeLet=null;
		if(getRequiredFiles()!=null && !getRequiredFiles().isEmpty()) {
			 edgeLet=new EdgeLet(id, getCloudletLength(), getNumberOfPes(),
					getCloudletFileSize(), getCloudletOutputSize(), getUtilizationModelCpu(), 
					getUtilizationModelRam(), getUtilizationModelBw(),edgeRecord);
		}
		else {
			 edgeLet=new EdgeLet(id, getCloudletLength(), getNumberOfPes(),
					getCloudletFileSize(), getCloudletOutputSize(), getUtilizationModelCpu(), 
					getUtilizationModelRam(), getUtilizationModelBw(),getRequiredFiles());	
		}
		return edgeLet;
	}
	public EdgeLet  newInstance(int id,double shrinkFactor) {
		EdgeLet edgeLet=null;
		if(getRequiredFiles()!=null && !getRequiredFiles().isEmpty()) {
			 edgeLet=new EdgeLet(id, getCloudletLength(), getNumberOfPes(),
					getCloudletFileSize(), getCloudletOutputSize(), getUtilizationModelCpu(), 
					getUtilizationModelRam(), getUtilizationModelBw(),edgeRecord);
		}
		else {
			 edgeLet=new EdgeLet(id, getCloudletLength(), getNumberOfPes(),
					getCloudletFileSize(), getCloudletOutputSize(), getUtilizationModelCpu(), 
					getUtilizationModelRam(), getUtilizationModelBw(),getRequiredFiles());	
		}
		return edgeLet;
	}


	public EdgeLet(int cloudletId, double cloudletLength, int pesNumber, double cloudletFileSize, double cloudletOutputSize,
			UtilizationModel utilizationModelCpu, UtilizationModel utilizationModelRam,
			UtilizationModel utilizationModelBw, boolean record) {
		super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu,
				utilizationModelRam, utilizationModelBw, record);
		this.edgeRecord=record;
	}

	public EdgeLet(int cloudletId, double cloudletLength, int pesNumber, double cloudletFileSize, double cloudletOutputSize,
			UtilizationModel utilizationModelCpu, UtilizationModel utilizationModelRam,
			UtilizationModel utilizationModelBw, List<String> fileList) {
		super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu,
				utilizationModelRam, utilizationModelBw, fileList);
	
	}


	
}
