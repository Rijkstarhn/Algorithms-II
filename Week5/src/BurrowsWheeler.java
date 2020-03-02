import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    private static final int R = 256;
    
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int length = csa.length();
        char[] t = new char[length];
        for (int i = 0; i < length; i++) {
            int pos = csa.index(i);
            t[i] = s.charAt((pos+length-1)%length);
            if (pos == 0) {
                t[i] = s.charAt(length-1);
                BinaryStdOut.write(i);
            }
        }
        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(t[i]);
        }
        BinaryStdOut.close();
    }
    
    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String input = BinaryStdIn.readString();
        int length = input.length();
        // construct the next[]
        int[] next = new int[length];
            // Use key-index to sort while get the next[]
        int[] count = new int[R+1];
        char[] tFirst = new char[length];
        for (int i = 0; i < length; i++) {
            count[input.charAt(i)+1]++;
        }
        for (int i = 0; i < R; i++) {
            count[i+1] += count[i];
        }
        for (int i = 0; i < length; i++) {
            int pos = count[input.charAt(i)]++;
            tFirst[pos] = input.charAt(i);
            next[pos] = i;
        }
        // Invert the message from next[], first, and t[]
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(tFirst[first]);
            first = next[first];
        }
        String original = sb.toString();
        BinaryStdOut.write(original);
        BinaryStdOut.close();
    }
    
    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
