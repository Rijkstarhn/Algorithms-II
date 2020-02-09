
public class Edge implements Comparable<Edge> {
    
    private final int v, w;
    private final double weight;
    
    public Edge (int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }
    
    public int either() {
        return this.v;
    }
    
    public int other(int v) {
        if (v == this.v) return this.w;
        else if (v == this.w) return this.v;
        else return -1;
    }
    
    public int compareTo(Edge that) {
        if (this.weight > that.weight) return 1;
        else if (this.weight < that.weight) return -1;
        else return 0;
    }
    
    public double weight() {
        return weight;
    }
    
    @Override
    public String toString() {
        String s = Integer.toString(v) + "->" + Integer.toString(w);
        return s;
    }
    
    public static void main(String[] args) {
        
    }
}
