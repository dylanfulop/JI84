package com.JI84.math;
import java.text.DecimalFormat;

import com.JI84.graphing.Window;

public class MathMode {
	private boolean rMode; //radian mode
	private int roundMode;//-1 = float (fewest decimal places), 0-8 = 0-8 decimals
	private int eqMode; //0 = function 1 = parametric 2 = polar
	private Window wdw;
	/**
	 * @return If the current setting is radian mode (true) or degree mode (false)
	 */
	public boolean isrMode() {
		return rMode;
	}
	/**
	 * Set whether or not to use radian mode
	 */
	public void setrMode(boolean rMode) {
		this.rMode = rMode;
	}
	/**
	 * @return The rounding mode -1=float (fewest decimal places), 0-8 = 0-8 decimals no matter what
	 */
	public int getRoundMode() {
		return roundMode;
	}
	/**
	 * Set the rounding mode -1=float (fewest decimal places), 0-8 = 0-8 decimals no matter what
	 */
	public void setRoundMode(int roundMode) {
		this.roundMode = roundMode;
	}
	/**Return the graphing mode
	 * 0-function
	 * 1-parametric
	 * 2-polar
	 **/
	public int getEqMode() {
		return eqMode;
	}
	/**
	 * Set the graphing mode
	 * 0-function
	 * 1-parametric
	 * 2-polar
	 **/
	public void setEqMode(int eqMode) {
		this.eqMode = eqMode;
	}
	/**
	 * @return The window object  associated with this mode representing the window settings
	 */
	public Window getWdw() {
		return wdw;
	}
	/**
	 * Replace the window settings object with a new one
	 */
	public void setWdw(Window wdw) {
		this.wdw = wdw;
	}
	/**
	 * Constructor for creating a new mathmode object
	 * @param rMode Radian mode (true) or degree mode (false)
	 * @param roundMode -1=float (fewest decimal places), 0-8 = 0-8 decimals no matter what
	 * @param eqMode 0 = function 1 = parametric 2 = polar
	 * @param wdw A window object containing the window settings
	 */
	public MathMode(boolean rMode, int roundMode, int eqMode, Window wdw) {
		this.rMode = rMode;
		this.roundMode = roundMode;
		this.eqMode = eqMode;
		this.wdw = wdw;
	}
	/**
	 * @param The double to be formatted
	 * @return The formatted double based on the rounding settings
	 */
	public String format(double d){
		DecimalFormat fmt = new DecimalFormat(getFormatString());
		return fmt.format(d);
	}
	/**
	 * @return The format string associated with the current round mode
	 */
	private String getFormatString(){
		if(roundMode == -1)
			return "0.################################";
		String s = "0";
		if(roundMode == 0)
			return s;
		s+=".";
		for(int i = 0; i < roundMode; i++)
			s+="0";
		return s;
	}
	/**
	 * @return The variable string associated with the current equation mode
	 */
	public String getVar() {
		if(eqMode == 0)
			return "x";
		if(eqMode == 1)
			return "t";
		if(eqMode == 2)
			return "\u0398";
		return "var";
	}

}
