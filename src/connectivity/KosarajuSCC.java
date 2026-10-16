package connectivity;
import graph.CityGraph;
import graph.RoadEdge;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class KosarajuSCC {
    private final int[] componentId;
    private final List<List<Integer>> components = new ArrayList<>();
    private long operationsCount = 0;

    public KosarajuSCC(CityGraph graph) {
        int V = graph.getVerticesCount();
        this.componentId = new int[V];
        boolean[] visited = new boolean[V];
        Deque<Integer> finishStack = new ArrayDeque<>();

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                fillFinishOrder(graph, i, visited, finishStack);
            }
        }

        CityGraph transposed = graph.buildTranspose();
        operationsCount += graph.getEdgesCount();

        boolean[] visitedPass2 = new boolean[V];
        while (!finishStack.isEmpty()) {
            int v = finishStack.pop();
            if (!visitedPass2[v]) {
                List<Integer> currentComponent = new ArrayList<>();
                int currentCompIndex = components.size();
                collectComponent(transposed, v, visitedPass2, currentCompIndex, currentComponent);
                components.add(currentComponent);
            }
        }
    }

    private void fillFinishOrder(CityGraph graph, int u, boolean[] visited, Deque<Integer> stack) {
        visited[u] = true;
        operationsCount++;
        for (RoadEdge edge : graph.getNeighbors(u)) {
            operationsCount++;
            if (!visited[edge.v]) {
                fillFinishOrder(graph, edge.v, visited, stack);
            }
        }
        stack.push(u);
    }

    private void collectComponent(CityGraph transposed, int u, boolean[] visited, int compIdx, List<Integer> compList) {
        visited[u] = true;
        componentId[u] = compIdx;
        compList.add(u);
        operationsCount++;

        for (RoadEdge edge : transposed.getNeighbors(u)) {
            operationsCount++;
            if (!visited[edge.v]) {
                collectComponent(transposed, edge.v, visited, compIdx, compList);
            }
        }
    }

    public int getComponentsCount() {
        return components.size();
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    public int getComponentId(int vertex) {
        return componentId[vertex];
    }

    public boolean areStronglyConnected(int u, int v) {
        return componentId[u] == componentId[v];
    }

    public long getOperationsCount() {
        return operationsCount;
    }
}