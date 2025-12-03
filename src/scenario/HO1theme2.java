package scenario;

import algorithme.*;
import graph.*;
import java.util.*;

public class HO1theme2 {
    public static void executer() throws Exception {

        graph g = new graph();
        g.chargerDepuisFichier("graphtheme2.txt");
        int idCT = g.getId("CT");
        int C = 10;
        System.out.println("=== Thème 2 ===");
        System.out.println("Sommets chargés : " + g.getNbcroisements());
        System.out.println("Capacité camion : " + C + "\n");

        executerApproche1(g, C, idCT);
        executerApproche2(g, C, idCT);
    }
    public static void executerApproche1(graph g, int C, int idCT) {

        // Liste des points réels à visiter (capacité > 0)
        List<Integer> nonVisites = new ArrayList<>();
        for (int i = 0; i < g.getNbcroisements(); i++)
            if (i != idCT && g.getCapacite(i) > 0)
                nonVisites.add(i);

        List<Integer> ordre = new ArrayList<>();
        ordre.add(idCT);

        int courant = idCT;

        // Algorithme PPV CORRECT
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
        List<List<Integer>> tournees =
                decoupecapacite.decouper(ordre, caps, C, idCT);

        System.out.println("=== Approche 1 : Plus proche voisin ===");
        afficher(ordre, tournees, g);
    }

    public static void executerApproche2(graph g, int C, int idCT) {

        // 1) Construire la liste des points : CT + contenance > 0
        List<Integer> points = new ArrayList<>();
        points.add(idCT);
        for (int i = 0; i < g.getNbcroisements(); i++)
            if (i != idCT && g.getCapacite(i) > 0)
                points.add(i);

        // 2) Matrice distances
        int[][] dist = construireMatriceDistances(g, points);

        // 3) MST via Prim
        int[] parent = Prim.calculer(dist, 0);

        // 4) DFS préordre TRIÉ PAR CAPACITÉ (clé du résultat correct)
        List<Integer> preordreReduit = preordreAvecCapacites(parent, 0, points, g);

        // 5) Traduction indices MST → indices réels
        List<Integer> ordre = new ArrayList<>();
        for (int idx : preordreReduit)
            ordre.add(points.get(idx));

        // 6) Shortcutting
        ordre = shortcutting.appliquer(ordre);

        // 7) Tournées
        int[] caps = g.getToutesCapacites();
        List<List<Integer>> tournees =
                decoupecapacite.decouper(ordre, caps, C, idCT);

        System.out.println("=== Approche 2 : MST + DFS + shortcutting ===");
        afficher(ordre, tournees, g);
    }


    // ----------------------------------------------------------
    //              OUTILS INTERNES
    // ----------------------------------------------------------
    private static void afficher(List<Integer> ordre, List<List<Integer>> tournees, graph g) {
        System.out.print("Ordre : ");
        for (int v : ordre) System.out.print(g.getNom(v) + " ");
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

    private static List<Integer> DFSparent(int[] parent) {
        int n = parent.length;
        List<List<Integer>> enfants = new ArrayList<>();
        for (int i = 0; i < n; i++) enfants.add(new ArrayList<>());

        for (int v = 0; v < n; v++)
            if (parent[v] != -1)
                enfants.get(parent[v]).add(v);

        List<Integer> ordre = new ArrayList<>();
        dfs(0, enfants, ordre);
        return ordre;
    }

    private static void dfs(int u, List<List<Integer>> enfants, List<Integer> ordre) {
        ordre.add(u);
        for (int v : enfants.get(u))
            dfs(v, enfants, ordre);
    }
    private static List<Integer> preordreAvecCapacites(
            int[] parent, int rootIndex,
            List<Integer> points, graph g) {

        int n = parent.length;
        List<List<Integer>> enfants = new ArrayList<>();
        for (int i = 0; i < n; i++) enfants.add(new ArrayList<>());

        for (int v = 0; v < n; v++)
            if (parent[v] != -1)
                enfants.get(parent[v]).add(v);

        List<Integer> ordre = new ArrayList<>();
        boolean[] visite = new boolean[n];

        dfsTrie(rootIndex, enfants, visite, ordre, points, g);
        return ordre;
    }

    private static void dfsTrie(
            int u, List<List<Integer>> enfants,
            boolean[] visite, List<Integer> ordre,
            List<Integer> points, graph g) {

        visite[u] = true;
        ordre.add(u);

        List<Integer> fils = new ArrayList<>(enfants.get(u));

        // tri PAR CAPACITÉ CROISSANTE (clé pour avoir le bon ordre)
        fils.sort(Comparator.comparingInt(idx ->
                g.getCapacite(points.get(idx)))
        );

        for (int v : fils)
            if (!visite[v])
                dfsTrie(v, enfants, visite, ordre, points, g);
    }
}
