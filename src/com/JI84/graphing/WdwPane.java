package com.JI84.graphing;

import com.JI84.math.MathMode;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class WdwPane extends HBox{
	private Label xMin;
	private Label xMax;
	private Label xScale;
	private Label yMin;
	private Label yMax;
	private Label yScale;
	private Label stepSize;
	private Label tmin;
	private Label tmax;
	private TextField[] values;
	private Window wdw;
	private MathMode mode;
	
	/**
	 * creates a pane with textifelds and labels to represent different settings of a window
	 * located inside the mode, and uses the mode as well to check if it needs extra labels/textfields for
	 * parameter min and max
	 */
	public WdwPane(MathMode mode){
		this.mode = mode;
		this.wdw = mode.getWdw();
		if(wdw instanceof ParametricWindow)
			values = new TextField[9];
		else
			values = new TextField[7];
		
		VBox labels = new VBox();
		VBox texts = new VBox();
		xMin = new Label("x min");
		xMax = new Label("x max");
		xScale = new Label("x scale");
		yMin = new Label("y min");
		yMax = new Label("y max");
		yScale = new Label("y scale");
		stepSize = new Label("step size");
		if(wdw instanceof ParametricWindow){
			ParametricWindow pwdw = (ParametricWindow) wdw;
			tmin = new Label(pwdw.getParameter() + " min");
			tmax = new Label(pwdw.getParameter() + " max");
		}

		
		labels.setSpacing(30);
		labels.setPadding(new Insets(20, 20, 20, 20));
		
		for(int i = 0; i < values.length; i++){
			String s = getText(i);
			values[i] = new TextField(s);
			values[i].setOnAction(new TextFieldHandler());
			texts.getChildren().add(values[i]);
		}
		texts.setSpacing(21);
		texts.setPadding(new Insets(20, 20, 20, 20));
		
		labels.getChildren().addAll(xMin, xMax, xScale, yMin, yMax, yScale, stepSize);
		if(wdw instanceof ParametricWindow)
			labels.getChildren().addAll(tmin, tmax);
		
		this.getChildren().addAll(labels, texts);
		
		this.setSpacing(10);
		this.setPadding(new Insets(20, 20, 20, 20));

	}

	/**
	 * get the text that should fill the textfield values[i]
	 */
	private String getText(int i) {
		switch(i)
		{
			case 0:
				return wdw.getXmin() + "";
			case 1:
				return wdw.getXmax() + "";
			case 2:
				return wdw.getXscale() + "";
			case 3:
				return wdw.getYmin() + "";
			case 4:
				return wdw.getYmax() + "";
			case 5:
				return wdw.getYscale() + "";
			case 6:
				return wdw.getStepSize() + "";
			case 7:
				return ((ParametricWindow)wdw).getTmin() + "";
			case 8:
				return ((ParametricWindow)wdw).getTmax() + "";
		}
		return null;
	}
	
	/**
	 * update setting corresponding to textfield values[i]
	 */
	private void updateSetting(int i){
		switch(i)
		{
			case 0:
				 wdw.setXmin(Double.parseDouble(values[i].getText()));
				 break;
			case 1:
				 wdw.setXmax(Double.parseDouble(values[i].getText()));
				 break;
			case 2:
				 wdw.setXscale(Double.parseDouble(values[i].getText()));
				 break;
			case 3:
				 wdw.setYmin(Double.parseDouble(values[i].getText()));
				 break;
			case 4:
				 wdw.setYmax(Double.parseDouble(values[i].getText()));
				 break;
			case 5:
				 wdw.setYscale(Double.parseDouble(values[i].getText()));
				 break;
			case 6:
				 wdw.setStepSize(Double.parseDouble(values[i].getText()));
				 break;
			case 7:
				((ParametricWindow)wdw).setTmin(Double.parseDouble(values[i].getText()));
				break;
			case 8:
				((ParametricWindow)wdw).setTmax(Double.parseDouble(values[i].getText()));
				break;
		}
	}
	/**
	 * update all window settings based on values in each textfield
	 */
	private void updateSettings(){
		for(int i = 0; i < values.length; i++){
			updateSetting(i);
		}
	}
	/**
	 * Handles events from textfields
	 * Whenever enter is pressed on a textfield in the window pane, all settings in the window should be updated
	 */
	private class TextFieldHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event) {
			updateSettings();
		}		
	}
	/**
	 * Update the actual pane itself including updating the settings and recreating the pane from scratch,
	 * called when mode is switched so as to add/remove/rename extra labels for t/theta min and max
	 */
	public void updatePane(){
		updateSettings();
		this.getChildren().clear();
		this.wdw = mode.getWdw();
		if(wdw instanceof ParametricWindow)
			values = new TextField[9];
		else
			values = new TextField[7];
		
		VBox labels = new VBox();
		VBox texts = new VBox();
		xMin = new Label("x min");
		xMax = new Label("x max");
		xScale = new Label("x scale");
		yMin = new Label("y min");
		yMax = new Label("y max");
		yScale = new Label("y scale");
		stepSize = new Label("step size");
		if(wdw instanceof ParametricWindow){
			ParametricWindow pwdw = (ParametricWindow) wdw;
			tmin = new Label(pwdw.getParameter() + " min");
			tmax = new Label(pwdw.getParameter() + " max");
		}

		
		labels.setSpacing(30);
		labels.setPadding(new Insets(20, 20, 20, 20));
		
		for(int i = 0; i < values.length; i++){
			String s = getText(i);
			values[i] = new TextField(s);
			values[i].setOnAction(new TextFieldHandler());
			texts.getChildren().add(values[i]);
		}
		texts.setSpacing(21);
		texts.setPadding(new Insets(20, 20, 20, 20));
		
		labels.getChildren().addAll(xMin, xMax, xScale, yMin, yMax, yScale, stepSize);
		if(wdw instanceof ParametricWindow)
			labels.getChildren().addAll(tmin, tmax);
		
		this.getChildren().addAll(labels, texts);
		
		this.setSpacing(10);
		this.setPadding(new Insets(20, 20, 20, 20));
	}

}
