package org.edge.ui;


import org.edge.entity.ConfiguationEntity;
import org.edge.entity.ConfiguationEntity.BrokerEntity;
import org.edge.entity.UIEntity;
import org.edge.utils.NumberTextField;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main  extends Application{
	
	
	private Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		GridPane grid = new GridPane();
    	grid.setAlignment(Pos.CENTER);
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(10,10,10,10));

    	Scene scene = new Scene(grid, 600,300);
    	
    	

  

    	CheckBox box=new CheckBox("trace");
    	grid.add(box, 0, 0);
    	Label users = new Label("number of users");
    	grid.add(users, 0,1);
    	NumberTextField numberUser=new NumberTextField();
    	
    	numberUser.setMaxWidth(50);
    	grid.add(numberUser,1,1);
    	numberUser.setText("1");
    	
    	
    	Label datacenterLabel = new Label("number of datacenters:");
    	grid.add(datacenterLabel, 0,2);
    	NumberTextField datacenter=new NumberTextField();
    	datacenter.setText("1");
    	datacenter.setMaxWidth(50);
    	grid.add(datacenter,1,2);
    	
    	Label numberOfIoTdeviceLabel = new Label("number of iotDevices:");
    	grid.add(numberOfIoTdeviceLabel, 0,3);
    	NumberTextField iotDevices=new NumberTextField();
    	iotDevices.setText("1");
    	
    	iotDevices.setMaxWidth(50);
    	grid.add(iotDevices,1,3);
    	
    	Label numberOfEdgedeviceLabel = new Label("number of edgeDevices:");
    	grid.add(numberOfEdgedeviceLabel, 0,4);
    	NumberTextField edgeDevices=new NumberTextField();
    	edgeDevices.setText("1");
    	
    	edgeDevices.setMaxWidth(50);
    	grid.add(edgeDevices,1,4);
    
    	Label numberOfVMsLabel = new Label("number of VMs:");
    	grid.add(numberOfVMsLabel, 0,5);
    	NumberTextField numberOfVMs=new NumberTextField();
    	numberOfVMs.setText("1");
    	
    	numberOfVMs.setMaxWidth(50);
    	grid.add(numberOfVMs,1,5);
    	
      	
	
    	TextField brokerName = addInputWithTextReminder(grid, "broker name", 0, 6);
    	
    	brokerName.setText("broker1");
    	
    	
    	Button btn = new Button("next step");
    	btn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				try {
					String numberOfUser = numberUser.getText();
					UIEntity entity=new UIEntity();
					entity.setNumberOfUser(Integer.parseInt(numberOfUser));
					entity.setNumberOfDataCenter(Integer.parseInt(datacenter.getText()));
					entity.setNumberOfIotDevice(Integer.parseInt(iotDevices.getText()));
					entity.setNumberOfEdgeDevice(Integer.parseInt(edgeDevices.getText()));
					entity.setBrokerName(brokerName.getText());
					entity.setNumberOfVms(Integer.parseInt(numberOfVMs.getText()));
					entity.setTrace(box.isSelected());
					BrokerEntity broker=new BrokerEntity();
					broker.setName(entity.getBrokerName());
					ConfiguationEntity configuationEntity=new ConfiguationEntity();
					configuationEntity.setBroker(broker);
					configuationEntity.setTrace_flag(box.isSelected());
					configuationEntity.setNumUser(entity.getNumberOfUser());
					
			
					
					primaryStage.setUserData(entity);
					entity.setConfiguationEntity(configuationEntity);
					new IoTCreation().start(primaryStage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    	HBox hbBtn = new HBox(10);
    	hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	hbBtn.getChildren().add(btn);
    	grid.add(hbBtn, 1, 7);
 
        primaryStage.setTitle("IotSim");
        primaryStage.setScene(scene);
        primaryStage.show();
	}

	private TextField addInputWithTextReminder(GridPane grid, String textReminder,int col,int row) {
		Label userName = new Label(textReminder);
    	grid.add(userName, col, row);
    	TextField textField = new TextField();
    	
    	grid.add(textField, col+1, row);
    	return textField;
    	
	}
	
}
