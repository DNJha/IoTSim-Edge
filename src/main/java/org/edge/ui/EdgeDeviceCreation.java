package org.edge.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.text.html.parser.Entity;

import org.edge.core.edge.EdgeDevice;
import org.edge.core.iot.IoTDevice;
import org.edge.entity.ConfiguationEntity;
import org.edge.entity.ConfiguationEntity.BwProvisionerEntity;
import org.edge.entity.ConfiguationEntity.EdgeDataCenterEntity;
import org.edge.entity.ConfiguationEntity.HostEntity;
import org.edge.entity.ConfiguationEntity.IotDeviceEntity;
import org.edge.entity.ConfiguationEntity.NetworkModelEntity;
import org.edge.entity.ConfiguationEntity.PeEntity;
import org.edge.entity.ConfiguationEntity.RamProvisionerEntity;
import org.edge.entity.ConfiguationEntity.VmSchedulerEntity;
import org.edge.entity.UIEntity;
import org.edge.utils.NumberTextField;
import org.edge.utils.PackageUtils;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class EdgeDeviceCreation extends Application {

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);

		grid.setPadding(new Insets(10, 10, 10, 10));

		Scene scene = new Scene(grid, 800, 600);
		Label label = new Label("atrributes of edge devices:");

		grid.add(label, 0, 0);

		TextField ram = addInputWithTextReminder(grid, "ramProvisioner:", 0, 1);
		ram.setText("org.cloudbus.cloudsim.provisioners.RamProvisionerSimple");

		TextField ramSize = addInputWithTextReminder(grid, "ramSize:", 0, 2);
		ramSize.setText("100000");

		TextField bw = addInputWithTextReminder(grid, "bwProvisioner:", 0, 3);
		bw.setText("org.cloudbus.cloudsim.provisioners.BwProvisionerSimple");
		TextField bwSize = addInputWithTextReminder(grid, "bwSize:", 0, 4);
		bwSize.setText("100000");

		TextField storage = addInputWithTextReminder(grid, "storage:", 0, 5);
		storage.setText("100000");
		TextField numberOfPes = addInputWithTextReminder(grid, "numberOfPe:", 0,6);
		numberOfPes.setText("1");

		TextField peProvisionerClassName = addInputWithTextReminder(grid, "peProvisionerClassName:", 0, 7);
		peProvisionerClassName.setText("org.cloudbus.cloudsim.provisioners.PeProvisionerSimple");

		TextField mips = addInputWithTextReminder(grid, "mips:", 0, 8);
		mips.setText("1000000");

		TextField vmScheduler = addInputWithTextReminder(grid, "vmScheduler:", 3, 1);
		vmScheduler.setText("org.cloudbus.cloudsim.VmSchedulerTimeShared");

		TextField max_IoTDevice_capacity = addInputWithTextReminder(grid, "max_IoTDevice_capacity:", 3, 2);
		max_IoTDevice_capacity.setText("10");

		TextField max_battery_capacity = addInputWithTextReminder(grid, "max_battery_capacity:", 3, 3);
		max_battery_capacity.setText("100.0");

		TextField battery_drainage_rate = addInputWithTextReminder(grid, "battery_drainage_rate:", 3, 4);
		battery_drainage_rate.setText("1");

		TextField current_battery_capacity = addInputWithTextReminder(grid, "current_battery_capacity:", 3, 5);
		current_battery_capacity.setText("100.0");

		TextField edgeType = addInputWithTextReminder(grid, "edgeType:", 3, 6);
		edgeType.setText("RASPBERRY_PI");

		ChoiceBox<String> networkType = new ChoiceBox<String>(
				FXCollections.observableArrayList("wifi", "wlan", "4G", "3G", "bluetooth", "lan"));
		networkType.setTooltip(new Tooltip("please select one of the network type"));
		networkType.getSelectionModel().select(0);
		Label networkTypeL = new Label("networkType:");
		grid.add(networkTypeL, 3, 7);

		grid.add(networkType,4, 7);

		ChoiceBox<String> communicationProtocol = new ChoiceBox<String>(
				FXCollections.observableArrayList("AMQP", "XMPP", "MQTT", "CoAP"));
		communicationProtocol.setTooltip(new Tooltip("please select one of the communicationProtocol type"));
		communicationProtocol.getSelectionModel().select(0);
		Label communicationProtocolL = new Label("communicationProtocol:");
		grid.add(communicationProtocolL, 3, 8);

		grid.add(communicationProtocol, 4, 8);

		primaryStage.setTitle("IotSim");
		primaryStage.setScene(scene);
		primaryStage.show();
		Button btn = new Button("next");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * @param event
			 */
			@Override
			public void handle(ActionEvent event) {
				try {
					UIEntity userData = (UIEntity) primaryStage.getUserData();
					int numberOfEdgeDevice = userData.getNumberOfEdgeDevice();
					List<HostEntity> entities=new ArrayList<>();
					ConfiguationEntity configuationEntity = userData.getConfiguationEntity();
					EdgeDataCenterEntity edgeDataCenterEntity = configuationEntity.getEdgeDatacenter().get(0);
					edgeDataCenterEntity.getCharacteristics().setHostListEntities(entities);
					edgeDataCenterEntity.getVmAllocationPolicy().setHostEntities(entities);
					for (int i = 0; i < numberOfEdgeDevice; i++) {

						HostEntity entity = new HostEntity();
						entity.setBattery_drainage_rate(getDoubleValue(battery_drainage_rate.getText()));
						BwProvisionerEntity bwProvisioner = new BwProvisionerEntity();
						bwProvisioner.setBwSize(getIntValue(bwSize.getText()));
						bwProvisioner.setClassName(bw.getText());
						entity.setBwProvisioner(bwProvisioner);
						RamProvisionerEntity ramProvisioner = new RamProvisionerEntity();
						ramProvisioner.setRamSize(getIntValue(ramSize.getText()));
						ramProvisioner.setClassName(ram.getText());
						entity.setRamProvisioner(ramProvisioner);

						entity.setCurrent_battery_capacity(getDoubleValue(current_battery_capacity.getText()));
						entity.setEdgeType(edgeType.getText());
						entity.setMax_battery_capacity(getDoubleValue(max_battery_capacity.getText()));
						entity.setMax_IoTDevice_capacity(getIntValue(max_IoTDevice_capacity.getText()));
						NetworkModelEntity networkModel = new NetworkModelEntity();
						networkModel
								.setCommunicationProtocol(communicationProtocol.getSelectionModel().getSelectedItem());
						networkModel.setNetworkType(networkType.getSelectionModel().getSelectedItem());
						entity.setNetworkModel(networkModel);

						List<PeEntity> peEntities=new ArrayList<>();
						for (int in=0;in<Integer.parseInt(numberOfPes.getText());in++) {
							PeEntity pe=new PeEntity();
							pe.setId(in+1);
							pe.setMips(Integer.parseInt(mips.getText()));
							pe.setPeProvisionerClassName(peProvisionerClassName.getText());
							peEntities.add(pe);
							
							
						}
						entity.setId(i+1);
						entity.setPeEntities(peEntities);
						entity.setStorage(Long.parseLong(storage.getText()));
						VmSchedulerEntity vmSchedulerEntity = new VmSchedulerEntity();
						vmSchedulerEntity.setClassName(vmScheduler.getText());
						entity.setVmScheduler(vmSchedulerEntity);
						entities.add(entity);
					}
						
					new VMCreation().start(primaryStage);
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
					new EdgeDataCenterCreation().start(primaryStage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
		grid.add(btn, 3, 13);
		grid.add(btn2, 2, 13);

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
