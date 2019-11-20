package org.edge.core.feature;

public class EdgeState {
	/**

	 *
	 *
	 *
	 */
	public static final int BASE=111;
	public static final int REQUEST_CONNECTION = BASE+1;
	public static final int REQUEST_DISCONNECTION = BASE+2;


	public static final int CONNECTION_ESTABLISHED = BASE+3;
	public static final int DISCONNECTED = BASE+4;


	public static final int SENDING_TO_EDGE = BASE+5;
	public static final int SENDING_FINISHED = BASE+6;

	public static final int SENSING = BASE+7;
	public static final int WAIT_TO_SENSE = BASE+8;



	public static final int GENERATING = BASE+9;
	public static final int GENERATING_FINISHED = BASE+10;


	public static final int PROCESSING_FINISHED = BASE+11;

	public static final int RECEIVING =BASE+ 12;
	public static final int RECEIVING_FINISHED = BASE+13;

	public static final int MOVING = BASE+14;
	public static final int MOVING_FINISHED = BASE+15;

	public static final int BATTERY = BASE+16;
	public static final int BATTERY_DRAINED = BASE+17;

	public static final int REQUEST_ACTUATING = BASE+18;
	public static final int ACTUATING_FINISHED =BASE+ 19;

	public static final int CONNECTING_ACK = BASE+20;
	public static final int DISCONNECTING_ACK = BASE+21;
	public static final int SUCCESS =  BASE+22;
	public static final int FAILURE =  BASE+23;
	public static final int INITILIZE_CONNECTION = BASE+24;
	public static final int PROCESSING = BASE+25;

	public static final int PROCESS_COMPLETED = BASE+26;
	public static final int NO_AVAILIBLE_DEVICE = BASE+27;
	public static final int LOST_CONNECTION = BASE+28;

	public static final String UNSUPPORTED_IOT_DEVICE="unsupported_iot_device";
	public static final String UNSUPPORTED_COMMUNICATION_PROTOCOL = "unsupported_communication_protocol";
}
