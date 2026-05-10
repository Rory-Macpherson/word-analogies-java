package ie.atu.sw;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * Main user interface class for the Word Analogies application.
 * Handles all user input and output, loads embedding, collects words,
 * chooses similarity method, runs the engine and writes results to file.
 * lots of class level fields i use throughout the class, private so they can't be changed or seen by others.
 * result is the best. it does it loads.
 * @author Rory
 */
public class Menu {

    /** Default constructor. */
    public Menu() {}
    private final Scanner sc = new Scanner(System.in);
    private boolean wordsEntered = false;
    private int switchop;
    private int arithmeticType;
    private Map<String, double[]> map;
    private double[] result;
    private String outputPath = "./out.txt";
    private List<String> inputWords = new ArrayList<>();

    /**
     * this is a helper method to make sure the user uses a correct int, it stops me from writing it everywhere.
     * it is called when the user is to enter an int. if they enter a correct
     * int then the int gets passed back to the caller. if its not an int then they are asked to try again.
     * i catch an exception here if they get it wrong to stop the program crashing.
     * O(1) per iteration - loops only if the user enters bad input.
     *
     * @return a valid integer entered by the user
     */
    private int readInt() {
        while (true) {
            try {
                return sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                sc.next();
                System.out.print(ConsoleColour.RED);
                System.out.println("Invalid input. Please enter a number.");
                System.out.print(ConsoleColour.YELLOW);
            }
        }
    }

    /**
     * Displays the main menu and loops forever waiting for user input.
     * Delegates to the appropriate method based on the users selection.
     * O(1) per iteration, this method just loads once. although the while is constant.. so maybe a constant O(1)?
     *
     * @throws Exception if any delegated method encounters an unrecoverable error
     */
    public void start() throws Exception {
        System.out.print(ConsoleColour.YELLOW);
        System.out.println("************************************************************");
        System.out.println("*     ATU - Dept. of Computer Science    *");
        System.out.println("*                                                          *");
        System.out.println("*  Word Analogies with Vector Arithmetic & Virtual Threads *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");

        while (true) {
            System.out.println("************************************************************");
            System.out.println("************************************************************");
            System.out.println("(1) Enter Path to Embeddings File");
            System.out.println("(2) Enter words and required Vector Operation (default will be set to: king - man + woman)");
            System.out.println("(3) Choose Similarity Method (default will be set to: cosignDistance)");
            System.out.println("(4) Specify Output File (default will be set to: ./out.txt)");
            System.out.println("(5) Run");
            System.out.println("(6) Quit");

            System.out.print("-> ");
            int op = readInt();
            switch (op) {
                case 1 -> loadFile();
                case 2 -> enterWords();
                case 3 -> similarityMethod();
                case 4 -> setOutputFile();
                case 5 -> run();
                case 6 -> quit();
                default -> {
                    System.out.print(ConsoleColour.RED);
                    System.out.println("You made an incorrect selection, please try again.");
                    System.out.print(ConsoleColour.YELLOW);
                }
            }
        }
    }

    /**
     * this is called if the user wants to load a file.
     * its O(n) as it calls the makeIndex method on the
     * embeddingsLoader class. and that is O(n). the rest of this method is O(1).
     * i also catch an exception if they enter an incorrect file path,
     * this gives them the option to quit.
     *
     * @throws Exception if there is an error
     */
    private void loadFile() throws Exception {
        System.out.println("Enter path to embeddings file:");
        System.out.print("-> ");
        String path = sc.next();
        try {
            EmbeddingsLoader loader = new EmbeddingsLoader();
            map = loader.makeIndex(path);
            System.out.println("");
            System.out.println("File loaded from: " + path);
            System.out.println("");
        } catch (Exception e) {
            System.out.print(ConsoleColour.RED);
            System.out.println("File not found. Press enter to go back to menu.");
            sc.nextLine();
            System.out.print(ConsoleColour.YELLOW);
        }
    }

