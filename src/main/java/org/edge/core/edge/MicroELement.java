package org.edge.core.edge;

import java.util.List;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerVm;
import org.edge.core.feature.operation.EdgeOperation;

public class MicroELement extends Vm {




	


	private EdgeOperation edgeOperation;

	public EdgeOperation getEdgeOperation() {
		return edgeOperation;
	}
	public void setEdgeOperation(EdgeOperation edgeOperation) {
		this.edgeOperation = edgeOperation;
	}
	private int datasize;
	private int payload;
	private MicroELement upLink;
	


  /**
   * TODO 
   * Structure
   * MI 
   * *change the computing time for VMs
   * 
   * 
   */


	private List<MicroELement> downLink;
	
	
	
	public  void updateDataSize() {
		
	}public MicroELement getUpLink() {
		return upLink;
	}

	public void setUpLink(MicroELement upLink) {
		this.upLink = upLink;
	}

	public List<MicroELement> getDownLink() {
		return downLink;
	}

	public void setDownLink(List<MicroELement> downLink) {
		this.downLink = downLink;
	}
	/*
	public EdgeOperation getOperation() {
		return operation;
	}

	public void setOperation(EdgeOperation operation) {
		this.operation = operation;
	}*/

	public int getDatasize() {
		return datasize;
	}

	public void setDatasize(int datasize) {
		this.datasize = datasize;
	}

	public int getPayload() {
		return payload;
	}

	public void setPayload(int payload) {
		this.payload = payload;
	}




	
	public MicroELement(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm,
			CloudletScheduler cloudletScheduler) {
		super(id, userId, mips, numberOfPes, ram, bw, size,vmm, cloudletScheduler);
	}
	
	public MicroELement(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm,
			CloudletScheduler cloudletScheduler,String type,float shrinkingFactor) {
		super(id, userId, mips, numberOfPes, ram, bw, size,vmm, cloudletScheduler);
		
	}
	
	

}
