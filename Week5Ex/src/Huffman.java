import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.MinPQ;
//import edu.princeton.cs.algs4.Out;

public class Huffman {
    
    private final static int R = 256;
//    private BinaryStdOut compression;
    
    public Huffman(int[] freq) {
        Node root = buildTrie(freq);
    }
    
    private static class Node implements Comparable<Node>{
        private final char ch;
        private final int freq;
        private final Node left, right;
        
        public Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }
        
        public boolean isLeaf() {
            return left == null && right == null;
        }
        
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }
    
    public void expand() {
        Node root = readTrie();
        int N = BinaryStdIn.readInt();
        for (int i = 0; i < N; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                if (!BinaryStdIn.readBoolean())
                    x = x.left;
                else 
                    x = x.right;
            }
            BinaryStdOut.write(x.ch, 8);
        }
        BinaryStdIn.close();
    }
    
    private static Node readTrie() {
        if (BinaryStdIn.readBoolean()) {
            char ch = BinaryStdIn.readChar(8);
            return new Node(ch, 0, null, null);
        }
        // if is an internal node
        Node x = readTrie();
        Node y = readTrie();
        return new Node('\0', 0, x, y);// both left and right are internal
    }
//    
//    public Out compress() {
//         
//    }
    
    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch, 8);
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(x.left);
        writeTrie(x.right);
    }
    
    private static Node buildTrie(int[] freq) {
        MinPQ<Node> pq = new MinPQ<Huffman.Node>();
        for (char i = 0; i < R; i++) {
            if (freq[i] > 0) 
                pq.insert(new Node(i, freq[i], null, null));
        }
        while (pq.size() > 1) {
            Node x = pq.delMin();
            Node y = pq.delMin();
            pq.insert(new Node('\0', x.freq + y.freq, x, y));
        }
        return pq.delMin();
    }
}
