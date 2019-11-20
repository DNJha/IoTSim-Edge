package org.edge.protocol;

import java.util.*;

import org.edge.protocol.attribute.Architecture;
import org.edge.protocol.attribute.QoS;
import org.edge.protocol.attribute.SecurityProtocol;
import org.edge.protocol.attribute.Synchronism;
import org.edge.protocol.attribute.TransportProtocol;



public class XMPPProtocol extends CommonCommuniProtocol {
	private static final float BATTERY_DRAINAGE_RATE=1.50f;
	private static final float TRANSIMISON_SPEED=3.00f;

    public XMPPProtocol() {

    		super(5222,new TransportProtocol[] {TransportProtocol.TCP},new SecurityProtocol[] {SecurityProtocol.TLS,SecurityProtocol.SSL},0,Integer.MAX_VALUE,
    				new Architecture[] {Architecture.REQ_RSP,Architecture.PUB_SUB},"utf-8","XMPP",new Synchronism[] {Synchronism.ASYN},new QoS[] {QoS.UNKNOWN},BATTERY_DRAINAGE_RATE,TRANSIMISON_SPEED);

      
    }

	

}