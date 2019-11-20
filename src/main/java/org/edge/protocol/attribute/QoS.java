package org.edge.protocol.attribute;

public enum QoS {
	/**
	 * at most once
	 */
    AMO,
    /**
     * at least once
     */
    ALO,
    /**
     * exactly once 
     */
    EO,
    /**
     * the feature of CoAP
     * 1.Confirmable: need ack 
	 *	Non-Confirmable: don’t need ack 
	 *	Acknowledgment: confirms the reception of a confirmable message
	 *	Reset: conform the reception of a message not being processed
     */
    CONFIRMABLE,
    /**
     *  no information is found
     * 
     */
    UNKNOWN
}