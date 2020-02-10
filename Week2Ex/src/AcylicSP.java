import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Topological;

public class AcylicSP
{
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private final int start;
    
    public AcylicSP(EdgeWeightedDigraph g, int s) 
    {
        edgeTo = new DirectedEdge[g.V()];
        distTo = new double[g.V()];
        start = s;
        for (int i = 0; i < g.V(); i++)
        {
            distTo[i] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;
        Topological t = new Topological(g);
        for (int v : t.order())
        {
            for (DirectedEdge e : g.adj(v))
            {
                relax(e);
            }
        }
    }
    
    private void relax(DirectedEdge e) 
    {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) 
        {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }       
    }
    
    public Iterable<DirectedEdge> path(int d) {
        Stack<DirectedEdge> s = new Stack<DirectedEdge>();
        DirectedEdge e = edgeTo[d];
        while (e.from() != start) {
            s.push(e);
            e = edgeTo[e.from()];
        }
        s.push(e);
        return s;
    }
    
    public double dist(int d) {
        return distTo[d];
    }
    
    public static void main(String[] args) 
    {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        AcylicSP acsp = new AcylicSP(G, 6);
        System.out.println(acsp.path(12));
    }
}
