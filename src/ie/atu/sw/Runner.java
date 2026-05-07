package ie.atu.sw;

public class Runner {
    // O(1) - creates one Menu object and calls start(), all complexity is in the methods it delegates to
    public static void main(String[] args) throws Exception {
        new Menu().start();
    }
}
