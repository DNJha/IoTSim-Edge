package org.edge.ui;

import java.util.ArrayList;
import java.util.List;

import org.edge.entity.ConfiguationEntity;
import org.edge.entity.ConfiguationEntity.ConnectionEntity;
import org.edge.entity.ConfiguationEntity.IotDeviceEntity;
import org.edge.entity.ConfiguationEntity.MELEntities;
import org.edge.entity.UIEntity;
import org.edge.examples.Example2A;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConnectionCreation extends Application {

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);

		grid.setPadding(new Insets(10, 10, 10, 10));

		Scene scene = new Scene(grid, 800, 400);
		Label label = new Label("atrributes of connection:");
		UIEntity userData = (UIEntity) primaryStage.getUserData();
		ConfiguationEntity configuationEntity = userData.getConfiguationEntity();
		List<MELEntities> vmEntities = configuationEntity.getMELEntities();
		Label range = new Label(" vm id range:"+vmEntities.get(0).getVmid()+"-"+vmEntities.get(vmEntities.size()-1).getVmid() );

		List<ConnectionEntity> connections = new ArrayList<>();
		configuationEntity.setConnections(connections);

		List<IotDeviceEntity> ioTDeviceEntities = configuationEntity.getIoTDeviceEntities();

		
		int index=2;
		grid.add(label, 0, 0);
		grid.add(range, 0, 1);
		
		
		List<TextField> textFields=new ArrayList<TextField>();
		for (IotDeviceEntity iotDeviceEntity : ioTDeviceEntities) {
			Label userName = new Label("iot "+iotDeviceEntity.assignmentId +" will connect to VM ");
			grid.add(userName, 0, index);
			TextField field=new TextField(vmEntities.get(0).getVmid()+"");
			textFields.add(field);
			grid.add(field, 1, index);
			index++;
		}

		Button button=new Button("build up simulator");
		grid.add(button,0,index+1);
		button.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Example2A up=new Example2A();
				int ioTAssignId=1;
				List<ConnectionEntity> connectionEntities=new ArrayList<>();
				for (TextField textField : textFields) {
					String text = textField.getText();
					int vmId = Integer.parseInt(text);		
					ConnectionEntity connectionEntity=new ConnectionEntity();
					connectionEntity.setVmId(vmId);
					connectionEntity.setAssigmentIoTId(ioTAssignId);
					connectionEntities.add(connectionEntity);
					ioTAssignId++;
				}
				configuationEntity.setConnections(connectionEntities);
				
			/*	Gson gson=new Gson();
				String json = gson.toJson(configuationEntity);*/
				up.initFromConfiguation(configuationEntity);
			}
		});
		primaryStage.setTitle("IotSim");
		primaryStage.setScene(scene);
		
		
		primaryStage.show();
	}

	
}
