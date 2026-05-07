package ie.atu.sw;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class EmbeddingsLoader {

	private Map<String, double[]> idx = new ConcurrentHashMap<>();
	
	
	/*this is the method that is first called in this class,
	 * it is the only public method, so it in turn calls all the other methods*/
	// O(n) - calls parse which reads every line once, where n is the number of lines in the file
	public Map<String, double[]> makeIndex(String book) throws Exception{
		parse(book);
		return idx;
	}
	
	/* this method takes in a string called book
	 * then it does alot, it tries to open a buffered reader, 
	 * that buffered reader tries to open a input stream
	 * that input stream opens a file input stream, which is the book
	 * so what happens is the file reader, opens the file
	 * and reads bytes, the input stream reader turns it into 
	 * characters and the buffer reader turns it into lines .
	 * then the while loop, this is saying string text = br.readline
	 * and then saying, if that is not null, process it, and passes
	 * it to the process method. */
	
	// O(n) - reads each line of the file exactly once, where n is the number of lines
	private void parse(String book) throws Exception{
		try(BufferedReader br  = new BufferedReader(new InputStreamReader(new FileInputStream(book)))){
			String text;
			while((text = br.readLine()) != null) {
				process(text);
			}
		}
		
	}
	
	/*ok this method is to process the whole thing, so it only gets given a
	 * line at a time, which i am hoping is only
	 * 51 words. one word and 50 doubles. although for now they are
	 * strings. so first we split them by commas as they have commas between them,
	 * that also deletes the commas. 2 for 1. 
	 * then we make the first one a vlaue called word, as
	 * that is infact the word we want to go in the map,
	 * then we pass the string array and the word to the add word method
	 * actually i think i will change that because i have more 
	 * proccesing to do.
	 * ok now this method also processes all the
	 * vectors from the parts array, into doubles and adds them to the
	 * new double array called vectors. 
	 * then it sends the word and the array to the addword method
	 * which hopefully will add word. 
	 * also note the vector starts at i-1, that is to make sure that
	 * the first double goes to position 0 as i starts at 1 in the for loop to
	 * skip the word we have added to the string word  */
	
	// O(k) - loops over the tokens in one line, where k is fixed at 51 (1 word + 50 doubles), so effectively O(1)
	private void process(String line) throws Exception{
		double[] vector = new double[50];
		String [] parts = line.split(",");
		String word = parts[0].trim();
		for (int i = 1; i < parts.length; i++) {
			vector[i-1] = Double.parseDouble(parts[i].trim());
		}
		addWord(word, vector);
		}		
		

	// O(1) - single put into a ConcurrentHashMap, average constant time
	private void addWord(String word, double[] vectors){
		idx.put(word, vectors);
		
		
	}
	
	
	
	
	
	
}
