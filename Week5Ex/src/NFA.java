import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.Stack;

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
                if (re[v] == txt.charAt(i) || re[v] == '.') match.add(v+1);
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
        Digraph g = new Digraph(M+1);
        Stack<Integer> op = new Stack<Integer>();
        for (int i = 0; i < M; i++) {
            int lp = i;
//            if (re[i] >= 'A' && re[i] <= 'Z') g.addEdge(i, i+1);
            if (re[i] == '(' || re[i] == ')' || re[i] == '*') g.addEdge(i, i+1);
            if (re[i] == '(' || re[i] == '|') op.push(i);
            else if (re[i] == ')') {
                int x = op.pop();
                if (re[x] == '|') {
                    lp = op.pop();
                    g.addEdge(lp, x+1);
                    g.addEdge(x, i);
                }
                else {
                    lp = x;
                }
            }
            if (i < M-1 && re[i+1] == '*') {
                g.addEdge(lp, i+1);
                g.addEdge(i+1, lp);
            }
        }
        return g;
    }
    
    public static void main(String[] args) {
        String txt = "AAAAAAAAAAAAAAAAAABD";
        String pattern = "((A*B|AC)D)";
        NFA nfa = new NFA(pattern);
        System.out.println(nfa.recognizes(txt));
    }
}
