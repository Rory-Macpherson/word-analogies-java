package ie.atu.sw;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Engine {
	//declaring some fields. the first is a map, that takes a string and a double
	//the next is a list which takes a map basically.
	private Map<String, Double> wordsMap = new ConcurrentHashMap<>();
	private List<Map.Entry<String, Double>> sortedList;
	
	/*Its a constructor of the class, it takes in a map of strings and 
	 * double arrays, and a double array. 
	 * then it makes a new set using the keys of this map. 
	 * then its a for loop that goes through every key in the keys
	 * set.
	 * Then for each key in the set, gets the value of the key, adds it to 
	 * a field called vector and and then it calls the 
	 * cosignDistance method from the vector arithmetic class, by feeding in vector and 
	 * the array selected word from the user,
	 * finally it saves the the ouput in a map as the new value of the 
	 * key from the keys array.  */
	// O(n) - iterates over every word in the map once to compute its similarity score, where n is the vocabulary size
	public Engine(Map<String, double[]> map, double[] SelectedWord, int choice) {
		Set<String>keys = map.keySet();
		for(var k : keys) {
			double[] vector = map.get(k);
			double val = switch(choice) {
			 case 1 -> VectorArithmetic.cosignDistance(vector, SelectedWord);
			 case 2 -> VectorArithmetic.dotProduct(vector, SelectedWord);
			 case 3 -> VectorArithmetic.euclideanDistance(vector, SelectedWord);
			 default -> VectorArithmetic.cosignDistance(vector, SelectedWord);
			};
			wordsMap.put(k, val);	
		}
		List<Map.Entry<String, Double>> list = new ArrayList<>(wordsMap.entrySet());
		sortedList = MergeSort.mergeSort(list);
		
		
	}

	// O(1) - simply returns an already computed and sorted list
	public List<Map.Entry<String, Double>> getSortedList() {
		return sortedList;
	}

	
	
	

}
