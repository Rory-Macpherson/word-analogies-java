package ie.atu.sw;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 *
 * 
 * Scores every word in the embeddings map against a target vector
 * using a specific similarity method, then sorts the results using a  MergeSort.
 * @author Rory
 * 
 */
public class Engine {

	/** the first is a map, that takes a string and a double */
	private Map<String, Double> wordsMap = new ConcurrentHashMap<>();

	/** the next is a list which takes a map basically */
	private List<Map.Entry<String, Double>> sortedList;

	/**
	 * Its a constructor of the class, it takes in a map of strings and
	 * double arrays, and a double array.
	 * then it makes a new set using the keys of this map.
	 * then its a for loop that goes through every key in the keys set.
	 * Then for each key in the set, gets the value of the key, adds it to
	 * a field called vector and then it calls the chosen similarity method
	 * from the VectorArithmetic class, by feeding in vector and
	 * the array selected word from the user.
	 * finally it saves the output in a map as the new value of the
	 * key from the keys array, so a new map full of the keys and the new results
	 * which are now based on how close they are to the selected word/array.
	 * MergeSort always sorts descending (highest value first), which is correct for
	 * cosine and dot product where a higher score means more similar.
	 * EuclideanDistance works the opposite, a smaller number means the words
	 * are closer together, so if they picked three then we reverse the list. which is O(n),
	 * its a damn expensive choice they made, this is a late find as i did not realize that
	 * this euclideanDistance needs smaller numbers. its sneaky.
	 * O(n) - for this loop but then it calls other methods. but this loop just does one thing n times.
	 * added virtual threads to method.
	 *
	 * @param map the full embedding map loaded by EmbeddingsLoader
	 * @param SelectedWord the computed target vector to compare all words against
	 * @param choice the similarity method to use: 1 = Cosine, 2 = Dot Product, 3 = Euclidean
	 */
	public Engine(Map<String, double[]> map, double[] SelectedWord, int choice) {
		Set<String> keys = map.keySet();
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			for (var k : keys) {
				pool.execute(new Runnable() {

					@Override
					public void run() {
						double[] vector = map.get(k);
						double val = switch (choice) {
							case 1 -> VectorArithmetic.cosignDistance(vector, SelectedWord);
							case 2 -> VectorArithmetic.dotProduct(vector, SelectedWord);
							case 3 -> VectorArithmetic.euclideanDistance(vector, SelectedWord);
							default -> VectorArithmetic.cosignDistance(vector, SelectedWord);
						};
						wordsMap.put(k, val);
					}
				});
			}
		}

		List<Map.Entry<String, Double>> list = new ArrayList<>(wordsMap.entrySet());
		sortedList = MergeSort.mergeSort(list);

		// O(n) but not always used
		if (choice == 3) {
			for (int i = 0; i < sortedList.size() / 2; i++) {
				var temp = sortedList.get(i);
				sortedList.set(i, sortedList.get(sortedList.size() - 1 - i));
				sortedList.set(sortedList.size() - 1 - i, temp);
			}
		}
	}

	/**
	 * Returns the sorted list of words and their similarity scores.
	 * O(1) - its a getter.
	 *
	 * @return list of word-score entries sorted by score descending
	 */
	public List<Map.Entry<String, Double>> getSortedList() {
		return sortedList;
	}
}
