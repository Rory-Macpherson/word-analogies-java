package ie.atu.sw;

/**
 * 
 * this is the class for the math heads.
 * Provides static methods for vector arithmetic and similarity calculations
 * used to compare word embeddings.
 * @author Rory
 */
public class VectorArithmetic {

	/** Default constructor. */
	public VectorArithmetic() {}

	/**
	 * most of these methods are very similar, to the extent i
	 * should probably narrow them down to one. but alas here.
	 * ok i refactored the repetitive code to only have
	 * one method that can do it all.
	 * O(n) - as it has to do something for every result in the array.
	 *
	 * @param one the first vector
	 * @param two the second vector
	 * @param op the operation to apply: 1 = addition, 2 = subtraction, 3 = multiplication, 4 = division
	 * @return a new vector with the operation applied element by element
	 */
	public static double[] operate(double[] one, double[] two, int op) {
		double[] result = new double[one.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = switch (op) {
				case 1 -> one[i] + two[i];
				case 2 -> one[i] - two[i];
				case 3 -> one[i] * two[i];
				case 4 -> one[i] / two[i];
				default -> throw new IllegalArgumentException("Unexpected value: " + op);
			};
		}
		return result;
	}

	/**
	 * ok this one i got from the brief, i can explain what it does but not why.
	 * O(n) - it does math for the length of the arrays.
	 *
	 * @param one the first vector
	 * @param two the second vector
	 * @return the dot product of the two vectors as a single double
	 */
	public static double dotProduct(double[] one, double[] two) {
		double result = 0;
		for (int i = 0; i < one.length; i++) {
			result += (one[i] * two[i]);
		}
		return result;
	}

	/**
	 * ok this one i got from the brief, i can explain what it does but not why.
	 * O(n) - it does 2 things per n, but we dont count constants so O(n), and one thing after, the sqrt.
	 *
	 * @param one the first vector
	 * @param two the second vector
	 * @return the euclidean distance between the two vectors
	 */
	public static double euclideanDistance(double[] one, double[] two) {
		double result = 0;
		double temp = 0;
		for (int i = 0; i < one.length; i++) {
			result = (one[i] - two[i]);
			temp += (result * result);
		}
		//square root not squirt!!!
		return Math.sqrt(temp);
	}

	/**
	 * ok this one i got from the brief, i can explain what it does but not why.
	 * O(n) or O(n*2) as it calls another method inside this method that is also O(n) so its 2 time O(n).
	 *
	 * @param one the first vector
	 * @param two the second vector
	 * @return the cosine similarity between the two vectors, between 0 and 1
	 */
	public static double cosignDistance(double[] one, double[] two) {
		double temp1 = 0;
		double temp2 = 0;
		double result = 0;
		for (int i = 0; i < one.length; i++) {
			temp1 += (one[i] * one[i]);
			temp2 += (two[i] * two[i]);
		}
		result = dotProduct(one, two) / (Math.sqrt(temp1 * temp2));
		return result;
	}
}
