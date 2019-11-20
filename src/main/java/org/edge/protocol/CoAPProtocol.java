package org.edge.protocol;

import org.edge.protocol.attribute.Architecture;
import org.edge.protocol.attribute.QoS;
import org.edge.protocol.attribute.SecurityProtocol;
import org.edge.protocol.attribute.Synchronism;
import org.edge.protocol.attribute.TransportProtocol;

public class CoAPProtocol extends CommonCommuniProtocol {

	private static final float BATTERY_DRAINAGE_RATE=1.00f;
	private static final float TRANSIMISON_SPEED=3.00f;

	
	public CoAPProtocol() {
		super(5683,new TransportProtocol[] {TransportProtocol.UDP},new SecurityProtocol[] {SecurityProtocol.DTLS},Integer.MAX_VALUE,Integer.MAX_VALUE,
				new Architecture[] {Architecture.REQ_RSP},"utf-8","CoAP",new Synchronism[] {Synchronism.SYN,Synchronism.ASYN},new QoS[] {QoS.CONFIRMABLE},BATTERY_DRAINAGE_RATE,TRANSIMISON_SPEED);
	}

  

	
	

}