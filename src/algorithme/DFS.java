package algorithme;
import java.util.*;
public class DFS {
    public static List<Integer> preordre(int[] parent, int root) {
        int n = parent.length;
        List<List<Integer>> enfants = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            enfants.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            int p = parent[i];
            if (p != -1) {
                enfants.get(p).add(i);
            }
        }
        List<Integer> ordre = new ArrayList<>();
        dfs(root, enfants, ordre);
        return ordre;
    }
    private static void dfs(int u, List<List<Integer>> enfants, List<Integer> ordre) {
        ordre.add(u);
        for (int v : enfants.get(u)) {
            dfs(v, enfants, ordre);
        }
    }
}
