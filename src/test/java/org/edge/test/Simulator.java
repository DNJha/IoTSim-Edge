package org.edge.test;

import java.util.*;

import org.edge.core.iot.IoTDevice;



public class Simulator {
	private static Simulator simulator=new Simulator();

	private List<IoTDevice> loTDevices;
	
	private Simulator() {
		
		loTDevices = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
//			TemperatureSensor sensor=new TemperatureSensor(i);
//			loTDevices.add(sensor);
    	}
		
    }
	

	public static Simulator getInstance() {
		return  simulator;
	}
    public static void main(String args[]) {
    	
    	
    	
    	
    	getInstance().startSimulation();
    	
    	
    }

    public void startSimulation() {
    	
    	//frame per second
    	double frame=100;
    	double preFrameTime = System.currentTimeMillis();
    	while ( true)
    	{
    		double currentFrame = System.currentTimeMillis();
    		double diff=currentFrame-preFrameTime;
    		if(diff>=frame) {
    			update(diff);
    			preFrameTime=currentFrame;
    		}
    	}

    }
    public void update(double frame) {
    	System.out.println(frame);
    	
    	
    	
    	
    }
}