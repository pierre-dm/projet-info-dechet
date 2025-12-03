package algorithme;

import graph.graph;
import graph.rue;
import java.util.*;

public class Coloration {

    public static int[] welshPowell(graph g) {
        int n = g.getNbcroisements();
        int[] couleur = new int[n];
        Arrays.fill(couleur, -1);

        List<Integer> sommets = new ArrayList<>();
        for (int i = 0; i < n; i++) sommets.add(i);

        sommets.sort((a, b) -> g.getDegre(b) - g.getDegre(a));

        int currentColor = 0;

        for (int v : sommets) {
            if (couleur[v] != -1) continue;

            couleur[v] = currentColor;

            for (int u : sommets) {
                if (couleur[u] == -1 && peutRecevoirCouleur(g, u, couleur, currentColor)) {
                    couleur[u] = currentColor;
                }
            }

            currentColor++;
        }

        Map<Integer, Integer> freq = new HashMap<>();
        for (int c = 0; c < currentColor; c++) {
            freq.put(c, 0);
        }
        for (int c : couleur) {
            freq.put(c, freq.get(c) + 1);
        }
        List<Integer> couleursTriees = new ArrayList<>(freq.keySet());
        couleursTriees.sort((a, b) -> freq.get(b) - freq.get(a));

        int[] newIndex = new int[currentColor];
        for (int i = 0; i < couleursTriees.size(); i++) {
            int oldColor = couleursTriees.get(i);
            newIndex[oldColor] = i;
        }
        for (int i = 0; i < n; i++) {
            couleur[i] = newIndex[couleur[i]];
        }
        return couleur;
    }

    private static boolean peutRecevoirCouleur(graph g, int s, int[] couleur, int c) {
        for (rue r : g.getAdj().get(s)) {
            int voisin = r.getDestination();
            if (couleur[voisin] == c) {
                return false;
            }
        }
        return true;
    }
}
