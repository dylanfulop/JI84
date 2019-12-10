package com.JI84.main;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class DisplayBox extends VBox{

	private ArrayList<String> input,output;
	/**
	 * Creates a display box pane
	 * The pane will display all the strings in the input list followed by the corresponding string in the output list
	 * in 2 different colors to represent input and output
	 * @param input
	 * @param output
	 */
	public DisplayBox(ArrayList<String> input, ArrayList<String> output){
		super();
		this.input = input;
		this.output = output;
		this.setSpacing(2 * Main.scale);
		this.setPadding(new Insets(12 * Main.scale, 20 * Main.scale, 12 * Main.scale, 20 * Main.scale));
		this.setMinSize(760 * Main.scale, 320 * Main.scale);
		this.update();

	}

	/**
	 * Updates the display box
	 * Will be called anytime a new element is added to input and output list
	 * The display box will add 2 extra labels to represent the new input/output pair, or will
	 * first delete the label at the bottom of the list to make room if there are more elements
	 * than fit in the display box
	 */
	public void update(){
		this.getChildren().clear();
		if(input.size() < 7){
			for(int i = 0; i < input.size(); i++){
				Label inl = new Label(input.get(i));
				Label outl = new Label(output.get(i));
				inl.setTextFill(Color.BLUE);
				outl.setTextFill(Color.GREEN);

				this.getChildren().addAll(inl, outl);
			}
		}else{
			input.remove(0);
			output.remove(0);
			for(int i = 0; i < input.size(); i++){
				Label inl = new Label(input.get(i));
				Label outl = new Label(output.get(i));
				inl.setTextFill(Color.BLUE);
				outl.setTextFill(Color.GREEN);

				this.getChildren().addAll(inl, outl);
			}
		}
	}

}
