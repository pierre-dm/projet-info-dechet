package scenario;
import graph.*;
import algorithme.dijkstra;

public class HO1Prob1Hyp1 {
    public void calculer(graph g, String nomCT, String croisement1, String croisement2) {

        int ct = g.getId(nomCT);
        int u = g.getId(croisement1);
        int v = g.getId(croisement2);

        dijkstra.Resultat res = dijkstra.executer(g.getAdj(), ct);

        int distU = res.dist[u];
        int distV = res.dist[v];

        int w = getPoidsRue(g, u, v);

        int cout = distU + w + distV;

        System.out.println("Coût CT -> " + croisement1 + "-" + croisement2 + " -> CT = " + cout);
    }

    private int getPoidsRue(graph g, int u, int v) {
        for (rue r : g.getAdj().get(u)) {
            if (r.getDestination() == v)
                return r.getDistance();
        }
        return -1;
    }
}