    /**
     * it was O(N^2) now with new map it is O(word x 50) but 50 is constant so O(n) where n = words.
     * that is only because it has to search through the whole map to find the value that matches
     * with the word the user enters. should i have used a hash map? maybe.
     * ok i changed it to a hash map.
     * the method starts with a null checker to make sure the map has been filled first.
     * then it clears the saved words array list, then it makes sure that the word is in the map
     * hashmap so O(1). then it gets the value of that key from the map, saves it in result
     * and then adds the word to the input word array.
     * while true, this goes on forever until its forcefully broken.
     * it calls the arithmetic method, this makes the user pick what math they want to
     * do with the words. then we ask them to enter the next word. as you need at least two words
     * to do math. if the word is not recognized you are thrown to being asked, or being asked again
     * if you want to add another word. basically this loop should let you add
     * as many words as you want, as what happens is it does the math as it goes,
     * gets a new 50 double array each time by adding or subtracting or whatever.
     * then finally it makes wordsEntered true.
     * oh also inputWords. each word gets added to that!! its so they can be filtered.
     * extra big O i know but it was not working without it.
     */
    private void enterWords() {
        if (map == null) {
            System.out.print(ConsoleColour.RED);
            System.out.println("Go back to the Shadow! You cannot pass.");
            System.out.println("Try, you fools!");
            System.out.println("Try, and load the file!");
            System.out.println("There is no file loaded to perform the Arithmetic with.");
            System.out.println("Please load the embeddings file first (Option 1).");
            System.out.print(ConsoleColour.YELLOW);
            return;
        }
        inputWords.clear();
        System.out.println("Enter first word:");
        System.out.print("-> ");
        String first = sc.next().trim().toLowerCase();
        if (map.get(first) == null) {
            System.out.print(ConsoleColour.RED);
            System.out.println("Word not recognised. Please try again using only basic words with no numbers or symbols.");
            System.out.print(ConsoleColour.YELLOW);
            return;
        }
        result = map.get(first);
        inputWords.add(first);

        while (true) {
            arithmetic();
            System.out.println("Enter next word:");
            System.out.print("-> ");
            String next = sc.next().trim().toLowerCase();
            if (map.get(next) == null) {
                System.out.print(ConsoleColour.RED);
                System.out.println("");
                System.out.println("Word not recognised, skipping.");
                System.out.println("");
                System.out.print(ConsoleColour.YELLOW);
            } else {
                result = VectorArithmetic.operate(result, map.get(next), switchop);
                inputWords.add(next);
            }
            System.out.println("Add another word? Type Y for yes, type literally anything else for no!");
            System.out.print("-> ");
            String answer = sc.next().trim().toLowerCase();
            if (!answer.equals("y")) break;
        }

        wordsEntered = true;
    }

    /**
     * O(1), it just sets an int! easy unless you pick a bad int then you get stuck in this loop forever.
     * Prompts the user to pick a vector arithmetic operation and stores their choice in switchop.
     */
    private void arithmetic() {
        System.out.println("************************************************************");
        System.out.println("Pick an operation:");
        System.out.println("Press 1 for addition");
        System.out.println("Press 2 for subtraction");
        System.out.println("Press 3 for multiplication");
        System.out.println("Press 4 for division");
        System.out.print("-> ");
        int op = readInt();
        switch (op) {
            case 1 -> switchop = 1;
            case 2 -> switchop = 2;
            case 3 -> switchop = 3;
            case 4 -> switchop = 4;
            default -> {
                System.out.print(ConsoleColour.RED);
                System.out.println("Invalid choice, please enter 1-4.");
                System.out.print(ConsoleColour.YELLOW);
                arithmetic();
            }
        }
    }

    /**
     * O(1) this is a fancy method because it makes big changes down the line but only actually 
     * saves an int, but you can just skip it as the default choice is 1.
     * Prompts the user to choose a similarity method and stores their choice in arithmeticType.
     */
    private void similarityMethod() {
        System.out.println("************************************************************");
        System.out.println("Please choose a similarity method:");
        System.out.println("Press 1 for Cosine Distance");
        System.out.println("Press 2 for Dot Product");
        System.out.println("Press 3 for Euclidean Distance");
        System.out.print("-> ");
        int op = readInt();
        switch (op) {
            case 1 -> arithmeticType = 1;
            case 2 -> arithmeticType = 2;
            case 3 -> arithmeticType = 3;
            default -> {
                System.out.print(ConsoleColour.RED);
                System.out.println("Invalid choice, defaulting to Cosine.");
                System.out.print(ConsoleColour.YELLOW);
                arithmeticType = 1;
            }
        }
    }

    /**
     * O(1) reads a string stores it and thats it, this is a simple method, it does make sure the path is
     * not empty, if it is empty it will leave it as the default which is ./out.txt. its all about making it easy.
     */
    private void setOutputFile() {
        sc.nextLine();
        System.out.println("Enter output file path. if you enter spacebar ./out.txt will be used:");
        System.out.print("-> ");
        String path = sc.nextLine().trim();
        if (!path.isEmpty()) {
            outputPath = path;
        }
        System.out.println("Output will be saved to: " + outputPath);
        System.out.println("");
    }

