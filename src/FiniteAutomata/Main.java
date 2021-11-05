package FiniteAutomata;


import java.util.Scanner;

public class Main {
    private static FiniteAutomata finiteAutomata = new FiniteAutomata();
    private static FiniteAutomata finiteAutomataForIdentifiers = new FiniteAutomata();
    private static FiniteAutomata finiteAutomataForIntegers = new FiniteAutomata();
    private static FiniteAutomata finiteAutomataForStrings = new FiniteAutomata();
    private static FiniteAutomata finiteAutomataForBooleans = new FiniteAutomata();

    private static void getFiniteAutomata() {
        try {
            finiteAutomata.read("C:\\Users\\iulia\\lftc\\src\\FiniteAutomata\\finiteAutomata.in");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void getFiniteAutomataForIdentifiers() {
        try {
            finiteAutomataForIdentifiers.read("C:\\Users\\iulia\\lftc\\src\\FiniteAutomata\\identifier.txt");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void getFiniteAutomataForIntegers() {
        try {
            finiteAutomataForIntegers.read("C:\\Users\\iulia\\lftc\\src\\FiniteAutomata\\integer.txt");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void getFiniteAutomataForStrings() {
        try {
            finiteAutomataForStrings.read("C:\\Users\\iulia\\lftc\\src\\FiniteAutomata\\string.txt");
            finiteAutomataForStrings.addMove("B","B"," ");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void getFiniteAutomataForBoolean() {
        try {
            finiteAutomataForBooleans.read("C:\\Users\\iulia\\lftc\\src\\FiniteAutomata\\boolean.txt");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }


    private static void showSetOfStates() {
        System.out.println(finiteAutomata.getSetOfStates());
    }

    private static void showAlphabet() {
        System.out.println(finiteAutomata.getAlphabet());
    }

    private static void showTransitionFunction() {
        System.out.println(finiteAutomata.getTransitionFunction());
    }

    private static void showInitialState() {
        System.out.println(finiteAutomata.getInitialState());
    }

    private static void showSetOfFinalStates() {
        System.out.println(finiteAutomata.getFinalStates());
    }


    private static void verifyIfDeterministic() {
        System.out.println(finiteAutomata.verifyDeterministic());
    }

    private static void verifySequence(String sequence) {
        System.out.println(finiteAutomata.verifySequence(sequence));
    }

    private static void verifyIdentifier(String identifier) {
        System.out.println(finiteAutomataForIdentifiers.verifySequence(identifier));
    }

    private static void verifyInteger(String integer) {
        System.out.println(finiteAutomataForIntegers.verifySequence(integer));
    }

    private static void verifyString(String string) {
        System.out.println(finiteAutomataForStrings.verifySequence(string));
    }

    private static void verifyBoolean(String bool) {
        System.out.println(finiteAutomataForBooleans.verifySequence(bool));
    }


    private static void showMenu() {
        System.out.println("1. Show set of states");
        System.out.println("2. Show alphabet");
        System.out.println("3. Show initial state");
        System.out.println("4. Show set of final states");
        System.out.println("5. Show transition function");
        System.out.println("6. Verify if the finite automaton is deterministic");
        System.out.println("7. Verify sequence");
        System.out.println("8. Verify identifier");
        System.out.println("9. Verify integer");
        System.out.println("10. Verify string");
        System.out.println("11. Verify boolean");
        System.out.println("0. Exit");
    }

    public static void main(String[] args) {
        Main.getFiniteAutomata();
        Main.getFiniteAutomataForIdentifiers();
        Main.getFiniteAutomataForIntegers();
        Main.getFiniteAutomataForBoolean();
        Main.getFiniteAutomataForStrings();
        Main.showMenu();
        Scanner scan = new Scanner(System.in);
        while (true) {
            int input = scan.nextInt();
            switch (input) {
                case 1: {
                    showSetOfStates();
                    break;
                }
                case 2: {
                    showAlphabet();
                    break;
                }
                case 3: {
                    showInitialState();
                    break;
                }
                case 4: {
                    showSetOfFinalStates();
                    break;
                }
                case 5: {
                    showTransitionFunction();
                    break;
                }
                case 6: {
                    verifyIfDeterministic();
                    break;
                }
                case 7: {
                    System.out.println("Input sequence:");
                    String value = scan.next();
                    verifySequence(value);
                    break;
                }
                case 8: {
                    System.out.println("Input identifier:");
                    String value = scan.next();
                    verifyIdentifier(value);
                    break;
                }
                case 9: {
                    System.out.println("Input integer:");
                    String value = scan.next();
                    verifyInteger(value);
                    break;
                }
                case 10: {
                    System.out.println("Input string:");
                    String value = scan.next();
                    verifyString("\""+value+"\"");
                    //System.out.println(finiteAutomataForStrings.getInitialState());
                    //System.out.println(finiteAutomataForStrings.getTransitionFunction());
                    //System.out.println(finiteAutomataForStrings.getFinalStates());
                    //verifyString("\"The sum is \"");
                    break;
                }
                case 11: {
                    System.out.println("Input boolean:");
                    String value = scan.next();
                    verifyBoolean(value);
                    break;
                }
                default:
                    System.out.println("Not a menu option.");
                    showMenu();

            }
            if (input == 0)
                break;
        }
    }
}
