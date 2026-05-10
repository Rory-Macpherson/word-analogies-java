package ie.atu.sw;

/**
 * @author Rory
 */

public class Runner {
    // O(1) or O(N^2) as it does call all the other methods. 
	/**
	 *main method, catches any exceptions and prints a message if caught.
	 * @param args command-line arguments (not used)
	 */
	public static void main(String[] args){
        try {
			new Menu().start();
		} catch (Exception e) {
			 System.out.println("Fatal error: " + e.getMessage());
		}
    }
}
