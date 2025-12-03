package algorithme;
import graph.graph;
import java.util.*;

public class plusprochevoisin {
    public static List<Integer> calculer(graph g, List<Integer> points) {

        List<Integer> aVisiter = new ArrayList<>(points);
        int start = aVisiter.remove(0);  // CT

        List<Integer> tour = new ArrayList<>();
        tour.add(start);

        int courant = start;

        while (!aVisiter.isEmpty()) {

            Resultat res = dijkstra.executer(g.getAdj(), courant);

            int best = -1;
            int bestDist = Integer.MAX_VALUE;

            for (int v : aVisiter) {
                if (res.dist[v] < bestDist) {
                    bestDist = res.dist[v];
                    best = v;
                }
            }

            tour.add(best);
            aVisiter.remove(Integer.valueOf(best));
            courant = best;
        }

        // Retour au dépôt
        tour.add(start);

        return tour;
    }
}
