import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class FlowNetwork {
    
    private int V;
    private Bag<FlowEdge>[] adj;
    
    public FlowNetwork(int v) {
        V = v;
        adj = (Bag<FlowEdge>[])new Bag[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new Bag<FlowEdge>();
        }
    }
    
    public FlowNetwork(In in) {
        
    }
    
    public void addEdge(FlowEdge e) {
        int v = e.from(), w = e.to();
        adj[v].add(e);
        adj[w].add(e);
    }
    
    public Iterable<FlowEdge> adj(int v) {
        return adj[v];
    }
    
    public int V() {
        return V;
    }
    
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("cdsadsadasdasdasdadadadadasdad");
        
    }
}
