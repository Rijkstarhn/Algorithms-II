import java.io.File;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class DepthFirstSearch {
	
	private final int s;
	private boolean[] marked;
	private int[] edgeTo;
	private int count;
	
	public DepthFirstSearch(Graph g, int s) {
		this.s = s;
		marked = new boolean[g.V()];
		edgeTo = new int[g.V()];
		count = 0;
		dfs(g,s);
	}
	
	private void dfs(Graph g, int v) {
		count ++;
		this.marked[v] = true;
		for (int w : g.adj(v)) {
			if (!this.marked(w)) {
				this.edgeTo[w] = v;
				dfs(g, w);
			}
		}
	}
	
	public boolean hasPathTo(int s) {
		return this.marked[s];
	}
	
	public Iterable<Integer> pathTo(int v){
		if (!hasPathTo(v)) return null;
		Stack<Integer> path = new Stack<Integer>();
		for (int x = v; x != s; x = this.edgeTo[x]) {
			path.push(x);
		}
		path.push(s);
		return path;
	}
	
	public boolean marked(int v) {
		return this.marked[v];
	}
	
	public int count() {
		return this.count;
	}
	
	public static void main(String[] args) {
//		Graph g = new Graph(6);
//		g.addEdge(0, 1);
//		g.addEdge(0, 3);
//		g.addEdge(1, 2);
//		g.addEdge(4, 5);
		File file = new File(args[0]);
		In in = new In(file);
		Graph g = new Graph(in);
		DepthFirstSearch dfs = new DepthFirstSearch(g, 0);
		System.out.println(dfs.marked(8));
		System.out.println(dfs.marked(6));
		System.out.println(dfs.hasPathTo(4));
		System.out.println(dfs.hasPathTo(11));
		System.out.println(dfs.pathTo(4));
		System.out.println(dfs.pathTo(12));
	}
}
