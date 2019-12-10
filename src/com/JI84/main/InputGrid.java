package com.JI84.main;
import java.util.ArrayList;

import com.JI84.math.ExpressionParser;
import com.JI84.math.MathMode;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class InputGrid extends GridPane{
	Button[][] inputButtons = new Button[6][8];
	private MathMode mode;
	private final String[][] inputButtonNames = 
		{
				{"x, t, \u0398", "sum", "product", "min", "max", "avg", "del", "clear"},
				{"^-1", "^2",   "sin",  "cos",  "tan",  "log",  "ln",  "^"},
				{"n\\|",   "sqrt(",     "7",    "8",    "9",    "/",     "+",   "\u03C0"},
				{"abs",   "",      "4",    "5",    "6",    "*",     "-",   "e"},
				{"(",    ")",      "1",    "2",    "3",    "\u00B0",     "ceil",   "floor"},
				{"round", ",",      "0",    ".",    "(-)",  "Ans",     "mod",   "enter"}};
	private final String[][] buttonCommandText = 
		{ 
				{"",   "sum(",     "product(",    "min(",       "max(",     "avg(",    "",     ""},
				{"^-1", "^2",   "sin(",  "cos(",  "tan(",  "log(",  "ln(",  "^"},
				{"\\|(",   "sqrt(",     "7",    "8",    "9",    "/",     " + ",  "\u03C0"},
				{"abs(",   "",      "4",    "5",    "6",    "*",     " - ",   "e"},
				{"(",    ")",      "1",    "2",    "3",    "\u00B0",     "ceil(",   "floor("},
				{"round(", ",",      "0",    ".",    "-",  "Ans",  "mod(",   ""}};;		

				private TextField inputTF;
				private ArrayList<String> input,output;
				private DisplayBox display;
				private TextField[] eqs;
				/**
				 * Constructor to create an input grid pane to go at the bottom of several panes
				 * @param inputTextF The textfield that the input grid will paste text into
				 * @param inlist The list representing various input strings which are added to the list as they are entered in the text field
				 * @param outlist The lsit representing the solution to every previous output after the equation in the tedxt field is parsed
				 * @param display The display box that the input and output strings will be added to/displayed in, to call update method 
				 * @param mode The mode/settings that should be used
				 * @param eqs The text fields of the equation pane if the input grid is being made in a equation pane
				 */
				public InputGrid(TextField inputTextF, ArrayList<String> inlist, ArrayList<String> outlist, DisplayBox display, MathMode mode, TextField[] eqs){
					super();
					this.mode = mode;
					this.display = display;
					this.input = inlist;
					this.output = outlist;
					this.inputTF = inputTextF;
					this.eqs = eqs;
					this.setAlignment(Pos.CENTER);
					this.setHgap(10 * Main.scale);
					this.setVgap(10 * Main.scale);
					this.setPadding(new Insets(12 * Main.scale, 20 * Main.scale, 12 * Main.scale, 20 * Main.scale));
					for(int i = 0; i < inputButtons.length; i++){
						for(int j = 0; j < inputButtons[i].length; j++){
							inputButtons[i][j] = new Button(inputButtonNames[i][j]);
							inputButtons[i][j].setMinSize(60 * Main.scale, 30 * Main.scale);
							inputButtons[i][j].setMaxSize(60 * Main.scale, 30 * Main.scale);
							inputButtons[i][j].setOnAction(new ButtonHandler(i, j));
							this.add(inputButtons[i][j], j, i);
						}
					}

				}
				/** The method that is called whenever the enter button is pressed or enter is pressed while 
				 *  the text field is selected
				 */
				public void enter(){
					input.add(inputTF.getText());
					try{
						ExpressionParser ep = new ExpressionParser(mode, Double.parseDouble(output.get(output.size() - 1)));
						output.add(mode.format(ep.readExp(inputTF.getText())));
					}catch(Exception exc){
						ExpressionParser ep = new ExpressionParser(mode, 0);
						output.add(mode.format(ep.readExp(inputTF.getText())));
					}
					display.update();
					inputTF.setText("");
				}

				/**
				 * The handler for all of the buttons in the 
				 * input grid
				 */
				private class ButtonHandler implements EventHandler<ActionEvent>{
					private int i;
					private int j;
					private ButtonHandler(int i, int j){
						this.i=i;
						this.j=j;
					}
					public void handle(ActionEvent event) {
						if(inputButtonNames[i][j].equals("enter")){
							//when the enter button is pressed
							if(input != null){
								//as long as there is an input list, it is on the main pane where the enter method can be called normally
								enter();
							}else{
								for(int i = 0; i < eqs.length; i++){
									//when enter is called in the equations pane, each equation needs to be updated based on what is currently in the textbox
									Main.equations.set(i, eqs[i].getText());
								}
							}
						}else if(inputButtonNames[i][j].equals("clear")){
							//when the clear button is pressed
							inputTF.setText(""); // clear the inputtf completely
							if(input != null){
								//if in the main pane clear the display then update it
								input.clear();
								output.clear();
								display.update();
							}
						}else if(inputButtonNames[i][j].equals("del")){
							//when the delete button is pressed
							inputTF.setText(inputTF.getText().substring(0, inputTF.getText().length() - 1));//delete the last character in the input textfield
						}else if(inputButtonNames[i][j].equals("x, t, \u0398")){
							//if the variable button is pressed
							inputTF.setText(inputTF.getText() + mode.getVar());//paste the variable for the current mode
						}else{
							//if any other button is pressed
							inputTF.setText(inputTF.getText() + buttonCommandText[i][j]); //paste the text that corresponds to each button based on the buttonCommandText array
						}
					}

				}
}
