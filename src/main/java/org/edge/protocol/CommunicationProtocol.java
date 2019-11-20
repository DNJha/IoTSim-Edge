package org.edge.protocol;

import java.util.*;

import org.edge.protocol.attribute.Architecture;
import org.edge.protocol.attribute.QoS;
import org.edge.protocol.attribute.SecurityProtocol;
import org.edge.protocol.attribute.Synchronism;
import org.edge.protocol.attribute.TransportProtocol;

public interface CommunicationProtocol {

    public TransportProtocol[] getSupportedTransPortProtocol();

    public SecurityProtocol[] getSupportedSecurityProtocol();

    public int getHeadSize();

    public long getMaxMessageSize();

    public Architecture[] getSupportedArchitecture();

    /**
     * by default, it is UTF-8
     * you can change it by overriding
     * @return
     */
    public String getEncoding();
    
    /**
     * the way to get a name should be XXXXProtocol;
     * so that his abstract class can get Name directly from class name
     * @return
     */
    public String getProtocolName();

    public int getRunningPort();

    public Synchronism[] getSupportedSynchronism();

    public QoS[] getSupportedQoS();

    public float getBatteryDrainageRate();
    public float getTransmissionSpeed();
    

}