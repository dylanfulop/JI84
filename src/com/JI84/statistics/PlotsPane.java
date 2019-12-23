package com.JI84.statistics;

import com.JI84.graphing.Plot;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class PlotsPane extends VBox{
	private PlotPane[] plots;
	public PlotsPane(){
		super();
		plots = new PlotPane[5];
		for(int i = 0; i < plots.length; i++){
			plots[i] = new PlotPane(i + 1);
		}
		this.getChildren().addAll(plots);
		this.setPadding(new Insets(10, 10, 20, 20));
		this.setSpacing(20);
	}

	public Plot getPlot(int i){
		return plots[i].getPlot();
	}
}
