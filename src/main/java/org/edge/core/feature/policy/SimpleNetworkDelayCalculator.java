package org.edge.core.feature.policy;

import org.edge.core.feature.EdgeLet;
import org.edge.core.feature.Mobility;
import org.edge.core.feature.Mobility.Location;
import org.edge.network.NetworkModel;
import org.edge.network.NetworkType;
import org.edge.protocol.CommunicationProtocol;

public class SimpleNetworkDelayCalculator implements NetworkDelayCalculationPolicy{

	/**
	 * this simple policy for now only returns delay of 1 unit.
	 *
	 */
	@Override
	public double getNetworkDelay(NetworkModel networkModel, EdgeLet let, Mobility self, Mobility target) {
		CommunicationProtocol communicationProtocol = networkModel.getCommunicationProtocol();
		NetworkType netWorkType = networkModel.getNetWorkType();
		double cloudletLength = let.getCloudletLength();
		Location location = self.location;
		double distanceDelay=0;
		if(target!=null) {
			Location location2 = target.location;
			double x=location2.x-location.x;
			double y=location2.y-location.y;
			distanceDelay = Math.sqrt((x*x)+(y*y));
		}
		float transmissionSpeed = communicationProtocol.getTransmissionSpeed();
		double speedRate = netWorkType.getSpeedRate();

		double minSpeed= Math.min(transmissionSpeed, speedRate);
		//to change the cloudlet length can make the whole calculation simply.
		//easier to do demostration.
		cloudletLength=(long) minSpeed;
		double delay=cloudletLength/ minSpeed;
	
		return delay+distanceDelay;
	}

}
