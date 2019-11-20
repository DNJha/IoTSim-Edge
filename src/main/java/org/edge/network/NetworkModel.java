package org.edge.network;

import org.edge.protocol.CommunicationProtocol;

/**
 * this model is like a network card in which it can specify network Type like wifi, 3G, 4G
 *  and communicationProtocol
 * @author cody
 *
 */
public class NetworkModel {

    private NetworkType networkType;
    


	public NetworkModel(NetworkType networkType) {
		this.networkType = networkType;
    }

  

	public void setNetworkType(NetworkType networkType) {
		this.networkType = networkType;
	}

	public CommunicationProtocol getCommunicationProtocol() {
		return communicationProtocol;
	}



	public void setCommunicationProtocol(CommunicationProtocol communicationProtocol) {
		this.communicationProtocol = communicationProtocol;
	}

	private CommunicationProtocol communicationProtocol;



    public NetworkType getNetWorkType() {
        return networkType;
    }

}