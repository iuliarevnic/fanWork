import FiniteAutomata.FiniteAutomata;
import javafx.util.Pair;
import model.PIF;
import model.Scanner;
import model.SymbolTable;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        /*SymbolTable symbolTable=new SymbolTable();
        //a+d+a=294, 294%256=38, 38%15=8 =>chain with index 8, position 0
        Pair<Integer,Integer> adaPair=symbolTable.add("ada");
        assert (adaPair.getKey()==8);
        assert (adaPair.getValue()==0);
        //d+a+a=294, 294%256=38, 38%15=8 =>chain with index 8, position 1
        Pair<Integer,Integer> daaPair=symbolTable.add("daa");
        assert (daaPair.getKey()==8);
        assert (daaPair.getValue()==1);
        //c+a+r+t+o+f=639, 639%256=127, 127%15=7 =>chain with index 7, position 0
        Pair<Integer,Integer> cartofPair=symbolTable.add("cartof");
        assert (cartofPair.getKey()==7);
        assert (cartofPair.getValue()==0);
        //a+u+l+e+o=534, 534%256=22, 22%15=7 =>chain with index 7, position 1
        Pair<Integer,Integer> auleoPair=symbolTable.add("auleo");
        assert (auleoPair.getKey()==7);
        assert (auleoPair.getValue()==1);
        System.out.println(symbolTable.toString());
        Pair<Integer,Integer> cartofSearchPair=symbolTable.search("cartof");
        assert (cartofSearchPair.getKey()==7);
        assert (cartofSearchPair.getValue()==0);

        System.out.println(symbolTable.add("daa"));//daa already exists*/

        //testing the scanner


        FiniteAutomata finiteAutomataForIdentifiers=new FiniteAutomata();
        try {
            finiteAutomataForIdentifiers.read("C:\\Users\\iulia\\lftc\\src\\FiniteAutomata\\identifier.txt");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        FiniteAutomata finiteAutomataForIntegers=new FiniteAutomata();
        try {
            finiteAutomataForIntegers.read("C:\\Users\\iulia\\lftc\\src\\FiniteAutomata\\integer.txt");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        //we initialise a new symbol table, a PIF and a scanner
        SymbolTable newSymbolTable=new SymbolTable();
        PIF programInternalForm=new PIF();
        Scanner Scanner=new Scanner();
        //we also initialise an error string
        StringBuilder error=new StringBuilder();

        //we test the first program, which computes the maximum out of three numbers
        //String file = "C:\\Users\\iulia\\lftc\\src\\p1.txt";

        //we test the second program, which computes the gcd of two numbers
        String file = "C:\\Users\\iulia\\lftc\\src\\p2.txt";

        //we test the error program
        //String file = "C:\\Users\\iulia\\lftc\\src\\p1err.txt";

        //we test the third program, which computes the sum of n numbers
        //String file = "C:\\Users\\iulia\\lftc\\src\\p3.txt";

        String line;
        int count=0;
        try {

            FileReader fileReader =
                    new FileReader(file);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            BufferedWriter writerSymbolTable = new BufferedWriter(new FileWriter("C:\\Users\\iulia\\lftc\\src\\ST.out"));
            BufferedWriter writerPIF = new BufferedWriter(new FileWriter("C:\\Users\\iulia\\lftc\\src\\PIF.out"));


            while((line = bufferedReader.readLine()) != null) {
                count++;
                //quick printing to check if the line tokenization is working correctly
                //System.out.println("Line strip is "+line.strip()+"\n");
                ArrayList<String> tokens=Scanner.lineTokenization(line.strip());
                //System.out.println(tokens);
                //tokenization working correctly
                for (String token: tokens){
                    if(Scanner.verifyTokenAsOperator(token) ||
                            (token.length()==1 && Scanner.verifyTokenAsSeparator(token.charAt(0))) ||
                            Scanner.verifyTokenAsReservedWord(token)) {
                        programInternalForm.add(token,new Pair(0,0));
                    }
                    /*else if(Scanner.verifyTokenAsConstant(token)){
                        programInternalForm.add("constant",newSymbolTable.retrievePositionOrAdd(token));
                    }
                    else if(Scanner.verifyTokenAsIdentifier(token)){
                        programInternalForm.add("identifier",newSymbolTable.retrievePositionOrAdd(token));
                    }*/
                    else if(finiteAutomataForIntegers.verifySequence(token)){//for integer constants
                        programInternalForm.add("constant",newSymbolTable.retrievePositionOrAdd(token));
                    }else if(Scanner.verifyTokenAsNonIntegerConstant(token)){
                        programInternalForm.add("constant",newSymbolTable.retrievePositionOrAdd(token));
                    }
                    else if(finiteAutomataForIdentifiers.verifySequence(token)){
                        programInternalForm.add("identifier",newSymbolTable.retrievePositionOrAdd(token));
                    }
                    else{
                        error.append("There is an error at line : "+count+": "+ token+ "\n");
                    }

                }
            }
            writerSymbolTable.write(newSymbolTable.toString());
            writerPIF.write(programInternalForm.toString());
            writerPIF.close();
            writerSymbolTable.close();
            if(error.length()!=0) {
                System.out.println(error);
            }
            else{
                System.out.println("The input program was found to be lexically correct.");
            }
            bufferedReader.close();
        } catch(IOException exception) {
            System.out.println(exception.getMessage());
        }

    }
}
