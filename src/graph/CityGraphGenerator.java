package graph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class CityGraphGenerator {

    public static CityGraph generateConnectedUndirected(int V, int E, long seed) {
        if (E < V - 1) {
            throw new IllegalArgumentException("A connected graph requires at least V - 1 edges.");
        }
        CityGraph graph = new CityGraph(V, false);

        Random rand = new Random(seed);
        Set<String> usedPairs = new HashSet<>();

        for (int i = 1; i < V; i++) {
            int target = rand.nextInt(i);
            double weight = 1 + rand.nextInt(100);
            graph.addRoad(target, i, weight);
            usedPairs.add(Math.min(target, i) + ":" + Math.max(target, i));
        }

        while (graph.getEdgesCount() < E) {
            int u = rand.nextInt(V);
            int v = rand.nextInt(V);
            if (u == v) continue;
            String key = Math.min(u, v) + ":" + Math.max(u, v);
            if (!usedPairs.contains(key)) {
                usedPairs.add(key);
                double weight = 1 + rand.nextInt(100);
                graph.addRoad(u, v, weight);
            }
        }
        return graph;
    }


    public static CityGraph generateDAG(int V, int E, long seed) {
        CityGraph graph = new CityGraph(V, true);
        Random rand = new Random(seed);
        Set<String> usedPairs = new HashSet<>();

        int maxPossibleEdges = V * (V - 1) / 2;
        int targetEdges = Math.min(E, maxPossibleEdges);

        while (graph.getEdgesCount() < targetEdges) {
            int a = rand.nextInt(V);
            int b = rand.nextInt(V);
            if (a == b) continue;
            int u = Math.min(a, b);
            int v = Math.max(a, b);
            String key = u + " >    " + v;
            if (!usedPairs.contains(key)) {
                usedPairs.add(key);
                double weight = 1 + rand.nextInt(100);
                graph.addRoad(u, v, weight);
            }
        }
        return graph;
    }


    public static CityGraph generateDirectedCyclic(int V, int E, long seed) {
        CityGraph graph = new CityGraph(V, true);
        Random rand = new Random(seed);
        Set<String> usedPairs = new HashSet<>();

        if (V >= 3 && E >= 3) {
            graph.addRoad(0, 1, 10);
            graph.addRoad(1, 2, 15);
            graph.addRoad(2, 0, 20);
            usedPairs.add("0 > 1");
            usedPairs.add("1 > 2");
            usedPairs.add("2 > 0");
        }

        while (graph.getEdgesCount() < E) {
            int u = rand.nextInt(V);
            int v = rand.nextInt(V);
            if (u == v) continue;
            String key = u + " > " + v;
            if (!usedPairs.contains(key)) {
                usedPairs.add(key);
                double weight = 1 + rand.nextInt(100);
                graph.addRoad(u, v, weight);
            }
        }
        return graph;
    }
}