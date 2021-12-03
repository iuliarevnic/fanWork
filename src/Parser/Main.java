package Parser;
;
import java.util.Scanner;

public class Main {

    private static Grammar grammar=new Grammar();


    private static void readGrammar(String path) {
        try {

            grammar.readGrammar(path);
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
        Scanner scan = new Scanner(System.in);
        System.out.print("name: ");
        String name = scan.nextLine();
        String beginningOfPathIulia = "C:\\Users\\iulia\\lftc\\src\\Parser\\g2.txt";
        String beginningOfPathAna = "/Users/anasavu/Documents/UBB/Sem5/FLCD/labs/fanWork/src/Parser/g1.txt";
        if (name.equals("iulia"))
            readGrammar(beginningOfPathIulia);
        else
            readGrammar(beginningOfPathAna);
        LL1 ll1=new LL1(grammar);
        ll1.parsingTable();
        showMenu();

        while (true) {
            System.out.print("Option No: ");
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
                    System.out.print("Give non-terminal: ");
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
