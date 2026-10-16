package connectivity;

import graph.CityGraph;
import graph.RoadEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopologicalSorter {
    private static final int UNVISITED = 0;

    private static final int VISITING = 1;

    private static final int VISITED = 2;

    private final List<Integer> ordering = new ArrayList<>();
    private boolean hasCycle = false;
    private long edgesExamined = 0;
    private long verticesVisited = 0;

    public TopologicalSorter(CityGraph graph) {
        int V = graph.getVerticesCount();
        int[] state = new int[V];

        for (int i = 0; i < V; i++) {
            if (state[i] == UNVISITED) {
                dfs(graph, i, state);
                if (hasCycle) break;
            }
        }

        if (hasCycle) {
            ordering.clear();
        } else {
            Collections.reverse(ordering);
        }
    }

    private void dfs(CityGraph graph, int u, int[] state) {
        state[u] = VISITING;
        verticesVisited++;

        for (RoadEdge edge : graph.getNeighbors(u)) {
            edgesExamined++;
            int v = edge.v;
            if (state[v] == VISITING) {
                hasCycle = true;
                return;
            }
            if (state[v] == UNVISITED) {
                dfs(graph, v, state);
                if (hasCycle) return;
            }
        }

        state[u] = VISITED;
        ordering.add(u);
    }
    public boolean isDAG() {
        return !hasCycle;
    }
    public boolean hasCycle() {
        return hasCycle;
    }



    public List<Integer> getTopologicalOrder() {
        return hasCycle ? null : ordering;
    }

    public long getTotalOperations() {
        return verticesVisited + edgesExamined;
    }

}