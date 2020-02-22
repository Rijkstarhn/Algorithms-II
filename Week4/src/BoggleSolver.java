import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    
    private int score;
    private Queue<String> allWords;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        
    }
    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        allWords = new Queue<String>();
        int boardSize = board.cols() * board.rows();
        for (int i = 0; i < boardSize - 1; i++) {
            int row = i / board.cols(), col = i % board.cols();
            boolean[] marked = new boolean[boardSize];
            String[] pattern = new String[20];
            getValidWords(row, col, board.rows(), board.cols(), marked);
        }
        return allWords;
    }
    
    private void getValidWords(int row, int col, int rowSize, int colSize, boolean[] marked) {
        for (int i = row - 1; i <= row + 1; i++) {
            if (i < 0 || i >= rowSize) continue;
            for (int j = col - 1; j <= col + 1; j++) {
                if (j < 0 || j >= colSize) continue;
                if (i == row && j == col) continue;
                else {
                    // Process the valid words
                    
                    // If the sub-string can be found in the dictionary, then continue getting valid word
                    // If cannot, give up this way
                    
                }
            }
        }
    }
    
    private Iterable<Integer> adj(int row, int col, int rowSize, int colSize) {
        ArrayList<Integer> adj = new ArrayList<Integer>();
        for (int i = row - 1; i <= row + 1; i++) {
            if (i < 0 || i >= rowSize) continue;
            for (int j = col - 1; j <= col + 1; j++) {
                if (j < 0 || j >= colSize) continue;
                if (i != row && j != col) adj.add(i * colSize + j);
            }
        }
        return adj;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return score;
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        solver.getAllValidWords(board);
        
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
