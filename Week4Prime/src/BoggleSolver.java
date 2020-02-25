import java.util.ArrayList;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
   
    private class Tries26<Value> {
        private static final int R = 26;        // UPPER CASE A-Z

        private Node root;      // root of trie
        private int n;          // number of keys in trie
        
        // R-way trie node
        private class Node {
            private Object val;
            private Node[] next = new Tries26.Node[R];
//            private char checked = 'n';
        }

       /**
         * Initializes an empty string symbol table.
         */
        public Tries26() {
        }
        
        /**
         * Returns the value associated with the given key.
         * @param key the key
         * @return the value associated with the given key if the key is in the symbol table
         *     and {@code null} if the key is not in the symbol table
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public Value get(String key) {
            if (key == null) throw new IllegalArgumentException("argument to get() is null");
            Node x = get(root, key, 0);
            if (x == null) return null;
            return (Value) x.val;
        }

        /**
         * Does this symbol table contain the given key?
         * @param key the key
         * @return {@code true} if this symbol table contains {@code key} and
         *     {@code false} otherwise
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public boolean contains(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            return get(key) != null;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c - 65], key, d+1);
        }
        
        /**
         * Inserts the key-value pair into the symbol table, overwriting the old value
         * with the new value if the key is already in the symbol table.
         * If the value is {@code null}, this effectively deletes the key from the symbol table.
         * @param key the key
         * @param val the value
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void put(String key, Value val) {
            if (key == null) throw new IllegalArgumentException("first argument to put() is null");
//            if (val == null) delete(key);
            else root = put(root, key, val, 0);
        }

        private Node put(Node x, String key, Value val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (x.val == null) n++;
                x.val = val;
                return x;
            }
            int c = key.charAt(d) - 'A';
            x.next[c] = put(x.next[c], key, val, d+1);
            return x;
        }
    }
    
    private int countCall;
    private int rowSize;
    private int colSize;
    private SeparateChainingHashST<String, Integer> allWords;
    private Tries26<Integer> dicTST = new Tries26<Integer>();
    private String[] dictionaryCopy;
    private Bag<Integer>[] adj;
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dictionaryCopy = dictionary;
        int score = 0;
        for (String s : dictionaryCopy) {
            if (s.length() < 3) score = 0;
            else if (s.length() >= 3 && s.length() <= 4) score = 1;
            else if (s.length() == 5) score = 2;
            else if (s.length() == 6) score = 3;
            else if (s.length() == 7) score = 5;
            else score = 11;
            dicTST.put(s, score);
        }
    }
    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        allWords = new SeparateChainingHashST<String, Integer>();
        rowSize = board.rows(); colSize = board.cols();
        int boardSize = colSize * rowSize;
        // Create a array form of board to cut down the time consuming
        adj = (Bag<Integer>[]) new Bag[rowSize*colSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                int v = i*colSize + j;
                adj[v] = new Bag<Integer>();
                if (checkWIsValid(i-1, j)) adj[v].add((i-1) * colSize + j);
                if (checkWIsValid(i+1, j)) adj[v].add((i+1) * colSize + j);
                if (checkWIsValid(i, j+1)) adj[v].add(i * colSize + j+1);
                if (checkWIsValid(i, j-1)) adj[v].add(i * colSize + j-1);
                if (checkWIsValid(i+1, j-1)) adj[v].add((i+1) * colSize + j-1);
                if (checkWIsValid(i+1, j+1)) adj[v].add((i+1) * colSize + j+1);
                if (checkWIsValid(i-1, j-1)) adj[v].add((i-1) * colSize + j-1);
                if (checkWIsValid(i-1, j+1)) adj[v].add((i-1) * colSize + j+1);
            }
        }
        for (int i = 0; i < boardSize; i++) {
            int row = i / colSize, col = i % colSize;
            boolean[] marked = new boolean[boardSize];
            ArrayList<Character> word = new ArrayList<Character>();
            marked[i] = true;
            Tries26<Integer>.Node current = dicTST.get(dicTST.root, Character.toString(board.getLetter(row, col)), 0);
            if (current == null) continue;
            char c = board.getLetter(row, col);
            if (c != 'Q') word.add(c);
            else { 
                word.add('Q'); 
                word.add('U');
                current = current.next[20];
            }
            getValidWords(board, i, marked, word, current);
        }
        return allWords.keys();
    }
    
    private boolean checkWIsValid(int i, int j) {
        return i >= 0 && i < rowSize && j >= 0 && j< colSize;
    }
    
    private void getValidWords(BoggleBoard board, int v, boolean[] marked, ArrayList<Character> word, Tries26<Integer>.Node current) {
        countCall ++;
        for (int w : adj[v]) {
            if (marked[w]) continue;
            char c = board.getLetter(w / colSize, w % colSize);
            Tries26<Integer>.Node next;
            if (current.next != null) {
                next = current.next[c-65];
            }
            else next = null;
            if (c != 'Q') word.add(c);
            else { 
                word.add('Q'); 
                word.add('U');
                if (next != null) next = next.next[20];// 'U' is the 21th letter in Alpha
            }
            String wordString = wordToString(word);
            marked[w] = true;
            if (next != null) {
                if (next.val != null && wordString.length() >= 3) allWords.put(wordString, 0);
                getValidWords(board, w, marked, word, next);
                removeTheLastLetter(w, marked, word);
            }
            else {
                removeTheLastLetter(w, marked, word);
            }
        }
    }
    
    private String wordToString(ArrayList<Character> word) {
        StringBuilder stringWord = new StringBuilder();
        for (char c : word) {
            stringWord.append(c);
        }
        return stringWord.toString();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dicTST.contains(word)) return 0;
        return dicTST.get(word);
    }
    
    private void removeTheLastLetter(int w, boolean[] marked, ArrayList<Character> word) {
        if (word.size() >= 2 && word.get(word.size()-1) == 'U' && word.get(word.size()-2) == 'Q') {
            marked[w] = false;
            word.remove(word.size()-1);
            word.remove(word.size()-1);
        }
        else {
            marked[w] = false;
            word.remove(word.size()-1);
        }
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        System.out.println(solver.dicTST.contains("PG"));
        System.out.println(solver.scoreOf("OQUS"));
        BoggleBoard board = new BoggleBoard(args[1]);
        solver.getAllValidWords(board);
        
        int score = 0;
//        Out out = new Out("result777.txt");
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
//            if (!solver.dicTST.contains(word)) System.out.println(false);
//            out.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
//        out.close();
        System.out.println(solver.countCall);
    }

}


