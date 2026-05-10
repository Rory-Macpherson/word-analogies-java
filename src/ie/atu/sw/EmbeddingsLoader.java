package ie.atu.sw;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * /**
 *
 * Loads word embeddings from a file into a ConcurrentHashMap.
 * Each line of the file contains a word followed by 50 comma separated doubles.
 * Uses virtual threads to process lines 
 * @author Rory
 */
public class EmbeddingsLoader {

	/** Default constructor. */
	public EmbeddingsLoader() {}

	private Map<String, double[]> idx = new ConcurrentHashMap<>();

	/**
	 * this is the method that is first called in this class,
	 * it is the only public method, so it in turn calls all the other methods.
	 * O(n) - calls parse which reads every line once, where n is the number of lines in the file.
	 *
	 * @param book the location of the embeddings file
	 * @return it returns the book sorted into a map, all trimmed with no commas or white space
	 * @throws Exception if the file cannot be read, caught by Menu.loadFile()
	 */
	public Map<String, double[]> makeIndex(String book) throws Exception {
		parse(book);
		return idx;
	}

	/**
	 * new parse method using virtual threads.
	 * you still pass it a string, then i make executor
	 * in a try so it closes when done.
	 * i get a buffer reader, which in turn reads from a file reader which in
	 * turn reads from the string that was passed in, which aims it at a file all going well.
	 * then i have a while loop to make sure there is still
	 * stuff in the book, basically while book is not empty
	 * then i make a runnable, which creates a method called run, which i override
	 * and say when the method run is called do this.
	 * and then it processes the line from the book with a new thread.
	 * i had to change text to line as the thread has to know what
	 * value its working on, if it was left as text, the value might change and crash,
	 * values inside a runnable must not change - also eclipse made me.
	 * finally we call pool to execute the task, that is when the thread is made,
	 * that runs task, that calls run and in turn calls process. easy right.
	 * lastly we close br to keep eclipse happy and also to stop it
	 * from resource leaking.. updated. now we put br in the try so it closes by itsself
	 * O(n) total work where n = lines in file, but concurrent virtual threads reduce wall-clock time significantly.
	 *
	 * @param book the location of the embedding file
	 * @throws Exception if the file cannot be opened or read
	 */
	private void parse(String book) throws Exception {
		try (var pool = Executors.newVirtualThreadPerTaskExecutor();
			 BufferedReader br = new BufferedReader(new FileReader(book))) {
			String text;
			while ((text = br.readLine()) != null) {
				String line = text;
				Runnable task = new Runnable() {

					@Override
					public void run() {
						try {
							process(line);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				pool.execute(task);
			}
		}
	}

	/**
	 * ok this method is to process the whole thing, so it only gets given a
	 * line at a time, which i am hoping is only
	 * 51 words. one word and 50 doubles. although for now they are
	 * strings. so first we split them by commas as they have commas between them,
	 * that also deletes the commas. 2 for 1.
	 * then we make the first one a value called word, as
	 * that is in fact the word we want to go in the map,
	 * then we pass the string array and the word to the add word method.
	 * ok now this method also processes all the
	 * vectors from the parts array, into doubles and adds them to the
	 * new double array called vectors.
	 * then it sends the word and the array to the addword method
	 * which hopefully will add word.
	 * also note the vector starts at i-1, that is to make sure that
	 * the first double goes to position 0 as i starts at 1 in the for loop to
	 * skip the word we have added to the string word.
	 * O(1) - processes a single line of fixed length, constant work per call.
	 * this is now done by threads as its called in the runnable.
	 *
	 * @param line a single line from the embedding file
	 * @throws Exception if a value cannot be parsed as a double
	 */
	private void process(String line) throws Exception {
		String[] parts = line.split(",");
		String word = parts[0].trim();
		double[] vector = new double[parts.length - 1];
		for (int i = 1; i < parts.length; i++) {
			vector[i - 1] = Double.parseDouble(parts[i].trim());
		}
		addWord(word, vector);
	}

	/**
	 * Adds a word and its vector to the index map.
	 * O(1) - just chuck it in there.
	 *
	 * @param word the word to add
	 * @param vectors the embedding vector for that word
	 */
	private void addWord(String word, double[] vectors) {
		idx.put(word, vectors);
	}
}
