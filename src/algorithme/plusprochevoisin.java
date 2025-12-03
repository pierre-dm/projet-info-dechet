package algorithme;
import graph.graph;
import java.util.*;

public class plusprochevoisin {
    public static List<Integer> calculer(graph g, int start) {
        int n = g.getNbcroisements();
        boolean[] visite = new boolean[n];
        List<Integer> ordre = new ArrayList<>();

        int courant = start;
        visite[courant] = true;
        ordre.add(courant);

        for (int k = 1; k < n; k++) {
            Resultat res = dijkstra.executer(g.getAdj(), courant);
            int[] dist = res.dist;
            int best = Integer.MAX_VALUE;
            int next = -1;

            for (int v = 0; v < n; v++) {
                if (!visite[v] && dist[v] < best) {
                    best = dist[v];
                    next = v;
                }
            }
            if (next == -1){
                break;
            }
            visite[next] = true;
            ordre.add(next);
            courant = next;
        }
        return ordre;
    }
}
