import connectivity.KosarajuSCC;
import connectivity.TopologicalSorter;
import graph.CityGraph;
import graph.CityGraphGenerator;
import mst.KruskalPlanner;
import mst.PrimPlanner;
import shortestpath.DijkstraRouter;

public class MainApp {

    public static void main(String[] args) {
        System.out.println("  SMART CITY INFRASTRUCTURE & ROUTE PLANNING — TEST SUITE ");

        runAllCorrectnessTests();

        System.out.println("  EXPERIMENTAL BENCHMARKING (AVERAGE OF 5 RUNS, SEED=42)  ");

        runBenchmarks();
    }


    private static void runAllCorrectnessTests() {
        CityGraph smallUndir = new CityGraph(4, false);
        smallUndir.addRoad(0, 1, 10);
        smallUndir.addRoad(0, 2, 6);
        smallUndir.addRoad(0, 3, 5);
        smallUndir.addRoad(1, 3, 15);
        smallUndir.addRoad(2, 3, 4);
        KruskalPlanner kruskal1 = new KruskalPlanner(smallUndir);
        PrimPlanner prim1 = new PrimPlanner(smallUndir);
        System.out.printf("Test 1: Small Graph| Kruskal MST Weight = %.1f, Prim MST Weight = %.1f (Match: %b)%n",
                kruskal1.getTotalWeight(), prim1.getTotalWeight(),
                Math.abs(kruskal1.getTotalWeight() - prim1.getTotalWeight()) < 1e-6);

        CityGraph disconnected = new CityGraph(4, false);
        disconnected.addRoad(0, 1, 5);
        disconnected.addRoad(2, 3, 8);
        KruskalPlanner kruskal2 = new KruskalPlanner(disconnected);
        PrimPlanner prim2 = new PrimPlanner(disconnected);
        System.out.printf("Test 2: Disconnected Graph| Kruskal Connected = %b, Prim Connected = %b%n",
                kruskal2.isConnected(), prim2.isConnected());

        CityGraph cyclicGraph = new CityGraph(4, true);
        cyclicGraph.addRoad(0, 1, 1);
        cyclicGraph.addRoad(1, 2, 1);
        cyclicGraph.addRoad(2, 0, 1);
        cyclicGraph.addRoad(2, 3, 1);
        TopologicalSorter topoCyclic = new TopologicalSorter(cyclicGraph);
        KosarajuSCC sccCyclic = new KosarajuSCC(cyclicGraph);
        System.out.printf("Test 3: Directed Cyclez| Cycle Detected = %b, SCC Count = %d, Components: %s%n",
                topoCyclic.hasCycle(), sccCyclic.getComponentsCount(), sccCyclic.getComponents());

        CityGraph multiMst = new CityGraph(4, false);
        multiMst.addRoad(0, 1, 5);
        multiMst.addRoad(1, 2, 5);
        multiMst.addRoad(2, 3, 5);
        multiMst.addRoad(3, 0, 5);
        multiMst.addRoad(0, 2, 5);
        KruskalPlanner kMulti = new KruskalPlanner(multiMst);
        PrimPlanner pMulti = new PrimPlanner(multiMst);
        System.out.printf("Test 4 & 6: Multiple MSTs & Duplicate Weights| Kruskal Weight = %.1f, Prim Weight = %.1f%n",
                kMulti.getTotalWeight(), pMulti.getTotalWeight());

        CityGraph spGraph = new CityGraph(5, true);
        spGraph.addRoad(0, 1, 4);
        spGraph.addRoad(1, 2, 3);
        spGraph.addRoad(0, 2, 10);
        spGraph.addRoad(3, 4, 2);
        DijkstraRouter router = new DijkstraRouter(spGraph, 0);
        System.out.printf("Test 5: Dijkstra & Unreachable Districts| Path 0->2: %s (dist=%.1f), District 4 Reachable: %b (dist=%s)%n",
                router.getPathTo(2), router.getShortestDistance(2),
                router.isReachable(4), router.getShortestDistance(4));

        CityGraph singleNode = new CityGraph(1, true);
        KruskalPlanner kSingle = new KruskalPlanner(singleNode);
        TopologicalSorter tSingle = new TopologicalSorter(singleNode);
        KosarajuSCC sccSingle = new KosarajuSCC(singleNode);
        DijkstraRouter dSingle = new DijkstraRouter(singleNode, 0);
        System.out.printf("Test 7: Single Vertex (V=1)| MST Connected=%b (weight=%.1f), TopoOrder=%s, SCC=%s, Dijkstra dist(0)=%.1f%n",
                kSingle.isConnected(), kSingle.getTotalWeight(),
                tSingle.getTopologicalOrder(), sccSingle.getComponents(), dSingle.getShortestDistance(0));
    }


