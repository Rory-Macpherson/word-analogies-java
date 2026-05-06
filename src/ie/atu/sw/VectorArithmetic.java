package ie.atu.sw;

import java.util.Iterator;

/*this is the class for the math heads*/
public class VectorArithmetic {

	/* most of these classes are very similar, to the extent i 
	 * should probably narrow them down to one. but alas here*/ 
	public static double[] add(double[] one, double[] two) {
		double[] result = new double[50];
			for (int i = 0; i < result.length; i++) {
				 result[i] = one[i] + two[i];
			}
			return result;
		
	}
	public static double[] minus(double[] one, double[] two) {
		double[] result = new double[50];
		for (int i = 0; i < result.length; i++) {
			 result[i] = one[i] - two[i];
		}
		return result;
	}
	public static double[] multiply(double[] one, double[] two) {
		double[] result = new double[50];
		for (int i = 0; i < result.length; i++) {
			 result[i] = one[i] * two[i];
		}
		return result;
	}
	public static double[] divide(double[] one, double[] two) {
		double[] result = new double[50];
		for (int i = 0; i < result.length; i++) {
			 result[i] = one[i] / two[i];
		}
		return result;
	}	
	
	
	
	public static double dotProduct(double[] one, double[] two) {
		double result = 0;
		for (int i = 0; i < 50; i++) {
			 result += (one[i] - two[i]);
		}
		return result;
	}
	
	public static double euclideanDistance(double[] one, double[] two) {
		double result = 0;
		int temp = 0;
		for (int i = 0; i < 50; i++) {
			result = (one[i] - two[i]);
			temp += (result * result);
		}
		return Math.sqrt(temp);
	}
	
	public static double cosignDistance(double[] one, double[] two) {
		double temp1 = 0;
		double temp2 = 0;
		double result = 0;
		for (int i = 0; i < 50; i++) {
			temp1 += (one[i] * one[i]);
			temp2 += (two[i] * two[i]);
		}
		result = dotProduct(one, two)/(Math.sqrt(temp1 * temp2));
		return result;
		
	}
}
