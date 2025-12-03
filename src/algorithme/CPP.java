package algorithme;
import graph.graph;
import graph.rue;
import java.util.*;

public class CPP {
    private static void chercherMatchingMin(List<Integer> impairs, int[][] dist, List<int[]> bestPairs, int[] bestCost, List<int[]> courantPairs, int courantCost) {
        if (impairs.isEmpty()) {
            if (courantCost < bestCost[0]) {
                bestCost[0] = courantCost;
                bestPairs.clear();
                bestPairs.addAll(courantPairs);
            }
            return;
        }
        int u = impairs.get(0);
        List<Integer> reste = impairs.subList(1, impairs.size());
        for (int i = 0; i < reste.size(); i++) {
            int v = reste.get(i);
            List<Integer> newImpairs = new ArrayList<>(reste);
            newImpairs.remove((Integer) v);
            int w = dist[u][v];
            courantPairs.add(new int[]{u, v});
            chercherMatchingMin(newImpairs, dist, bestPairs, bestCost, courantPairs, courantCost + w);
            courantPairs.remove(courantPairs.size() - 1);
        }
    }
    public static List<Integer> calculerCircuit(graph g, int idDepart) {
        int n = g.getNbcroisements();
        List<Integer> impairs = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            if (g.getDegre(u) % 2 != 0) {
                impairs.add(u);
            }
        }
        if (impairs.isEmpty()) {
            return Hierholzer.circuitEulerien(g, idDepart);
        }

        int[][] dist = new int[n][n];
        for (int u = 0; u < n; u++) {
            Resultat res = dijkstra.executer(g.getAdj(), u);
            dist[u] = res.dist;
        }
        List<int[]> bestPairs = new ArrayList<>();
        int[] bestCost = new int[]{Integer.MAX_VALUE};
        chercherMatchingMin(impairs, dist, bestPairs, bestCost, new ArrayList<>(), 0);
        List<int[]> edges = new ArrayList<>();
        List<List<int[]>> incidence = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            incidence.add(new ArrayList<>());
        }

        boolean[][] dejaVu = new boolean[n][n];
        for (int u = 0; u < n; u++) {
            for (rue r : g.getAdj().get(u)) {
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
        for (int[] pair : bestPairs) {
            int u = pair[0];
            int v = pair[1];
            Resultat res = dijkstra.executer(g.getAdj(), u);
            List<Integer> chemin = res.reconstruireChemin(v);
            for (int i = 0; i < chemin.size() - 1; i++) {
                int a = chemin.get(i);
                int b = chemin.get(i + 1);
                int id = edges.size();
                edges.add(new int[]{a, b});
                incidence.get(a).add(new int[]{id, b});
                incidence.get(b).add(new int[]{id, a});
            }
        }
        boolean[] used = new boolean[edges.size()];
        Stack<Integer> pile = new Stack<>();
        List<Integer> circuit = new ArrayList<>();
        pile.push(idDepart);
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