    private static void runBenchmarks() {
        String[] names = {"Small", "Medium", "Large"};
        int[] V_sizes = {10, 100, 1000};
        int[] E_sizes = {30, 500, 5000};
        int runs = 5;
        long seed = 42L;

        CityGraph warmupG = CityGraphGenerator.generateConnectedUndirected(100, 500, seed);
        new KruskalPlanner(warmupG);
        new PrimPlanner(warmupG);

        System.out.println("TABLE 1 - Kruskal vs. Prim Comparison (Connected Undirected Graphs)");
        System.out.printf("%-8s | %-6s | %-6s | %-14s | %-12s | %-12s | %-14s | %-12s | %-12s%n",
                "Size", "V", "E", "Kruskal ", "Kruskal Comp", "DSU Ops", "Prim ", "Prim Comp", "Weight Match");

        for (int i = 0; i < 3; i++) {
            int V = V_sizes[i];
            int E = E_sizes[i];
            CityGraph g = CityGraphGenerator.generateConnectedUndirected(V, E, seed);

            long kTimeSum = 0, pTimeSum = 0;
            KruskalPlanner kRes = null;
            PrimPlanner pRes = null;

            for (int r = 0; r < runs; r++) {
                long t1 = System.nanoTime();
                kRes = new KruskalPlanner(g);
                long t2 = System.nanoTime();
                kTimeSum += (t2 - t1);

                long t3 = System.nanoTime();
                pRes = new PrimPlanner(g);
                long t4 = System.nanoTime();
                pTimeSum += (t4 - t3);
            }

            double kAvgMs = (kTimeSum / (double) runs) / 1_000_000.0;
            double pAvgMs = (pTimeSum / (double) runs) / 1_000_000.0;
            boolean match = Math.abs(kRes.getTotalWeight() - pRes.getTotalWeight()) < 1e-6;

            System.out.printf("%-8s | %-6d | %-6d | %-14.4f | %-12d | %-12d | %-14.4f | %-12d | %-12b%n",
                    names[i], V, E, kAvgMs, kRes.getComparisons(), kRes.getDsuOperations(),
                    pAvgMs, pRes.getComparisons(), match);
        }

        System.out.println("\nTABLE 2: Topological Sort, Kosaraju SCC, and Dijkstra (Directed Graphs)");
        System.out.printf("%-8s | %-6s | %-6s | %-13s | %-13s | %-13s | %-10s | %-14s | %-12s%n",
                "Size", "V", "E", "Topo DAG ", "Topo Cyc ", "SCC Time ", "SCC Count", "Dijkstra ", "Relaxations");

        for (int i = 0; i < 3; i++) {
            int V = V_sizes[i];
            int E = E_sizes[i];
            CityGraph dag = CityGraphGenerator.generateDAG(V, E, seed);
            CityGraph cyclic = CityGraphGenerator.generateDirectedCyclic(V, E, seed);

            long topoDagTime = 0, topoCycTime = 0, sccTime = 0, dijkstraTime = 0;
            KosarajuSCC sccRes = null;
            DijkstraRouter dijkstraRes = null;

            for (int r = 0; r < runs; r++) {
                long t1 = System.nanoTime();
                new TopologicalSorter(dag);
                topoDagTime += (System.nanoTime() - t1);

                long t2 = System.nanoTime();
                new TopologicalSorter(cyclic);
                topoCycTime += (System.nanoTime() - t2);

                long t3 = System.nanoTime();
                sccRes = new KosarajuSCC(cyclic);
                sccTime += (System.nanoTime() - t3);

                long t4 = System.nanoTime();
                dijkstraRes = new DijkstraRouter(cyclic, 0);
                dijkstraTime += (System.nanoTime() - t4);
            }

            System.out.printf("%-8s | %-6d | %-6d | %-13.4f | %-13.4f | %-13.4f | %-10d | %-14.4f | %-12d%n",
                    names[i], V, E,
                    (topoDagTime / (double) runs) / 1_000_000.0,
                    (topoCycTime / (double) runs) / 1_000_000.0,
                    (sccTime / (double) runs) / 1_000_000.0,
                    sccRes.getComponentsCount(),
                    (dijkstraTime / (double) runs) / 1_000_000.0,
                    dijkstraRes.getRelaxationsCount());
        }
    }
}