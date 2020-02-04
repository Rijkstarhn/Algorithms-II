import java.io.File;
import java.util.ArrayList;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class SAP {
	
	private Digraph g;
	private int ancestor;
	private int length;
	
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if (G == null) throw new IllegalArgumentException();
		this.g = new Digraph(G);
		ancestor = -2;
		length = -2;
	}
	
	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		if (v >= this.g.V() || w >= this.g.V()) throw new IllegalArgumentException();
		if (this.length != -2) return this.length;
		calculateSingle(v, w);
		if (this.length == Integer.MAX_VALUE) return -1;
		return this.length;
	}
	
	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		if (v > this.g.V() || w > this.g.V()) throw new IllegalArgumentException();
		if (this.ancestor != -2) return this.ancestor;
		calculateSingle(v, w);
		if (this.length == Integer.MAX_VALUE) return -1;
		return this.ancestor;
	}
	
	private void calculateSingle(int v, int w) {
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.g, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.g, w);
		this.length = Integer.MAX_VALUE;
		int distance;
		for (int u = 0; u < this.g.V(); u ++) {
			if (bfsV.hasPathTo(u) && bfsW.hasPathTo(u)) {
				distance = bfsV.distTo(u) + bfsW.distTo(u);
				if (this.length > distance) {
					this.length = distance;
					this.ancestor = u;
				}
			}
		}
	}
	
	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (containNULL(v) || containNULL(w)) throw new IllegalArgumentException();
		if (this.length != -2) return this.length;
		calculateSet(v, w);
		if (this.length == Integer.MAX_VALUE) return -1;
		return this.length;
	}
	
	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (containNULL(v) || containNULL(w)) throw new IllegalArgumentException();
		if (this.ancestor != -2) return this.ancestor;
		calculateSet(v, w);
		if (this.length == Integer.MAX_VALUE) return -1;
		return this.ancestor;
	}
	
	private boolean containNULL(Iterable<Integer> v) {
		for (Integer x : v) {
			if (x == null) return true;
		}
		return false;
	}
	
	private void calculateSet(Iterable<Integer> v, Iterable<Integer> w) {
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.g, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.g, w);
		this.length = Integer.MAX_VALUE;
		int distance;
		for (int u = 0; u < this.g.V(); u ++) {
			if (bfsV.hasPathTo(u) && bfsW.hasPathTo(u)) {
				distance = bfsV.distTo(u) + bfsW.distTo(u);
				if (this.length > distance) {
					this.length = distance;
					this.ancestor = u;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		File file = new File(args[0]);
		In in = new In(file);
		Digraph g = new Digraph(in);
//		BreadthFirstDirectedPaths bfdp = new BreadthFirstDirectedPaths(g, 1);
//		System.out.println(bfdp.hasPathTo(6));
//		System.out.println(bfdp.distTo(6));
		SAP sap = new SAP(g);
//		int v = 1;
//		int u = 6;
//		System.out.println(sap.g.adj(6));
//		System.out.println(sap.length(v, u));
//		System.out.println(sap.ancestor(v,u));
		ArrayList<Integer> v = new ArrayList<Integer>();
//		v.add(12); v.add(9);
		v.add(1);
		ArrayList<Integer> u = new ArrayList<Integer>();
		u.add(6); 
//		u.add(7); u.add(3);
		System.out.println(sap.length(v, u));
		System.out.println(sap.ancestor(v, u));
	}
}
