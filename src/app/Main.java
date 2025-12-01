package app;
import graph.*;
import algorithme.*;
import java.util.*;

public class Main {
    private static int[][] construireMatriceDistances(graph g,List<Integer> points) {
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
    public static void main(String[] args) throws Exception {
        graph g = new graph();
        g.chargerDepuisFichier("graphtheme2.txt");

        System.out.println("Sommets chargés : " + g.getNbcroisements());

        int idCT = g.getId("CT");
        List<Integer> points = new ArrayList<>();
        points.add(idCT);
        for (int i = 0; i < g.getNbcroisements(); i++) {
            if (i == idCT) continue;
            if (g.getCapacite(i) > 0) {
                points.add(i);
            }
        }
        int[][] dist = construireMatriceDistances(g,points);
        System.out.println("Nombre de points de collecte (y compris CT) pris en compte dans le MST : " + points.size());
        System.out.print("Liste des points : ");
        for (int idx : points) {
            System.out.print(g.getNom(idx) + " ");
        }
        System.out.println();
        int[] parent = Prim.calculer(dist, idCT);
        List<Integer> preordre = DFS.preordre(parent, idCT);
        List<Integer> preordreReel = new ArrayList<>();
        for (int idx : preordre) {
            preordreReel.add(points.get(idx));
        }
        List<Integer> ordre = shortcutting.appliquer(preordreReel);

        int C = 10;
        int[] capacites = g.getToutesCapacites();

        List<List<Integer>> tournees = decoupecapacite.decouper(ordre, capacites, C, idCT);

        System.out.println("Ordre de visite : ");
        for (int v : ordre) {
            System.out.print(g.getNom(v) + " ");
        }
        System.out.println("\nTournées :");
        int num = 1;
        for (List<Integer> t : tournees) {
            System.out.print("T" + num++ + " : ");
             for (int v : t) {
               System.out.print(g.getNom(v) + " ");
            }
            System.out.println();
        }
    }
}