package org.edge.protocol;

import java.util.*;

import org.edge.protocol.attribute.Architecture;
import org.edge.protocol.attribute.QoS;
import org.edge.protocol.attribute.SecurityProtocol;
import org.edge.protocol.attribute.Synchronism;
import org.edge.protocol.attribute.TransportProtocol;
/** port specification: 
 *  	1833: no security encryption.
 *		8883: one-way authentication
 *		8884: two-way authentication
 */

public class MQTTProtocol extends CommonCommuniProtocol {
	public static final float BATTERY_DRAINAGE_RATE=1.0f;
	public static final float TRANSIMISON_SPEED=1.0f;

    public MQTTProtocol() {

		super(1833,new TransportProtocol[] {TransportProtocol.TCP},new SecurityProtocol[] {SecurityProtocol.TLS,SecurityProtocol.SSL},2,/*256M*/1024*1024*256,
				new Architecture[] {Architecture.REQ_RSP,Architecture.PUB_SUB},"utf-8","MQTT",new Synchronism[] {Synchronism.SYN,Synchronism.ASYN},new QoS[] {QoS.AMO,QoS.ALO,QoS.EO},BATTERY_DRAINAGE_RATE,TRANSIMISON_SPEED);

    
    }

  
	

}