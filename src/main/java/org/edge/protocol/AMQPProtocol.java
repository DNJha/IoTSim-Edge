package org.edge.protocol;

import java.util.*;

import org.edge.protocol.attribute.Architecture;
import org.edge.protocol.attribute.QoS;
import org.edge.protocol.attribute.SecurityProtocol;
import org.edge.protocol.attribute.Synchronism;
import org.edge.protocol.attribute.TransportProtocol;

public class AMQPProtocol extends CommonCommuniProtocol {
	public static final float BATTERY_DRAINAGE_RATE=1.0f;
	public static final float TRANSIMISON_SPEED=1.0f;

    public AMQPProtocol() {
    	
    	
    	super(5672,new TransportProtocol[] {TransportProtocol.TCP},new SecurityProtocol[] {SecurityProtocol.TLS,SecurityProtocol.SSL},8,Integer.MAX_VALUE,
				new Architecture[] {Architecture.REQ_RSP,Architecture.PUB_SUB},"utf-8","XMPP",new Synchronism[] {Synchronism.ASYN},new QoS[] {QoS.AMO,QoS.ALO,QoS.EO},BATTERY_DRAINAGE_RATE,TRANSIMISON_SPEED);

  
    	
    }

	

	

}