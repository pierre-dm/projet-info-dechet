package app;
import graph.*;
import algorithme.*;
import java.util.*;

public class Main {
    private static int[][] construireMatriceDistances(graph g) {
        int n = g.getNbcroisements();
        int[][] dist = new int[n][n];

        for (int i = 0; i < n; i++) {
            dijkstra.Resultat res = dijkstra.executer(g.getAdj(), i);
            dist[i] = res.dist;
        }

        return dist;
    }
    public static void main(String[] args) throws Exception {
        graph g = new graph();
        g.chargerDepuisFichier("graphtheme2.txt");
        System.out.println("Sommets chargés : " + g.getNbcroisements());
        int idCT = g.getId("CT");
        for (rue r : g.getAdj().get(idCT)) {
            System.out.println("CT -> " + g.getNom(r.getDestination())
                    + " (distance = " + r.getDistance() + ")");
        }
        dijkstra.Resultat res = dijkstra.executer(g.getAdj(), idCT);
        for (int i = 0; i < g.getNbcroisements(); i++) {
            System.out.println("CT -> " + g.getNom(i) + " = " + res.dist[i]);
        }
        int[][] dist = construireMatriceDistances(g);
        int[] parent = Prim.calculer(dist, idCT);
        List<Integer> preordre = DFS.preordre(parent, idCT);
        List<Integer> ordre = shortcutting.appliquer(preordre);

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