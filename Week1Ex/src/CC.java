import java.io.File;

import edu.princeton.cs.algs4.In;

public class CC {
	
	private int[] id;
	private int count;
	private boolean[] marked;
	
	public CC(Graph g) {
		id = new int[g.V()];
		marked = new boolean[g.V()];
		for (int v = 0; v < g.V(); v++) {
			if (!marked[v]) {
				dfs(g, v);
				count ++;
			}
		}
	}
	
	public int count() {
		return count;
	}
	
	public int id(int v) {
		return id[v];
	}
	
	private void dfs(Graph g, int v) {
		this.marked[v] = true;
		this.id[v] = count;
		for (int w : g.adj(v)) {
			if (!marked[w]) {
				dfs(g, w);
			}
		}
	}
	
	public boolean areConnected(int v, int w) {
		return id[v] == id[w];
	}
	
	public static void main(String[] args) {
		File file = new File(args[0]);
		In in = new In(file);
		Graph g = new Graph(in);
		CC cc = new CC(g);
		System.out.println(cc.count());
		System.out.println(cc.id(9));
		System.out.println(cc.areConnected(1, 10));
	}
}
