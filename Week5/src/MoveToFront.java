import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    
    private static final int R = 256;
 
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] orderedSequence = new char[R];
        for (int i = 0; i < R; i++) {
            orderedSequence[i] = (char)i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char input = BinaryStdIn.readChar(8);
            for (int i = 0; i < R; i++) {
                if (orderedSequence[i] == input) {
                    BinaryStdOut.write(i);
                    exch(orderedSequence, i);
                }
            }
        }
        BinaryStdOut.close();
    }
    
    private static void exch(char[] orderedSequence, int i) {
        char exch = orderedSequence[i];
        orderedSequence[i] = orderedSequence[0];
        orderedSequence[0] = exch;
    }
    
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] orderedSequence = new char[R];
        for (int i = 0; i < R; i++) {
            orderedSequence[i] = (char)i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(8);
            BinaryStdOut.write(orderedSequence[index]);
            exch(orderedSequence, index);
        }
        BinaryStdOut.close();
    }
    
    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        encode();
        decode();
    }
}
