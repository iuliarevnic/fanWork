package Parser;

import FiniteAutomata.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Grammar {

    private List<String> N;//set of non-terminal symbols
    private List<String> E;//set of terminal symbols
    private List<Pair<String, String>> P;//set of productions
    private String S;//start symbol

    public String getS() {
        return S;
    }



    public List<String> retrieveUsefulInformationFromLine(String line) {
        //we skip "=" which appears after every component of the Grammar
        return Arrays.stream(line.strip().split(" ")).skip(2L).collect(Collectors.toList());
    }

    public Grammar() {
        N = new ArrayList<>();
        E = new ArrayList<>();
        P = new ArrayList<>();
    }

    public List<String> getN() {
        return N;
    }

    public List<String> getE() {
        return E;
    }

    public List<Pair<String, String>> getP() {
        return P;
    }

    public List<Pair<String, String>> productionsForGivenNonTerminal(String nonTerminal){
        return P.stream().filter(pair-> pair.getKey().equals(nonTerminal)).collect(Collectors.toList());
    }

    public boolean verifyCFG(){
        for(Pair<String,String> pair : P){
            if(!N.contains(pair.getKey())){
                //multiple non-terminals on the left-hand side
                return false;
            }

        }
        return true;
    }

    public void readGrammar(String file) throws Exception {
        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            //read set of of non-terminals
            String line = bufferedReader.readLine();
            N = retrieveUsefulInformationFromLine(line);
            //read set of terminals
            line = bufferedReader.readLine();
            E = retrieveUsefulInformationFromLine(line);
            //read start symbol
            line = bufferedReader.readLine();
            S = retrieveUsefulInformationFromLine(line).toString();
            //read set of productions
            bufferedReader.readLine();
            line = bufferedReader.readLine();
            while (line != null) {
                List<String> tokens = Arrays.stream(line.strip()
                        .split("->"))
                        .collect(Collectors.toList());
                //verify if non-terminal belongs to N
                if (N.contains(tokens.get(0)) && validateRHS(tokens.get(1))) {
                    Pair<String,String>  pair= new Pair( tokens.get(0), tokens.get(1));
                    P.add(pair);
                    line = bufferedReader.readLine();
                } else {
                    throw new Exception("Non-terminal from P not found in N");
                }
            }
        }
    }

    private boolean validateRHS(String rhs) {
        String[] symbols=rhs.strip().split(" ");
        for(String symbol:symbols){
            if(!N.contains(symbol) && !E.contains(symbol))
                return false;
        }
        return  true;
    }
}
