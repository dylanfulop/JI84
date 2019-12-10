package com.JI84.main;

import java.util.ArrayList;

import com.JI84.math.MathMode;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainPane extends VBox{
	//define GUI components
	private TextField inputTF;
	private ArrayList<String> input, output;
	private DisplayBox display;
	private InputGrid inputGrid;
	/**
	 * Constructor for the main/calculator pane
	 * @param mode The current mode settings that should be used
	 */
	public MainPane(MathMode mode){
		super();
		//create lists for input and output to hold strings in the display box
		input = new ArrayList<String>();
		output = new ArrayList<String>();
		//space out the elements in the pane and set the padding
		setSpacing(5 * Main.scale);
		setPadding(new Insets(12 * Main.scale, 20 * Main.scale, 12 * Main.scale, 20 * Main.scale));

		
		//create an HBox and set its spacing and padding to 0 to place the input text field and input grid in
		HBox inputTextPane = new HBox();
		inputTextPane.setSpacing(0);
		inputTextPane.setPadding(new Insets(0, 0, 0, 0));
		
		//create and initialize GUI components
		display = new DisplayBox(input, output); //display window
		inputTF = new TextField();
		inputTF.setMinSize(760 * Main.scale, 30 * Main.scale); //set the size of the text field so it fills the frame properly
		inputTF.setText("");
		inputTF.setOnAction(new InputTFHandler()); //set the action listener for the input tf
		inputGrid = new InputGrid(inputTF, input, output, display, mode, null);//create the input grid object

		//add GUI components to panes
		inputTextPane.getChildren().add(inputTF);
		
		//add panes to root pane
		getChildren().add(display);
		getChildren().add(inputTextPane);
		getChildren().add(inputGrid);
		
	}
	
	/**
	 * The Handler for the text field so that the inputgrid's enter method is called whenever the inputtf's enter is pressed
	 *
	 */
	private class InputTFHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event) {
			inputGrid.enter();
		}
	}
	
	
}
