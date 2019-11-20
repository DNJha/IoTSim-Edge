package org.edge.network;

public enum NetworkType {
	
	WIFI(5d),
    WLAN(5d),
    FourG(4d),
    ThreeG(2d),
    BLUETOOTH(2d),
    LAN(3);
	private double value;
	private NetworkType(double  value){
		this.value=value;
	}
	public double getSpeedRate(){
		return value;
	}
}