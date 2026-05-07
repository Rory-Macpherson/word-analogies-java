package ie.atu.sw;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


//lots of class level fields i use throughout the class, private so they cant be changed or seen by others
public class Menu {
    private final Scanner sc = new Scanner(System.in);
    private boolean wordsEntered = false;
    private int switchop;
    private int arithmeticType;
    private Map<String, double[]> map;
    private double[] result;
    private String outputPath = "./out.txt";
    private List<String> inputWords = new ArrayList<>();

    /*this is a helper method so make sure the user uses a correct int, it stops me from writing it everywhere
    it is called when the user is to enter an int. if they enter a correct 
    int then the int gets passed back to the caller. if its an not an int then they are asked to try again  
    i catch an exception here if they get it wrong to stop the program crashing */
    private int readInt() {
        while (true) {
            try {
                return sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                sc.next();
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // O(1) per iteration, this method just loads once. although the while is constant.. so maybe a constant O(1)?
    public void start() throws Exception {
    	System.out.print(ConsoleColour.YELLOW);
        System.out.println("************************************************************");
        System.out.println("*     ATU - Dept. of Computer Science    *");
        System.out.println("*                                                          *");
        System.out.println("*  Word Analogies with Vector Arithmetic & Virtual Threads *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");


        while (true) {
            System.out.println("(1) Enter Path to Embeddings File>");
            System.out.println("(2) Enter words and required Vector Operation>");
            System.out.println("(3) Choose Similarity Method");
            System.out.println("(4) Run");
            System.out.println("(5) Specify Output File (default: ./out.txt)");
            System.out.println("(6) Quit");

            int op = readInt();
            switch (op) {
                case 1 -> loadFile();
                case 2 -> enterWords();
                case 3 -> similarityMethod();
                case 4 -> run();
                case 5 -> setOutputFile();
                case 6 -> {
                    System.out.println();
                    System.exit(0);
                }
                default -> System.out.println("You made an incorrect selection, please try again.");
            }
        }
    }

    // O(n) - this is called if the user want to load a file,
    //its O(n) as it calls the makeindex method on the 
    //embeddingsloader class. and that is o(n). the rest of this class is O(1)
    //i also catch an exception if they enter an incorect file path
    //this gives them the option to quit
    private void loadFile() throws Exception {
        System.out.print("Enter path to embeddings file: ");
        String path = sc.next();
        try {
            EmbeddingsLoader loader = new EmbeddingsLoader();
            map = loader.makeIndex(path);
            int size = 100;
            for (int i = 0; i < size; i++) {
                printProgress(i + 1, size);
            }
        } catch (Exception e) {
            System.out.println("File not found. Press 1 to try again or any number to quit.");
            int choice = readInt();
            if (choice == 1) {
                loadFile();
            } else {
                System.exit(0);
            }
        }
    }

    /*it was O(N^2) now with new map it is.. O(word x 50) but 50 is constant so O(n) where n = words?
     *  and that is only because it has to search through the whole map to find the value that matches 
     * with the word the user enters.. should i have used a hash map.. maybe. 
     * ok i changed it to a hash map
     * the method, starts with a null checker to make sure the map has been filled first
     */
    private void enterWords() {
        if (map == null) {
            System.out.println("Please load the embeddings file first (Option 1).");
            return;
        }
        /* then it clears the saved words array list, then it makes sure that the word is in the map
         * hashmap so O(1). then it it gets the gets the value of that key from the map, saves it in result
         * and then adds the word to the input word array
         * */
        inputWords.clear();
        System.out.print("Enter first word: ");
        String first = sc.next().trim().toLowerCase();
        if (map.get(first) == null) {
            System.out.println("Word not recognised. Please try again using only basic words with no numbers or symbols.");
            return;
        }
        result = map.get(first);
        inputWords.add(first);

        /*while true, this goes on forever until its forcefully broken. 
         * it calls the arithmetic method, this makes the user pick what maths they want to
         * do with the words. then we ask them to enter the next word. as you need at least two words 
         * to do maths.. if the word is not recognized you are thrown to being asked, or being asked again
         * if you want to add another word. basically this loop should let you add 
         * as many words as you want, as what happens is it does the maths as it goes,
         * gets a new 50 double array each time by adding or subtracting or what ever
         * then finally it makes wordentered true.
         * oh also input words. each word gets added to that!! its so they can be filtered. 
         * extra big O i know but it was not working without it.*/
        while (true) {
            arithmetic();
            System.out.print("Enter next word: ");
            String next = sc.next().trim().toLowerCase();
            if (map.get(next) == null) {
                System.out.println("Word not recognised, skipping.");
            } else {
                result = VectorArithmetic.operate(result, map.get(next), switchop);
                inputWords.add(next);
            }
            System.out.print("Add another word? Type Y for yes, type litraly anything else for no! ");
            String answer = sc.next().trim().toLowerCase();
            if (!answer.equals("y")) break;
        }

        wordsEntered = true;
    }

    // O(1), it just sets an int! easy unles you pick a bad int then you get stuck in this loop forever 
    private void arithmetic() {
        System.out.println("Pick an operation:");
        System.out.println("Press 1 for addition");
        System.out.println("Press 2 for subtraction");
        System.out.println("Press 3 for multiplication");
        System.out.println("Press 4 for division");
        int op = readInt();
        switch (op) {
            case 1 -> switchop = 1;
            case 2 -> switchop = 2;
            case 3 -> switchop = 3;
            case 4 -> switchop = 4;
            default -> {
                System.out.println("Invalid choice, please enter 1-4.");
                arithmetic();
            }
        }
    }

    // O(1) this is a fancy method because it does lots of things, but you can just skip it as the default choice is 1
    private void similarityMethod() {
        System.out.println("Please choose a similarity method:");
        System.out.println("Press 1 for Cosine Distance");
        System.out.println("Press 2 for Dot Product");
        System.out.println("Press 3 for Euclidean Distance");
        int op = readInt();
        switch (op) {
            case 1 -> arithmeticType = 1;
            case 2 -> arithmeticType = 2;
            case 3 -> arithmeticType = 3;
            default -> {
                System.out.println("Invalid choice, defaulting to Cosine.");
                arithmeticType = 1;
            }
        }
    }

    /* O(1) reads a string stores it and thats it, this is a simple method, it does make sure the path is 
     * not empty, if it is empty it will leave it as the default which is ./out.txt. its all about making it easy
     */
    private void setOutputFile() {
        System.out.print("Enter output file path (default: ./out.txt): ");
        String path = sc.next().trim();
        if (!path.isEmpty()) {
            outputPath = path;
        }
        System.out.println("Output will be saved to: " + outputPath);
    }

    /* O(n log n) this is the engine. read in that class why its n log n, i hate merge, hate it
    * few if statments making sure words have been set and embeddings are done
    * then we start using the fields we have saved
    * so we use the methodname the user wanted to use, if they did not choose then its 
    * just the cosine. we do that by using the if statment that sets the arithmetic type
    * then we call the engine, i call it that cause it does all the work!!!!
    * it needs a map, a result and the type of arithmetic the user wants. hells yeah
    * then the user can ask for how many output ansers they want. and they get it. 
    * then we use filtered to take the used words out of the map.
    * lastly it writes out all the maps to the terminal. i still think that the merge takes the 
    * cake on this, but writitng is slow aswell. 
    * also this method calls the write results method*/
    private void run() {
        if (map == null) {
            System.out.println("Please load the embeddings file first (Option 1).");
            return;
        }
        if (!wordsEntered) {
            System.out.println("Please enter words first (Option 2).");
            return;
        }
        String methodName = switch (arithmeticType) {
            case 1 -> "Cosine Distance";
            case 2 -> "Dot Product";
            case 3 -> "Euclidean Distance";
            default -> "Cosine Distance (default)";
        };
        if (arithmeticType == 0) arithmeticType = 1;
        System.out.println("Using similarity method: " + methodName);

        Engine engine = new Engine(map, result, arithmeticType);
        List<Map.Entry<String, Double>> list = engine.getSortedList();
        System.out.print("How many results do you want? ");
        int n = readInt();

        List<Map.Entry<String, Double>> filtered = new ArrayList<>();
        for (Map.Entry<String, Double> entry : list) {
            if (!inputWords.contains(entry.getKey())) {
                filtered.add(entry);
            }
        }

        System.out.println("\nResults:");
        for (int i = 0; i < n && i < filtered.size(); i++) {
            System.out.println((i + 1) + ") " + filtered.get(i).getKey() + " => " + filtered.get(i).getValue());
        }

        writeResults(filtered, n);
    }

    // O(n) as it writes n lines into a file,  
    private void writeResults(List<Map.Entry<String, Double>> list, int n) {
        try (PrintWriter pw = new PrintWriter(outputPath)) {
            for (int i = 0; i < n && i < list.size(); i++) {
                pw.println((i + 1) + ") " + list.get(i).getKey() + " => " + list.get(i).getValue());
            }
            System.out.println("Results saved to " + outputPath);
        } catch (Exception e) {
            System.out.println("Could not write to file: " + outputPath);
        }
    }

    // O(1) - draws a fixed-width bar of 50 characters regardless of input values
    public static void printProgress(int index, int total) {
        if (index > total) return;
        int size = 50;
        char done = '█';
        char todo = '░';

        int complete = (100 * index) / total;
        int completeLen = size * complete / 100;

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append((i < completeLen) ? done : todo);
        }

        System.out.print("\r" + sb + "] " + complete + "%");

        if (index == total) System.out.println("\n");
    }
}
