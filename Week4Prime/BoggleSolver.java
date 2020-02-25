import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdOut;
//import edu.princeton.cs.algs4.TrieST.Node;
//import edu.princeton.cs.algs4.TrieST.Node;

public class BoggleSolver {
   
    private class Tries26<Value> {
        private static final int R = 26;        // UPPER CASE A-Z

        private Node root;      // root of trie
        private int n;          // number of keys in trie
        
        // R-way trie node
        private class Node {
            private Object val;
            private Node[] next = new Tries26.Node[R];
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
            if (val == null) delete(key);
            else root = put(root, key, val, 0);
        }

        private Node put(Node x, String key, Value val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (x.val == null) n++;
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[c - 65] = put(x.next[c - 65], key, val, d+1);
            return x;
        }
        
        /**
         * Removes the key from the set if the key is present.
         * @param key the key
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void delete(String key) {
            if (key == null) throw new IllegalArgumentException("argument to delete() is null");
            root = delete(root, key, 0);
        }

        private Node delete(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) {
                if (x.val != null) n--;
                x.val = null;
            }
            else {
                char c = key.charAt(d);
                x.next[c - 65] = delete(x.next[c - 65], key, d+1);
            }

            // remove subtrie rooted at x if it is completely empty
            if (x.val != null) return x;
            for (int c = 0; c < R; c++)
                if (x.next[c] != null)
                    return x;
            return null;
        }
    }
    
//    private int score;
    private int rowSize;
    private int colSize;
    private SeparateChainingHashST<String, Integer> allWords;
    private Tries26<Integer> dicTST = new Tries26<Integer>();
    private String[] dictionaryCopy;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dictionaryCopy = dictionary;
        int count = 0;
        for (String s : dictionaryCopy) {
            dicTST.put(s, count++);
        }
    }
    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        allWords = new SeparateChainingHashST<String, Integer>();
        rowSize = board.rows(); colSize = board.cols();
        int boardSize = colSize * rowSize;
        for (int i = 0; i < boardSize; i++) {
            int row = i / colSize, col = i % colSize;
            boolean[] marked = new boolean[boardSize];
            ArrayList<Character> word = new ArrayList<Character>();
            marked[row * colSize + col] = true;
            Tries26<Integer>.Node current = dicTST.get(dicTST.root, Character.toString(board.getLetter(row, col)), 0);
            if (current == null) continue;
            char c = board.getLetter(row, col);
            if (c != 'Q') word.add(c);
            else { 
                word.add('Q'); 
                word.add('U');
                current = current.next[20];
            }
            getValidWords(board, row, col, marked, word, current);
        }
        return allWords.keys();
    }
    
    private void getValidWords(BoggleBoard board, int row, int col, boolean[] marked, ArrayList<Character> word, Tries26<Integer>.Node current) {
        for (int i = row - 1; i <= row + 1; i++) {
            if (i < 0 || i >= rowSize) continue;
            for (int j = col - 1; j <= col + 1; j++) {
                if (j < 0 || j >= colSize) continue;
                if (marked[i * colSize + j]) continue;
                else {
                    // Process the valid words
                    char c = board.getLetter(i, j);
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
                    marked[i * colSize + j] = true;
                    // If the sub-string can be found in the dictionary, then continue getting valid word
                    // If cannot, give up this way
                    
                    if (next != null) {
                        if (next.val != null && !allWords.contains(wordString)) {
                            int wordLength = wordString.length();
                            if (wordLength < 3) ;
                            else if (wordLength >= 3 && wordLength <= 4) allWords.put(wordString, 1);
                            else if (wordLength == 5) allWords.put(wordString, 2);
                            else if (wordLength == 6) allWords.put(wordString, 3);
                            else if (wordLength == 7) allWords.put(wordString, 5);
                            else allWords.put(wordString, 11);
                            
                        }
                        getValidWords(board, i, j, marked, word, next);
                    }
                    else {
                        if (word.size() >= 2 && word.get(word.size()-1) == 'U' && word.get(word.size()-2) == 'Q') {
                            marked[i * colSize + j] = false;
                            word.remove(word.size()-1);
                            word.remove(word.size()-1);
                        }
                        else {
                            marked[i * colSize + j] = false;
                            word.remove(word.size()-1);
                        }
                        continue;
                    }
                }
            }
        }
        if (word.size() >= 2 && word.get(word.size()-1) == 'U' && word.get(word.size()-2) == 'Q') {
            marked[row * colSize + col] = false;
            word.remove(word.size()-1);
            word.remove(word.size()-1);
        }
        else {
            marked[row * colSize + col] = false;
            word.remove(word.size()-1);
        }
        return;
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
        if (allWords == null && dicTST.contains(word)) {
            
            if (word.length() < 3) return 0;
            else if (word.length() >= 3 && word.length() <= 4) return 1;
            else if (word.length() == 5) return 2;
            else if (word.length() == 6) return 3;
            else if (word.length() == 7) return 5;
            else return 11;
        }
        else if (allWords == null) return 0;
        return allWords.get(word);
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
//        solver.dicTST.put("PIA", 1);
//        System.out.println(solver.dicTST.keysWithPrefix("PI"));
        System.out.println(solver.dicTST.contains("PG"));
//        Queue<String> s = (Queue<String>)solver.dicTST.keysWithPrefix("PI");
//        for (String s : solver.dicTST.keysWithPrefix("PI")) {
//            System.out.println(s);
//        }
        System.out.println(solver.scoreOf("READ"));
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
    }

}