    /**
     * O(n log n) this is the engine. read in that class why its n log n, i hate merge, hate it.
     * few if statements making sure words have been set and embedding are done.
     * then we start using the fields we have saved.
     * so we use the methodName the user wanted to use, if they did not choose then its
     * just the cosine. we do that by using the if statement that sets the arithmetic type.
     * then we call the engine, i call it that cause it does all the work!!!!
     * it needs a map, a result and the type of arithmetic the user wants. hells yeah.
     * then the user can ask for how many output answers they want. and they get it.
     * then we use filtered to take the used words out of the map.
     * lastly it writes out all the maps to the terminal. i still think that the merge takes the
     * cake on this, but writing is slow as well.
     * also this method calls the write results method.
     */
    private void run() {
        if (map == null) {
            System.out.print(ConsoleColour.RED);
            System.out.println("Please load the embeddings file first (Option 1).");
            System.out.print(ConsoleColour.YELLOW);
            return;
        }

        if (!wordsEntered) {
            System.out.print(ConsoleColour.WHITE);
            System.out.println("************************************************************");
            System.out.println("No words were entered, Default words provided will be 'king - man + woman'.");
            System.out.println("");
            System.out.println("Press 1 to go back to the main menu and enter words correctly or press any number to use default.");
            System.out.println("Press enter after key to continue");
            System.out.println("");
            System.out.print("-> ");
            System.out.println("");
            System.out.print(ConsoleColour.YELLOW);
            int input = readInt();
            switch (input) {
                case 1 -> { return; }
                default -> {
                    System.out.print(ConsoleColour.BLUE);
                    System.out.println("****************");
                    System.out.println("New achievement!");
                    System.out.println("****************");
                    System.out.println("Congratulations, Crawler!");
                    System.out.println("Instead of using the program as inteneded, you have taken the lazy way out!");
                    System.out.println("Reward: For one as lazy as you, You unlocked autofill!");
                    System.out.println("The program now auto fills in the words to be vectored.");
                    System.out.println("Thanks for wasting my time building the UI.");
                    System.out.println("Default words used -> King - man + woman");
                    System.out.print(ConsoleColour.YELLOW);

                    if (map.get("king") == null || map.get("man") == null || map.get("woman") == null) {
                        System.out.print(ConsoleColour.BLUE_UNDERLINED);
                        System.out.println("************************************************************");
                        System.out.println("Well thats sneaky, you are clearly either using a poor imbeddings file or trying to crash my program!");
                        System.out.println("************************************************************");
                        System.out.println("Default words not found in this embeddings file. Please enter words manually.");
                        System.out.print(ConsoleColour.YELLOW);
                        return;
                    }

                    double[] result1 = VectorArithmetic.operate(map.get("king"), map.get("man"), 2);
                    result = VectorArithmetic.operate(result1, map.get("woman"), 1);
                    //changed to arraylist to fix java.base/java.util.ImmutableCollections.uoe(ImmutableCollections.java:159)
                    inputWords = new ArrayList<>(List.of("king", "man", "woman"));
                    System.out.println("");
                }
            }
        }

        if (arithmeticType == 0) arithmeticType = 1;
        String methodName = switch (arithmeticType) {
            case 1 -> "Cosine Distance";
            case 2 -> "Dot Product";
            case 3 -> "Euclidean Distance";
            default -> "Cosine Distance (default)";
        };
        System.out.println("************************************************************");
        System.out.println("Using similarity method: " + methodName);
        System.out.println("");

        Engine engine = new Engine(map, result, arithmeticType);
        List<Map.Entry<String, Double>> list = engine.getSortedList();
        System.out.println("How many results do you want?");
        System.out.print("-> ");
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
        System.out.println("************************************************************");

        writeResults(filtered, n);
    }

    /**
     * O(n) as it writes n lines into a file.
     * takes in the sorted filtered list of results and n, which is how many the user wants.
     * opens a PrintWriter pointed at the output path - the try-with-resources makes sure
     * the file is closed automatically when done even if something goes wrong.
     * the loop goes up to n but also checks it hasn't gone past the end of the list,
     * so if the user asks for 100 results but there are only 50 it wont crash.
     * each line is written as a numbered entry with the word and its score.
     * if the file can't be written to, the catch prints an error instead of crashing.
     *
     * @param list the sorted filtered list of word-score entries
     * @param n the number of results the user wants written
     */
    private void writeResults(List<Map.Entry<String, Double>> list, int n) {
        try (PrintWriter pw = new PrintWriter(outputPath)) {
            for (int i = 0; i < n && i < list.size(); i++) {
                pw.println((i + 1) + ") " + list.get(i).getKey() + " => " + list.get(i).getValue());
            }
            System.out.println("");
            System.out.println("Results saved to " + outputPath);
            System.out.println("");
        } catch (Exception e) {
            System.out.print(ConsoleColour.RED);
            System.out.println("Could not write to file: " + outputPath);
            System.out.print(ConsoleColour.YELLOW);
        }
    }

    /**
     * O(1) - prints a farewell message and exits the program.
     */
    private void quit() {
        System.out.print(ConsoleColour.RESET);
        System.out.println(ConsoleColour.GREEN + "Farewell, Wherever you fare till your eyries receive you at the journey's end!");
        System.out.println("Thanks for using Rory's Word Vector!" + ConsoleColour.RESET);
        System.exit(0);
    }
}
