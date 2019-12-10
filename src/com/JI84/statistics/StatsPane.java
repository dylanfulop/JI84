package com.JI84.statistics;

import java.util.ArrayList;

import com.JI84.main.Main;
import com.JI84.math.ExpressionParser;
import com.JI84.math.MathMode;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatsPane extends VBox{
	private ArrayList<Label> labels;
	private ComboBox<String> cb1;
	private ComboBox<String> cb2;
	/**
	 * Create statspane object
	 */
	public StatsPane() {
		labels = new ArrayList<Label>();
		ArrayList<String> selections = new ArrayList<String>();
		for(int i = 1; i <= 8; i++)
			selections.add("L" + i);
		cb1 = new ComboBox<String>();
		cb2 = new ComboBox<String>();
		cb1.getItems().addAll(selections);
		cb2.getItems().addAll(selections);
		cb1.getSelectionModel().selectFirst();
		cb2.getSelectionModel().selectFirst();

		cb1.setOnAction(new Listener());
		cb2.setOnAction(new Listener());

		update();

	}

	/**
	 * Update the text fields with the values from the lists currently selected
	 */
	public void update() {
		this.getChildren().clear();

		this.setPadding(new Insets(20*Main.scale,20*Main.scale,20*Main.scale,20*Main.scale));
		this.setSpacing(20*Main.scale);
		labels.clear();

		if(cb1.getSelectionModel().getSelectedItem().equals(cb2.getSelectionModel().getSelectedItem()))
		{
			fillLabels("", cb1.getSelectionModel().getSelectedItem());
		}else {
			fillLabels("1st ", cb1.getSelectionModel().getSelectedItem());
			fillLabels("2nd ", cb2.getSelectionModel().getSelectedItem());
		}



		this.getChildren().addAll(cb1, cb2);
		for(Label l : labels)
			getChildren().add(l);
	}
	
	private void fillLabels(String prefix, String l) {
		MathMode mode = new MathMode(true, 0, 0, null);
		ExpressionParser exp = new ExpressionParser(mode, 0);
		String avg = prefix + "mean: " + exp.readExp("avg(" + l + ")");
		labels.add(new Label(avg));
		String var = prefix + "variance: " + exp.readExp("variance(" + l + ")");
		labels.add(new Label(var));
		String std = prefix + "std dev: " + exp.readExp("stddev(" + l + ")");
		labels.add(new Label(std));
	}

	private class Listener implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event) {
			update();
		}
	}
}
