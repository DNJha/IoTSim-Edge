package org.edge.ui;

import java.util.ArrayList;
import java.util.List;

import org.edge.entity.ConfiguationEntity.EdgeDataCenterEntity;
import org.edge.entity.ConfiguationEntity.EdgeDatacenterCharacteristicsEntity;
import org.edge.entity.ConfiguationEntity.VmAllcationPolicyEntity;
import org.edge.core.iot.IoTDevice;
import org.edge.entity.UIEntity;
import org.edge.utils.PackageUtils;

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

public class EdgeDataCenterCreation extends Application {

	private Stage primaryStage;
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);

		grid.setPadding(new Insets(10, 10, 10, 10));

		Scene scene = new Scene(grid, 600, 600);
		Label label = new Label("atrributes of edge datacenter:");

		grid.add(label, 0, 0);


		TextField name = addInputWithTextReminder(grid, "name:", 0, 1);
		name.setText("defaultDataCenter");
		TextField architecture = addInputWithTextReminder(grid, "architecture:", 0, 2);
		architecture.setText("x86");
	
		

		TextField os = addInputWithTextReminder(grid, "os:", 0, 3);
		os.setText("linux");

	
		TextField vmm = addInputWithTextReminder(grid, "vmm:", 0,4);
		vmm.setText("Xen");
		
		TextField cost = addInputWithTextReminder(grid, "cost:", 0, 5);
		cost.setText("1.0");
		
		TextField timeZone = addInputWithTextReminder(grid, "timeZone:", 2, 1);
		timeZone.setText("10.0");
		
		TextField costPerSec = addInputWithTextReminder(grid, "costPerSec:", 2, 2);
		costPerSec.setText("0.0");
		TextField costPerMem = addInputWithTextReminder(grid, "costPerMem:", 2, 3);
		costPerMem.setText("0.05");
		
		
		TextField costPerStorage = addInputWithTextReminder(grid, "costPerStorage:", 2, 4);
		costPerStorage.setText("0.001");
		
		TextField costPerBw = addInputWithTextReminder(grid, "costPerBw:", 2, 5);
		costPerBw.setText("0.0");
		TextField schedulingInterval = addInputWithTextReminder(grid, "schedulingInterval", 0, 6);
		schedulingInterval.setText("1.0");
		
		
		TextField vmPolicyClassName = addInputWithTextReminder(grid, "vmPolicyClassName", 2, 6);
		vmPolicyClassName.setText("org.cloudbus.cloudsim.VmAllocationPolicySimple");
		
		String[] protoclArray= {"AMQP","XMPP","MQTT","CoAP"};
		
		HBox hbBtn = new HBox(10);
		Label communicationProtocolL = new Label("communicationProtocol supported:");
    	hbBtn.setAlignment(Pos.BASELINE_LEFT);
    	hbBtn.getChildren().add(communicationProtocolL);
		
		grid.add(hbBtn, 0,7);
		int col=0;
		List<CheckBox> protocolSupportList=new ArrayList<>();
		for (String string : protoclArray) {
			CheckBox pro=new CheckBox(string);
			protocolSupportList.add(pro);
			grid.add(pro, col,8);
			col++;
		}
		
			
		Label ioTDeviceSupported = new Label("ioTDevice Supported:");
		grid.add(ioTDeviceSupported, 0,9);
		
		List<Class> classsFromPackage = PackageUtils.getClasssFromPackage("org.edge");
		List<Class> availableIoTs = new ArrayList<>();
		List<String> availableIoTName = new ArrayList<>();
		col=0;
		int row=10;
		List<CheckBox> ioTDeviceSupportList=new ArrayList<>();
		for (Class class1 : classsFromPackage) {
			if (IoTDevice.class.isAssignableFrom(class1) && IoTDevice.class != class1) {
				availableIoTs.add(class1);
				availableIoTName.add(class1.getSimpleName());
				CheckBox iotName=new CheckBox(class1.getSimpleName());
				ioTDeviceSupportList.add(iotName);
				if(col>4) {
					col=0;	
					row++;
				}
				grid.add(iotName, col,row);
				col++;
			}
			
		}
		
		
		
		

		primaryStage.setTitle("IotSim");
		primaryStage.setScene(scene);
		primaryStage.show();
		Button btn = new Button("next");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					UIEntity userData = (UIEntity) primaryStage.getUserData();
					
					List<EdgeDataCenterEntity> edgeDatacenter =new ArrayList<>();
					userData.getConfiguationEntity().setEdgeDatacenter(edgeDatacenter);
					
					int numberOfDataCenter = userData.getNumberOfDataCenter();
					
					for (int i = 0; i <numberOfDataCenter; i++) {
						EdgeDataCenterEntity centerEntity=new EdgeDataCenterEntity();
						EdgeDatacenterCharacteristicsEntity characteristics=new EdgeDatacenterCharacteristicsEntity();
						characteristics.setArchitecture(architecture.getText());
						characteristics.setCost(getDoubleValue(cost.getText()));
						characteristics.setCostPerBw(getDoubleValue(costPerBw.getText()));
						characteristics.setCostPerMem(getDoubleValue(costPerMem.getText()));
						characteristics.setCostPerSec(getDoubleValue(costPerSec.getText()));
						characteristics.setCostPerStorage(getDoubleValue(costPerStorage.getText()));
						characteristics.setOs(os.getText());
						characteristics.setTimeZone(getDoubleValue(timeZone.getText()));
						characteristics.setVmm(vmm.getText());
						
						List<String> communicationProtocolSupported=new ArrayList<>();
						for (CheckBox box : protocolSupportList) {
							if(box.isSelected()) {
								communicationProtocolSupported.add(box.getText());								
							}
						}			
						characteristics.setCommunicationProtocolSupported(communicationProtocolSupported);
						
						List<String> ioTDeviceClassNameSupported=new ArrayList<>();
						i=0;
						for (CheckBox box : ioTDeviceSupportList) {
							if(box.isSelected()) {					
								ioTDeviceClassNameSupported.add(availableIoTs.get(i).getName());								
							}
							i++;
						}			
						characteristics.setIoTDeviceClassNameSupported(ioTDeviceClassNameSupported);
						centerEntity.setCharacteristics(characteristics);
						centerEntity.setName(name.getText());
						centerEntity.setSchedulingInterval(getDoubleValue(schedulingInterval.getText()));
						VmAllcationPolicyEntity vmAllocationPolicy=new VmAllcationPolicyEntity();
						vmAllocationPolicy.setClassName(vmPolicyClassName.getText());
						centerEntity.setVmAllocationPolicy(vmAllocationPolicy);	
						edgeDatacenter.add(centerEntity);
					}
					
				
					new EdgeDeviceCreation().start(primaryStage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
		Button btn2 = new Button("go back");
		btn2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					new IoTCreation().start(primaryStage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		grid.add(btn, 2, row+1);
		grid.add(btn2, 1,row+1);

	}

	protected double getDoubleValue(String text) {
		double parseDouble = 1;
		try {
			parseDouble = Double.parseDouble(text);
		} catch (NumberFormatException exception) {

		}
		return parseDouble;

	}
	protected int getIntValue(String text) {
		int parseDouble = 1;
		try {
			parseDouble = Integer.parseInt(text);
		} catch (NumberFormatException exception) {

		}
		return parseDouble;

	}

	private TextField addInputWithTextReminder(GridPane grid, String textReminder, int col, int row) {
		Label userName = new Label(textReminder);
		grid.add(userName, col, row);
		TextField textField = new TextField();

		grid.add(textField, col + 1, row);
		return textField;

	}

}
