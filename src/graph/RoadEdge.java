package graph;

public class RoadEdge implements Comparable<RoadEdge> {
    public final int u;
    public final int v;
    public final double weight;

    public RoadEdge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }
    public int otherEnd(int vertex) {
        if (vertex == u) return v;
        if (vertex == v) return u;
        throw new IllegalArgumentException("Invalid edge vertex");
    }


    @Override
    public int compareTo(RoadEdge other) {
        return Double.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return String.format("(%d - %d, cost: %.1f)", u, v, weight);
    }
}