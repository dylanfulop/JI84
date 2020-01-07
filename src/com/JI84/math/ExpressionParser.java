package com.JI84.math;

import java.util.Random;

import com.JI84.main.Main;
import com.JI84.statistics.StatsUtil;

public class ExpressionParser {

	private double ans;
	private Trig trig;
	private MathMode mode;

	/**
	 * Creates an expression parser object
	 * @param mode The mode settings that are to be used by this expression parser object
	 * @param ans The previous answer to replace 'Ans' in the string to parse
	 */
	public ExpressionParser(MathMode mode, double ans){
		this.mode = mode;
		this.ans = ans;
	}

	/**
	 * Read an expression in a general case, Where the variables valuable is set to 0 and the variable is the current mode's default variable
	 * @param exp The expression to be read
	 * @return The double value that the expression evaluates to
	 */
	public double readExp(String exp){
		return readExp(0, mode.getVar(), exp);
	}

	/**
	 * Read and evaluate a mathematical expression
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The double value that the expression evaluates to
	 */
	public double readExp(double x, String var, String exp){
		trig = new Trig(mode.isrMode());
		exp = reduce(x, var, exp);
		try{
			return Double.parseDouble(exp);
		}catch (NumberFormatException exc){

		}
		String[] sepAdd = exp.split("\\+");
		double result = 0;
		if(sepAdd.length > 1){
			for(String s : sepAdd)
				result += readExp(x, var,s);
			return result;
		}else{
			String[] sepSubtract = exp.split(" - ");
			if(sepSubtract.length > 1){
				result = readExp(x, var,sepSubtract[0]);
				for(int i = 1; i < sepSubtract.length; i++){
					result -= readExp(x, var,sepSubtract[i]);
				}
				return result;
			}else{
				String[] sepMultiply = exp.split("\\*");
				if(sepMultiply.length > 1){
					result = 1;
					for(String s : sepMultiply)
						result *= readExp(x, var,s);
					return result;
				}else{
					String[] sepDivide = exp.split("/");
					if(sepDivide.length > 1){
						result = readExp(x, var,sepDivide[0]);
						for(int i = 1; i < sepDivide.length; i++){
							result /= readExp(x, var,sepDivide[i]);
						}
						return result;
					}else{
						String[] sepExp = exp.split("\\^");
						if(sepExp.length > 1){
							result = readExp(x, var,sepExp[0]);
							for(int i = 1; i < sepExp.length; i++){
								result = Math.pow(result, readExp(x, var,sepExp[i]));
							}
							return result;
						}else{
							String[] sepRt = exp.split("\\|");
							if(sepRt.length > 1){
								result = readExp(x, var,sepRt[0].replace("\\", ""));
								for(int i = 1; i < sepRt.length; i++){
									result = Math.pow(readExp(x,var,sepRt[i].replace("\\", "")), 1.0/result);
								}
								return result;
							}else{
								if(exp.contains("_d_")){
									String[] sepdeg = exp.split("_d_");
									return trig.degree(readExp(x, var, sepdeg[0]));
								}else{
									if(exp.contains("_r_")){
										String[] seprad = exp.split("_r_");
										return trig.radian(readExp(x, var, seprad[0]));
									}
								}
							}
						}
					}
				}
			}
		}


		return 0.0/0.0;
	}

	/**
	 * Takes a string and reduces it, returning the reduced string
	 * The string is reduced by taking shortcuts for roots and replacing them with the more complicated/readable text, 
	 * also handling implicit multiplication and parenthesis/brackets/absolute value
	 * and also, replacing the variable with its value and pi/e with their value and Ans with its value
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return a simplified expression to be read
	 */
	private String reduce(double x, String var, String exp){
		exp = implicitMult(x, var, exp);
		exp = insertLists(x, var, exp);
		exp = exp.replace("summation", "sigma");//allows using either for a summation
		exp = exp.replace("product", "PI");//allows using either terminoligy for a product
		exp = exp.replace("sqrt", "2\\|"); //change sqrt to square root funciton that works
		exp = exp.replace("rt", "\\|"); //change nrt to the proper function
		exp = parameterFuncs(x, var, exp); //solve functions with multiple parameters so parentheses dont get eliminated
		exp = solveAbs(x, var, exp); //solve things inside |()| as absolute value before solving parentheses so it doesnt confuse
		exp = solveParentheses(x, var, exp, '[', ']'); //solve brackets before anything else
		exp = solveParentheses(x, var, exp, '(', ')'); //solve parentheses nex
		exp = exp.replace("\u03C0", "" + Math.PI);//unicode for pi replace with value of pi
		exp = exp.replace("\u00B0", "_d_"); //split function does not read unicode characters correctly, fix this by replacing degree symbol
		exp = exp.replace("pi", "" + Math.PI);//replace 'pi' with value of pi
		exp = exp.replace("e", "" + Math.E);//replace 'e' with euler's number
		exp = exp.replace(var, "" + x); //replace variable with variable value (for function/graphing)
		exp = exp.replace("Ans", ans + ""); //replace 'Ans' with the previous answer
		return exp;
	}

