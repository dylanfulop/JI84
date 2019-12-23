package com.JI84.graphing;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.JI84.main.Main;
import com.JI84.math.ExpressionParser;
import com.JI84.math.MathMode;
import com.JI84.statistics.PlotsPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class GraphPane extends BorderPane{
	private MathMode mode;
	private Button graphBtn;
	private Button valueBtn;
	private Button zeroBtn;
	private Button minBtn;
	private Button maxBtn;
	private Button intersectBtn;
	private Button findBtn;
	private Label bottomLabel;
	private Label outputLab;
	private Label outputLab2;
	private TextField valueTF;
	private HBox bottomBar;
	private GridPane topBar;
	private ComboBox<String> selCurve1;
	private ComboBox<String> selCurve2;
	private int boundToSelect; //-1 left 0 neither 1 right
	private Line leftBound;
	private Line rightBound;
	private PlotsPane plotsPane;
	private final String[] colors = {"RED", "BLUE", "YELLOW", "GREEN", "PURPLE", "ORANGE", "AQUA", "BLACK"};

	//total window is 800x800 graph should be 700x700

	/**
	 * Creates a new graph pane by creating axes and tick marks with the current window's settings
	 * and adding buttons to the bottom pane
	 */
	public GraphPane(MathMode mode, PlotsPane pp){
		this.mode = mode;
		this.plotsPane = pp;
		boundToSelect = 0;

		bottomBar = new HBox();
		bottomBar.setSpacing(5 * Main.scale);
		bottomBar.setPadding(new Insets(20 * Main.scale,20 * Main.scale,20 * Main.scale,20 * Main.scale));

		topBar = new GridPane();
		topBar.setHgap(5 * Main.scale);
		topBar.setVgap(5 * Main.scale);
		topBar.setPadding(new Insets(20 * Main.scale,20 * Main.scale,20 * Main.scale,20 * Main.scale));


		graphBtn = new Button("Graph");
		graphBtn.setOnAction(new ButtonListener());

		valueBtn = new Button("Value");
		valueBtn.setOnAction(new ButtonListener());

		zeroBtn = new Button("Zero");
		zeroBtn.setOnAction(new ButtonListener());

		minBtn = new Button("Minimum");
		minBtn.setOnAction(new ButtonListener());

		maxBtn = new Button("Maximum");
		maxBtn.setOnAction(new ButtonListener());

		intersectBtn = new Button("Intersection");
		intersectBtn.setOnAction(new ButtonListener());

		findBtn = new Button("Find");
		findBtn.setOnAction(new ButtonListener());

		valueTF = new TextField("0");
		valueTF.setOnAction(new TextFieldListener());
		bottomLabel = new Label(mode.getVar() + "=");


		bottomBar.getChildren().addAll(graphBtn, intersectBtn, zeroBtn, minBtn, maxBtn, valueBtn);

		outputLab = new Label("");
		outputLab2 = new Label("");

		ArrayList<String> selections = new ArrayList<String>(8);
		for(int i=1;i<=8;i++)
			selections.add("Y" + i);
		selCurve1 = new ComboBox<String>();
		selCurve1.getItems().addAll(selections);
		selCurve2 = new ComboBox<String>();
		selCurve2.getItems().addAll(selections);


		this.setBottom(bottomBar);
		this.setTop(topBar);

		Window wdw = mode.getWdw();

		Line xaxis = transform(wdw.getXmin(), 0, wdw.getXmax(), 0);
		Line yaxis = transform(0, wdw.getYmin(), 0, wdw.getYmax());
		for(double y = wdw.getYmin(); y <= wdw.getYmax(); y+=wdw.getYscale()){
			double xscl = (700.0 * Main.scale)/(wdw.getXmax()-wdw.getXmin()); 
			getChildren().add(transform(-3/xscl, y, 3/xscl, y));
		}
		for(double x = wdw.getXmin(); x <= wdw.getXmax(); x+=wdw.getXscale()){
			double yscl = (700.0 * Main.scale)/(wdw.getYmax()-wdw.getYmin()); 
			getChildren().add(transform(x, -3/yscl, x, 3/yscl));
		}


		this.getChildren().addAll(xaxis, yaxis);
		this.setOnMouseClicked(new MouseListener());

	}

	/**
	 * updates the selCurve comboboxes before they are added in case of mode chagne
	 */
	private void updateSelCurve(){
		int sel1 = selCurve1.getSelectionModel().getSelectedIndex();
		int sel2 = selCurve2.getSelectionModel().getSelectedIndex();

		if(sel1 == -1)
			sel1 = 0;
		if(sel2 == -1)
			sel2 = 1;

		if(mode.getEqMode() == 0){
			ArrayList<String> selections = new ArrayList<String>(8);
			for(int i=1;i<=8;i++)
				selections.add("Y" + i);
			selCurve1 = new ComboBox<String>();
			selCurve1.getItems().addAll(selections);
			selCurve2 = new ComboBox<String>();
			selCurve2.getItems().addAll(selections);

			selCurve1.getSelectionModel().select(sel1);
			selCurve2.getSelectionModel().select(sel2);
		}else if(mode.getEqMode() == 1){
			ArrayList<String> selections = new ArrayList<String>(8);
			for(int i=1;i<=8;i++)
				selections.add("Y" + i + ", X" + i);
			selCurve1 = new ComboBox<String>();
			selCurve1.getItems().addAll(selections);
			selCurve2 = new ComboBox<String>();
			selCurve2.getItems().addAll(selections);

			selCurve1.getSelectionModel().select(sel1);
			selCurve2.getSelectionModel().select(sel2);
		}else if(mode.getEqMode() == 2){
			ArrayList<String> selections = new ArrayList<String>(8);
			for(int i=1;i<=8;i++)
				selections.add("R" + i);
			selCurve1 = new ComboBox<String>();
			selCurve1.getItems().addAll(selections);
			selCurve2 = new ComboBox<String>();
			selCurve2.getItems().addAll(selections);

			selCurve1.getSelectionModel().select(sel1);
			selCurve2.getSelectionModel().select(sel2);
		}
	}


	/**
	 * Graphs equation Yi/Ri/Yi&Xi
	 * To be called for each equation
	 * Based on mode it will act slightly differently
	 * Works by creating many small line segments to try to map out the much bigger shape that needs to be drawn based on the equation
	 */
	private void graph(int i){

		if(mode.getEqMode() == 0){
			//function
			Window wdw = mode.getWdw();
			double x = wdw.getXmin();
			ExpressionParser exparse = new ExpressionParser(mode, 0);
			double y1 = exparse.readExp("Y" + i + "(" + x + ")");
			double y2;
			while(x <= wdw.getXmax() - wdw.getStepSize()){
				x+= wdw.getStepSize();
				y2 = exparse.readExp("Y" + i + "(" + x + ")");
				if(y2 < wdw.getYmax() && y1 < wdw.getYmax() && y2 > wdw.getYmin() && y1 > wdw.getYmin()){
					Line l = transform(x-wdw.getStepSize(),y1,x,y2);
					l.setStroke(Color.valueOf(colors[i-1]));
					this.getChildren().add(l);
				}
				y1 = y2;
			}
		}else if(mode.getEqMode() == 1){
			//parametric
			ParametricWindow wdw = (ParametricWindow)mode.getWdw();
			double t = wdw.getTmin();
			ExpressionParser exparse = new ExpressionParser(mode, 0);
			double x1 = exparse.readExp("X" + i + "(" + t + ")");
			double y1 = exparse.readExp("Y" + i + "(" + t + ")");
			double x2, y2;
			while(t <= wdw.getTmax() - wdw.getStepSize()){
				t+= wdw.getStepSize();
				x2 = exparse.readExp("X" + i + "(" + t + ")");
				y2 = exparse.readExp("Y" + i + "(" + t + ")");
				if(Math.min(x1, x2) > wdw.getXmin() && Math.max(x1, x2) < wdw.getXmax() && Math.min(y1, y2) > wdw.getYmin() && Math.max(y1, y2) < wdw.getYmax()){
					Line l = transform(x1,y1,x2,y2);
					l.setStroke(Color.valueOf(colors[i-1]));
					this.getChildren().add(l);
				}
				x1 = x2;
				y1 = y2;
			}

		}else if(mode.getEqMode() == 2){
			//polar
			ParametricWindow wdw = (ParametricWindow)mode.getWdw();
			double t = wdw.getTmin();
			ExpressionParser exparse = new ExpressionParser(mode, 0);
			double r = exparse.readExp("R" + i + "(" + t + ")");
			double x1 = r*Math.cos(t);
			double y1 = r*Math.sin(t);
			double x2, y2;
			while(t <= wdw.getTmax() - wdw.getStepSize()){
				t+= wdw.getStepSize();
				r = exparse.readExp("R" + i + "(" + t + ")");
				x2 = r*Math.cos(t);
				y2 = r*Math.sin(t);
				if(Math.min(x1, x2) > wdw.getXmin() && Math.max(x1, x2) < wdw.getXmax() && Math.min(y1, y2) > wdw.getYmin() && Math.max(y1, y2) < wdw.getYmax()){
					Line l = transform(x1,y1,x2,y2);
					l.setStroke(Color.valueOf(colors[i-1]));
					this.getChildren().add(l);
				}
				x1 = x2;
				y1 = y2;
			}
		}

	}

	//x xmin --> 50 xmax-->750
	//y --> -y ymax--->25 ymin-->725
	/**
	 * transforms a line in the math/cartesian/window-based x-y coordinate system
	 * into a line that fits into the 700x700 window in the graph pane using the window's
	 * settings to scale and shift the graphs properly
	 * Parameters are the x and y coordinates of the line before the transformation
	 */
	private Line transform(double x1, double y1, double x2, double y2){
		Window wdw = mode.getWdw();
		double xscl = (700.0 * Main.scale)/(wdw.getXmax()-wdw.getXmin()); //scale 1 x-pixel: this many x
		double yscl = (700.0 * Main.scale)/(wdw.getYmax()-wdw.getYmin()); 

		double xshft = (50 * Main.scale)-wdw.getXmin()*xscl;
		double yshft = (75 * Main.scale)-wdw.getYmin()*yscl;

		return new Line(x1*xscl+xshft,(800 * Main.scale) - ((y1)*yscl+yshft),x2*xscl+xshft,(800 * Main.scale) - ((y2)*yscl+yshft));
	}
	/**
	 * takes an x coordinate on the pane and transforms it to the correct x coordinate
	 */
	private double transformX(double x){
		Window wdw = mode.getWdw();
		double xscl = (700.0 * Main.scale)/(wdw.getXmax()-wdw.getXmin());
		double xshft = (50 * Main.scale)-wdw.getXmin()*xscl;
		return (x-xshft)/xscl;
	}


	/**
	 * Handles events from the buttons
	 * Graph button will update the axes and tick marks based on the window's settings, then
	 * graph the equations from the equation pane/equations list of the Main class 
	 */
	private class ButtonListener implements EventHandler<ActionEvent>{

		public void handle(ActionEvent event) {
			String text = ((Button)event.getSource()).getText();
			if(text.equals("Graph")){
				graph();
			}else if(text.equals("Value")){
				value();
			}else if(text.equals("Intersection")){
				updateSelCurve();
				boundToSelect = -1;
				topBar.getChildren().clear();
				topBar.add(selCurve1, 1, 0);
				topBar.add(outputLab, 3, 0);
				topBar.add(selCurve2, 2, 0);
				findBtn.setText("Find Intersection");
				bottomLabel.setText("left: " + ", " + "right:");
				if(bottomBar.getChildren().size() > 6){
					bottomBar.getChildren().set(6, findBtn);
					bottomBar.getChildren().set(7, bottomLabel);
				}else{
					bottomBar.getChildren().add(findBtn);
					bottomBar.getChildren().add(bottomLabel);
				}
			}else if(text.equals("Zero")){
				updateSelCurve();
				boundToSelect = -1;
				topBar.getChildren().clear();
				topBar.add(selCurve1, 1, 0);
				topBar.add(outputLab, 2, 0);
				findBtn.setText("Find Zero");
				bottomLabel.setText("left: " + ", " + "right:");
				if(bottomBar.getChildren().size() > 6){
					bottomBar.getChildren().set(6, findBtn);
					bottomBar.getChildren().set(7, bottomLabel);
				}else{
					bottomBar.getChildren().add(findBtn);
					bottomBar.getChildren().add(bottomLabel);
				}
			}else if(text.equals("Maximum")){
				updateSelCurve();
				boundToSelect = -1;
				topBar.getChildren().clear();
				topBar.add(selCurve1, 1, 0);
				topBar.add(outputLab, 2, 0);
				findBtn.setText("Find Maximum");
				bottomLabel.setText("left: " + ", " + "right:");
				if(bottomBar.getChildren().size() > 6){
					bottomBar.getChildren().set(6, findBtn);
					bottomBar.getChildren().set(7, bottomLabel);
				}else{
					bottomBar.getChildren().add(findBtn);
					bottomBar.getChildren().add(bottomLabel);
				}
			}else if(text.equals("Minimum")){
				updateSelCurve();
				boundToSelect = -1;
				topBar.getChildren().clear();
				topBar.add(selCurve1, 1, 0);
				topBar.add(outputLab, 2, 0);
				findBtn.setText("Find Minimum");
				bottomLabel.setText("left: " + ", " + "right:");
				if(bottomBar.getChildren().size() > 6){
					bottomBar.getChildren().set(6, findBtn);
					bottomBar.getChildren().set(7, bottomLabel);
				}else{
					bottomBar.getChildren().add(findBtn);
					bottomBar.getChildren().add(bottomLabel);
				}
			}else if(event.getSource().equals(findBtn)){
				find(text);
			}

		}
	}
	/**
	 * When the mouse is pressed for selecting points/bounds 
	 */
	private class MouseListener implements EventHandler<MouseEvent>{
		public void handle(MouseEvent event) {
			double x = event.getX();
			DecimalFormat fmt = new DecimalFormat("0.###");
			if(boundToSelect == -1){
				getChildren().remove(leftBound);
				leftBound = new Line(x, 25, x, 725);
				leftBound.setStroke(Color.valueOf(colors[selCurve1.getSelectionModel().getSelectedIndex()]));
				getChildren().add(leftBound);
				boundToSelect = 1;
				bottomLabel.setText("left: " + fmt.format(transformX(leftBound.getStartX())) + ", " + "right:");
			}else if(boundToSelect == 1){
				if(x > leftBound.getStartX()){
					getChildren().remove(rightBound);
					rightBound = new Line(x, 25, x, 725);
					rightBound.setStroke(Color.valueOf(colors[selCurve1.getSelectionModel().getSelectedIndex()]));
					getChildren().add(rightBound);
					boundToSelect = 0;
					bottomLabel.setText("left: " + fmt.format(transformX(leftBound.getStartX())) + ", " + "right:" + fmt.format(transformX(rightBound.getStartX())));
				}
			}
		}
	}
	/**
	 * When the enter button is presse din any text fields
	 */
	private class TextFieldListener implements EventHandler<ActionEvent>{

		public void handle(ActionEvent event) {
			if(event.getSource().equals(valueTF))
				value();
		}

	}

	/**
	 * called when graph button is pushed
	 */
	private void graph(){
		topBar.getChildren().clear();
		getChildren().clear();
		Window wdw = mode.getWdw();
		Line xaxis = transform(wdw.getXmin(), 0, wdw.getXmax(), 0);
		Line yaxis = transform(0, wdw.getYmin(), 0, wdw.getYmax());

		setBottom(bottomBar);
		setTop(topBar);
		getChildren().addAll(xaxis, yaxis);
		for(double y = wdw.getYmin(); y <= wdw.getYmax(); y+=wdw.getYscale()){
			double xscl = (700.0 * Main.scale)/(wdw.getXmax()-wdw.getXmin()); 
			getChildren().add(transform(-3/xscl, y, 3/xscl, y));
		}

		for(double x = wdw.getXmin(); x <= wdw.getXmax(); x+=wdw.getXscale()){
			double yscl = (700.0 * Main.scale)/(wdw.getYmax()-wdw.getYmin()); 
			getChildren().add(transform(x, -3/yscl, x, 3/yscl));
		}
		if(mode.getEqMode() != 1){
			for(int i = 1; i <= 8; i++){
				if(Main.equations.get(i-1) != null && !Main.equations.get(i-1).equals(""))
					graph(i);
			}
		}else{
			for(int i = 1; i <= 7; i++){
				if(Main.equations.get(2*(i-1)) != null && !Main.equations.get(2*(i-1)).equals(""))
					if(Main.equations.get(2*(i-1)+1) != null && !Main.equations.get(2*(i-1)+1).equals(""))
						graph(i);
			}	
		}
		for(int i = 0; i < 5; i++){
			Plot p = plotsPane.getPlot(i);
			if(p != null)
				for(Shape s : p.graph(wdw)){
					this.getChildren().add(s);
				}
		}
	}

	/**
	 * Called when min button is pushed
	 */
	private void min(String exp, double x1, double x2, double xMin, double stepsize, int i, int maxI, boolean forIntersect) {
		ExpressionParser exparse = new ExpressionParser(mode, 0);
		double yMin = exparse.readExp(xMin, "x", exp);
		if(i < maxI) {
			if(i != 0) {
				x1 = Math.max(x1, xMin - stepsize*25);
				x2 = Math.min(x2, xMin + stepsize*25);
			}
			double x = x1;
			while(x < x2) {
				double y = exparse.readExp(x, "x", exp);
				if(y < yMin) {
					yMin = y;
					xMin = x;
				}
				x += stepsize;
			}
			min(exp, x1, x2, xMin, stepsize/2.0, i+1, maxI, forIntersect);
		}else {
			DecimalFormat fmt = new DecimalFormat("0.###");
			double y = exparse.readExp(selCurve1.getSelectionModel().getSelectedItem()+ "(" + xMin + ")");
			outputLab.setText(selCurve1.getSelectionModel().getSelectedItem()+ "(" + fmt.format(xMin) + ")=" + fmt.format(y));
			if(forIntersect) {
				y = exparse.readExp(selCurve1.getSelectionModel().getSelectedItem()+ "(" + xMin + ")");
				outputLab.setText(outputLab.getText() + ", " + selCurve2.getSelectionModel().getSelectedItem()+ "(" + fmt.format(xMin) + ")=" + fmt.format(y));
			}

			getChildren().removeAll(leftBound, rightBound);
			leftBound = null;
			rightBound = null;
		}
	}


	/**
	 * The method called when the find "..." button is pressed to find certain things
	 * @param toFind The string that is on the button
	 */
	private void find(String toFind){
		toFind = toFind.replace("Find ", "");
		ExpressionParser exparse = new ExpressionParser(mode, 0);
		if(toFind.equals("Maximum")){
			if(leftBound != null && rightBound != null){
				double x1 = transformX(leftBound.getStartX());
				double x2 = transformX(rightBound.getStartX());
				min("-1*" + selCurve1.getSelectionModel().getSelectedItem() + "(x)", x1, x2, x1, mode.getWdw().getStepSize(), 0, 7, false);
			}
		}else if(toFind.equals("Minimum")){
			if(leftBound != null && rightBound != null){
				double x1 = transformX(leftBound.getStartX());
				double x2 = transformX(rightBound.getStartX());
				min(selCurve1.getSelectionModel().getSelectedItem() + "(x)", x1, x2, x1, mode.getWdw().getStepSize(), 0, 7, false);
			}
		}else if(toFind.equals("Zero")){
			double x1 = transformX(leftBound.getStartX());
			double x2 = transformX(rightBound.getStartX());
			min("abs(" + selCurve1.getSelectionModel().getSelectedItem() + "(x))", x1, x2, x1, mode.getWdw().getStepSize(), 0, 7, false);
		}else if(toFind.equals("Intersection")){
			double x1 = transformX(leftBound.getStartX());
			double x2 = transformX(rightBound.getStartX());
			min("abs(" + selCurve1.getSelectionModel().getSelectedItem() + "(x) - " + selCurve2.getSelectionModel().getSelectedItem() + "(x))", x1, x2, x1, mode.getWdw().getStepSize(), 0, 7, true);

		}
	}


	/**
	 * Called when value button pushed or enter pressed in value text field
	 */
	private void value(){
		updateSelCurve();
		topBar.getChildren().clear();
		topBar.add(selCurve1, 1, 0);
		topBar.add(outputLab, 2, 0);
		bottomLabel = new Label(mode.getVar() + "=");
		if(bottomBar.getChildren().size() > 6){
			bottomBar.getChildren().set(6, bottomLabel);
			bottomBar.getChildren().set(7, valueTF);
		}else{
			bottomBar.getChildren().add(bottomLabel);
			bottomBar.getChildren().add(valueTF); 
		}
		if(mode.getEqMode() == 1){
			topBar.add(outputLab2, 3, 0);
			ExpressionParser exparse = new ExpressionParser(mode, 0);

			String exp = "Y" + (selCurve1.getSelectionModel().getSelectedIndex() + 1) + "(" + exparse.readExp(valueTF.getText()) + ")";
			outputLab.setText("Y" + (selCurve1.getSelectionModel().getSelectedIndex() + 1) + "(" + valueTF.getText() + ")=" + exparse.readExp(exp));

			exp = "X" + (selCurve1.getSelectionModel().getSelectedIndex() + 1) + "(" + exparse.readExp(valueTF.getText()) + ")";
			outputLab2.setText("X" + (selCurve1.getSelectionModel().getSelectedIndex() + 1) + "(" + valueTF.getText() + ")=" + exparse.readExp(exp));

		}else{
			ExpressionParser exparse = new ExpressionParser(mode, 0);
			String exp = selCurve1.getSelectionModel().getSelectedItem() +  "(" + exparse.readExp(valueTF.getText()) + ")";
			outputLab.setText(selCurve1.getSelectionModel().getSelectedItem()+ "(" + valueTF.getText() + ")=" + exparse.readExp(exp));
		}
	}
}
