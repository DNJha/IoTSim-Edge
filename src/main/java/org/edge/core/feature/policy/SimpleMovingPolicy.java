package org.edge.core.feature.policy;

import org.edge.core.feature.Mobility;
import org.edge.core.feature.Mobility.Location;

public class SimpleMovingPolicy implements MovingPolicy{

	/**
	 * moving a straight line, when reach the end, it will reverse the direction.
	 */
	@Override
	public void updateLocation(Mobility mobility) {

		Location location = mobility.location;
		location.x += mobility.volecity;
		mobility.totalMovingDistance+=Math.abs(mobility.volecity);
		if((location.x>=mobility.range.endX) || (location.x<=mobility.range.beginX)) {
			mobility.volecity=-mobility.volecity;
			if(location.x>mobility.range.endX) {
				location.x=mobility.range.endX;
			}
			if(location.x<mobility.range.beginX) {
				location.x=mobility.range.beginX;
			}

		}

	}

}
