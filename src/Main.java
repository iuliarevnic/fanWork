import javafx.util.Pair;
import model.SymbolTable;

public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable=new SymbolTable();
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
    }
}
