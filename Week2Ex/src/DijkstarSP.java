import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Stack;

public class DijkstarSP {
    
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Double> pq;
    private final int start;
    
    public DijkstarSP (EdgeWeightedDigraph g, int s) {
        edgeTo = new DirectedEdge[g.V()];
        distTo = new double[g.V()];
        pq = new IndexMinPQ<Double>(g.V());
        start = s;
        for (int i = 0; i < distTo.length; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while(!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge edge : g.adj(v)) {
                relax(edge);
            }
        }
    }
    
    public Iterable<DirectedEdge> path(int d) {
        Stack<DirectedEdge> s = new Stack<DirectedEdge>();
        DirectedEdge e = edgeTo[d];
        while (e.from() != start) {
            s.push(e);
            e = edgeTo[e.from()];
        }
        s.push(e);
        return s;
    }
    
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else pq.insert(w, distTo[w]);
        }
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        DijkstarSP dsp = new DijkstarSP(G, 0);
        for(DirectedEdge e : dsp.path(1)) {
            System.out.println(e.toString());
        }
    }
}
