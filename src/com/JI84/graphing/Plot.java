package com.JI84.graphing;

import java.util.ArrayList;

import com.JI84.main.Main;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class Plot {
	private int xlist;
	private int ylist;
	private Paint color;
	private boolean connect;
	public Plot(int xl, int yl){
		this(xl, yl, Color.BLACK);
	}
	public Plot(int xl, int yl, Paint c){
		this(xl, yl, c, false);
	}
	public Plot(int xl, int yl, Paint c, boolean connect){
		this.xlist = xl;
		this.ylist = yl;
		this.color = c;
		this.connect = connect;
	}

	/**
	 * get the list of shapes that should be graphed in order to represent this plot
	 */
	public ArrayList<Shape> graph(Window wdw){
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		Double[] xl = Main.lists[xlist - 1];
		Double[] yl = Main.lists[ylist - 1];
		int length = Math.min(length(xl), length(yl));
		if(connect){
			double x1 = transformX(xl[0], wdw);
			double y1 = transformY(yl[0], wdw);
			Circle c = new Circle(x1, y1, 4);
			c.setFill(color);
			shapes.add(c);
			for(int i = 1; i < length; i++){
					double x2 = transformX(xl[i], wdw);
					double y2 = transformY(yl[i], wdw);
					c = new Circle(x2, y2, 4);
					c.setFill(color);
					shapes.add(c);
					Line l = new Line(x1, y1, x2, y2);
					l.setStroke(color);
					shapes.add(l);
					x1 = x2;
					y1 = y2;
			}
		}else{
			for(int i = 0; i < length; i++){
				double x = transformX(xl[i], wdw);
				double y = transformY(yl[i], wdw);
				Circle c = new Circle(x, y, 4);
				c.setFill(color);
				shapes.add(c);
			}
		}
		return shapes;
	}

	private double transformX(double x, Window wdw){
		double xscl = (700.0 * Main.scale)/(wdw.getXmax()-wdw.getXmin()); 
		double xshft = (50 * Main.scale)-wdw.getXmin()*xscl;
		return x*xscl+xshft;
	}
	private double transformY(double y, Window wdw){
		double yscl = (700.0 * Main.scale)/(wdw.getYmax()-wdw.getYmin()); 
		double yshft = (75 * Main.scale)-wdw.getYmin()*yscl;
		return (800 * Main.scale) - ((y)*yscl+yshft);
	}
	private int length(Double[] ar){
		for(int i = 0; i < ar.length; i++){
			if(ar[i] == null)
				return i;
		}
		return ar.length;
	}

}
