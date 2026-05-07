package ie.atu.sw;



/*this is the class for the math heads*/
public class VectorArithmetic {

	/* most of these classes are very similar, to the extent i 
	 * should probably narrow them down to one. but alas here
	 * 
	 * ok i refactored the repetitive code to only have 
	 * one method that can do it all*/ 
	// O(n) - single loop over vector length (n=50), one arithmetic op per element
	public static double[] operate(double[] one, double[] two, int op) {
	    double[] result = new double[50];
	    for (int i = 0; i < result.length; i++) {
	        result[i] = switch(op) {
	            case 1 -> one[i] + two[i];
	            case 2 -> one[i] - two[i];
	            case 3 -> one[i] * two[i];
	            case 4 -> one[i] / two[i];
			default -> throw new IllegalArgumentException("Unexpected value: " + op);
	        };
	    }
	    return result;
	}	
	
	
	// O(n) - single loop over vector length (n=50), accumulates one multiply-add per element
	//ok this one i got from the brief, i can explain what it does but not why
	public static double dotProduct(double[] one, double[] two) {
		double result = 0;
		for (int i = 0; i < 50; i++) {
			 result += (one[i] * two[i]);
		}
		return result;
	}
	// O(n) - single loop over vector length (n=50), computes squared differences then one sqrt
	//ok this one i got from the brief, i can explain what it does but not why
	public static double euclideanDistance(double[] one, double[] two) {
		double result = 0;
		int temp = 0;
		for (int i = 0; i < 50; i++) {
			result = (one[i] - two[i]);
			temp += (result * result);
		}
		//square root not squirt!!!
		return Math.sqrt(temp);
	}
	// O(n) - one loop for magnitudes plus a dotProduct call (also O(n)), constant number of operations after
	//ok this one i got from the brief, i can explain what it does but not why
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
