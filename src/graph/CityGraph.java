package graph;

import java.util.ArrayList;
import java.util.List;


public class CityGraph {
    private final int verticesCount;

    private final boolean directed;
    private final List<List<RoadEdge>> adjList;
    private final List<RoadEdge> allEdges;

    public CityGraph(int verticesCount, boolean directed) {
        this.verticesCount = verticesCount;
        this.directed = directed;
        this.adjList = new ArrayList<>(verticesCount);
        for (int i = 0; i < verticesCount; i++) {
            adjList.add(new ArrayList<>());
        }

        this.allEdges = new ArrayList<>();
    }

    public void addRoad(int u, int v, double weight) {
        RoadEdge edge = new RoadEdge(u, v, weight);
        adjList.get(u).add(edge);
        allEdges.add(edge);
        if (!directed && u != v) {
            adjList.get(v).add(edge);
        }
    }

    public int getVerticesCount() {
        return verticesCount;
    }

    public int getEdgesCount() {
        return allEdges.size();
    }
    public boolean isDirected() {
        return directed;
    }

    public List<RoadEdge> getNeighbors(int u) {
        return adjList.get(u);
    }

    public List<RoadEdge> getAllEdges() {
        return allEdges;
    }


    public CityGraph buildTranspose() {
        CityGraph reversed = new CityGraph(verticesCount, true);
        for (RoadEdge e : allEdges) {
            reversed.addRoad(e.v, e.u, e.weight);
        }
        return reversed;
    }
}