import edu.princeton.cs.algs4.Queue;

public class FordFulkerson {
    
    private boolean[] marked;
    private FlowEdge[] edgeTo;
    private double value;
    
    public FordFulkerson(FlowNetwork G, int s, int t) {
        value = 0.0;
        while(hasAugmentingPath(G, s, t)) {
            double bottleNeck = Double.POSITIVE_INFINITY;
            // calculate the bottleneck capacity
            for (int i = t; i != s; i = edgeTo[i].other(i)) {
                bottleNeck = Math.min(bottleNeck, edgeTo[i].residualCapacityTo(i));
            }
            // increase the flow on the path with bottleneck capacity
            for (int i = t; i != s; i = edgeTo[i].other(i)) {
                edgeTo[i].addResidualFlowTo(i, bottleNeck); 
            }
            value += bottleNeck;
        }
    }
    
    private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
        edgeTo = new FlowEdge[G.V()];
        marked = new boolean[G.V()];
        Queue<Integer> queue = new Queue<Integer>();
        queue.enqueue(s);
        marked[s] = true;
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (FlowEdge e : G.adj(v)) {
                int w = e.other(v);
                if (e.residualCapacityTo(w) > 0 && !marked[w]) {
                    edgeTo[w] = e;
                    marked[w] = true;
                    queue.enqueue(w);
                }
            }
        }
        return marked[t];
    }
    
    public boolean inCut(int v) {
        return marked[v];
    }
    
    public double value() {
        return value;
    }
}
