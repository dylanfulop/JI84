package com.JI84.statistics;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.JI84.graphing.EqPane;
import com.JI84.main.Main;
import com.JI84.math.ExpressionParser;
import com.JI84.math.MathMode;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class StatsPane extends GridPane{
	private ArrayList<Label> labels;
	private ComboBox<String> cb1;
	private ComboBox<String> cb2;
	private ComboBox<String> cby;
	private ComboBox<String> cbr;
	private EqPane eqPane;
	private ListPane lPane;
	private HBox copyYBox;
	private HBox copyRBox;
	private String line;
	/**
	 * Create statspane object
	 */
	public StatsPane(EqPane eqp) {
		eqPane = eqp;
		line = "";
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

		cbr = new ComboBox<String>();
		cbr.getItems().addAll(selections);
		cbr.getSelectionModel().selectFirst();

		cby = new ComboBox<String>();
		for(int i = 1; i <= 8; i++)
			cby.getItems().add("Y" + i);
		cby.getSelectionModel().selectFirst();
		Button copytoy = new Button("Copy Line to");
		copytoy.setOnAction(new Listener());
		Button copytor = new Button("Copy Residuals to");
		copytor.setOnAction(new Listener());
		copyYBox = new HBox();
		copyYBox.getChildren().addAll(copytoy, cby);
		copyRBox = new HBox();
		copyRBox.getChildren().addAll(copytor, cbr);

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
		this.setHgap(10*Main.scale);
		this.setVgap(20*Main.scale);
		labels.clear();

		if(cb1.getSelectionModel().getSelectedItem().equals(cb2.getSelectionModel().getSelectedItem()))
		{
			fillLabels("", cb1.getSelectionModel().getSelectedItem());
		}else {
			fillLabels("1st ", cb1.getSelectionModel().getSelectedItem());
			fillLabels("2nd ", cb2.getSelectionModel().getSelectedItem());
			extraLabels(cb1.getSelectionModel().getSelectedItem(), cb2.getSelectionModel().getSelectedItem());
		}



		add(cb1, 0, 0);
		add(cb2, 1, 0);
		int nLPC = 13; //number of labels per column
		for(int i = 0; i < labels.size(); i++){
			Label l = labels.get(i);
			add(l, i/nLPC, (i%nLPC)+1);
		}
	}

	private void extraLabels(String l1, String l2){
		MathMode mode = new MathMode(true, 0, 0, null);
		ExpressionParser exp = new ExpressionParser(mode, 0);
		double n = exp.readExp("numEntries(" + l1 + ")");
		if(n ==  exp.readExp("numEntries(" + l2 + ")")){
			double stdx =  exp.readExp("stddev(" + l1 + ")");
			double stdy =  exp.readExp("stddev(" + l2 + ")");
			double meanx = exp.readExp("avg(" + l1 + ")");
			double meany = exp.readExp("avg(" + l2 + ")");
			double r = 0;
			for(int i = 0; i < n; i++){
				int fl = Integer.parseInt(l1.replace("L", ""));
				int sl = Integer.parseInt(l2.replace("L", ""));
				double x = Main.lists[fl-1][i];
				double y = Main.lists[sl-1][i];
				double z1 = (x - meanx)/stdx;
				double z2 = (y - meany)/stdy;
				r += z1*z2;
			}
			r /= (n-1);
			labels.add(new Label("r: " + r));
			labels.add(new Label("r^2: " + Math.pow(r, 2)));
			double m = r*stdy/stdx;
			double b = meany - m*meanx;
			DecimalFormat fmt = new DecimalFormat("0.#####");
			labels.add(new Label(l2 + " ~= " + fmt.format(m) + "*" + l1 + " + " + fmt.format(b)));
			line = m + "*x + " + b;
			add(copyYBox, 2, 4);
			add(copyRBox, 2, 5);
		}
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
		String mo = prefix + "(smallest) mode: " + exp.readExp("mode(" + l + ")");
		labels.add(new Label(mo));
		String n = prefix + "n: " + exp.readExp("numEntries(" + l + ")");
		labels.add(new Label(n));
		String min = prefix + "min: " + exp.readExp("min(" + l + ")");
		labels.add(new Label(min));
		double Q1 = exp.readExp("FQ(" + l + ")");
		String q1 = prefix + "Q1: " + Q1;
		labels.add(new Label(q1));
		String md = prefix + "median: " + exp.readExp("med(" + l + ")");
		labels.add(new Label(md));
		double Q3 =  exp.readExp("TQ(" + l + ")");
		String q3 = prefix + "Q3: " + Q3;
		labels.add(new Label(q3));
		String max = prefix + "max: " + exp.readExp("max(" + l + ")");
		labels.add(new Label(max));
		String iqr = prefix + "IQR: " + (Q3-Q1);
		labels.add(new Label(iqr));
		String s = prefix + "sum: " + exp.readExp("sum(" + l + ")");
		labels.add(new Label(s));
		String pr = prefix + "product: " + exp.readExp("prod(" + l + ")");
		labels.add(new Label(pr));

	}

	private class Listener implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event) {
			update();
			if(event.getSource() instanceof Button){
				if(((Button)event.getSource()).getText().equals("Copy Line to")){
					int eqI = Integer.parseInt(cby.getSelectionModel().getSelectedItem().replace("Y", "")) - 1;
					Main.equations.set(eqI, line);
					eqPane.update();
				}
				if(((Button)event.getSource()).getText().equals("Copy Residuals to")){
					int lI = Integer.parseInt(cbr.getSelectionModel().getSelectedItem().replace("L", "")) - 1;
					int xlI = Integer.parseInt(cb1.getSelectionModel().getSelectedItem().replace("L", "")) - 1;
					int ylI = Integer.parseInt(cb2.getSelectionModel().getSelectedItem().replace("L", "")) - 1;
					int length = Math.min(length(Main.lists[xlI]), length(Main.lists[ylI]));
					MathMode mode = new MathMode(true, 0, 0, null);
					ExpressionParser expParse = new ExpressionParser(mode, 0);
					for(int i = 0; i < length; i++){
						double residual = expParse.readExp(Main.lists[xlI][i], "x", line);
						residual = Main.lists[ylI][i] - residual;
						Main.lists[lI][i] = residual;
					}
					lPane.update();
				}
			}
		}
		private int length(Double[] ar){
			for(int i = 0; i < ar.length; i++){
				if(ar[i] == null)
					return i;
			}
			return ar.length;
		}
	}

	public void setLP(ListPane listPane) {
		this.lPane = listPane;
	}

}
