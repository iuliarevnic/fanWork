package model;

import javafx.util.Pair;

import java.util.ArrayList;

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
        //need to adapt this to include special characters
        return token.matches("^[0]|[+-]?[1-9][0-9]*|\"([a-zA-Z0-9])*\"|true|false|'[0-9a-zA-Z]']$");
    }

    private Pair<String,Integer> getOperatorToken(String line,Integer currentIndex){
        String token="";
        Integer start=currentIndex;
        while(currentIndex < line.length() && verifyOperatorContainingToken(line.charAt(currentIndex))){
            token+=line.charAt(currentIndex);
            currentIndex+=1;
        }
        if(verifyTokenAsOperator(token)){
            return new Pair(token,currentIndex);
        }
        else{
            return new Pair("",start);
        }
    }

    public ArrayList<String> lineTokenization(String line){

        String token="";
        Integer index=-1;
        ArrayList<String> tokenList=new ArrayList<>();
        while(index<line.length()){
            if(token.equals(" ")) {
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
                    index=operator.getValue()-1;
                    tokenList.add(operator.getKey());
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
