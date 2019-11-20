package org.edge.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.edge.entity.MicroElementTopologyEntity;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class MicroElementTopologyTest {

	
	
	/**
	 *     0
	 *   1   2
	 * 3  4  5  6 7 
	 *   8
	 */
	@Test
	public void buildupTopology() {
		List<MicroElementTopologyEntity> elementTopologyEntities=new ArrayList<MicroElementTopologyEntity>();
		
		elementTopologyEntities.add(creatreElement(0,null,1,2,3));
		elementTopologyEntities.add(creatreElement(1,0,3,4));
		elementTopologyEntities.add(creatreElement(2,0,5,6,7));
		elementTopologyEntities.add(creatreElement(3,1));
		elementTopologyEntities.add(creatreElement(4,1));
		elementTopologyEntities.add(creatreElement(5,2,8));
		elementTopologyEntities.add(creatreElement(6,2,8));
		elementTopologyEntities.add(creatreElement(7,2,8));
		
		elementTopologyEntities.add(creatreElement(8,3,5,6,7));
		
		Gson gson=new Gson();
		String json = gson.toJson(elementTopologyEntities);
		System.out.println(json);
		
	}
	
	@Test
	public void convertToClass() {
		InputStream resource = this.getClass().getClassLoader().getResourceAsStream("MELTopology.json");
		Gson gson=new Gson();
		Type ty=new TypeToken<List<MicroElementTopologyEntity>>() {}.getType();
		List<MicroElementTopologyEntity> fromJson = gson.fromJson(new JsonReader(new InputStreamReader(resource)),ty);
		
		System.out.println(fromJson);
		
	}
	
	private MicroElementTopologyEntity creatreElement(Integer id,Integer upLinkID,Integer... downlinkIDs) {
		MicroElementTopologyEntity elementTopologyEntity=new MicroElementTopologyEntity();
		List<Integer> downLinkIds=new ArrayList<>();
		for (Integer downLinkId : downlinkIDs) {
			downLinkIds.add(downLinkId);
		}
	    elementTopologyEntity.setDownLinkIds(downLinkIds);
		elementTopologyEntity.setId(id);
		elementTopologyEntity.setUpLinkId(upLinkID);
		return elementTopologyEntity;
	}
}
