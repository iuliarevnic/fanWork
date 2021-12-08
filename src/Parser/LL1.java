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
        //call the method which computes the FIRST/FOLLOW set
        System.out.println("inainte de first");
        First();
        System.out.println("inainte de follow");
        Follow();
        System.out.println("inainte de parse table");
        parsingTable();
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

        //we construct the follow
        boolean isChanged = true;
        while(isChanged) {
            isChanged = false;

            //List<String> lhs = this.grammar.getP().stream().map(Pair::getKey).collect(Collectors.toList());

            //parse through all the nonTerminals
            for(String nonTerminal: nonTerminals) {

                //get all the pairs which have on the lhs the nonTerminal
                List<Pair<String, List<String>>> productionsHavingNonTerminalInLHS = this.grammar.getPairsWhereGivenNonTerminalIsInRHS(nonTerminal);
//                System.out.print(nonTerminal + " ");
//                System.out.println(productionsHavingNonTerminalInLHS);


                Set<String> result = new HashSet<>();
                Set<String> copyPrevious = follow.get(nonTerminal);
                Set<String> copy = Set.copyOf(follow.get(nonTerminal));
                //for each lhs, perform operations

                for (Pair<String, List<String>> pair : productionsHavingNonTerminalInLHS) {
                    List<String> rhsOfNonTerminal = pair.getValue();
                    for (int i = 0; i < rhsOfNonTerminal.size(); i++) {

                        if (rhsOfNonTerminal.get(i).equals(nonTerminal))
                            //check if nonTerminal is on the last position of the production
                            if (i == rhsOfNonTerminal.size() - 1) {
                                //check on the lhs of the production
                                Set<String> lhsOfPair = this.follow.get(pair.getKey());
                                result.addAll(lhsOfPair);
                            } else {
                                boolean isFollowingTerminal = false;
                                for (String terminal : terminals)
                                    if (terminal.equals(rhsOfNonTerminal.get(i + 1))) {
                                        result.add(terminal);
                                        isFollowingTerminal = true;
                                        break;
                                    }
                                if (!isFollowingTerminal) {
                                    //if epsilon appears in the First(followingNonTerminal)
                                    //then Follow(lhsNonTerminal) = First(followingNonTerminal) \ epsilon U Follow (pair.getKey())
                                    Set<String> first = this.first.get(rhsOfNonTerminal.get(i + 1));
                                    if (first.contains("E")) {
                                        result.addAll(this.first.get(rhsOfNonTerminal.get(i + 1)));
                                        result.remove("E");
                                        result.addAll(this.follow.get(pair.getKey()));
                                    }
                                    //otherwise,
                                    //Follow(lhsNonTerminal) = First(followingNonTerminal)
                                    else {
                                        result.addAll(this.first.get(rhsOfNonTerminal.get(i + 1)));
                                    }

                                }
                            }
                        if (result.size() != 0) {
                            copyPrevious.addAll(result);
                            copyPrevious.remove("");
                        }
                        else {
                            copyPrevious.addAll(result);
                        }
                    }
                }
                if(!copyPrevious.equals(copy)){
                    isChanged = true;
                    this.follow.get(nonTerminal).addAll(copyPrevious);
                }
//                this.first.get(nonTerminal).addAll(result);
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

    private Set<String> computeFirstForProduction(List<String> rhs){
        Set<String> firstForProduction=new HashSet<>();
        boolean isEpsilon = true;
        int i=0;
        while(i<rhs.size()){
            String symbol=rhs.get(i);
            //System.out.println(symbol);
            Set<String> firstForSymbol=first.get(symbol);
            if(!symbol.equals("E"))
                firstForProduction.addAll(firstForSymbol);
            if(!symbol.equals("E") && !firstForSymbol.contains("E")){
                isEpsilon=false;
                break;
            }
            firstForProduction.remove("E");
            i++;
        }
        if(isEpsilon)
            firstForProduction.add("E");
        return firstForProduction;
    }

    public void parsingTable() {
        String finalTerminal = "$";
        Map<Pair<String, String>, String> table = new HashMap<>();
        //complete table with pop for every (terminal,terminal) pair
        //($,$)=acc
        for (String terminal: this.grammar.getE())
        {
            Pair<String, String> pair = new Pair<>(terminal, terminal);
            table.put(pair, "pop");
        }

        table.put(new Pair<>(finalTerminal, finalTerminal), "acc");

        List<String> nonTerminals=grammar.getN();
        for(String nonTerminal:nonTerminals){
            //for each non-terminal we get the list of productions
            for( Pair<String, List<String>> production: grammar.productionsForGivenNonTerminal(nonTerminal)){
                //we get the First set for the current rhs
                Set<String> firstForProduction=this.computeFirstForProduction(production.getValue());
                int productionIndex=grammar.getP().indexOf(production)+1;
                if(!firstForProduction.contains("E")){
                    for(String terminal:firstForProduction){
                        if(!this.first.get(nonTerminal).contains(terminal))
                            break;
                        Pair<String, String> pair = new Pair<>(nonTerminal, terminal);
                        String result=String.join(" ",production.getValue());
                        table.put(pair, result+" "+productionIndex);
                    }
                }
                else{
                    for(String terminal: this.follow.get(nonTerminal)){
                        Pair<String, String> pair;
                        String result=String.join(" ",production.getValue());
                        if(terminal.equals("E")) {
                            pair = new Pair<>(nonTerminal, "$");
                        }
                        else{
                            pair=new Pair<>(nonTerminal, terminal);
                        }
                        table.put(pair,result+" "+productionIndex);
                    }
                }
            }
        }

        System.out.println(table);
        System.out.println(table.size());
    }

}
