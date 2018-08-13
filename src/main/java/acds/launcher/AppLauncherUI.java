package acds.launcher;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Appl Launcher 
 *
 */
public class AppLauncherUI extends Application {

	AppLaunchPanel launchPanel = new AppLaunchPanel();
	AppConfiguration appConfig = new AppConfiguration();
	AppLauncher	appLauncher = new AppCommandLauncher();
	
	/**
	 * Standard main method.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		launch(args);
	}

	/**
	 * Standard JFX Start Method.
	 * 
	*/
	public void start(Stage stage) throws Exception {
		appConfig.init("launcher", getParameters().getRaw());		
		appLauncher.setAppConfig(appConfig);
		launchPanel.setLauncher(appLauncher);
		launchPanel.setAppConfig(appConfig);
		launchPanel.showMainPanel(stage);
	}


	


}
