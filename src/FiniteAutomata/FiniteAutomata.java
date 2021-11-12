package FiniteAutomata;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class FiniteAutomata {
    private List<String> Q;//finite set of states
    private List<String> E;//finite alphabet
    private HashMap<Pair<String, String>, List<String>> D;//transition function
    private String q0;//initial state
    private List<String> F;//set of final states

    public FiniteAutomata() {
        Q = new ArrayList<>();
        E = new ArrayList<>();
        D = new HashMap<>();
        F = new ArrayList<>();
    }

    public List<String> getSetOfStates() {
        return Q;
    }

    public List<String> getAlphabet() {
        return E;
    }

    public HashMap<Pair<String, String>, List<String>> getTransitionFunction() {
        return D;
    }

    public String getInitialState() {
        return q0;
    }

    public List<String> getFinalStates() {
        return F;
    }

    public List<String> retrieveUsefulInformationFromLine(String line) {
        return Arrays.stream(line.strip().split(" ")).skip(2L).collect(Collectors.toList());//we skip "=" which appears after every component of the FA
    }

    public List<Pair<String, String>> getPossibleMoves(String nextState, String sequence) {
        Set<Pair<String, String>> allMoves = D.keySet();
        return allMoves.stream().filter(move -> move.getKey().equals(nextState) && D.get(move).contains(sequence)).collect(Collectors.toList());
    }

    public boolean verifyDeterministic() {
        Set<Pair<String, String>> moves = D.keySet();
        for (Pair<String, String> move : moves) {
            List<String> possibleSymbols = D.get(move);
            for (String state : Q) {
                if (!state.equals(move.getValue())) {
                    List<String> possibleSymbolsDifferentMove = D.get(new Pair(move.getKey(), state));
                    if (possibleSymbolsDifferentMove != null)
                        if (possibleSymbolsDifferentMove.stream().anyMatch(value -> possibleSymbols.stream().anyMatch(position -> position.equals(value)))) {
                            return false;
                        }
                }
            }
        }
        return true;
    }

    public boolean verifySequence(String sequence) {
        //System.out.println(sequence);
        if (verifyDeterministic()) {
            //System.out.println("intra aici");
            List<Pair<String, String>> nextMoves;
            boolean found = false;
            nextMoves = getPossibleMoves(q0, sequence.substring(0, 1));
            //System.out.println(nextMoves);
            Queue<Pair<String, String>> queue = new LinkedList<>(nextMoves);
            int position = 1;
            if (!queue.isEmpty() && position == sequence.length() && F.contains(queue.peek().getValue())) {
                return true;
            }
            while (!queue.isEmpty() && position != sequence.length()) {
                Pair<String, String> move = queue.remove();
                nextMoves = getPossibleMoves(move.getValue(), sequence.substring(position, position + 1));
                position += 1;
                queue.addAll(nextMoves);
                if (position == sequence.length() && !nextMoves.isEmpty() && F.contains(nextMoves.get(0).getValue())) {
                    found = true;
                }
            }
            return found;
        }
        return false;

    }
    public void addMove(String firstState, String secondState, String symbol){
        Pair<String, String> move = new Pair(firstState, secondState);
        D.get(move).add(symbol);
    }
    private boolean verifyMove(Pair<String, String> moveStates) {
        Set<Pair<String, String>> moveList = D.keySet();
        return moveList.stream().anyMatch(move -> move.verifyEquality(moveStates.getKey(), moveStates.getValue()));

    }

    public void read(String file) throws Exception {
        //the structure of the a file containing a finite automaton is the following:
        //Q = set of states separated by spaces
        //E = alphabet elements separated by spaces
        //q0 = initial state
        //F = final states separated by spaces
        //D =
        //list of moves of the form "firstState, secondState = alphabet element which gets processed", each one on a row
        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            Q = retrieveUsefulInformationFromLine(line);
            line = bufferedReader.readLine();
            E = retrieveUsefulInformationFromLine(line);
            line = bufferedReader.readLine();
            q0 = retrieveUsefulInformationFromLine(line).get(0);
            if (!Q.contains(q0)) {
                throw new Exception("Initial state not found in the set of states.");
            }
            line = bufferedReader.readLine();
            F = retrieveUsefulInformationFromLine(line);
            for (String finalState : F) {
                if (!Q.contains(finalState)) {
                    throw new Exception("Final state "+finalState+" not found in the set of states");
                }
            }
            bufferedReader.readLine();
            line = bufferedReader.readLine();
            while (line != null) {
                List<String> tokens = Arrays.stream(line.strip()
                        .replace("=", "")//we only need the states and the element processed, so we
                        .replace(",", "")//eliminate the = and the ,
                        .split(" "))
                        .collect(Collectors.toList());
                if (Q.contains(tokens.get(0)) && Q.contains(tokens.get(0))) {//we verify that each state in a move belongs to the set of states
                    Pair<String, String> move = new Pair(tokens.get(0), tokens.get(1));
                    if (E.contains(tokens.get(3))) {//we verify that the element to be processed belongs to the alphabet
                        if (verifyMove(move)) {//if we already have the move between those states, we just add the alphabet element
                            D.get(move).add(tokens.get(3));
                        } else {
                            D.put(move, new ArrayList<>());//if the move does not exist, we add it to the transition function
                            D.get(move).add(tokens.get(3));

                        }
                        line = bufferedReader.readLine();
                    } else {
                        throw new Exception("Element from the move not found in the alphabet");
                    }
                } else {
                    throw new Exception("One or both states not found in the set of states");
                }
            }
        }
    }


}

