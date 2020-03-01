import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    
    private static int first;
    private static char[] t;
    private static int[] next;
    private static final int R = 256;
    
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(BinaryStdIn.readString());
//        String s = "ABRACADABRA!";
//        CircularSuffixArray csa = new CircularSuffixArray(s);
        int length = csa.length();
        t = new char[length];
        for (int i = 0; i < length; i++) {
            int pos = csa.index(i);
            t[i] = s.charAt((pos+length-1)%length);
            if (pos == 0) {
                first = i;
                t[i] = s.charAt(length-1);
                BinaryStdOut.write(first);
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
        first = BinaryStdIn.readInt();
        String input = BinaryStdIn.readString();
        first = 3;
//        String input = "ARD!RCAAAABB";
        int length = input.length();
//        for (int i = 0; i < length; i++) {
//            t[i] = input.charAt(i);
//        }
        // construct the next[]
        next = new int[length];
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
        BurrowsWheeler bw = new BurrowsWheeler();
        bw.transform();
        bw.inverseTransform();
    }
}
