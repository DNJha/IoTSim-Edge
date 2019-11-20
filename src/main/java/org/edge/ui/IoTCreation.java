package org.edge.ui;

import java.util.ArrayList;
import java.util.List;

import org.edge.core.feature.Mobility.Location;
import org.edge.core.iot.IoTDevice;
import org.edge.entity.ConfiguationEntity;
import org.edge.entity.ConfiguationEntity.IotDeviceEntity;
import org.edge.entity.ConfiguationEntity.MobilityEntity;
import org.edge.entity.ConfiguationEntity.MovingRangeEntity;
import org.edge.entity.ConfiguationEntity.NetworkModelEntity;
import org.edge.entity.UIEntity;
import org.edge.utils.PackageUtils;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class IoTCreation extends Application {
	private static final double maxWidth = 100;

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);

		grid.setPadding(new Insets(10, 10, 10, 10));

		Scene scene = new Scene(grid, 600, 600);
		Label label = new Label("atrributes of IoT devices:");

		grid.add(label, 0, 0);

		List<Class> classsFromPackage = PackageUtils.getClasssFromPackage("org.edge");
		List<Class> availableIoTs = new ArrayList<>();
		List<String> availableIoTName = new ArrayList<>();
		for (Class class1 : classsFromPackage) {
			if (IoTDevice.class.isAssignableFrom(class1) && IoTDevice.class != class1) {
				availableIoTs.add(class1);
				availableIoTName.add(class1.getSimpleName());
			}
		}

		ChoiceBox<String> choiceBox = new ChoiceBox<String>(FXCollections.observableArrayList(availableIoTName));
		choiceBox.setTooltip(new Tooltip("please select one of the ioT type"));
		choiceBox.getSelectionModel().select(0);
		Label className = new Label("ioT Class Name:");
		choiceBox.setMaxWidth(maxWidth);
		grid.add(className, 0, 1);
		grid.add(choiceBox, 1, 1);
		TextField iotType = addInputWithTextReminder(grid, "iotType:", 0, 2);
		iotType.setText("default type");
		grid.add(new Label("e.g. envirmental sensor"), 2, 2);
		TextField name = addInputWithTextReminder(grid, "name:", 0, 3);
		name.setText("default name");
		TextField data_frequency = addInputWithTextReminder(grid, "data_frequency:", 0, 4);
		data_frequency.setText("1");
		TextField dataGenerationTime = addInputWithTextReminder(grid, "dataGenerationTime:", 0, 5);
		dataGenerationTime.setText("1");
		TextField complexityOfDataPackage = addInputWithTextReminder(grid, "complexityOfDataPackage:", 0, 6);
		complexityOfDataPackage.setText("1");
		TextField dataSize = addInputWithTextReminder(grid, "dataSize:", 0, 7);
		dataSize.setText("10");
		TextField max_battery_capacity = addInputWithTextReminder(grid, "max_battery_capacity:", 0, 8);
		max_battery_capacity.setText("1");
		TextField battery_drainage_rate = addInputWithTextReminder(grid, "battery_drainage_rate:", 0, 9);
		battery_drainage_rate.setText("1");
		TextField processingAbility = addInputWithTextReminder(grid, "processingAbility:", 0, 10);
		processingAbility.setText("1");
		Label networkModel = new Label("atrributes of networkModel:");

		grid.add(networkModel, 0, 11);
		
	
		
		/*
		 * WIFI(5d), WLAN(5d), FourG(4d), ThreeG(2d), BLUETOOTH(2d), LAN(3);
		 */
		ChoiceBox<String> networkType = new ChoiceBox<String>(
				FXCollections.observableArrayList("wifi", "wlan", "4G", "3G", "bluetooth", "lan"));
		networkType.setTooltip(new Tooltip("please select one of the network type"));
		networkType.getSelectionModel().select(0);
		Label networkTypeL = new Label("networkType:");
		grid.add(networkTypeL, 0, 12);

		grid.add(networkType, 1, 12);

		ChoiceBox<String> communicationProtocol = new ChoiceBox<String>(
				FXCollections.observableArrayList("AMQP", "XMPP", "MQTT", "CoAP"));
		communicationProtocol.setTooltip(new Tooltip("please select one of the communicationProtocol type"));
		communicationProtocol.getSelectionModel().select(0);
		Label communicationProtocolL = new Label("communicationProtocol:");
		grid.add(communicationProtocolL, 0, 13);

		grid.add(communicationProtocol, 1, 13);

		CheckBox box=new CheckBox("movable");
		grid.add(box, 2, 3);	
		/*box.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
			}
		});
		*/
		TextField x = addInputWithTextReminder(grid, "x:", 2, 4);
		x.setText("0");

		TextField y = addInputWithTextReminder(grid, "y:", 2, 5);
		y.setText("0");
		
		TextField rangeBegin = addInputWithTextReminder(grid, "rangeBegin:", 2, 6);
		rangeBegin.setText("0");
		TextField rangeEnd = addInputWithTextReminder(grid, "rangeEnd:", 2, 7);
		rangeEnd.setText("0");
		
		TextField velocity = addInputWithTextReminder(grid, "velocity:", 2,8);
		velocity.setText("0");
		
		primaryStage.setTitle("IotSim");
		primaryStage.setScene(scene);
		primaryStage.show();
		Button btn = new Button("next");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					UIEntity userData = (UIEntity) primaryStage.getUserData();
					int numberOfIotDevice = userData.getNumberOfIotDevice();
					ConfiguationEntity configuationEntity = userData.getConfiguationEntity();

					userData.setConfiguationEntity(configuationEntity);
					List<IotDeviceEntity> ioTDeviceEntities = new ArrayList<>();
					configuationEntity.setIoTDeviceEntities(ioTDeviceEntities);

					IotDeviceEntity iot = new IotDeviceEntity();

					int selectedIndex = choiceBox.getSelectionModel().getSelectedIndex();
					iot.setIoTClassName(availableIoTs.get(selectedIndex).getName());
					iot.setBattery_drainage_rate(getDoubleValue(battery_drainage_rate.getText()));
					iot.setComplexityOfDataPackage(getIntValue(complexityOfDataPackage.getText()));
					iot.setData_frequency(getDoubleValue(data_frequency.getText()));
					iot.setDataGenerationTime(getDoubleValue(dataGenerationTime.getText()));
					iot.setDataSize(getIntValue(dataSize.getText()));
					// iot.setDataTemplate(getDa(dataSize.getText()));
					iot.setIotType(iotType.getText());
					iot.setMax_battery_capacity(getIntValue(max_battery_capacity.getText()));
					iot.setName(name.getText());
					iot.setProcessingAbility(getDoubleValue(processingAbility.getText()));
					NetworkModelEntity networkModelEntity = new NetworkModelEntity();
					networkModelEntity
							.setCommunicationProtocol(communicationProtocol.getSelectionModel().getSelectedItem());
					networkModelEntity.setNetworkType(networkType.getSelectionModel().getSelectedItem());

					iot.setNetworkModelEntity(networkModelEntity);
					iot.setNumberofEntity(numberOfIotDevice);
					ioTDeviceEntities.add(iot);
						
					  MobilityEntity mobilityEntity=new MobilityEntity();
					  mobilityEntity.setLocation(new Location(Double.parseDouble(x.getText()),Double.parseDouble(y.getText()), 0));
					  
					  mobilityEntity.setMovable(box.isSelected()); 
					  MovingRangeEntity rangeEntity=new MovingRangeEntity();
					  rangeEntity.beginX=Integer.parseInt(rangeBegin.getText());
					  rangeEntity.endX=Integer.parseInt(rangeEnd.getText());
					  mobilityEntity.setRange(rangeEntity);
					  
					  mobilityEntity.setVolecity(Double.parseDouble(velocity.getText()));
					 iot.setMobilityEntity(mobilityEntity);
					/***
					 * 
					 * "mobilityEntity": { "movable": false, "location": { "x": 0.0, "y": 0.0, "z":
					 * 0.0 } },
					 * 
					 * 
					 */
					// iot.setMobilityEntity(mobilityEntity);

					new EdgeDataCenterCreation().start(primaryStage);
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
					new Main().start(primaryStage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
		grid.add(btn, 1, 14);
		grid.add(btn2, 0, 14);

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
		textField.setMaxWidth(maxWidth);
		grid.add(textField, col + 1, row);
		return textField;

	}

}
