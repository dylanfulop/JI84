package com.JI84.math;
import com.JI84.graphing.EqPane;
import com.JI84.graphing.ParametricWindow;
import com.JI84.graphing.WdwPane;
import com.JI84.graphing.Window;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ModePane extends VBox{
	private MathMode mode;
	private Button radians;
	private Button degrees;
	private Button flt;
	private Button[] rounds;
	private Button function;
	private Button parametric;
	private Button polar;
	private WdwPane wdwpane;
	private EqPane eqpane;
	public ModePane(MathMode mode, WdwPane wdwpane, EqPane eqpane){
		this.mode = mode;
		this.wdwpane = wdwpane;
		this.eqpane = eqpane;
		rounds = new Button[9];

		this.setPadding(new Insets(20, 20, 20, 20));
		this.setSpacing(30);

		HBox trigBox = new HBox();
		trigBox.setSpacing(15);
		radians = new Button("Radians");
		degrees = new Button("Degrees");
		radians.setOnAction(new BtnListener());
		degrees.setOnAction(new BtnListener());
		trigBox.getChildren().add(radians);
		trigBox.getChildren().add(degrees);


		HBox rbox = new HBox();
		rbox.setSpacing(10);
		flt = new Button("float");
		flt.setOnAction(new BtnListener());
		rbox.getChildren().add(flt);
		for(int i = 0; i < 9; i++){
			rounds[i] = new Button(i + "");
			rounds[i].setOnAction(new BtnListener());
			rbox.getChildren().add(rounds[i]);
		}

		HBox ftypeBox = new HBox();
		ftypeBox.setSpacing(15);
		function = new Button("Function");
		parametric = new Button("Parametric");
		polar = new Button("Polar");
		function.setOnAction(new BtnListener());
		parametric.setOnAction(new BtnListener());
		polar.setOnAction(new BtnListener());
		ftypeBox.getChildren().add(function);
		ftypeBox.getChildren().add(parametric);
		ftypeBox.getChildren().add(polar);



		this.getChildren().addAll(trigBox, rbox, ftypeBox);

	}

	private class BtnListener implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event) {
			Button esource = (Button)event.getSource();
			if(esource.equals(radians)){
				mode.setrMode(true);
			}else if(esource.equals(degrees)){
				mode.setrMode(false);
			}else if(esource.equals(flt)){
				mode.setRoundMode(-1);
			}else if(esource.equals(function)){
				mode.setEqMode(0);
				mode.setWdw(new Window(mode.getWdw()));
				wdwpane.updatePane();
				eqpane.update();
			}else if(esource.equals(parametric)){
				mode.setEqMode(1);
				mode.setWdw(new ParametricWindow(mode.getWdw(), -5, 5));
				wdwpane.updatePane();
				eqpane.update();
			}else if(esource.equals(polar)){
				mode.setEqMode(2);
				mode.setWdw(new ParametricWindow(mode.getWdw(), 0, 2*Math.PI, "\u0398"));
				wdwpane.updatePane();
				eqpane.update();
			}else{	
				for(int i = 0; i < 9; i++){
					if(esource.equals(rounds[i])){
						mode.setRoundMode(i);
					}
				}
			}
		}

	}

}
