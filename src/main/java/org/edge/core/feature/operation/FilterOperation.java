package org.edge.core.feature.operation;

public class FilterOperation implements EdgeOperation {

	private static final String type="filter";
	@Override
	public String type() {
		
		return type;
	}

	@Override
	public double getShinkingFactor() {
		// TODO Auto-generated method stub
		return 0.5;
	}
	@Override
	public double getIncreasingFactor() {
		// TODO Auto-generated method stub
		return 2;
	}

	

}
