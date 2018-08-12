package acds.launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * 
 *
 */
public class AppLaunchPanel extends Pane {

	AppLauncher launcher = null ;

	AppConfiguration appConfig = null ;


	List<ComboBox<String>> comboControls = new ArrayList<ComboBox<String>>();
	List<CheckBox> checkControls = new ArrayList<CheckBox>();
	


	public void setAppConfig(AppConfiguration appConfig) {
		this.appConfig = appConfig;
	}

	public void setLauncher(AppLauncher launchAction) {
		this.launcher = launchAction;
	}


	public void showMainPanel(Stage stage) {
		
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 50, 50, 50));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		int row = 0;

		for (Object key : appConfig.getList("ui.combo.list")) {

			gridPane.add(new Label(appConfig.getString(key + ".label")), 0, row, 1, 1);
			ComboBox<String> cb = new ComboBox<String>();
			cb.setId((String) key);
			comboControls.add(cb);

			List<Object> opts = appConfig.getList(key + ".opts");
			for (Object opt : opts) {
				cb.getItems().add((String) opt);
			}
			
			cb.setValue(appConfig.getString(key + ".default"));
	
			gridPane.add(cb, 1, row, 2, 1);
			row++;
		}
		
		row++;

		Button okButton = new Button("OK");
		okButton.setOnAction(value -> {
			try {
				doAction();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		gridPane.add(okButton, 1, row, 1, 1);

		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(value -> {
			doExit();
		});
		gridPane.add(cancelButton, 2, row, 1, 1);

		VBox rightBox = new VBox();
		rightBox.setPadding(new Insets(10, 50, 50, 50));
		rightBox.setSpacing(10);

		for (Object key : appConfig.getList("ui.check.list")) {

			CheckBox cb = new CheckBox(appConfig.getString(key + ".label"));
			cb.setId((String) key);

			checkControls.add(cb);

			rightBox.getChildren().add(cb);

		}

		// Instantiating the BorderPane class
		BorderPane mainPane = new BorderPane();
		mainPane.setRight(rightBox);
		mainPane.setCenter(gridPane);

		// Creating a scene object
		Scene scene = new Scene(mainPane);

		// Setting title to the Stage
		stage.setTitle("BorderPane Example");

		// Adding scene to the stage
		stage.setScene(scene);

		// Displaying the contents of the stage
		stage.show();

	}
	
	
	/**
	 * Get property (name,values) from the current UI settings
	 * 
	 * @return
	 */
	public Map<String,String> getValues() {

		Map<String,String> propList = new HashMap<String,String>();
		
		String propValue = "" ;
		String propName  = null ;
		
		for (ComboBox<String> cb : comboControls) {
			String id = cb.getId() ;
			propName = appConfig.getString(id + ".property");
			
			
			propValue =cb.getValue();		
			propList.put(propName, propValue);
		}

		for (CheckBox cb : checkControls) {
			String id = cb.getId() ;
			propName = appConfig.getString(id + ".property");
			
			if ( cb.isSelected() ) {
				propValue="true";
			} else {
				propValue="false";
			}
			
			propList.put(propName, propValue);
		}	
		
		return propList;
	}

	/**
	 * @throws IOException 
	 * 
	 * 
	 */
	public void doAction() throws IOException {
		launcher.launch(getValues());
	}

	
	public void doExit() {
		launcher.exit();
	}


}
