import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.TST;

public class LZW {
    
    private final int R = 256;
    private final int W = 12;
    private final int L = 4096; //L = 2^W
    
    public void compress() {
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        // record all the char in the radix
        for (int i = 0; i < R; i++) {
            st.put(""+(char)i, i);
        }
        int code = R+1;
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);
            BinaryStdOut.write(st.get(s), W);
            int t = s.length();
            if (t < input.length() && code < L) {
                st.put(input.substring(0, t+1), code+1);
            }
            input = input.substring(t);
        }
        BinaryStdOut.write(R,W);// Stop
        BinaryStdOut.close();
    }
}
