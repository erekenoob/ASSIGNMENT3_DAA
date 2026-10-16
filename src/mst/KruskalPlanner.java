package mst;
import dsu.DisjointSetUnion;
import graph.CityGraph;
import graph.RoadEdge;
import java.util.ArrayList;
import java.util.List;


public class KruskalPlanner {

    private final List<RoadEdge> mstEdges = new ArrayList<>();
    private double totalWeight = 0.0;
    private boolean connected = false;

    private long edgesExamined = 0;

    private long comparisons = 0;
    private long dsuOperations = 0;

    public KruskalPlanner(CityGraph graph) {
        int V = graph.getVerticesCount();
        if (V == 0) {
            connected = true;
            return;
        }
        if (V == 1) {
            connected = true;
            return;
        }

        List<RoadEdge> sortedEdges = new ArrayList<>(graph.getAllEdges());
        sortedEdges.sort((a, b) -> {
            comparisons++;
            return Double.compare(a.weight, b.weight);
        });

        DisjointSetUnion dsu = new DisjointSetUnion(V);

        for (RoadEdge edge : sortedEdges) {
            edgesExamined++;
            if (dsu.union(edge.u, edge.v)) {
                mstEdges.add(edge);
                totalWeight += edge.weight;
                if (mstEdges.size() == V - 1) {
                    break;
                }
            }
        }

        this.dsuOperations = dsu.getTotalOperations();
        this.connected = (mstEdges.size() == V - 1);
    }

    public List<RoadEdge> getMstEdges() {
        return mstEdges;
    }
    public double getTotalWeight() {
        return totalWeight;
    }

    public boolean isConnected() {
        return connected;
    }


    public long getEdgesExamined() {
        return edgesExamined;
    }



    public long getComparisons() {
        return comparisons;
    }
    public long getDsuOperations() {
        return dsuOperations;
    }
}