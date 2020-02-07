import java.util.ArrayList;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class EdgeWeightedGraph {
    
    private int V, E;
    private Bag<Edge>[] adj;
    
    public EdgeWeightedGraph(In in) {
        V = in.readInt();
        E = in.readInt();
        adj = (Bag<Edge>[]) new Bag[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new Bag<Edge>();
        }
        while(!in.isEmpty()) {
            int v = in.readInt(), w = in.readInt();
            double weight = in.readDouble();
            Edge e = new Edge(v, w, weight);
            adj[v].add(e);
            adj[w].add(e);
        }
    }
    
    public EdgeWeightedGraph(int v) {
        this.V = v;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new Bag<Edge>();
        }
    }
    
    public void addEdge(Edge e) {
        int v = e.either(), w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
    }
    
    public Iterable<Edge> adj(int v){
        return adj[v];
    }
    
    public Iterable<Edge> edges() {
//        Bag<Edge> edgeBag = new Bag<Edge>();
        ArrayList<Edge> ae = new ArrayList<Edge>();
        for (Bag<Edge> b : adj) {
            for(Edge e : b) {
                if (!ae.contains(e)) ae.add(e);
            }
        }
        return ae;
    }
    
    public int V() {
        return V;
    }
    
    public static void main(String[] args) {
        Edge e1 = new Edge(0, 2, 0.5);
        int either = e1.either();
        System.out.println(either);
        System.out.println(e1.other(either));
        Edge e2 = new Edge(2, 1, 0.7);
        System.out.println(e2.compareTo(e1));
        EdgeWeightedGraph eg = new EdgeWeightedGraph(3);
        eg.addEdge(e1);
        eg.addEdge(e2);
        for (Edge edge : eg.adj(2)) {
            System.out.println(edge.either() + "->" + edge.other(edge.either()));
        }
        System.out.println("All edges in graph:");
        for (Edge edge : eg.edges()) {
            System.out.println(edge.either() + "->" + edge.other(edge.either()));
        }
    }
}
