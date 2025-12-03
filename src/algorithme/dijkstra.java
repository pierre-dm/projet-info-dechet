package algorithme;
import graph.rue;
import java.util.*;

public class dijkstra {
    public static Resultat executer(List<List<rue>> adj, int source) {
        int n = adj.size();
        int[] dist = new int[n];
        int[] parent = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.add(new int[]{0, source});

        while (!pq.isEmpty()) {
            int[] courant = pq.poll();
            int u = courant[1];
            if (visited[u]){
                continue;
            }
            visited[u] = true;
            for (rue rue : adj.get(u)) {
                int v = rue.getDestination();
                int w = rue.getDistance();
                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    parent[v] = u;
                    pq.add(new int[]{dist[v], v});
                }
            }
        }
        return new Resultat(dist, parent);
    }
}
