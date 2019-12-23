package com.JI84.statistics;

import com.JI84.graphing.Plot;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class PlotPane extends GridPane{
	private Label name;
	private ComboBox<String> xl;
	private ComboBox<String> yl;
	private ComboBox<String> color;
	private CheckBox connect;
	private CheckBox on;

	private static String[] colorList = {"Black", "Red", "Blue", "Green", "Yellow",  "Orange", "Purple",  "Magenta", "Cyan", "Pink"};

	public PlotPane(int i){
		super();
		this.setPadding(new Insets(10, 10, 20, 20));
		this.setVgap(20);
		this.setHgap(5);

		name = new Label("Plot " + i);
		this.add(name, 0, 0);

		String[] ls = new String[8];
		for(int j = 1; j <= 8; j++)
			ls[j-1] = "L" + j;

		Label x = new Label("x list: ");
		Label y = new Label("y list: ");
		xl = new ComboBox<String>();
		yl = new ComboBox<String>();
		xl.getItems().addAll(ls);
		yl.getItems().addAll(ls);
		xl.getSelectionModel().select(i - 1);
		yl.getSelectionModel().select(i);
		this.add(x, 0, 1);
		this.add(xl, 1, 1);
		this.add(y, 3, 1);
		this.add(yl, 4, 1);

		color = new ComboBox<String>();
		color.getItems().addAll(colorList);
		color.getSelectionModel().select(i-1);
		Label colors = new Label("Point Color:");
		this.add(colors, 6, 1);
		this.add(color, 7, 1);

		connect = new CheckBox();
		Label cl = new Label("Connect Dots:");
		this.add(cl, 9, 1);
		this.add(connect, 10, 1);

		on = new CheckBox();
		Label ol = new Label("Enable Plot:");
		this.add(ol, 12, 1);
		this.add(on, 13, 1);
	}

	public Plot getPlot(){
		if(on.isSelected()){
			int x = Integer.parseInt(xl.getSelectionModel().getSelectedItem().replace("L", ""));
			int y = Integer.parseInt(yl.getSelectionModel().getSelectedItem().replace("L", ""));
			Plot p = new Plot(x, y, Color.valueOf(color.getSelectionModel().getSelectedItem()), connect.isSelected());
			return p;
		}else{
			return null;
		}
	}
}
