package algorithme;
import java.util.*;
public class DFS {

    public static List<Integer> preordre(int[] parent, int root, Comparator<Integer> triEnfants) {

        int n = parent.length;

        // Construire la liste des enfants à partir des parents
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

        // Appliquer un tri éventuel sur chaque liste d'enfants
        if (triEnfants != null) {
            for (List<Integer> liste : enfants) {
                liste.sort(triEnfants);
            }
        }

        // DFS classique
        List<Integer> ordre = new ArrayList<>();
        boolean[] visite = new boolean[n];
        dfs(root, enfants, visite, ordre);
        return ordre;
    }

    private static void dfs(int u, List<List<Integer>> enfants, boolean[] visite, List<Integer> ordre) {
        visite[u] = true;
        ordre.add(u);
        for (int v : enfants.get(u)) {
            if (!visite[v]) {
                dfs(v, enfants, visite, ordre);
            }
        }
    }
}
