package scenario;

import algorithme.*;
import graph.*;
import java.util.*;

public class HO1theme2 {
    public static class Resultat {
        public final List<Integer> ordre;
        public final List<List<Integer>> tournees;

        public Resultat(List<Integer> ordre, List<List<Integer>> tournees) {
            this.ordre = ordre;
            this.tournees = tournees;
        }
    }

    public static Resultat calculer(graph g, int C, int idCT) {
        List<Integer> points = new ArrayList<>();
        points.add(idCT);
        for (int i = 0; i < g.getNbcroisements(); i++) {
            if (i == idCT){
                continue;
            }
            if (g.getCapacite(i) > 0) {
                points.add(i);
            }
        }
        int[][] dist = construireMatriceDistances(g, points);
        int rootIndex = 0;
        int[] parent = Prim.calculer(dist, rootIndex);
        List<Integer> preordreReduit = preordreAvecCapacites(parent, rootIndex, points, g);
        List<Integer> preordreReel = new ArrayList<>();
        for (int idx : preordreReduit) {
            preordreReel.add(points.get(idx));
        }
        List<Integer> ordre = shortcutting.appliquer(preordreReel);
        int[] capacites = g.getToutesCapacites();
        List<List<Integer>> tournees = decoupecapacite.decouper(ordre, capacites, C, idCT);
        return new Resultat(ordre, tournees);
    }
    public static void executerEtAfficher(graph g, int C, int idCT) {
        Resultat res = calculer(g, C, idCT);
        System.out.println("    HO1 - Thème 2");
        System.out.println("Ordre de visite (MST + DFS + capacite) :");
        for (int v : res.ordre) {
            System.out.print(g.getNom(v) + " ");
        }
        System.out.println();
        System.out.println("\nTournées :");
        int num = 1;
        for (List<Integer> t : res.tournees) {
            System.out.print("T" + num++ + " : ");
            for (int v : t) {
                System.out.print(g.getNom(v) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static int[][] construireMatriceDistances(graph g, List<Integer> points) {
        int n = points.size();
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            int source = points.get(i);
            dijkstra.Resultat res = dijkstra.executer(g.getAdj(), source);
            for (int j = 0; j < n; j++) {
                int cible = points.get(j);
                dist[i][j] = res.dist[cible];
            }
        }
        return dist;
    }
    private static List<Integer> preordreAvecCapacites(int[] parent, int rootIndex, List<Integer> points, graph g) {
        int n = parent.length;
        List<List<Integer>> enfants = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            enfants.add(new ArrayList<>());
        }
        for (int v = 0; v < n; v++) {
            int p = parent[v];
            if (p != -1) {
                enfants.get(p).add(v);
            }
        }

        List<Integer> ordre = new ArrayList<>();
        boolean[] visite = new boolean[n];
        dfsCap(rootIndex, enfants, visite, ordre, points, g);
        return ordre;
    }
    private static void dfsCap(int u, List<List<Integer>> enfants, boolean[] visite, List<Integer> ordre, List<Integer> points, graph g) {
        visite[u] = true;
        ordre.add(u);
        List<Integer> fils = new ArrayList<>(enfants.get(u));
        fils.sort(Comparator.comparingInt(idx -> g.getCapacite(points.get(idx))));
        for (int v : fils) {
            if (!visite[v]) {
                dfsCap(v, enfants, visite, ordre, points, g);
            }
        }
    }
}
