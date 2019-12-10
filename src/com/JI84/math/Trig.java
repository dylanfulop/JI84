package com.JI84.math;

public class Trig {
	private boolean rMode;

	public Trig(boolean rMode){
		this.rMode = rMode;
	}
	
	public double degree(double x){
		if(rMode){
			return x * Math.PI/180.0;
		}
		return x;
	}
	public double radian(double x){
		if(rMode){
			return x;
		}
		return x * 180.0/Math.PI;
	}

	public double sin(double x){
		if(rMode){
			return Math.sin(x);
		}else{
			return Math.sin(x * (Math.PI/180.0));
		}
	}
	public double cos(double x){
		if(rMode){
			return Math.cos(x);
		}else{
			return Math.cos(x * (Math.PI/180.0));
		}
	}
	public double tan(double x){
		if(rMode){
			return Math.tan(x);
		}else{
			return Math.tan(x * (Math.PI/180.0));
		}
	}
	public double asin(double x){
		if(rMode){
			return Math.asin(x);
		}else{
			return Math.asin(x) * (180.0/Math.PI);
		}
	}
	public double acos(double x){
		if(rMode){
			return Math.acos(x);
		}else{
			return Math.acos(x) * (180.0/Math.PI);
		}
	}
	public double atan(double x){
		if(rMode){
			return Math.atan(x);
		}else{
			return Math.atan(x) * (180.0/Math.PI);
		}
	}


}
