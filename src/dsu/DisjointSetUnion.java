package dsu;


public class DisjointSetUnion {
    private final int[] parent;
    private final int[] rank;
    private int componentsCount;

    private long findCalls = 0;
    private long unionCalls = 0;
    private long parentAccesses = 0;

    public DisjointSetUnion(int n) {
        this.parent = new int[n];
        this.rank = new int[n];
        this.componentsCount = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        findCalls++;
        parentAccesses++;
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
            parentAccesses++;
        }
        return parent[x];
    }

    public boolean union(int x, int y) {
        unionCalls++;
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        parentAccesses++;
        componentsCount--;
        return true;
    }

    public int getComponentsCount() {
        return componentsCount;
    }
    public long getFindCalls() {
        return findCalls;
    }


    public long getUnionCalls() {
        return unionCalls;
    }


    public long getTotalOperations() {
        return findCalls + unionCalls + parentAccesses;
    }

}