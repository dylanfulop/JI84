package com.JI84.graphing;

public class Window {
	protected double xmin;
	protected double xmax;
	protected double ymin;
	protected double ymax;
	protected double xscale;
	protected double yscale;
	protected double stepSize;
	/**
	 * Creates a window object and stores each instance variable to the parameters
	 * @param xmin
	 * @param xmax
	 * @param xscale
	 * @param ymin
	 * @param ymax
	 * @param yscale
	 * @param stepSize
	 */
	public Window(double xmin, double xmax, double xscale, double ymin, double ymax, double yscale, double stepSize) {
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		this.xscale = xscale;
		this.yscale = yscale;
		this.stepSize = stepSize;
	}
	/**
	 * creates a new window object that has all the properties of the window passed as a parameter
	 * @param wdw
	 */
	public Window(Window wdw) {
		this.xmin = wdw.getXmin();
		this.xmax = wdw.getXmax();
		this.ymin = wdw.getYmin();
		this.ymax = wdw.getYmax();
		this.xscale = wdw.getXscale();
		this.yscale = wdw.getYscale();
		this.stepSize = wdw.getStepSize();
	}
	/**
	 * returns the xscale
	 * scale refers to the distance between tick marks on the graph
	 */
	public double getXscale() {
		return xscale;
	}
	/**
	 * sets the value of the xscale
	 */
	public void setXscale(double xscale) {
		this.xscale = xscale;
	}
	/**
	 * returns the yscale
	 */
	public double getYscale() {
		return yscale;
	}
	/**
	 * sets the value of the yscale
	 */
	public void setYscale(double yscale) {
		this.yscale = yscale;
	}
	/**
	 * returns the xmin
	 */
	public double getXmin() {
		return xmin;
	}
	/**
	 * sets the value of xmin
	 */
	public void setXmin(double xmin) {
		this.xmin = xmin;
	}
	/**
	 * returns the xmax
	 */
	public double getXmax() {
		return xmax;
	}
	/**
	 * sets the value of xmax
	 */
	public void setXmax(double xmax) {
		this.xmax = xmax;
	}
	/**
	 * returns the ymin
	 */
	public double getYmin() {
		return ymin;
	}
	/**
	 * sets the value of ymin
	 */
	public void setYmin(double ymin) {
		this.ymin = ymin;
	}
	/**
	 * returns the ymax
	 */
	public double getYmax() {
		return ymax;
	}
	/**
	 * sets the value of ymax
	 */
	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	/**
	 * returns the step size
	 * step size is used in graphing to represent the change in value of the parameter variable used to approximate the graph
	 */
	public double getStepSize() {
		return stepSize;
	}
	/**
	 * changes the value of the step size
	 * @param stepSize
	 */
	public void setStepSize(double stepSize) {
		this.stepSize = stepSize;
	}
	
	
}
