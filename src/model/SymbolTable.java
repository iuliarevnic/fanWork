package model;

import java.util.ArrayList;

import javafx.util.Pair;


public class SymbolTable {

    private ArrayList<Node<String>> chainList;//list of chains
    private int chainCount;//number of chains

    class Node<Key> {
        Key key;//inputToken
        Node<Key> next;//pointer to the next node

        public Node(Key key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", next=" + next +
                    '}';
        }
    }

    public SymbolTable() {
        chainList = new ArrayList<>();
        chainCount = 15;//initially start with 15 chains

        //initialize chainList with null values
        for (int i = 0; i < chainCount; i++)
            chainList.add(null);
    }


    public Pair<Integer, Integer> search(String inputToken) {
        int chainIndex = computeHashValue(inputToken);//identify the right chain
        Node<String> startNode = chainList.get(chainIndex);
        int position = 0;
        // Search key in chain
        while (startNode != null) {
            if (startNode.key.equals(inputToken))//inputToken found in the chain with index chainIndex, at position position
                return new Pair(chainIndex, position);
            startNode = startNode.next;
            position++;
        }
        return null;//the inputToken is not in the symbol table
    }


    private int computeHashValue(String inputToken) {
        int hashValue = 0;
        for (int characterIndex = 0; characterIndex < inputToken.length(); characterIndex++) {
            hashValue = hashValue + inputToken.charAt(characterIndex);//for each character, add its ASCII code

        }
        hashValue = hashValue % 256;// then do modulo 256
        return hashValue % chainCount;//we compute modulo chainCount to make sure the hashValue isn't greater
                                      // than the chain with the largest index
    }


    public Pair<Integer, Integer> add(String inputToken) {
        int chainIndex = computeHashValue(inputToken);//identify the right chain
        Node<String> startNode = chainList.get(chainIndex);
        int position = 0;
        if (startNode != null) {//chain is not null, we need to add a node at the end of the chain
            while (startNode.next != null) {
                startNode = startNode.next;
                position++;
            }
            Node<String> inputTokenNode = new Node<>(inputToken);
            startNode.next = inputTokenNode;
            position++;
            return new Pair(chainIndex, position);
        } else {//chain is null, inputToken becomes the startNode
            Node<String> inputTokenNode = new Node<>(inputToken);
            chainList.set(chainIndex, inputTokenNode);
            return new Pair(chainIndex, 0);//position is 0 because the inputToken is the startNode
        }

    }

    @Override
    public String toString() {
        String symbolTableString = "Symbol table\n";
        int chainIndex=0;
        for (Node<String> chain : chainList) {
            symbolTableString+="Chain number " + chainIndex + ": "+chain+"\n";
            chainIndex++;
        }
        return symbolTableString;
    }
}
