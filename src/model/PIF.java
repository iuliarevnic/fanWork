package model;

import javafx.util.Pair;

import java.util.LinkedList;

public class PIF {
    private LinkedList<Pair<String, Pair<Integer,Integer>>> programInternalForm;

    public PIF() {
        this.programInternalForm = new LinkedList<>();
    }

    public void add(String token,Pair<Integer,Integer> position)
    {
        Pair<String,Pair<Integer,Integer>> newToken=new Pair(token,position);
        programInternalForm.addLast(newToken);
    }

    @Override
    public String toString() {
        String result="PIF\n";
        for(Pair<String,Pair<Integer,Integer>> programInternalFormElement: programInternalForm){
            result+=programInternalFormElement+"\n";
        }
        return result;
    }




}
