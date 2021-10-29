package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TokenList {
    private ArrayList<String> listOfOperators;
    private ArrayList<String> listOfSeparators;
    private ArrayList<String> listOfReservedWords;



    public TokenList() {
        this.listOfOperators=new ArrayList<>();
        this.listOfSeparators=new ArrayList<>();
        this.listOfReservedWords= new ArrayList<>();
        getListOfAcceptedTokens();
    }

    public ArrayList<String> getListOfOperators() {
        return listOfOperators;
    }

    public ArrayList<String> getListOfSeparators() {
        return listOfSeparators;
    }

    public ArrayList<String> getListOfReservedWords() {
        return listOfReservedWords;
    }

    public void getListOfAcceptedTokens(){
        String file = "C:\\Users\\iulia\\lftc\\src\\model\\listOfTokens.in";
        String line;
        int i=1;
        try {
            FileReader fileReader =
                    new FileReader(file);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //listOfTokens.in starts with the list of operators
                if(i < 16) {
                    listOfOperators.add(line.strip());
                }
                //next are the separators
                else if(i <=23){
                    if (line.strip().equals("space")){
                        listOfSeparators.add(" ");
                    }
                    else{
                        listOfSeparators.add(line.strip());
                    }
                }
                else{
                    listOfReservedWords.add(line.strip());
                }
                i++;
            }
            listOfReservedWords.add("identifier");
            bufferedReader.close();
        } catch(IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
