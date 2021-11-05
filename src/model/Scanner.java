package model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    TokenList tokenList;
    public Scanner() {
        tokenList=new TokenList();
    }



    public Boolean verifyTokenAsOperator(String token){
        for(String operator:tokenList.getListOfOperators()){
            if (operator.equals(token)){
                return true;
            }
        }
        return false;
    }

    private Boolean verifyOperatorContainingToken(char character){
        String string=Character.toString(character);
        for(String operator:tokenList.getListOfOperators()){
            if (operator.contains(string)){
                return true;
            }
        }
        return false;
    }

    public Boolean verifyTokenAsSeparator(Character character){
        for(String separator:tokenList.getListOfSeparators()){
            if (separator.charAt(0)==character){
                return true;
            }
        }
        return false;
    }

    private Boolean verifyReservedWordContainingToken(char character){
        String string=Character.toString(character);
        for(String operator:tokenList.getListOfReservedWords()){
            if (operator.contains(string)){
                return true;
            }
        }
        return false;
    }

    public Boolean verifyTokenAsReservedWord(String token){
        for(String reservedWord:tokenList.getListOfReservedWords()){
            if (reservedWord.equals(token)){
                return true;
            }
        }
        return false;
    }

    public Boolean verifyTokenAsIdentifier(String token) {
        //based on the specification of the language, an identifier is
        //identifier ::= letter {letter | digit}
        //letter ::= a|b|…|z|A|B|…|Z
        //digit ::= 0|1|2|…|9

        return token.matches("^[a-zA-Z][a-zA-Z0-9]{0,255}");
    }

    public Boolean verifyTokenAsConstant(String token){
        return token.matches("^[0]|[+-]?[1-9][0-9]*|\\\"([a-zA-Z0-9])*\\\"|true|false|'[0-9a-zA-Z]']$");
    }
    public Boolean verifyTokenAsNonIntegerConstant(String token){
        return token.matches("^\\\"([a-zA-Z0-9])*\\\"|true|false|'[0-9a-zA-Z]']$");
    }

    private Pair<String,Integer> getReservedWordToken(String line,Integer currentIndex){
        String token="";
        Integer start=currentIndex;
        while(currentIndex < line.length() && verifyReservedWordContainingToken(line.charAt(currentIndex))){
            token+=line.charAt(currentIndex);
            currentIndex+=1;
        }
        if(verifyTokenAsReservedWord(token)){
            return new Pair(token,currentIndex);
        }
        else{
            return new Pair("",start);
        }
    }

    private Pair<String,Integer> getIdentifierToken(String line,Integer currentIndex){
        String token="";
        Integer start=currentIndex;
        //we look ahead
        while(currentIndex < line.length() && verifyTokenAsIdentifier(token)){
            token+=line.charAt(currentIndex);
            currentIndex+=1;
        }
        //we verify the obtained operator against the list retrieved from listOfTokens.in
        if(verifyTokenAsIdentifier(token)){
            return new Pair(token,currentIndex);
        }
        else{
            return new Pair("",start);
        }
    }
    private Pair<String,Integer> getOperatorToken(String line,Integer currentIndex){
        //if we find part of an operator(only operators can contain multiple characters)
        //we get the whole operator
        String token="";
        Integer start=currentIndex;
        //we look ahead
        while(currentIndex < line.length() && verifyOperatorContainingToken(line.charAt(currentIndex))){
            token+=line.charAt(currentIndex);
            currentIndex+=1;
        }
        //we verify the obtained operator against the list retrieved from listOfTokens.in
        if(verifyTokenAsOperator(token)){
            return new Pair(token,currentIndex);
        }
        else{
            return new Pair("",start);
        }
    }

    public ArrayList<String> lineTokenization(String line){

        //we parse a line and split it into tokens
        String token="";//current token
        Integer index=-1;
        ArrayList<String> tokenList=new ArrayList<>();//list of all tokens in the given line
        while(index<line.length()){
            //System.out.println("index e "+index);
            if(token.equals(" ")) {
                //we ignore spaces
                token="";
            }
            else if(index!=-1 && verifyTokenAsReservedWord(String.valueOf(line.charAt(index)))){
                tokenList.add(String.valueOf(line.charAt(index)));//we add the operator to our tokenList
                token="";
            }else if(index!=-1 && verifyReservedWordContainingToken(line.charAt(index))){
                Pair<String,Integer> reservedWord;
                reservedWord=this.getReservedWordToken(line,index);
                if (!reservedWord.getKey().isEmpty()) {
                    if(!token.isEmpty() && token.length()!=1){
                        token=token.substring(0,token.length()-1);
                        tokenList.add(token);
                    }
                    index=reservedWord.getValue()-1;//we skip to the end of the operator
                    tokenList.add(reservedWord.getKey());//we add the operator to our tokenList
                    token="";
                }
            }else if(index!=-1 && ((String.valueOf(line.charAt(index)).equals("<") && String.valueOf(line.charAt(index+1)).equals("="))
                    || (String.valueOf(line.charAt(index)).equals("<") && String.valueOf(line.charAt(index+1)).equals("-")) ||
                    (String.valueOf(line.charAt(index)).equals(">") && String.valueOf(line.charAt(index+1)).equals("=")))){
                Pair<String,Integer> operator;
                operator=this.getOperatorToken(line,index);
                if (!operator.getKey().isEmpty()) {
                    if(!token.isEmpty() && token.length()!=1){
                        token=token.substring(0,token.length()-1);
                        tokenList.add(token);
                    }
                    index=operator.getValue()-1;//we skip to the end of the operator
                    tokenList.add(operator.getKey());//we add the operator to our tokenList
                    token="";
                }
            } else if(index!=-1 && verifyTokenAsOperator(String.valueOf(line.charAt(index)))){
                tokenList.add(String.valueOf(line.charAt(index)));//we add the operator to our tokenList
                token="";
            }
            else if(index!=-1 && verifyOperatorContainingToken(line.charAt(index))){
                Pair<String,Integer> operator;
                operator=this.getOperatorToken(line,index);
                if (!operator.getKey().isEmpty()) {
                    if(!token.isEmpty() && token.length()!=1){
                        token=token.substring(0,token.length()-1);
                        tokenList.add(token);
                    }
                    index=operator.getValue()-1;//we skip to the end of the operator
                    tokenList.add(operator.getKey());//we add the operator to our tokenList
                    token="";
                }
            }
            else if(index!=-1 && line.charAt(index) == '\n'){
                if(!token.isEmpty() && token.length()!=1){
                    token=token.substring(0,token.length()-1);
                    tokenList.add(token);
                }
                token="";
            }
            else if(index!=-1 && verifyTokenAsSeparator(line.charAt(index))){
                if(!token.isEmpty() && token.length()!=1) {
                    token=token.substring(0,token.length()-1);
                    tokenList.add(token);
                }
                if(line.charAt(index)!=' ') {
                    tokenList.add(Character.toString(line.charAt(index)));
                }
                token="";
            }
            index++;
            if(index<line.length()){
                token += line.charAt(index);
            }
        }
        if(!token.isEmpty() && !token.equals(" ")){
            tokenList.add(token);
        }
        return tokenList;

    }


}
