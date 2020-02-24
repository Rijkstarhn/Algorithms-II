import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {
    
//    private int score;
    private int rowSize;
    private int colSize;
    private SeparateChainingHashST<String, Integer> allWords;
    private TST<Integer> dicTST = new TST<Integer>();
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        int i = 0;
        for (String s : dictionary) {
            dicTST.put(s, i++);
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
            char c = board.getLetter(row, col);
            if (c != 'Q') word.add(c);
            else { word.add('Q'); word.add('U');}
            getValidWords(board, row, col, marked, word);
        }
        return allWords.keys();
    }
    
    private void getValidWords(BoggleBoard board, int row, int col, boolean[] marked, ArrayList<Character> word) {
        for (int i = row - 1; i <= row + 1; i++) {
            if (i < 0 || i >= rowSize) continue;
            for (int j = col - 1; j <= col + 1; j++) {
                if (j < 0 || j >= colSize) continue;
                if (marked[i * colSize + j]) continue;
                else {
                    // Process the valid words
                    char c = board.getLetter(i, j);
                    if (c != 'Q') word.add(c);
                    else { word.add('Q'); word.add('U');}
                    String wordString = wordToString(word);
                    marked[i * colSize + j] = true;
                    // If the sub-string can be found in the dictionary, then continue getting valid word
                    // If cannot, give up this way
                    Queue<String> queue  = (Queue<String>)this.dicTST.keysWithPrefix(wordString);
                    if (!queue.isEmpty()) {
                        if (dicTST.contains(wordString) && !allWords.contains(wordString)) {
                            int wordLength = wordString.length();
                            if (wordLength < 3) ;
                            else if (wordLength >= 3 && wordLength <= 4) allWords.put(wordString, 1);
                            else if (wordLength == 5) allWords.put(wordString, 2);
                            else if (wordLength == 6) allWords.put(wordString, 3);
                            else if (wordLength == 7) allWords.put(wordString, 5);
                            else allWords.put(wordString, 11);
                            
                        }
                        getValidWords(board, i, j, marked, word);
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
        System.out.println(solver.scoreOf("PIGS"));
        BoggleBoard board = new BoggleBoard(args[1]);
        solver.getAllValidWords(board);
        
        int score = 0;
//        Out out = new Out("result777.txt");
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            if (!solver.dicTST.contains(word)) System.out.println(false);
//            out.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
//        out.close();
    }

}
