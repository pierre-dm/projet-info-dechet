package algorithme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Resultat{
    public final int[] dist;
    public final int[] parent;

    public Resultat(int[] dist, int[] parent) {
        this.dist = dist;
        this.parent = parent;
    }
    public List<Integer> reconstruireChemin(int cible) {
        List<Integer> chemin = new ArrayList<>();
        if (cible < 0 || cible >= dist.length) return chemin;
        if (dist[cible] == Integer.MAX_VALUE) {
            return chemin;
        }
        int courant = cible;
        while (courant != -1) {
            chemin.add(courant);
            courant = parent[courant];
        }
        Collections.reverse(chemin);
        return chemin;
    }
}