	private String insertLists(double x, String var, String exp) {
		for(int i = 1; i < 8; i++) {
			if(exp.indexOf("L" + i) != -1) {
				Double[] d = Main.lists[i-1];
				String l = "";
				l+= d[0];
				for(int j = 1; j < d.length; j++) {
					if(d[j] != null)
						l += ", " + d[j];
				}
				exp = exp.replace("L" + i, l);
			}
		}
		return exp;
	}

	/**Allows for certain instances of implicit multiplication**/
	private String implicitMult(double x, String var, String exp) {
		exp = exp.replace(")(", ")*(");//turn (...)(...) into multiplication
		for(int i = 0; i <= 9; i++){

			exp = exp.replace("Y" + i + "(", "placeholder1");//make sure it doesnt turn Yn(x) into a multiplication
			exp = exp.replace("X" + i + "(", "placeholder2");//make sure it doesnt turn Xn(x) into a multiplication
			exp = exp.replace("R" + i + "(", "placeholder3");//make sure it doesnt turn Rn(x) into a multiplication
			exp = exp.replace(i+"(", i+"*("); //turn n(...) into multiplication
			exp = exp.replace("placeholder1", "Y" + i + "(");
			exp = exp.replace("placeholder2", "X" + i + "(");
			exp = exp.replace("placeholder3", "R" + i + "(");

			exp = exp.replace(i+var, i+"*"+var);//turn nx into multiplication

			exp = exp.replace("-(", "-1*(");//read -(...) correctly, even if the inside is also negative
			exp = exp.replace("-" + var, "-1*" + var);//if y1 = -x and x is -1, no more error

			//for these functions, should be able to put a multiplier in front (front multiply = fm)
			String[] fmFuncs = {"sin","cos","tan","arcsin","arctan","arccos","log","ln","csc","cot","sec","abs","floor","ceil","sqrt","pi","\u03C0","e"};
			for(String s : fmFuncs)
				exp = exp.replace(i+s, i+"*"+s);


		}
		return exp;
	}

