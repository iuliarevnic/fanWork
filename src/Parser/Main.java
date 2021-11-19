package Parser;
;
import java.util.Scanner;

public class Main {

    private static Grammar grammar=new Grammar();


    private static void readGrammar() {
        try {

            grammar.readGrammar("C:\\Users\\iulia\\lftc\\src\\Parser\\g2.txt");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void showSetOfProductions() {
        System.out.println(grammar.getP());
    }


    private static void showNonTerminals() {
        System.out.println(grammar.getN());
    }


    private static void showTerminals() {
        System.out.println(grammar.getE());
    }

    private static void showStartSymbol() {
        System.out.println(grammar.getS());
    }

    private static void showMenu() {
        System.out.println("1. Show terminals");
        System.out.println("2. Show non-terminals");
        System.out.println("3. Show set of productions");
        System.out.println("4. Show start symbol");
        System.out.println("5. Show productions for a given non-terminal");
        System.out.println("6. Check CFG");
        System.out.println("0. Exit");

    }

    public static void main(String[] args) {
        readGrammar();
        showMenu();
        Scanner scan = new Scanner(System.in);
        while (true) {
            int i = scan.nextInt();
            switch (i) {
                case 1 -> {
                    showTerminals();
                }
                case 2 -> {
                    showNonTerminals();
                }
                case 3 -> {
                    showSetOfProductions();
                }
                case 4 -> {
                    showStartSymbol();
                }
                case 5 -> {
                    System.out.println("Give non-terminal:");
                    scan.nextLine();
                    String nonTerminal=scan.nextLine();
                    System.out.println(grammar.productionsForGivenNonTerminal(nonTerminal));
                }
                case 6 -> {
                    System.out.println(grammar.verifyCFG());
                }
            }
            if (i == 0) {
                break;
            }
        }
    }

}
