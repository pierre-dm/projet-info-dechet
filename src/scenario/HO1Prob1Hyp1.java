package scenario;
import graph.*;
import algorithme.dijkstra;
import java.util.*;

public class HO1Prob1Hyp1 {
    public void calculer(graph g, String nomCT, String croisement1, String croisement2) {

        int ct = g.getId(nomCT);
        int u = g.getId(croisement1);
        int v = g.getId(croisement2);

        dijkstra.Resultat res = dijkstra.executer(g.getAdj(), ct);

        int distU = res.dist[u];
        int distV = res.dist[v];
        int w = getPoidsRue(g, u, v);
        if (w == -1) {
            System.out.println("La rue entre " + croisement1 + " et " + croisement2 + " n'existe pas !");
            return;
        }
        int coutTotal = distU + w + distV;
        System.out.println("Rue à visiter : " + croisement1 + " - " + croisement2);
        System.out.println();
        afficherChemin(g, res.parent, ct, u,v);
        System.out.println();
        System.out.println("cout total :  " + coutTotal);
    }

    private int getPoidsRue(graph g, int u, int v) {
        for (rue r : g.getAdj().get(u)) {
            if (r.getDestination() == v) {
                return r.getDistance();
            }
        }
        return -1;
    }
    private void afficherChemin(graph g, int[] parent, int ct, int u, int v) {
        List<Integer> chemin = new ArrayList<>();
        int courant = u;
        while (courant != -1) {
            chemin.add(courant);
            if (courant == ct) break;
            courant = parent[courant];
        }
        Collections.reverse(chemin);
        for (int i = 0; i < chemin.size() - 1; i++) {
            int a = chemin.get(i);
            int b = chemin.get(i + 1);
            int d = getPoidsRue(g, a, b);
            System.out.println(g.getNom(a) + " -> " + g.getNom(b) + " (distance " + d + ")");
        }
        int c = getPoidsRue(g, u, v);
        System.out.println(g.getNom(u) + " -> " + g.getNom(v) + " (distance " + c + ")");
        courant = v;
        while (courant != ct) {
            int suivant = parent[courant];
            if (suivant == -1) {
                System.out.println("  Erreur : impossible de remonter jusqu'au CT.");
                return;
            }
            int d = getPoidsRue(g, courant, suivant);
            System.out.println(g.getNom(courant) + " -> " + g.getNom(suivant) + " (distance " + d + ")");
            courant = suivant;
        }
    }
}
