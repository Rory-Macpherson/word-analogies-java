package ie.atu.sw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/*This is a merge sort, it is a fast algorithm, sitting at speed of O(n log n)
 * with a space of O(n).
 * the speed complexity is two different things. so we have the log n, 
 * that is splitting the size of the list in half until you are at
 * single values. Then the n is adding them all back up. so the way i look at it
 * is for sake of argument, lets say n = 10. 
 * 10 x log 10 == Big O notation
 * so for each log you look at 10 values. average*/

public class MergeSort {

	/*this is a method called mergeSort
	 * it returns a list, each entry of that list is a string and a double
	 * its this way as it was a map originally.
	 * when you call it you must give it a list that is a map, of strings and doubles.
	 * its just called list
	 * the if statement is the base case for recursion.  */
	// O(n log n) - list is halved log n times recursively, and each level does O(n) work merging
	public static List<Entry<String, Double>> mergeSort(List<Entry<String, Double>> list) {
		if (list.size() <= 1)
			return list;

		//finding the mid section of list
		int mid = list.size() / 2;

		//recursion calls again and again until it hits base case. at that point, 
		//left and right will call merge while being only one value in size each
		List<Entry<String, Double>> left = mergeSort(list.subList(0, mid));
		List<Entry<String, Double>> right = mergeSort(list.subList(mid, list.size()));

		return merge(left, right);
	}

	/*the longest and most hard to read method title ever. it returns an list, of strings and doubles.
	//you feed it two lists of strings and doubles.
	// results is just another arraylist that we fill and return
	//i and j are just counters. */
	
	
	// O(n) - each element from left and right is visited exactly once during the merge
	private static List<Entry<String, Double>> merge(List<Entry<String, Double>> left, List<Entry<String, Double>> right) {
		List<Entry<String, Double>> result = new ArrayList<>();
		int i = 0; 
		int j = 0;

		
		/*while i and j are less than the size of left and right,
		 * compare the current value of left and right and see which is larger,
		 * add that to result. i changed this to larger so i can just grab the 
		 * highest results easier.  */
		
		while (i < left.size() && j < right.size()) {
			if (left.get(i).getValue() >= right.get(j).getValue()) {
				result.add(left.get(i++));
			} else {
				result.add(right.get(j++));
			}
		}

		/*this is for if one array finishes before the other, 
		 * or is smaller in size then you just add the rest of the 
		 * longer one to result. */
		while (i < left.size())
			result.add(left.get(i++));
		while (j < right.size())
			result.add(right.get(j++));

		//finally, return result. 
		return result;
	}
}