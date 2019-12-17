package com.JI84.statistics;

import com.JI84.main.Main;
import com.JI84.math.ExpressionParser;
import com.JI84.math.MathMode;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ListPane extends GridPane
{

	private Label[] labels;
	private TextField[][] entries;
	private Double[][] lists;
	private MathMode mode;
	private StatsPane statPane;
	public ListPane(MathMode m, StatsPane sp) {
		mode = m;
		statPane = sp;
		this.lists = Main.lists;

		this.setPadding(new Insets(20 * Main.scale, 20 * Main.scale,20 * Main.scale,20 * Main.scale));
		this.setVgap(5 * Main.scale);
		this.setHgap(5 * Main.scale);

		labels = new Label[8];
		entries = new TextField[8][20];

		for(int i = 0; i < labels.length; i++) {
			labels[i] = new Label("L" + (i+1));
			this.add(labels[i], i, 0);
		}
		for(int i = 0; i < entries.length; i++) {
			entries[i][0] = new TextField();
			entries[i][0].setOnAction(new TFHandler(i, 0));
		}
		update();

	}

	public void update() {
		statPane.update();
		for(int i = 0; i < entries.length; i++) {
			for(int j = 0; j < entries[0].length; j++) {
				if(entries[i][j] !=null) {
					try {
						this.add(entries[i][j], i, j+1);
					}catch(java.lang.IllegalArgumentException e) {
						entries[i][j].setOnAction(new TFHandler(i, j));
					}
				}
			}
		}
	}

	private class TFHandler implements EventHandler<ActionEvent>{
		private int i;
		private int j;
		public TFHandler(int I, int J) {
			i = I;
			j = J;
		}
		public void handle(ActionEvent event) {
			TextField tf = (TextField)event.getSource();
			String s = tf.getText();
			ExpressionParser ep = new ExpressionParser(mode, 0);
			double d = ep.readExp(s);
			if(d != 0.0/0.0) {
				lists[i][j] = d;
				entries[i][j+1] = new TextField();
				entries[i][j+1].setOnAction(new TFHandler(i, j+1));
				update();
			}

		}	
	}

}
