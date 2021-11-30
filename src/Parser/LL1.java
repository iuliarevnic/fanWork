package Parser;

import FiniteAutomata.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.copyOfRange;

public class LL1 {
    private Grammar grammar;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;

    public LL1(Grammar grammar) {
        this.grammar = grammar;
        this.first = new HashMap<>();
        this.follow = new HashMap<>();

        List<String> nonTerminals = this.grammar.getN();
        for(String nonTerminal: nonTerminals) {
            this.first.put(nonTerminal, new HashSet<>());
            this.follow.put(nonTerminal, new HashSet<>());
        }
        //we include the terminals so we can compute directly
        List<String> terminals = this.grammar.getE();
        for(String terminal: terminals) {
            this.first.put(terminal, new HashSet<>());
        }
        //call the method which computes the FIRST set
        First();
    }

    private String concatenationFirst(String firstSymbol, String secondSymbol) {
        if(firstSymbol.equals("E"))
            return secondSymbol;
        return firstSymbol;
    }

    private Set<String> concatenationTwoSets(Set<String> firstSet, Set<String> secondSet) {
        Set<String> result = new HashSet<>();
        if(firstSet.isEmpty())
            return secondSet;
        if(secondSet.isEmpty())
            return firstSet;
        for(String firstSetSymbol: firstSet) {
            for(String secondSetSymbol: secondSet)
                result.add(concatenationFirst(firstSetSymbol, secondSetSymbol));
        }
        return result;
    }

    public void First() {
        List<String> nonTerminals = this.grammar.getN();
        List<String> terminals = this.grammar.getE();
        List<Pair<String, List<String>>> productions = this.grammar.getP();
        //initialise F0
        for(String nonTerminal: nonTerminals) {
            //we get all productions for the current nonTerminal
            List<Pair<String, List<String>>> productionsForGivenNonTerminal = this.grammar.productionsForGivenNonTerminal(nonTerminal);
            for(Pair<String, List<String>> pair: productionsForGivenNonTerminal) {
                //if the first symbol is a terminal or epsilon(represented as E), we can add it directly to the FIRST set
                if(terminals.contains(pair.getValue().get(0)) || pair.getValue().get(0).equals("E"))
                    this.first.get(nonTerminal).add(pair.getValue().get(0));
            }
        }
        for(String terminal: terminals) {
            this.first.get(terminal).add(terminal);
        }
        //we verify if the previous set has changed to know if we keep going
        boolean isChanged = true;
        while(isChanged) {
            isChanged = false;
            List<String> lhs=productions.stream().map(Pair::getKey).collect(Collectors.toList());
            for(String lhsNonTerminal: lhs) {
                List<List<String>> rhs=productions.stream().filter(pair->pair.getKey().equals(lhsNonTerminal))
                        .map(Pair::getValue).collect(Collectors.toList());
                for(List<String> rhsSequence: rhs) {
                    Set<String> copyPrevious = first.get(lhsNonTerminal);
                    Set<String> copy = Set.copyOf(first.get(lhsNonTerminal));
                    Set<String> result = new HashSet<>();
                    if(rhsSequence.get(0).equals("E"))
                    {
                        continue;
                    }
                    else if(rhsSequence.size() == 1)
                    {
                        if(terminals.contains(rhsSequence.get(0)))
                            result.add(rhsSequence.get(0));
                        else
                            //we add the FIRST elements of the non-terminal found, if they don't already exist
                            result.addAll(this.first.get(rhsSequence.get(0)));
                    }
                    else {
                        result.addAll(this.first.get(rhsSequence.get(0)));
                    }

                    copyPrevious.addAll(result);
                    if(!copyPrevious.equals(copy)) {
                        isChanged = true;
                        //replace first with the newly formed set
                        this.first.get(lhsNonTerminal).addAll(copyPrevious);
                    }

                }
            }
        }

        for(String symbol: first.keySet()) {
            if(nonTerminals.contains(symbol))
                System.out.println(symbol + " FIRST=" + first.get(symbol));
        }
    }

    private String getStringFromList(String list) {
        return  list.substring(1, list.length()-1);
    }

    public void Follow() {
        List<String> nonTerminals = this.grammar.getN();
        List<String> terminals = this.grammar.getE();
        List<Pair<String, List<String>>> productions = this.grammar.getP();

        //initialize L0
        String S = getStringFromList(this.grammar.getS());

        for(String nonTerminal: nonTerminals) {
            System.out.println();
            if (nonTerminal.equals(S))
                this.follow.get(nonTerminal).add("E");
            else
                this.follow.get(nonTerminal).add("");
        }


        for (String symbol: this.follow.keySet())
            if(nonTerminals.contains(symbol))
                System.out.println(symbol + " FOLLOW = " + this.follow.get(symbol));

        System.out.println("after initialization");

        //we construct the follow
        boolean isChanged = true;
        while(isChanged) {
            isChanged = false;

            //List<String> lhs = this.grammar.getP().stream().map(Pair::getKey).collect(Collectors.toList());

            //parse through all the nonTerminals
            for(String nonTerminal: nonTerminals) {

                //get all the pairs which have on the lhs the nonTerminal
                List<Pair<String, List<String>>> productionsHavingNonTerminalInLHS = this.grammar.getPairsWhereGivenNonTerminalIsInRHS(nonTerminal);
                System.out.println(productionsHavingNonTerminalInLHS);

                //for each lhs, perform operations
                Set<String> result = new HashSet<>();
                for (Pair<String, List<String>> pair : productionsHavingNonTerminalInLHS) {
                    List<String> lhsOfNonTerminal = pair.getValue();
                    for (int i = 0; i < lhsOfNonTerminal.size(); i++) {

                        if (lhsOfNonTerminal.get(i).equals(nonTerminal))
                            //check if nonTerminal is on the last position of the production
                            if (i == lhsOfNonTerminal.size() - 1) {
                                //check on the lhs of the production
                                Set<String> previousLOfNonTerminal = this.follow.get(pair.getKey());
                                result.addAll(previousLOfNonTerminal);
                            } else {
                                boolean isFollowingTerminal = false;
                                for (String terminal : terminals)
                                    if (terminal.equals(lhsOfNonTerminal.get(i + 1))) {
                                        result.add(terminal);
                                        isFollowingTerminal = true;
                                    }
                                if (!isFollowingTerminal) {
                                    //if epsilon appears in the First(followingNonTerminal)
                                    //then Follow(lhsNonTerminal) = First(followingNonTerminal) \ epsilon U Follow (pair.getKey())
                                    Set<String> first = this.first.get(lhsOfNonTerminal.get(i + 1));
                                    if (first.contains("E")) {
                                        result.addAll(this.first.get(lhsOfNonTerminal.get(i + 1)));
                                        result.addAll(this.follow.get(pair.getKey()));
                                        result.remove("E");
                                    }
                                    //otherwise,
                                    //Follow(lhsNonTerminal) = First(followingNonTerminal)
                                    else {
                                        result.addAll(this.first.get(lhsOfNonTerminal.get(i + 1)));
                                    }

                                }
                            }
                    }
                }
                this.first.get(nonTerminal).addAll(result);
            }
        }

        for (String symbol: this.follow.keySet())
            if(nonTerminals.contains(symbol))
                System.out.println(symbol + " FOLLOW = " + this.follow.get(symbol));
    }

    public void printFollow() {
        for (String symbol: this.follow.keySet())
            if(this.grammar.getN().contains(symbol))
                System.out.println(symbol + " FOLLOW = " + this.follow.get(symbol));
    }

}
