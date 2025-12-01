package algorithme;

public class Prim {
    public static int[] calculer(int[][] dist, int root) {
        int n = dist.length;
        int[] parent = new int[n];
        int[] key = new int[n];
        boolean[] inMST = new boolean[n];

        final int INF = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            key[i] = INF;
            parent[i] = -1;
        }
        key[root] = 0;

        for (int count = 0; count < n - 1; count++) {
            int u = -1;
            int min = INF;

            for (int v = 0; v < n; v++) {
                if (!inMST[v] && key[v] < min) {
                    min = key[v];
                    u = v;
                }
            }

            if (u == -1) break;

            inMST[u] = true;

            for (int v = 0; v < n; v++) {
                if (!inMST[v] && dist[u][v] < key[v]) {
                    key[v] = dist[u][v];
                    parent[v] = u;
                }
            }
        }

        return parent;
    }
}
