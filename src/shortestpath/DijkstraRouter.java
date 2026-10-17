package shortestpath;

import graph.CityGraph;
import graph.RoadEdge;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;


public class DijkstraRouter {
    private final int source;
    private final double[] distTo;
    private final int[] parentOf;

    private long edgesExamined = 0;
    private long relaxationsCount = 0;
    private long pqOperations = 0;

    private static class NodeDistance implements Comparable<NodeDistance> {
        final int vertex;
        final double dist;

        NodeDistance(int vertex, double dist) {
            this.vertex = vertex;
            this.dist = dist;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Double.compare(this.dist, other.dist);
        }
    }

    public DijkstraRouter(CityGraph graph, int source) {
        int V = graph.getVerticesCount();
        this.source = source;
        this.distTo = new double[V];
        this.parentOf = new int[V];


        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        Arrays.fill(parentOf, -1);
        distTo[source] = 0.0;


        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        pq.offer(new NodeDistance(source, 0.0));
        pqOperations++;


        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            pqOperations++;
            int u = current.vertex;

            if (current.dist > distTo[u]) {
                continue;
            }

            for (RoadEdge edge : graph.getNeighbors(u)) {
                if (edge.weight < 0) {
                    throw new IllegalArgumentException("Dijkstra's algorithm does not support negative edge weights!");
                }
                edgesExamined++;
                int v = edge.otherEnd(u);

                if (distTo[u] + edge.weight < distTo[v]) {
                    distTo[v] = distTo[u] + edge.weight;
                    parentOf[v] = u;
                    relaxationsCount++;
                    pq.offer(new NodeDistance(v, distTo[v]));
                    pqOperations++;
                }
            }
        }
    }

    public double getShortestDistance(int target) {
        return distTo[target];
    }

    public boolean isReachable(int target) {
        return distTo[target] != Double.POSITIVE_INFINITY;
    }


    public List<Integer> getPathTo(int target) {
        if (!isReachable(target)) {
            return null;
        }
        List<Integer> path = new ArrayList<>();
        for (int curr = target; curr != -1; curr = parentOf[curr]) {
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }

    public long getEdgesExamined() {
        return edgesExamined;
    }

    public long getRelaxationsCount() {
        return relaxationsCount;
    }

    public long getPqOperations() {
        return pqOperations;
    }
}