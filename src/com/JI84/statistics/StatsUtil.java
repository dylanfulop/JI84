package com.JI84.statistics;

import com.JI84.math.ExpressionParser;

public class StatsUtil {

	public static double[] sort(String[] split, double x, String var, ExpressionParser exp){
		double[] newAr = new double[split.length];
		for(int i = 0; i < split.length; i++){
			double d = exp.readExp(x, var, split[i]);
			newAr[i] = d;
		}
		mergeSort(newAr, 0, newAr.length-1);
		return newAr;
	}

	private static void mergeSort(double[] ar, int left, int right){
		if(left < right){
			int mid = (left + right) / 2;
			mergeSort(ar, left, mid);
			mergeSort(ar, mid+1, right);
			merge(ar, left, mid, right);
		}
	}

	private static void merge(double[] ar, int start, int mid, int end) {
		int l1 = mid-start+1;
		int l2 = end-mid;
		double[] left = new double[l1];
		double[] right = new double[l2];
		for(int i = 0; i < l1; i++)
			left[i] = ar[start+i];
		for(int i = 0; i < l2; i++)
			right[i] = ar[mid+1+i];
		int i = 0;
		int j = 0;
		int k = start;
		while(i < l1 && j < l2){
			if(left[i] < right[j]){
				ar[k] = left[i];
				i++;
			}else{
				ar[k] = right[j];
				j++;
			}
			k++;
		}
		while(i < l1){
			ar[k] = left[i];
			k++;
			i++;
		}
		while(j < l2){
			ar[k] = right[j];
			k++;
			j++;
		}
	}
	

}
