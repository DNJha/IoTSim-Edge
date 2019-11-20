package org.edge.entity;

import java.util.List;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class MicroElementTopologyEntity {
	private int id;
	private Integer upLinkId;
	private List<Integer> downLinkIds;
	

	
}
