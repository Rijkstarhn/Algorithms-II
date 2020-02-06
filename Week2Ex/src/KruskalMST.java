import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.UF;

public class KruskalMST {
    
    private Queue<Edge> mst;
    
    public KruskalMST(EdgeWeightedGraph g) {
        mst = new Queue<Edge>();
        MinPQ<Edge> mpq = new MinPQ<Edge>();
        for (Edge edge : g.edges()) {
            mpq.insert(edge);
        }
        UF uf = new UF(g.V());
        while (!mpq.isEmpty() && mst.size() < g.V() - 1) {
            Edge edge = mpq.delMin();
            int v = edge.either(), w = edge.other(v);
            if (!uf.connected(v, w)) {
                mst.enqueue(edge);
                uf.union(v, w);
            }
            
        }
    }
    
    public Iterable<Edge> edges() {
        return mst;
    }
}
