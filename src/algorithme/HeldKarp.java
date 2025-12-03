package algorithme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Algorithme de Held-Karp pour résoudre exactement un TSP
 * sur un petit graphe complet.
 *
 * Hypothèses :
 * - le sommet 0 du graphe réduit est le centre de traitement (CT)
 * - dist[i][j] contient la distance minimale entre i et j (symétrique ou non)
 * - dist[i][i] = 0
 *
 * Complexité : O(n^2 * 2^n) avec n = nombre de sommets du sous-graphe.
 */
public class HeldKarp {

    /** Résultat : coût total minimal + tournée correspondante. */
    public static class Resultat {
        /** Coût total de la tournée (CT -> ... -> CT). */
        public final int coutTotal;
        /**
         * Tournée complète sous forme de séquence de sommets,
         * en commençant et terminant par 0 (CT).
         * Exemple : [0, 3, 1, 2, 0].
         */
        public final int[] tour;

        public Resultat(int coutTotal, int[] tour) {
            this.coutTotal = coutTotal;
            this.tour = tour;
        }

        @Override
        public String toString() {
            return "coutTotal=" + coutTotal + ", tour=" + Arrays.toString(tour);
        }
    }

    /**
     * Résout le TSP en partant de 0 et en revenant à 0
     * en visitant tous les sommets exactement une fois.
     *
     * @param dist matrice de distances dist[i][j]
     * @return Resultat (coût minimal + tournée optimale)
     */
    public static Resultat resoudre(int[][] dist) {
        int n = dist.length;
        if (n <= 1) {
            // cas dégénéré
            int[] tour = new int[]{0};
            return new Resultat(0, tour);
        }

        // Nombre total d'états de masque (2^n)
        int nbEtats = 1 << n;
        final int INF = Integer.MAX_VALUE / 4;

        // dp[mask][j] = coût minimal pour :
        //  - partir de 0
        //  - visiter tous les sommets de "mask"
        //  - finir en j
        int[][] dp = new int[nbEtats][n];
        for (int i = 0; i < nbEtats; i++) {
            Arrays.fill(dp[i], INF);
        }

        // parent[mask][j] = sommet précédent avant d'arriver à j dans l'état (mask)
        int[][] parent = new int[nbEtats][n];
        for (int i = 0; i < nbEtats; i++) {
            Arrays.fill(parent[i], -1);
        }

        // On part de 0 : seul le bit 0 est actif dans le masque.
        int maskDepart = 1 << 0;
        dp[maskDepart][0] = 0;

        // Parcours de tous les masques
        for (int mask = 0; mask < nbEtats; mask++) {
            // On ignore les masques qui ne contiennent pas 0 :
            // on impose que la tournée commence toujours à 0.
            if ((mask & (1 << 0)) == 0) continue;

            for (int last = 0; last < n; last++) {
                if ((mask & (1 << last)) == 0) continue; // last ne fait pas partie du masque
                int coutActuel = dp[mask][last];
                if (coutActuel >= INF) continue;

                // On essaie d'ajouter un nouveau sommet "next" non encore visité
                for (int next = 0; next < n; next++) {
                    if ((mask & (1 << next)) != 0) continue; // déjà visité
                    int nextMask = mask | (1 << next);
                    int nouveauCout = coutActuel + dist[last][next];
                    if (nouveauCout < dp[nextMask][next]) {
                        dp[nextMask][next] = nouveauCout;
                        parent[nextMask][next] = last;
                    }
                }
            }
        }

        // Fermeture de la tournée : on doit revenir à 0 depuis un sommet final j.
        int fullMask = (1 << n) - 1;
        int meilleurCout = INF;
        int dernierSommet = -1;

        for (int j = 1; j < n; j++) { // dernier sommet différent de 0
            int cout = dp[fullMask][j];
            if (cout >= INF) continue;
            cout += dist[j][0]; // retour à 0

            if (cout < meilleurCout) {
                meilleurCout = cout;
                dernierSommet = j;
            }
        }

        if (dernierSommet == -1) {
            // Aucun tour complet possible
            return new Resultat(INF, new int[0]);
        }

        // Reconstruction de la tournée optimale en remontant parent[][].
        List<Integer> cheminInverse = new ArrayList<>();
        int mask = fullMask;
        int current = dernierSommet;

        // On ajoute les sommets de la fin vers le début (en partant de "dernierSommet").
        while (current != -1) {
            cheminInverse.add(current);
            int prev = parent[mask][current];
            mask = mask ^ (1 << current); // retire current du masque
            current = prev;
        }

        // A ce stade, cheminInverse contient par ex. [dernier, ..., 0]
        // On le remet dans l'ordre et on ajoute 0 à la fin pour le retour.
        List<Integer> tourList = new ArrayList<>();
        // Remet dans l'ordre correct : 0 -> ... -> dernier
        for (int i = cheminInverse.size() - 1; i >= 0; i--) {
            tourList.add(cheminInverse.get(i));
        }
        // On ajoute le retour à 0 pour fermer le tour.
        tourList.add(0);

        int[] tour = tourList.stream().mapToInt(Integer::intValue).toArray();
        return new Resultat(meilleurCout, tour);
    }
}
