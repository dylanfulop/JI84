package com.JI84.math;

public class MathUtil {

	public static double limit(double x, String var, String exp, ExpressionParser ep, int maxLoops){
		int i = 3;
		double step = Math.pow(2, -i);
		double y1 = ep.readExp(x+step, var, exp);
		double y2 = ep.readExp(x-step, var, exp);
		double d1 = Math.abs(y2 - y1);
		double d2 = d1 - step;//must be smaller so that loop runs the first time
		while(d2 <= d1 && i < maxLoops && step > Math.pow(3, -15)){
			step/=2.0;
			d1 = d2;
			y1 = ep.readExp(x+step, var, exp);
			y2 = ep.readExp(x-step, var, exp);
			d2 = Math.abs(y2 - y1);
			i++;
		}
		if(d2 > Math.pow(2, -4))
			return 0.0/0.0;

		return (y1+y2)/2.0;
	}
	
	public static double deriv(double c, String var, String fx, ExpressionParser ep, int maxLoops){
		String fc = ep.readExp(c, var, fx) + "";
		String exp = "(" + fx + " - " + fc + ")/(" + var + " - " + c + ")"; 
		return limit(c, var, exp, ep, maxLoops);
	}
	public static double integ(double x1, double x2, String var, String exp, ExpressionParser ep, int maxLoops){
		String dx = (x2-x1) + "/" + maxLoops;
		String xi = x1 + " + (" + dx + ")*(i - 0.5)";
		String ex = exp.replace(var, "(" + xi + ")");
		
		return series(1, maxLoops, "i", "(" + ex + ")*" + dx, ep);
	}
	
	public static double series(int i, int maxi, String var, String exp, ExpressionParser ep){
		double sum = 0;
		while(i <= maxi){
			sum += ep.readExp(i, var, exp);
			i++;
		}
		return sum;
	}
	public static double product(int i, int maxi, String var, String exp, ExpressionParser ep){
		double product = 1;
		while(i <= maxi){
			product *= ep.readExp(i, var, exp);
			i++;
		}
		return product;
	}
	
	public static double factorial(double d){
		if(d >= 0 && Math.round(d) == d){
			double result = 1;
			for(int i = 2; i <= d; i++)
				result*=i;
			return result;
		}else{
			return 0.0/0.0;
		}
	}

	public static double perm(double n, double r) {
		if(n >= 0 && r >= 0 && Math.round(n) == n && Math.round(r) == r){
			if(n < r)
				return 0;
			double result = 1;
			for(int i = 0; i < r; i++){
				result*=(n-i);
			}
			return result;
		}
		return 0.0/0.0;
	}

	public static double comb(double n, double r) {
		return perm(n, r)/factorial(r);
	}


}
