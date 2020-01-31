import java.io.File;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class Digraph {
	
	private final int v;
	private Bag<Integer>[] adj;
	private int E;
	
	public Digraph(int V) {
		v = V;
		E = 0;
		adj = (Bag<Integer>[]) new Bag[v];
		for (int i = 0; i < this.v; i ++) {
			adj[i] = new Bag<Integer>();
		}
	}
	
	public Digraph(In in) {
		this.v = in.readInt();
		int E = in.readInt();
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
	
	public void addEdge(int v, int w) {
		adj[v].add(w);
		this.E ++;
	}
	
	public int V() {
		return this.v;
	}
	
	public int E() {
		return E;
	}
	
	public Iterable<Integer> adj(int v){
		return adj[v];
	}
	
	public Digraph reverse() {
		Digraph reverse = new Digraph(this.V());
		for(int i = 0; i <  this.v; i ++) {
			for (int x : adj[i]) {
				reverse.addEdge(x, i);
			}
		}
		return reverse;
	}
	
	public static void main(String[] args) {
		File file = new File(args[0]);
		In in = new In(file);
		Digraph dg = new Digraph(in);
		System.out.println(dg.V());
		System.out.println(dg.E());
		Digraph rdg = dg.reverse();
		System.out.println(rdg.V());
		System.out.println(rdg.E());
		for(int x : rdg.adj[4]) {
			System.out.println(x);
		}
	}
}
