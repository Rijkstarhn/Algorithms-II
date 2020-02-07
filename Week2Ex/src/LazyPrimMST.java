import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

public class LazyPrimMST {
    
    private boolean[] marked;
    private Queue<Edge> mst;
    private MinPQ<Edge> pq;
    
    public LazyPrimMST(EdgeWeightedGraph g) {
        marked = new boolean[g.V()];
        mst = new Queue<Edge>();
        pq = new MinPQ<Edge>();
        for (Edge e : g.adj(0)) {
            pq.insert(e);
        }
        while (!pq.isEmpty() && mst.size() < g.V() - 1) {
            Edge minEdge = pq.delMin();
            int v = minEdge.either(), w = minEdge.other(v);
            if (marked[v] && marked[w]) continue;
            else {
                mst.enqueue(minEdge);
                if (!marked[v]) visit(g, v);
                if (!marked[w]) visit(g, w);
            }
            
        }
    }
    
    private void visit(EdgeWeightedGraph g, int v) {
        marked[v] = true;
        for (Edge e : g.adj(v)) {
            if(!marked[e.other(v)]) pq.insert(e);
        }
    }
    
    public Iterable<Edge> mst() {
        return mst;
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph eg = new EdgeWeightedGraph(in);
        LazyPrimMST mst = new LazyPrimMST(eg);
        for (Edge edge : mst.mst()) {
            System.out.println(edge.either() + "->" + edge.other(edge.either()));
        }
    }
}
