import java.util.ArrayList;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    
    private static final int R = 256;
 
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        ArrayList<Character> sequence = new ArrayList<Character>();
        for (int i = 0; i < R; i++) {
            sequence.add((char)i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char input = BinaryStdIn.readChar();
            int index = sequence.indexOf(input);
            BinaryStdOut.write(index, 8);
            sequence.remove(index);
            sequence.add(0, input);
        }
        BinaryStdOut.close();
    }
    
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        ArrayList<Character> sequence = new ArrayList<Character>();
        for (int i = 0; i < R; i++) {
            sequence.add((char)i);
        }
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char input = sequence.get(index);
            BinaryStdOut.write(input);
            sequence.remove(index);
            sequence.add(0, input);
        }
        BinaryStdOut.close();
    }
    
    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
