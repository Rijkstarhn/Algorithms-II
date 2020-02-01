import java.io.File;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class SCC {
	
	private int count;
	private int[] id;
	private boolean[] marked;
	
	
	public SCC(Digraph g) {
		id = new int[g.V()];
		marked = new boolean[g.V()];
		scc(g);
	}
	
	private void scc(Digraph g) {
		DepthFirstOrder dfo = new DepthFirstOrder(g.reverse());
		for (int v : dfo.reversePost()) {
			if (!marked[v]) {
				dfs(g, v);
				count ++;
			}
		}
	}
	
	private void dfs(Digraph g, int v) {
		marked[v] = true;
		id[v] = count;
		for (int x : g.adj(v)) {
			if (!marked[x])
				dfs(g, x);
		}
	}
	
	public boolean stronglyConnected(int v, int w) {
		return id[v] == id[w];
	}
	
	public int count() {
		return count;
	}
	
	int id(int v) {
		return id[v];
	}
	
	public static void main(String[] args) {
		File file = new File(args[0]);
		In in = new In(file);
		Digraph dg = new Digraph(in);
		SCC scc = new SCC(dg);
		System.out.println(scc.count());
		System.out.println(scc.stronglyConnected(9, 6));
		
	}
}
