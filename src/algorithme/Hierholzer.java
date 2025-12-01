package algorithme;
import graph.graph;
import graph.rue;
import java.util.*;

public class Hierholzer {
    public static List<Integer> circuitEulerien(graph g, int start) {
        int n = g.getNbcroisements();
        List<List<rue>> adj = g.getAdj();
        List<int[]> edges = new ArrayList<>();
        List<List<int[]>> incidence = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            incidence.add(new ArrayList<>());
        }
        boolean[][] dejaVu = new boolean[n][n];
        for (int u = 0; u < n; u++) {
            for (rue r : adj.get(u)) {
                int v = r.getDestination();
                if (!dejaVu[u][v]) {
                    dejaVu[u][v] = dejaVu[v][u] = true;
                    int id = edges.size();
                    edges.add(new int[]{u, v});
                    incidence.get(u).add(new int[]{id, v});
                    incidence.get(v).add(new int[]{id, u});
                }
            }
        }
        boolean[] used = new boolean[edges.size()];
        Stack<Integer> pile = new Stack<>();
        List<Integer> circuit = new ArrayList<>();
        pile.push(start);
        while (!pile.isEmpty()) {
            int u = pile.peek();
            List<int[]> liste = incidence.get(u);
            int i = 0;
            while (i < liste.size() && used[liste.get(i)[0]]) {
                i++;
            }

            if (i == liste.size()) {
                circuit.add(u);
                pile.pop();
            } else {
                int edgeId = liste.get(i)[0];
                int v = liste.get(i)[1];
                used[edgeId] = true;
                pile.push(v);
            }
        }
        Collections.reverse(circuit);
        return circuit;
    }
}
