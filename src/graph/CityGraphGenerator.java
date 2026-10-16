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



}