	/**
	 * Solve all the values in an expression inside parentheses
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return the same string, but everything inside parentheses is evaluated and replaced by what it evaluates to
	 */
	private String solveParentheses(double x, String var, String exp, char open, char close){
		String s = "" + exp;
		int closeIndex = exp.indexOf(close);
		int openIndex = -1;
		if(closeIndex != -1){
			while(s.indexOf(open) != -1){
				closeIndex = s.indexOf(close);
				for(int i = closeIndex; i >= 0; i--){
					if(s.charAt(i) == open){
						openIndex = i;
						String r = "";
						for(int j = 0; j < openIndex; j++){
							r+=s.charAt(j);
						}
						r += readExp(x, var, s.substring(openIndex+1, closeIndex));
						for(int j = closeIndex + 1; j < s.length(); j++){
							r+=s.charAt(j);
						}
						s = r;
						break;
					}
				}
			}
			return s;
		}else{
			return exp;
		}
	}
	/**
	 * Solve all the values in an expression inside absolute value symbols and parentheses and evaluates ex. |(-3)| = 3
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return the same string, but everything inside parentheses with absolute value signs is evaluated and replaced by the absolute value of what it evaluates to
	 */
	private String solveAbs(double x, String var, String exp){
		String s = "" + exp;
		int closeIndex = exp.indexOf(")|");
		int openIndex = -1;
		if(closeIndex != -1){
			while(s.indexOf("|(") != -1){
				closeIndex = s.indexOf(")|");
				for(int i = closeIndex-1; i >= 0; i--){
					if(s.substring(i, i+2).equals("|(")){
						openIndex = i;
						String r = "";
						for(int j = 0; j < openIndex; j++){
							r+=s.charAt(j);
						}
						r += Math.abs(readExp(x, var, s.substring(openIndex+2, closeIndex)));
						for(int j = closeIndex + 2; j < s.length(); j++){
							r+=s.charAt(j);
						}
						s = r;
						break;
					}
				}
			}
			return s;
		}else{
			return exp;
		}
	}
	/**
	 * Solve functions in an expression with parameters like func(x) or func(x, y) or even func(x1, x2, ..., xn)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String parameterFuncs(double x, String var, String exp) {
		//many/unlimited parameter functions
		exp = max(x, var, exp);
		exp = min(x, var, exp);
		exp = sum(x, var, exp);
		exp = stats(x, var, exp);
		exp = product(x, var, exp);

		//limited parameter functions
		exp = logb(x, var, exp);
		exp = mod(x, var, exp);
		exp = random(x, var, exp);
		exp = series(x, var, exp);
		exp = calc(x, var, exp);

		//one parameter functions
		exp = functions(x, var, exp);
		exp = trig(x, var, exp);
		exp = log(x, var, exp);
		exp = abs(x, var, exp);
		exp = round(x, var, exp);

		return exp;
	}

	/**
	 * Called by the parameter funcs method, solves some methods related to rounding such as ceiling floor and round functions
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String round(double x, String var, String exp){
		int fi = exp.indexOf("floor");
		while(fi != -1){
			int closeIndex = findCloseIndex("floor", exp, fi);
			String s = exp.substring(fi + 6, closeIndex);
			exp = exp.replace(exp.substring(fi, closeIndex+1), ""+Math.floor(readExp(x, var, s)));
			fi = exp.indexOf("floor");
		}
		int ci = exp.indexOf("ceil");
		while(ci != -1){
			int closeIndex = findCloseIndex("ceil", exp, ci);
			String s = exp.substring(ci + 5, closeIndex);
			exp = exp.replace(exp.substring(ci, closeIndex+1), ""+Math.ceil(readExp(x, var, s)));
			ci = exp.indexOf("ceil");
		}
		int ri = exp.indexOf("round");
		while(ri != -1){
			int closeIndex = findCloseIndex("round", exp, ri);
			String s = exp.substring(ri + 6, closeIndex);
			double d = readExp(x, var, s);
			double r = Math.ceil(d);
			if(d - Math.floor(d) < r - d)
				r = Math.floor(d);
			exp = exp.replace(exp.substring(ri, closeIndex+1), ""+r);
			ri = exp.indexOf("round");
		}

		return exp;

	}

	/**
	 * Called by the parameter funcs method, solves one parameter log functions the base 10 logarithm log(x) and the natural logarithm ln(x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String log(double x, String var, String exp){
		int lI = exp.indexOf("log");
		while(lI != -1){
			int closeIndex = findCloseIndex("log", exp, lI);
			String s = exp.substring(lI + 4, closeIndex);
			exp = exp.replace(exp.substring(lI, closeIndex+1), ""+Math.log10(readExp(x, var, s)));
			lI = exp.indexOf("log");
		}
		lI = exp.indexOf("ln");
		while(lI != -1){
			int closeIndex = findCloseIndex("ln", exp, lI);
			String s = exp.substring(lI + 3, closeIndex);
			exp = exp.replace(exp.substring(lI, closeIndex+1), ""+Math.log(readExp(x, var, s)));
			lI = exp.indexOf("ln");
		}

		return exp;

	}
	/**
	 * Called by the parameter funcs method, solves abs(x) as the absolute value of x
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String abs(double x, String var, String exp){
		int aI = exp.indexOf("abs");
		while(aI != -1){
			int closeIndex = findCloseIndex("abs", exp, aI);
			String s = exp.substring(aI + 4, closeIndex);
			exp = exp.replace(exp.substring(aI, closeIndex+1), ""+Math.abs(readExp(x, var, s)));
			aI = exp.indexOf("abs");
		}
		return exp;
	}
	/**
	 * Called by the parameter funcs method, solves several trig functions split up into several methods
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String trig(double x, String var, String exp){
		exp = asin(x, var, exp);
		exp = acos(x, var, exp);
		exp = atan(x, var, exp);
		exp = sin(x, var, exp);
		exp = cos(x, var, exp);
		exp = tan(x, var, exp);
		exp = sec(x, var, exp);
		exp = csc(x, var, exp);
		exp = cot(x, var, exp);
		return exp;
	}
	/**
	 * Called by the trig method, solves the arcsin(x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String asin(double x, String var, String exp){
		int sI = exp.indexOf("arcsin");
		while(sI != -1){
			int closeIndex = findCloseIndex("arcsin", exp, sI);
			String s = exp.substring(sI + 7, closeIndex);
			exp = exp.replace(exp.substring(sI, closeIndex+1), ""+trig.asin(readExp(x, var, s)));
			sI = exp.indexOf("arcsin");
		}
		return exp;
	}
	/**
	 * Called by the trig method, solves the arccos(x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String acos(double x, String var, String exp){
		int cI = exp.indexOf("arccos");
		while(cI != -1){
			int closeIndex = findCloseIndex("arccos", exp, cI);
			String s = exp.substring(cI + 7, closeIndex);
			exp = exp.replace(exp.substring(cI, closeIndex+1), ""+trig.acos(readExp(x, var, s)));
			cI = exp.indexOf("arccos");
		}
		return exp;
	}
	/**
	 * Called by the trig method, solves the arctan(x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String atan(double x, String var, String exp){
		int tI = exp.indexOf("arctan");
		while(tI != -1){
			int closeIndex = findCloseIndex("arctan", exp, tI);
			String s = exp.substring(tI + 7, closeIndex);
			exp = exp.replace(exp.substring(tI, closeIndex+1), ""+trig.atan(readExp(x, var, s)));
			tI = exp.indexOf("arctan");
		}
		return exp;
	}
	/**
	 * Called by the trig method, solves the sin(x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String sin(double x, String var, String exp){
		int sI = exp.indexOf("sin");
		while(sI != -1){
			int closeIndex = findCloseIndex("sin", exp, sI);
			String s = exp.substring(sI + 4, closeIndex);
			exp = exp.replace(exp.substring(sI, closeIndex+1), ""+trig.sin(readExp(x, var, s)));
			sI = exp.indexOf("sin");
		}
		return exp;
	}
	/**
	 * Called by the trig method, solves the csc(x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String csc(double x, String var, String exp){
		int sI = exp.indexOf("csc");
		while(sI != -1){
			int closeIndex = findCloseIndex("csc", exp, sI);
			String s = exp.substring(sI + 4, closeIndex);
			exp = exp.replace(exp.substring(sI, closeIndex+1), ""+1/trig.sin(readExp(x, var, s)));
			sI = exp.indexOf("csc");
		}
		return exp;
	}
	/**
	 * Called by the trig method, solves the cos(x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String cos(double x, String var, String exp){
		int cI = exp.indexOf("cos");
		while(cI != -1){
			int closeIndex = findCloseIndex("cos", exp, cI);
			String s = exp.substring(cI + 4, closeIndex);
			exp = exp.replace(exp.substring(cI, closeIndex+1), ""+trig.cos(readExp(x, var, s)));
			cI = exp.indexOf("cos");
		}
		return exp;
	}
	/**
	 * Called by the trig method, solves the sec(x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String sec(double x, String var, String exp){
		int cI = exp.indexOf("sec");
		while(cI != -1){
			int closeIndex = findCloseIndex("sec", exp, cI);
			String s = exp.substring(cI + 4, closeIndex);
			exp = exp.replace(exp.substring(cI, closeIndex+1), ""+1/trig.cos(readExp(x, var, s)));
			cI = exp.indexOf("sec");
		}
		return exp;
	}
	/**
	 * Called by the trig method, solves the tan(x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String tan(double x, String var, String exp){
		int tI = exp.indexOf("tan");
		while(tI != -1){
			int closeIndex = findCloseIndex("tan", exp, tI);
			String s = exp.substring(tI + 4, closeIndex);
			exp = exp.replace(exp.substring(tI, closeIndex+1), ""+trig.tan(readExp(x, var, s)));
			tI = exp.indexOf("tan");
		}
		return exp;
	}
	/**
	 * Called by the trig method, solves the cot(x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String cot(double x, String var, String exp){
		int tI = exp.indexOf("cot");
		while(tI != -1){
			int closeIndex = findCloseIndex("cot", exp, tI);
			String s = exp.substring(tI + 4, closeIndex);
			exp = exp.replace(exp.substring(tI, closeIndex+1), ""+1/trig.tan(readExp(x, var, s)));
			tI = exp.indexOf("cot");
		}
		return exp;
	}
	/**
	 * A method to simplify the functions that require finding the clsing index of paretnhesis following them
	 * @param key The function to be used
	 * @param exp The expression to be read
	 * @param openIndex The index where the parenthesis opens
	 * @return The index of the matching closing parentheses
	 */
	private int findCloseIndex(String key, String exp, int openIndex){
		int openCount = 0;
		int closeIndex = 0;
		for(int i = openIndex+key.length(); i < exp.length(); i++){
			if(exp.charAt(i) == '('){
				openCount++;
			}
			if(exp.charAt(i) == ')'){
				openCount--;
			}
			if(openCount == 0){
				closeIndex = i;
				break;
			}
		}
		return closeIndex;
	}
	/**
	 * Called by the parameter funcs method, solves functions Yi(x) or Xi(x) or Ri(x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String functions(double x, String var, String exp){
		int yI = exp.indexOf("Y");
		while(yI != -1){
			int closeIndex = findCloseIndex("Yi", exp, yI);
			String s1 = exp.substring(yI+1, yI+2);
			String s2 = exp.substring(yI + 3, closeIndex);
			int yn = Integer.parseInt(s1);
			if(mode.getEqMode() == 0)
				exp = exp.replace(exp.substring(yI, closeIndex+1), "" + readExp(readExp(x, var, s2), "x", Main.equations.get(yn-1)));
			else
				exp = exp.replace(exp.substring(yI, closeIndex+1), "" + readExp(readExp(x, var, s2), "t", Main.equations.get(2*(yn-1))));
			yI = exp.indexOf("Y");
		}
		if(mode.getEqMode() == 1){
			int xI = exp.indexOf("X");

			while(xI != -1){
				int closeIndex = findCloseIndex("Xi", exp, xI);

				String s1 = exp.substring(xI+1, xI+2);
				String s2 = exp.substring(xI + 3, closeIndex);
				int xn = Integer.parseInt(s1);
				exp = exp.replace(exp.substring(xI, closeIndex+1), "" + readExp(readExp(x, var, s2), "t", Main.equations.get(2*(xn-1) + 1)));
				xI = exp.indexOf("X");
			}
		}
		if(mode.getEqMode() == 2){
			int rI = exp.indexOf("R");

			while(rI != -1){
				int closeIndex = findCloseIndex("Ri", exp, rI);

				String s1 = exp.substring(rI+1, rI+2);
				String s2 = exp.substring(rI + 3, closeIndex);
				int rn = Integer.parseInt(s1);
				exp = exp.replace(exp.substring(rI, closeIndex+1), "" + readExp(readExp(x, var, s2), "\u0398", Main.equations.get(rn-1)));
				rI = exp.indexOf("R");
			}
		}
		return exp;
	}

	/**
	 * A method to simplify the functions that require finding the clsing index of paretnhesis following them that have multiple parameters
	 * @param key The function to be used
	 * @param exp The expression to be read
	 * @param openIndex The index where the parenthesis opens
	 * @return The index of the matching closing parentheses and the expression with commas from the same function modified to not be confused
	 * inside an array of size 2
	 */
	private Object[] findCloseIndexMultiParam(String key, String exp, int openIndex){
		int openCount = 0;
		int closeIndex = 0;
		for(int i = openIndex+key.length(); i < exp.length(); i++){
			if(exp.charAt(i) == '('){
				openCount++;
			}
			if(exp.charAt(i) == ')'){
				openCount--;
			}
			if(openCount == 0){
				closeIndex = i;
				break;
			}
			//check to see if commas are extra/for different multi-param functions, then replace with unique set of characters so can be split accurately
			if(exp.charAt(i) == ','){
				if(openCount == 1){
					exp = exp.substring(0, i) + "_,_" + exp.substring(i + 1);
					i++;
				}
			}
		}
		return new Object[]{closeIndex, exp};
	}
	/**
	 * Called by the parameter funcs method, solves the largest number in a list of entries max(a, b, c, d, e, f, ...)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String max(double x, String var, String exp){
		int maxI = exp.indexOf("max");
		while(maxI != -1){
			Object[] fcimp = findCloseIndexMultiParam("max", exp, maxI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(maxI+4, closeIndex).split("_,_");
			double max = readExp(x, var, split[0]);
			for(String s : split){
				max = Math.max(readExp(x, var, s), max);
			}
			exp = exp.replace(exp.substring(maxI, closeIndex+1), "" + max);
			maxI = exp.indexOf("max");
		}
		return exp;
	}
	/**
	 * Called by the parameter funcs method, solves the smallest number in a list of entries min(a, b, c, d, e, f, ...)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String min(double x, String var, String exp){
		int minI = exp.indexOf("min");
		while(minI != -1){
			Object[] fcimp = findCloseIndexMultiParam("min", exp, minI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(minI+4, closeIndex).split("_,_");
			double min = readExp(x, var, split[0]);
			for(String s : split){
				min = Math.min(readExp(x, var, s), min);
			}
			exp = exp.replace(exp.substring(minI, closeIndex+1), "" + min);
			minI = exp.indexOf("min");
		}
		return exp;
	}
	/**
	 * Called by the parameter funcs method, solves the sum of a list of entries sum(a, b, c, d, e, f, ...)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String sum(double x, String var, String exp){
		int sumI = exp.indexOf("sum");
		while(sumI != -1){
			Object[] fcimp = findCloseIndexMultiParam("sum", exp, sumI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(sumI+4, closeIndex).split("_,_");
			double sum = 0;
			for(String s : split){
				sum += readExp(x, var, s);
			}
			exp = exp.replace(exp.substring(sumI, closeIndex+1), "" + sum);
			sumI = exp.indexOf("sum");
		}
		return exp;
	}
	/**
	 * Called by the parameter funcs method, solves the average of a list of entries avg(a, b, c, d, e, f, ...)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String stats(double x, String var, String exp){
		int avgI = exp.indexOf("avg");
		while(avgI != -1){
			Object[] fcimp = findCloseIndexMultiParam("avg", exp, avgI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(avgI+4, closeIndex).split("_,_");
			double avg = 0;
			int n = 0;
			for(String s : split){
				avg += readExp(x, var, s);
				n++;
			}
			avg/=n;
			exp = exp.replace(exp.substring(avgI, closeIndex+1), "" + avg);
			avgI = exp.indexOf("avg");
		}
		int varI = exp.indexOf("variance");
		while(varI != -1){
			Object[] fcimp = findCloseIndexMultiParam("variance", exp, varI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(varI+9, closeIndex).split("_,_");
			double variance = 0;
			double avg = 0;
			int n = 0;
			for(String s : split){
				avg += readExp(x, var, s);
				n++;
			}
			avg/=n;
			for(String s : split){
				double dist = readExp(x, var, s) - avg;
				variance += dist*dist;
			}
			variance/=(n-1);
			exp = exp.replace(exp.substring(varI, closeIndex+1), "" + variance);
			varI = exp.indexOf("variance");
		}
		int stdI = exp.indexOf("stddev");
		while(stdI != -1){
			Object[] fcimp = findCloseIndexMultiParam("stddev", exp, stdI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(stdI+7, closeIndex).split("_,_");
			double variance = 0;
			double avg = 0;
			int n = 0;
			for(String s : split){
				avg += readExp(x, var, s);
				n++;
			}
			avg/=n;
			for(String s : split){
				double dist = readExp(x, var, s) - avg;
				variance += dist*dist;
			}
			variance/=(n-1);
			exp = exp.replace(exp.substring(stdI, closeIndex+1), "" + Math.sqrt(variance));
			stdI = exp.indexOf("stddev");
		}
		int nI = exp.indexOf("numEntries");
		while(nI != -1){
			Object[] fcimp = findCloseIndexMultiParam("numEntries", exp, nI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(nI+11, closeIndex).split("_,_");
			int count = 0;
			if(split[0].equals("") || split[0].equals("null")){
				count--;
			}
			for(String s : split){
				count++;
			}
			exp = exp.replace(exp.substring(nI, closeIndex+1), "" + count);
			nI = exp.indexOf("numEntries");
		}
		exp = sortStats(x, var, exp);
		return exp;
	}
	public String sortStats(double x, String var, String exp){
		int FQI = exp.indexOf("FQ");
		while(FQI != -1){
			Object[] fcimp = findCloseIndexMultiParam("FQ", exp, FQI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(FQI+3, closeIndex).split("_,_");
			double fq = 0;
			double[] ar = StatsUtil.sort(split, x, var, this);
			int len = ar.length/2;
			if(len == 0)
				fq = ar[0];
			else if(len % 2 == 0){
				fq = ar[len/2];
				fq += ar[len/2 - 1];
				fq /= 2;
			}else{
				fq = ar[len/2];
			}
			exp = exp.replace(exp.substring(FQI, closeIndex+1), "" + fq);
			FQI = exp.indexOf("FQ");
		}
		int TQI = exp.indexOf("TQ");
		while(TQI != -1){
			Object[] fcimp = findCloseIndexMultiParam("TQ", exp, TQI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(TQI+3, closeIndex).split("_,_");
			double tq = 0;
			double[] ar = StatsUtil.sort(split, x, var, this);
			int mid = ar.length/2;
			if(ar.length % 2 != 0)
				mid+=1;
			int len = ar.length - mid;
			if(ar.length == 1)
				tq = ar[0];
			else if(len % 2 == 0){
				tq = ar[mid+len/2];
				tq += ar[mid+len/2 - 1];
				tq /= 2;
			}else{
				tq = ar[mid+len/2];
			}
			
			exp = exp.replace(exp.substring(TQI, closeIndex+1), "" + tq);
			TQI = exp.indexOf("TQ");
		}
		int medI = exp.indexOf("med");
		while(medI != -1){
			Object[] fcimp = findCloseIndexMultiParam("med", exp, medI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(medI+4, closeIndex).split("_,_");
			double median = 0;
			double[] ar = StatsUtil.sort(split, x, var, this);
			if(ar.length % 2 == 0){
				median = ar[ar.length/2];
				median += ar[ar.length/2 - 1];
				median /= 2;
			}else{
				median = ar[ar.length/2];
			}
			exp = exp.replace(exp.substring(medI, closeIndex+1), "" + median);
			medI = exp.indexOf("med");
		}
		int modeI = exp.indexOf("mode");
		while(modeI != -1){
			Object[] fcimp = findCloseIndexMultiParam("mode", exp, modeI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(modeI+5, closeIndex).split("_,_");
			double[] ar = StatsUtil.sort(split, x, var, this);
			double mode = ar[0];
			int modeS = 1;
			int curS = 1;
			for(int i = 1; i < ar.length; i++){
				if(ar[i] == ar[i-1])
					curS++;
				else
					curS = 1;
				if(ar[i] == mode){
					modeS++;
				}else{
					if(curS > modeS){
						mode = ar[i];
						modeS = curS;
					}
				}
			}
			exp = exp.replace(exp.substring(modeI, closeIndex+1), "" + mode);
			modeI = exp.indexOf("mode");
		}
		return exp;
	}
	
	/**
	 * Called by the parameter funcs method, solves the product of a list of entries product(a, b, c, d, e, f, ...)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String product(double x, String var, String exp){
		int productI = exp.indexOf("prod");
		while(productI != -1){
			Object[] fcimp = findCloseIndexMultiParam("prod", exp, productI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(productI+5, closeIndex).split("_,_");
			double product = 1;
			for(String s : split){
				product *= readExp(x, var, s);
			}
			exp = exp.replace(exp.substring(productI, closeIndex+1), "" + product);
			productI = exp.indexOf("prod");
		}
		return exp;
	}
	/**
	 * Called by the parameter funcs method, solves the base n logarithm of x logb(n, x)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String logb(double x, String var, String exp){
		int logbI = exp.indexOf("logb");
		while(logbI != -1){
			Object[] fcimp = findCloseIndexMultiParam("logb", exp, logbI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(logbI+5, closeIndex).split("_,_");
			if(split.length == 2){
				double log = Math.log(readExp(x, var, split[1]))/Math.log(readExp(x, var, split[0]));
				exp = exp.replace(exp.substring(logbI, closeIndex+1), "" + log);
				logbI = exp.indexOf("logb");
			}else{
				//error
				exp = exp.replace(exp.substring(logbI, closeIndex+1), "NaN");
			}
		}
		return exp;
	}
	
	/**
	 * Called by the parameter funcs method, solves series and products in sigma and PI notation
	 * called as sigma(var, initial var value, max var value, expression in terms of var)
	 * or sigma(initial i value, max i value, expression in terms of i) or PI(same arguments)
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String series(double x, String var, String exp){
		int sumI = exp.indexOf("sigma");
		while(sumI != -1){
			Object[] fcimp = findCloseIndexMultiParam("sigma", exp, sumI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(sumI+6, closeIndex).split("_,_");
			if(split.length == 4){
				//4 params
				String v = split[0];
				int i = (int) Math.round(readExp(x, var, split[1]));
				int maxi = (int) Math.round(readExp(x, var, split[2]));
				String ex = split[3];
				double result = MathUtil.series(i, maxi, v, ex, this);
				System.out.println(3);
				exp = exp.replace(exp.substring(sumI, closeIndex+1), "" + result);
				sumI = exp.indexOf("sigma");
			}else if(split.length == 3){
				//3 params, variable chosen to be i by default
				int i = (int) Math.round(readExp(x, var, split[0]));
				int maxi = (int) Math.round(readExp(x, var, split[1]));
				String ex = split[2];
				double result = MathUtil.series(i, maxi, "i", ex, this);
				System.out.println(3);
				exp = exp.replace(exp.substring(sumI, closeIndex+1), "" + result);
				sumI = exp.indexOf("sigma");
			}else{
				//error
				exp = exp.replace(exp.substring(sumI, closeIndex+1), "NaN");
				sumI = exp.indexOf("sigma");
			}
		}
		int prodI = exp.indexOf("PI");
		while(prodI != -1){
			Object[] fcimp = findCloseIndexMultiParam("PI", exp, prodI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(prodI+3, closeIndex).split("_,_");
			if(split.length == 4){
				//4 params
				String v = split[0];
				System.out.println(split[0] + " " + split[1] + " " + split[2] + " " + split[3]);
				int i = (int) Math.round(readExp(x, var, split[1]));
				int maxi = (int) Math.round(readExp(x, var, split[2]));
				String ex = split[3];
				double result = MathUtil.product(i, maxi, v, ex, this);
				exp = exp.replace(exp.substring(prodI, closeIndex+1), "" + result);
				prodI = exp.indexOf("PI");
			}else if(split.length == 3){
				//3 params, variable chosen to be i by default
				int i = (int) Math.round(readExp(x, var, split[0]));
				int maxi = (int) Math.round(readExp(x, var, split[1]));
				String ex = split[2];
				double result = MathUtil.product(i, maxi, "i", ex, this);
				exp = exp.replace(exp.substring(prodI, closeIndex+1), "" + result);
				prodI = exp.indexOf("PI");
			}else{
				//error
				exp = exp.replace(exp.substring(prodI, closeIndex+1), "NaN");
				prodI = exp.indexOf("PI");
			}
		}
		return exp;
	}
	/**
	 * Called by the parameter funcs method, solves derivatives and integrals
	 * passed in form of deriv(var, expression in terms of var, value of var to evaluate at, (optional) maxloops/accuracy)
	 * max loops/accuracy affects the time it takes to find an approximation and the accuracy of the approximation, default is 1000
	 * 1000 is higher than needed for most functions
	 * also solves integrals as fnInt(a, b, expression in terms of var, var, (optional) maxloops/accuracy) a and b are initial and final var values
	 * maxloops default is 500 for intergals
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String calc(double x, String var, String exp){
		int integI = exp.indexOf("fnInt");
		while(integI != -1){
			Object[] fcimp = findCloseIndexMultiParam("fnInt", exp, integI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(integI+6, closeIndex).split("_,_");
			if(split.length == 5){
				//5 params
				double a = readExp(x, var, split[0]);
				double b = readExp(x, var, split[1]);
				String ex = split[2];
				String v = split[3].replace(" ", "");
				int loops = (int)Math.round(readExp(x, var, split[4]));
	
				System.out.println(a + " " + b + " " + ex + " " + v + " " + loops);
				
				double result = MathUtil.integ(a, b, v, ex, this, loops);
				exp = exp.replace(exp.substring(integI, closeIndex+1), "" + result);
				integI = exp.indexOf("fnInt");
			} else if(split.length == 4){
				//4 params use default maxloops of 500
				double a = readExp(x, var, split[0]);
				double b = readExp(x, var, split[1]);
				String ex = split[2];
				String v = split[3].replace(" ", "");	
	
				double result = MathUtil.integ(a, b, v, ex, this, 500);
				exp = exp.replace(exp.substring(integI, closeIndex+1), "" + result);
				integI = exp.indexOf("fnInt");
			}else{
				//error
				exp = exp.replace(exp.substring(integI, closeIndex+1), "NaN");
				integI = exp.indexOf("fnInt");
			}
		}
		int ddI = exp.indexOf("deriv");
		while(ddI != -1){
			Object[] fcimp = findCloseIndexMultiParam("deriv", exp, ddI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(ddI+6, closeIndex).split("_,_");
			if(split.length == 4){
				//4 params
				String v = split[0];
				String ex = split[1];
				double c = readExp(x, var, split[2]);
				int loops = (int)Math.round(readExp(x, var, split[3]));
				System.out.println(v + " " + ex + " " + c);
				double result = MathUtil.deriv(c, v, ex, this, loops);
				exp = exp.replace(exp.substring(ddI, closeIndex+1), "" + result);
				ddI = exp.indexOf("deriv");
			} else if(split.length == 3){
				//3 params, use default maxloops/accuracy of 1000
				String v = split[0];
				String ex = split[1];
				double c = readExp(x, var, split[2]);
				System.out.println(v + " " + ex + " " + c);
				double result = MathUtil.deriv(c, v, ex, this, 1000);
				exp = exp.replace(exp.substring(ddI, closeIndex+1), "" + result);
				ddI = exp.indexOf("deriv");
			}else{
				//error
				exp = exp.replace(exp.substring(ddI, closeIndex+1), "NaN");
				ddI = exp.indexOf("deriv");
			}
		}
		
		return exp;
	}
	/**
	 * Called by the parameter funcs method, solves the modulus of 2 numbers mod(a, b) = a % b
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String mod(double x, String var, String exp){
		int modI = exp.indexOf("mod");
		while(modI != -1){
			Object[] fcimp = findCloseIndexMultiParam("mod", exp, modI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(modI+4, closeIndex).split("_,_");
			if(split.length == 2){
				double mod = readExp(x, var, split[0]) % readExp(x, var, split[1]);
				exp = exp.replace(exp.substring(modI, closeIndex+1), "" + mod);
				modI = exp.indexOf("mod");
			}else{
				//error
				exp = exp.replace(exp.substring(modI, closeIndex+1), "NaN");
			}
		}
		return exp;
	}
	/**
	 * Called by the parameter funcs method, evaluates randint(lowerbound, upperbound) and rand(lowerbound, upperbound) by generating
	 * random integers or real numbers between lowerbound and upperbound
	 * @param x The value of the variable that should be used
	 * @param var The text representing the variable that should be used
	 * @param exp The expression to be read
	 * @return The expression with the functions solved and replaced
	 */
	private String random(double x, String var, String exp){
		int randI = exp.indexOf("randint");
		while(randI != -1){
			Object[] fcimp = findCloseIndexMultiParam("randint", exp, randI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(randI+8, closeIndex).split("_,_");
			if(split.length == 2){
				double up = readExp(x, var, split[1]);
				double down = readExp(x, var, split[0]);
				Random r = new Random();
				int rand = r.nextInt((int)(up - down) + 1) + (int)down;
				exp = exp.replace(exp.substring(randI, closeIndex+1), "" + rand);
				randI = exp.indexOf("randint");
			}else{
				//error
				exp = exp.replace(exp.substring(randI, closeIndex+1), "NaN");
			}
		}
		randI = exp.indexOf("rand");
		while(randI != -1){
			Object[] fcimp = findCloseIndexMultiParam("rand", exp, randI);
			int closeIndex = (int)fcimp[0];
			exp = (String)fcimp[1];
			String[] split = exp.substring(randI+5, closeIndex).split("_,_");
			if(split.length == 2){
				double up = readExp(x, var, split[1]);
				double down = readExp(x, var, split[0]);
				double rand = (Math.random() * (up-down)) + down;
				exp = exp.replace(exp.substring(randI, closeIndex+1), "" + rand);
				randI = exp.indexOf("rand");
			}else{
				//error
				exp = exp.replace(exp.substring(randI, closeIndex+1), "NaN");
			}
		}
		return exp;
	}


}
