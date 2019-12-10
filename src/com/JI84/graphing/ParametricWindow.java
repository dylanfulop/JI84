package com.JI84.graphing;

public class ParametricWindow extends Window{
	private double tmin;
	private double tmax;
	private String parameter;
	/**
	 * Creates a ParametricWindow by setting instance variables to passed values, and the parameter variable to t
	 * @param xmin
	 * @param xmax
	 * @param xscale
	 * @param ymin
	 * @param ymax
	 * @param yscale
	 * @param tmin
	 * @param tmax
	 * @param stepSize
	 */
	public ParametricWindow(double xmin, double xmax, double xscale, double ymin, double ymax, double yscale, double tmin, double tmax,
			double stepSize) {
		super(xmin, xmax, xscale, ymin, ymax, yscale, stepSize);
		this.tmin = tmin;
		this.tmax = tmax;
		this.parameter = "t";

	}
	/**
	 * Creates a ParametricWindow by setting instance variables to passed values, and the parameter variable to t, 
	 * and the remaining instance variables to the values of the window passed
	 * @param wdw
	 * @param tmin
	 * @param tmax
	 */
	public ParametricWindow(Window wdw, double tmin, double tmax) {
		super(wdw.getXmin(), wdw.getXmax(), wdw.getXscale(), wdw.getYmin(), wdw.getYmax(), wdw.getYscale(), wdw.getStepSize());
		this.tmin = tmin;
		this.tmax = tmax;
		this.parameter = "t";
	}
	/**
	 * Creates a ParametricWindow  by setting instance variables to passed values,  and the remaining 
	 * instance variables to the values of the window passed
	 * @param wdw
	 * @param tmin
	 * @param tmax
	 * @param parameter
	 */
	public ParametricWindow(Window wdw, int tmin, double tmax, String parameter) {
		super(wdw.getXmin(), wdw.getXmax(), wdw.getXscale(), wdw.getYmin(), wdw.getYmax(), wdw.getYscale(), wdw.getStepSize());
		this.tmin = tmin;
		this.tmax = tmax;
		this.parameter = parameter;
	}
	/**
	 * Returns the minimum value of the parameter
	 */
	public double getTmin() {
		return tmin;
	}
	/**
	 * Sets the minimum value of the parameter to tmin
	 */
	public void setTmin(double tmin) {
		this.tmin = tmin;
	}
	/**
	 * Returns the maximum value of the parameter
	 */
	public double getTmax() {
		return tmax;
	}
	/**
	 * Sets the maximum value of the parameter to tmax
	 */
	public void setTmax(double tmax) {
		this.tmax = tmax;
	}
	/**
	 * Sets the parameter variable (t or theta)
	 */
	public void setParamater(String t){
		parameter = t;
	}
	/**
	 * Returns the parameter variable (t or theta)
	 */
	public String getParameter(){
		return parameter;
	}
}
