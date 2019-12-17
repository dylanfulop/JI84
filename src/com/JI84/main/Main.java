package com.JI84.main;

import java.util.ArrayList;

import com.JI84.graphing.EqPane;
import com.JI84.graphing.GraphPane;
import com.JI84.graphing.WdwPane;
import com.JI84.graphing.Window;
import com.JI84.math.MathMode;
import com.JI84.math.ModePane;
import com.JI84.statistics.ListPane;
import com.JI84.statistics.StatsPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application{
public static final double scale = 8.0/8.0;
	
private MathMode mode;
private Window window;

public static Double[][] lists;
public static ArrayList<String> equations;

/**
 * Start the application
 */
	public void start(Stage primaryStage) throws Exception {
		lists = new Double[8][20];
		equations = new ArrayList<String>();
		for(int i = 1; i <= 16; i++){//so that we can use indexes other than 0 to refer to equations
			equations.add("");
		}
		
		StackPane root = new StackPane();
		window = new Window(-10, 10, 1, -10, 10, 1, 0.05);
		mode = new MathMode(true, -1, 0, window);
		
		
		//setup tab layouts and properties
		TabPane tabPane = new TabPane();
		MainPane mainPane = new MainPane(mode);//root pane
		EqPane eqPane = new EqPane(mode);
		WdwPane wdwPane = new WdwPane(mode);
		GraphPane graph = new GraphPane(mode);
		ModePane modePane = new ModePane(mode, wdwPane, eqPane);
		StatsPane statPane = new StatsPane();
		ListPane listPane = new ListPane(mode, statPane);
		
		//setup tab variables
		Tab mainTab = new Tab();
		mainTab.setText("Main");
		mainTab.setClosable(false);
		mainTab.setContent(mainPane);
		Tab eqTab = new Tab();
		eqTab.setText("y=");
		eqTab.setClosable(false);
		eqTab.setContent(eqPane);
		Tab wdwTab = new Tab();
		wdwTab.setText("window");
		wdwTab.setClosable(false);
		wdwTab.setContent(wdwPane);
		Tab graphTab = new Tab();
		graphTab.setClosable(false);
		graphTab.setText("graph");
		graphTab.setContent(graph);
		Tab modeTab = new Tab();
		modeTab.setClosable(false);
		modeTab.setText("mode");
		modeTab.setContent(modePane);
		Tab listTab = new Tab();
		listTab.setClosable(false);
		listTab.setText("lists");
		listTab.setContent(listPane);
		Tab statTab = new Tab();
		statTab.setClosable(false);
		statTab.setContent(statPane);
		statTab.setText("stats");
		
		
		//set up tab pane
		tabPane.getSelectionModel().select(0);
		tabPane.getTabs().addAll(mainTab, eqTab, wdwTab, graphTab, modeTab, listTab, statTab);
		root.getChildren().add(tabPane);
		
		//create scene and place it on stage
		Scene scene = new Scene(root, 800 * Main.scale, 800 * Main.scale);
		primaryStage.setTitle("JI84");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();


	}

	/**
	 * Launch the application when program is ran
	 */
	public static void main(String[] args)    {      
		launch(args);
	}

}

