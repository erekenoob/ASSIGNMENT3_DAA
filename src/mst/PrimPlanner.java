package mst;

import graph.CityGraph;
import graph.RoadEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PrimPlanner {
    private final List<RoadEdge> mstEdges = new ArrayList<>();
    private double totalWeight = 0.0;
    private boolean connected = false;

    private long edgesExamined = 0;
    private long comparisons = 0;
    private long pqOperations = 0;

    public PrimPlanner(CityGraph graph) {
        int V = graph.getVerticesCount();
        if (V <= 1) {
            connected = true;
            return;
        }

        boolean[] inTree = new boolean[V];
        PriorityQueue<RoadEdge> pq = new PriorityQueue<>((a, b) -> {
            comparisons++;
            return Double.compare(a.weight, b.weight);
        });

        includeDistrict(graph, 0, inTree, pq);

        while (!pq.isEmpty() && mstEdges.size() < V - 1) {
            RoadEdge cheapest = pq.poll();
            pqOperations++;
            edgesExamined++;

            int u = cheapest.u;
            int v = cheapest.v;

            if (inTree[u] && inTree[v]) {
                continue;
            }

            mstEdges.add(cheapest);
            totalWeight += cheapest.weight;

            int nextDistrict = inTree[u] ? v : u;
            includeDistrict(graph, nextDistrict, inTree, pq);
        }

        this.connected = (mstEdges.size() == V - 1);
    }

    private void includeDistrict(CityGraph graph, int u, boolean[] inTree, PriorityQueue<RoadEdge> pq) {
        inTree[u] = true;
        for (RoadEdge edge : graph.getNeighbors(u)) {
            edgesExamined++;
            int neighbor = edge.otherEnd(u);
            if (!inTree[neighbor]) {
                pq.offer(edge);
                pqOperations++;
            }
        }
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

    public long getPqOperations() {
        return pqOperations;
    }
}