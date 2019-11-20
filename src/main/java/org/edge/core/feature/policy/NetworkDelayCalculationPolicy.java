package org.edge.core.feature.policy;

import org.edge.core.feature.EdgeLet;
import org.edge.core.feature.Mobility;
import org.edge.network.NetworkModel;

public interface  NetworkDelayCalculationPolicy {


	/**
	 * calculate network delay using networkModel and the edge let.
	 * @param networkModel the communication protocol and network type will be considered.
	 * @param let the size of the data will be considered.
	 * @param self the distance maybe considered
	 * @param target the distance maybe considered
	 * @return
	 */
	public double getNetworkDelay(NetworkModel networkModel,EdgeLet let,Mobility self,Mobility target);

}
