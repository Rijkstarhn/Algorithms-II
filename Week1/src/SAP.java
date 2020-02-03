import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

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
		if (this.length != -2) return this.length;
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.g, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.g, w);
		this.length = bfsV.distTo(0) + bfsW.distTo(0);
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
		return this.length;
	}
	
	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		if (this.ancestor != -2) return this.ancestor;
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.g, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.g, w);
		this.length = bfsV.distTo(0) + bfsW.distTo(0);
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
		return this.ancestor;
	}
	
	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (this.length != -2) return this.length;
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.g, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.g, w);
		this.length = bfsV.distTo(0) + bfsW.distTo(0);
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
		return this.length;
	}
	
	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (this.ancestor != -2) return this.ancestor;
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.g, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.g, w);
		this.length = bfsV.distTo(0) + bfsW.distTo(0);
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
		return this.ancestor;
	}
	
	public static void main(String[] args) {
		File file = new File(args[0]);
		In in = new In(file);
		Digraph g = new Digraph(in);
		SAP sap = new SAP(g);
//		int v = 21;
//		int u = 23;
//		System.out.println(sap.length(v, u));
//		System.out.println(sap.ancestor(v,u));
		ArrayList<Integer> v = new ArrayList<Integer>();
		v.add(13); v.add(23); v.add(24);
		ArrayList<Integer> u = new ArrayList<Integer>();
		u.add(6); u.add(16); u.add(17);
		System.out.println(sap.length(v, u));
		System.out.println(sap.ancestor(v, u));
	}
}
