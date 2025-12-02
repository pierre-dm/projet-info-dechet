package algorithme;
import graph.graph;
import graph.rue;
import graph.graph;

import java.util.*;


public class Coloration {

    public static int[] welshPowell(graph g) {
        int n = g.getNbcroisements();
        int[] couleur = new int[n];
        Arrays.fill(couleur, -1);

        // Trier les sommets par degré décroissant
        List<Integer> sommets = new ArrayList<>();
        for (int i = 0; i < n; i++) sommets.add(i);

        sommets.sort((a,b) -> g.getDegre(b) - g.getDegre(a));

        int currentColor = 0;

        for (int s : sommets) {
            if (couleur[s] != -1) continue;

            couleur[s] = currentColor;

            for (int t : sommets) {
                if (couleur[t] == -1 && !sontVoisins(g, s, t)) {
                    couleur[t] = currentColor;
                }
            }

            currentColor++;
        }

        return couleur;
    }

    private static boolean sontVoisins(graph g, int a, int b) {
        for (rue r : g.getAdj().get(a)) {
            if (r.getDestination() == b) return true;
        }
        return false;
    }

}
