package com.JI84.graphing;

import com.JI84.main.InputGrid;
import com.JI84.main.Main;
import com.JI84.math.MathMode;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class EqPane extends GridPane{
	private Label[] eqLab;
	private TextField[] eqs;
	private RadioButton[] rbtns;
	private MathMode mode;
	private int currentSel;
	/**
	 * Creates an equation pane in function mode, which can be changed by calling pane.update() after changing the mode
	 * Parameters eqmode
	 **/
	public EqPane(MathMode mode){
		super();
		this.mode = mode;
		this.setMinSize(2 * Main.scale, 10 * Main.scale);
		this.setHgap(10 * Main.scale);
		this.setVgap(25 * Main.scale);
		this.setPadding(new Insets(10 * Main.scale, 10 * Main.scale, 10 * Main.scale, 10 * Main.scale));
		eqLab = new Label[8];
		eqs = new TextField[8];
		rbtns = new RadioButton[8];
		currentSel = 0;

		ToggleGroup group = new ToggleGroup();
		for(int i = 0; i < 8; i++){
			eqLab[i] = new Label("Y" + (i+1));
			eqs[i] = new TextField();
			rbtns[i] = new RadioButton("Type in Y" + (i+1));
			rbtns[i].setOnAction(new BtnHandler());
			rbtns[i].setToggleGroup(group);
			eqs[i].setMinSize(500 * Main.scale, 30 * Main.scale);
			this.add(eqLab[i], 0, i);
			this.add(eqs[i], 1, i);
			this.add(rbtns[i], 2, i);

			eqs[i].setOnAction(new InputHandler());
		}
		rbtns[currentSel].setSelected(true);

		InputGrid g = new InputGrid(eqs[currentSel], null, null, null, mode, eqs);

		this.add(g, 1, 9);


	}
/**
 * Updates the pane by recreating all of its elements based on the mode
 * Should be called whenever equation mode is changed or a radio button is selected
 * As the input grid will need to change its target text field when the radio buttons are 
 * selected
 **/
	public void update(){
		this.getChildren().clear();
		eqLab = new Label[8];
		eqs = new TextField[8];
		rbtns = new RadioButton[8];
		this.setVgap(25 * Main.scale);
		if(mode.getEqMode() == 0){
			ToggleGroup group = new ToggleGroup();
			for(int i = 0; i < 8; i++){
				eqLab[i] = new Label("Y" + (i+1));
				eqs[i] = new TextField(Main.equations.get(i));
				rbtns[i] = new RadioButton("Type in Y" + (i+1));
				rbtns[i].setOnAction(new BtnHandler());
				rbtns[i].setToggleGroup(group);
				eqs[i].setMinSize(500 * Main.scale, 30 * Main.scale);
				this.add(eqLab[i], 0, i);
				this.add(eqs[i], 1, i);
				this.add(rbtns[i], 2, i);

				eqs[i].setOnAction(new InputHandler());
			}
			rbtns[currentSel].setSelected(true);
			InputGrid g = new InputGrid(eqs[currentSel], null, null, null, mode, eqs);

			this.add(g, 1, 9);
		}else if(mode.getEqMode() == 2){
			//polar mode
			ToggleGroup group = new ToggleGroup();
			for(int i = 0; i < 8; i++){
				eqLab[i] = new Label("R" + (i+1));
				eqs[i] = new TextField(Main.equations.get(i));
				rbtns[i] = new RadioButton("Type in R" + (i+1));
				rbtns[i].setOnAction(new BtnHandler());
				rbtns[i].setToggleGroup(group);
				eqs[i].setMinSize(500 * Main.scale, 30 * Main.scale);
				this.add(eqLab[i], 0, i);
				this.add(eqs[i], 1, i);
				this.add(rbtns[i], 2, i);

				eqs[i].setOnAction(new InputHandler());
			}
			rbtns[currentSel].setSelected(true);
			InputGrid g = new InputGrid(eqs[currentSel], null, null, null, mode, eqs);

			this.add(g, 1, 9);
		}else{
			//parametric mode
			ToggleGroup group = new ToggleGroup();
			this.setVgap(5 * Main.scale);
			//need more labels if in parametric mode
			eqLab = new Label[12];
			eqs = new TextField[12];
			rbtns = new RadioButton[12];
			for(int i = 0; i < 12; i++){
				eqLab[i] = new Label("Y" + (i+2)/2);
				eqs[i] = new TextField(Main.equations.get(i));
				rbtns[i] = new RadioButton("Type in Y" + (i+2)/2);
				rbtns[i].setOnAction(new BtnHandler());
				rbtns[i].setToggleGroup(group);
				eqs[i].setMinSize(500 * Main.scale, 30 * Main.scale);
				this.add(eqLab[i], 0, 2*i);
				this.add(eqs[i], 1, 2*i);
				this.add(rbtns[i], 2, 2*i);
				eqs[i].setOnAction(new InputHandler());
				
				i++;
				eqLab[i] = new Label("X" + (i+1)/2);
				eqs[i] = new TextField(Main.equations.get(i));
				rbtns[i] = new RadioButton("Type in X" + (i+1)/2);
				rbtns[i].setOnAction(new BtnHandler());
				rbtns[i].setToggleGroup(group);
				eqs[i].setMinSize(500 * Main.scale, 30 * Main.scale);
				this.add(eqLab[i], 0, 2*(i-1) + 1);
				this.add(eqs[i], 1, 2*(i-1) + 1);
				this.add(rbtns[i], 2, 2*(i-1) + 1);

				eqs[i].setOnAction(new InputHandler());
			}
			rbtns[currentSel].setSelected(true);
			InputGrid g = new InputGrid(eqs[currentSel], null, null, null, mode, eqs);

			this.add(g, 1, 24);
		}
	}

	/**
	 * Handles events from the radio buttons
	 * Should update the equation pane to account for new input grid object
	 * with different text field
	 */
	private class BtnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event) {
			if(mode.getEqMode() == 1){
				String s = ((RadioButton)event.getSource()).getText();
				if(s.indexOf("X") == -1){
					//y
					s = s.replace("Type in Y", "");
					currentSel = 2 * Integer.parseInt(s) - 2;
				}else{
					//x
					s = s.replace("Type in X", "");
					currentSel = 2 * Integer.parseInt(s) - 1;
				}
			}else{
				String s = ((RadioButton)event.getSource()).getText().replace("Type in Y", "");
				s = s.replace("Type in X", "");
				s = s.replace("Type in R", "");
				currentSel = Integer.parseInt(s) - 1;
			}
			update();
		}
	}

	/**
	 *Handles events from the text fields
	 * Should simply update the equation list in the main class
	**/
	private class InputHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event) {
			for(int i = 0; i < eqs.length; i++){
				Main.equations.set(i, eqs[i].getText());
			}
		}
	}

}
