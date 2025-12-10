package scenario;

import algorithme.*;
import graph.*;
import java.util.*;

public class HO1theme2 {
    public static void executer(Scanner sc) throws Exception {

        graph g = new graph();
        g.chargerDepuisFichier("graphtheme2.txt");
        int idCT = g.getId("CT");
        int C = 10;
        System.out.println();
        System.out.println("HO1 Thème 2 ");
        System.out.println();
        System.out.println("Sommets chargés : " + g.getNbcroisements());
        System.out.println("Capacité camion : " + C + "\n");
        System.out.println();
        System.out.println("1. H01 Thème 2 approche 1");
        System.out.println("2. H01 Thème 2 approche 2");
        System.out.println("0. retour");
        System.out.println();
        System.out.print("Votre choix : ");
        String choix = sc.nextLine().trim();
        switch (choix) {
            case "1":
                executerApproche1(g, C, idCT);
                break;
            case "2":
                executerApproche2(g, C, idCT);
                break;
            case "0":
                return;
            default:
                System.out.println("Choix invalide.\n");
        }
    }
    public static void executerApproche1(graph g, int C, int idCT) {

        List<Integer> nonVisites = new ArrayList<>();
        for (int i = 0; i < g.getNbcroisements(); i++) {
            if (i != idCT && g.getCapacite(i) > 0) {
                nonVisites.add(i);
            }
        }
        List<Integer> ordre = new ArrayList<>();
        ordre.add(idCT);

        int courant = idCT;
        while (!nonVisites.isEmpty()) {

            Resultat res = dijkstra.executer(g.getAdj(), courant);

            int best = -1;
            int minDist = Integer.MAX_VALUE;

            for (int v : nonVisites) {
                if (res.dist[v] < minDist) {
                    minDist = res.dist[v];
                    best = v;
                }
            }
            ordre.add(best);
            nonVisites.remove(Integer.valueOf(best));
            courant = best;
        }

        int[] caps = g.getToutesCapacites();
        List<List<Integer>> tournees = decoupecapacite.decouper(ordre, caps, C, idCT);

        System.out.println("\nApproche 1 : Plus proche voisin ");
        afficher(ordre, tournees, g);
    }

    public static void executerApproche2(graph g, int C, int idCT) {

        List<Integer> points = new ArrayList<>();
        points.add(idCT);
        for (int i = 0; i < g.getNbcroisements(); i++) {
            if (i != idCT && g.getCapacite(i) > 0) {
                points.add(i);
            }
        }

        int[][] dist = construireMatriceDistances(g, points);

        int[] parent = Prim.calculer(dist, 0);

        List<Integer> preordreReduit = DFS.preordre(parent, 0, Comparator.comparingInt(idx -> g.getCapacite(points.get(idx))));
        List<Integer> ordre = new ArrayList<>();
        for (int idx : preordreReduit) {
            ordre.add(points.get(idx));
        }
        ordre = shortcutting.appliquer(ordre);
        int[] caps = g.getToutesCapacites();
        List<List<Integer>> tournees =
                decoupecapacite.decouper(ordre, caps, C, idCT);

        System.out.println("\nApproche 2 : MST");
        afficher(ordre, tournees, g);
    }

    private static void afficher(List<Integer> ordre, List<List<Integer>> tournees, graph g) {
        System.out.print("\nOrdre : ");
        for (int v : ordre) System.out.print(g.getNom(v) + " ");
        System.out.println();
        System.out.println("\nTournées :");

        int i = 1;
        for (List<Integer> t : tournees) {
            System.out.print("T" + i++ + " : ");
            for (int v : t) System.out.print(g.getNom(v) + " ");
            System.out.println();
        }
        System.out.println();
    }

    private static int[][] construireMatriceDistances(graph g, List<Integer> pts) {
        int m = pts.size();
        int[][] dist = new int[m][m];

        for (int i = 0; i < m; i++) {
            int src = pts.get(i);
            var res = dijkstra.executer(g.getAdj(), src);
            for (int j = 0; j < m; j++)
                dist[i][j] = res.dist[pts.get(j)];
        }
        return dist;
    }
}
