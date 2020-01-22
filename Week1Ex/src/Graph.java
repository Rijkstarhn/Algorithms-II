import java.io.File;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class Graph {
	
	private final int v;
	private Bag<Integer>[] adj;
	private int E;
	
	Graph(int V){
		this.v = V;
		this.E = 0;
		adj = (Bag<Integer>[])new Bag[v];
		for (int i = 0; i < this.v; i ++) {
			adj[i] = new Bag<Integer>();
		}
	}
	
	Graph(In in){
		this.v = in.readInt();
		this.E = in.readInt();
		adj = (Bag<Integer>[])new Bag[v];
		for (int i = 0; i < this.v; i ++) {
			adj[i] = new Bag<Integer>();
		}
		while(!in.isEmpty()) {
			int v = in.readInt();
			int w = in.readInt();
			addEdge(v, w);
		}
	}
	
	public int V() {
		return this.v;
	}
	
	public int E() {
		for (int i = 0; i < this.v; i ++) {
			E += adj[i].size();
		}
		return E;
	}
	
	public void addEdge(int v, int w) {
		adj[v].add(w);
		adj[w].add(v);
	}
	
	public Iterable<Integer> adj(int v){
		return adj[v];
	}
	
	public static void main(String[] args) {
		File file = new File(args[0]);
		In in = new In(file);
		Graph g = new Graph(in);
		for (int x : g.adj(0)) {
			System.out.println(x);
		}
	}
}
