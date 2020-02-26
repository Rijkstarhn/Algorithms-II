import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;

public class NFA {
    
    private char[] re;
    private Digraph g;
    private int M;
    
    public NFA(String regexp) {
        M = regexp.length();
        re = regexp.toCharArray();
        g = buildEpsilonTransitionDigraph();
    }
    
    public boolean recognizes(String txt) {
        // Get started from the 0 state
        Bag<Integer> pc = new Bag<Integer>();
        DirectedDFS dfs = new DirectedDFS(g, 0);
        for (int v = 0; v < g.V(); v++) {
            if (dfs.marked(v)) pc.add(v);
        }
        // Simulate the NFA
        for (int i = 0; i < txt.length(); i++) {
            Bag<Integer> match = new Bag<Integer>();
            for (int v : pc) {
                if (v == M) continue;
                if (re[v] == txt.charAt(i) || re[v] == '.') match.add(i+1);
            }
            dfs = new DirectedDFS(g, match);
            pc = new Bag<Integer>();
            for (int v = 0; v < g.V(); v++) {
                if (dfs.marked(v)) pc.add(v);
            }
        }
        for (int v : pc) {
            if (v == M) return true;
        }
        return false;
    }
    
    public Digraph buildEpsilonTransitionDigraph() {
        
    }
    
    public static void main(String[] args) {
        String txt = "GCGGCGTGTGTGCGAGAGAGTGGGTTTAAAGCTGGCGCGGAGGCGGCTGGCGCGGAGGCTG";
        String pattern = "GCGAGGCGGCTG";
        NFA nfa = new NFA(pattern);
        System.out.println(nfa.recognizes(txt));
    }
}
