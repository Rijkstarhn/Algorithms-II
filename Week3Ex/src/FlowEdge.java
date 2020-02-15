
public class FlowEdge {
    private final int v, w;
    private final double capacity;
    private double flow;
    
    public FlowEdge(int v, int w, double capacity) {
        this.v = v;
        this.w = w;
        this.capacity = capacity;
    }
    
    public int from() {
        return v;
    }
    
    public int to() {
        return w;
    }
    
    public int other(int v) {
        if (v == this.v) return this.w;
        else if (v == this.w) return this.v;
        else throw new RuntimeException("Illegal endpoint");
    }
    
    public double capacity() {
        return capacity;
    }
    
    public double flow() {
        return flow;
    }
    
    public double residualCapacityTo(int v) {
        if (v == this.v) return flow;
        else if (v == w) return capacity - flow;
        else throw new IllegalArgumentException();
    }
    
    public void addResidualFlowTo(int v, double delta) {
        if (v == this.v) flow -= delta;
        else if (v == w) flow += delta;
        else throw new IllegalArgumentException();
    }
    
    public static void main(String[] args) {
        FlowEdge fe = new FlowEdge(3, 2, 5.0);
        System.out.println("from:" + fe.from() + " " + "to:" + fe.to());
        int v = fe.from();
        System.out.println("other v:" + fe.other(v));
        fe.addResidualFlowTo(v, 0.5);
        System.out.println("residual capacity to:" + fe.residualCapacityTo(v));
        System.out.println("flow: " + fe.flow());
    }
}
