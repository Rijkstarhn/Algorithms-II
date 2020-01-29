import java.io.File;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class BreadthFirstSearch {
	
	private boolean[] marked;
	private int[] edgeTo;
	private int[] distTo;
	
	public BreadthFirstSearch(Graph g, int s) {
		marked = new boolean[g.V()];
		edgeTo = new int[g.V()];
		distTo = new int[g.V()];
		bfs(g, s);
	}
	
	private void bfs(Graph g, int s) {
		Queue<Integer> q = new Queue<Integer>();
		q.enqueue(s);
		marked[s] = true;
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int x : g.adj(v)) {
				if (!marked[x]) {
					marked[x] = true;
					q.enqueue(x);
					edgeTo[x] = v;
					distTo[x] = distTo[v] + 1;
				}
			}
		}
	}
	
	public boolean hasPathTo(int v) {
		return marked[v];
	}
	 
	public int distTo(int v) {
		return distTo[v];
	}
	
	public Iterable<Integer> pathTo(int v) {
		if (!hasPathTo(v)) return null;
		Stack<Integer> path = new Stack<Integer>();
		int x;
		for (x = v; distTo[x] != 0; x = this.edgeTo[x]) {
			path.push(x);
		}
		path.push(x);
		return path;
	}
	
	public static void main(String[] args) {
		File file = new File(args[0]);
		In in = new In(file);
		Graph g = new Graph(in);
		BreadthFirstSearch bfs = new BreadthFirstSearch(g, 0);
		System.out.println(bfs.distTo[4]);
		System.out.println(bfs.hasPathTo(6));
 	}
}
