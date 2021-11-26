package Parser;

import FiniteAutomata.Pair;

import java.util.*;
import java.util.stream.Collectors;

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

    private void First() {
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
            //get all non-terminals in the lhs of productions
            List<String> lhs=productions.stream().map(Pair::getKey).collect(Collectors.toList());
            for(String lhsNonTerminal: lhs) {
                //get all the rhs for the given lhsNonTerminal
                List<List<String>> rhs=productions.stream().filter(pair->pair.getKey().equals(lhsNonTerminal))
                        .map(Pair::getValue).collect(Collectors.toList());
                //for each rhs, we check the first symbol
                for(List<String> rhsSequence: rhs) {
                    //previous set for current lhsNonTerminal
                    Set<String> copyPrevious = first.get(lhsNonTerminal);
                    Set<String> copy = Set.copyOf(first.get(lhsNonTerminal));
                    //result will contain the possible newly added symbols
                    Set<String> result = new HashSet<>();
                    if(rhsSequence.get(0).equals("E"))
                    {
                        continue;
                    }
                    else if(rhsSequence.size() == 1)
                    {
                        //if first symbol is a terminal we add it to the result set
                        if(terminals.contains(rhsSequence.get(0)))
                            result.add(rhsSequence.get(0));
                        else
                            //we add the FIRST elements of the non-terminal found, if they don't already exist
                            result.addAll(this.first.get(rhsSequence.get(0)));
                    }
                    else {
                        //if first non-terminal contains epsilon, we go to the next one, and apply the same reasoning
                        if(this.first.get(rhsSequence.get(0)).contains("E")){
                        result = firstFromConcatenationTwoSets(this.first.get(rhsSequence.get(0)),
                                this.first.get(rhsSequence.get(1)));
                        for (int i = 2; i < rhsSequence.size(); i++) {
                            result = firstFromConcatenationTwoSets(result,
                                    this.first.get(rhsSequence.get(i)));
                        }}
                        else{
                            result.addAll(this.first.get(rhsSequence.get(0)));
                        }
                    }
                    //we update the FIRST set for the current lhsNonTerminal
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

    private String firstFromConcatenationTwoSymbols(String firstSymbol, String secondSymbol) {
        if(firstSymbol.equals("E"))
            return secondSymbol;
        return firstSymbol;
    }

    private Set<String> firstFromConcatenationTwoSets(Set<String> firstSet, Set<String> secondSet) {
        Set<String> result = new HashSet<>();
        if(firstSet.isEmpty())
            return secondSet;
        if(secondSet.isEmpty())
            return firstSet;
        for(String firstSetSymbol: firstSet) {
            for(String secondSetSymbol: secondSet)
                result.add(firstFromConcatenationTwoSymbols(firstSetSymbol, secondSetSymbol));
        }
        return result;
    }

}
