package org.edge.ui;

import java.util.ArrayList;
import java.util.List;

import org.edge.entity.ConfiguationEntity;
import org.edge.entity.ConfiguationEntity.MELEntities;
import org.edge.entity.UIEntity;

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
/**
 * "vmid": 1,
	"mips": 1111,
	"size": 1111,
	"ram": 1222,
	"bw": 1111,
	"pesNumber": 1,
	"vmm": "xxx",
	"cloudletSchedulerClassName": "org.cloudbus.cloudsim.CloudletSchedulerTimeShared"
 * 
 * 
 * 
 * 
 */
public class VMCreation extends  Application{

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
		Label label = new Label("atrributes of VMs:");

		grid.add(label, 0, 0);

		

		TextField mips = addInputWithTextReminder(grid, "mips:", 0, 1);
		mips.setText("10000");
		TextField size = addInputWithTextReminder(grid, "size:", 0, 2);
		size.setText("1000");
		TextField ram = addInputWithTextReminder(grid, "ram:", 0, 3);
		ram.setText("1000");
	
		TextField bw = addInputWithTextReminder(grid, "bw:", 0, 4);
		bw.setText("1000");
		TextField pesNumber = addInputWithTextReminder(grid, "pesNumber:", 0, 5);
		pesNumber.setText("1");
		
		TextField vmm = addInputWithTextReminder(grid, "vmm:", 0, 6);
		vmm.setText("Xen");
		
		TextField cloudletSchedulerClassName = addInputWithTextReminder(grid, "cloudletSchedulerClassName:", 0, 7);
		cloudletSchedulerClassName.setText("org.cloudbus.cloudsim.CloudletSchedulerTimeShared");
		
		
			
		
		primaryStage.setTitle("IotSim");
		primaryStage.setScene(scene);
		primaryStage.show();
		Button btn = new Button("next");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					UIEntity userData = (UIEntity) primaryStage.getUserData();
					int numberOfVms = userData.getNumberOfVms();
					
					List<MELEntities> entities=new ArrayList<>();
					for (int i = 0; i <numberOfVms; i++) {
						MELEntities entity=new MELEntities();
						entity.setBw(getIntValue(bw.getText()));
						entity.setCloudletSchedulerClassName(cloudletSchedulerClassName.getText());
						entity.setMips(getIntValue(mips.getText()));
						entity.setPesNumber(getIntValue(pesNumber.getText()));
						entity.setRam(getIntValue(ram.getText()));
						entity.setSize(getIntValue(size.getText()));
						entity.setVmm(vmm.getText());
						entity.setVmid(i+1);
						entities.add(entity);
					}
					ConfiguationEntity configuationEntity = userData.getConfiguationEntity();
					configuationEntity.setMELEntities(entities);
					
					
					
					new ConnectionCreation().start(primaryStage);
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
					new EdgeDeviceCreation().start(primaryStage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		grid.add(btn, 1,8);
		grid.add(btn2, 0,8);

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
