package algorithme;

import graph.graph;
import java.util.*;

public class CapacitePlanning {

    public static Map<Integer, List<Integer>> decouperJours(
            graph g,
            int[] couleurs,
            int[] quantites,
            int N,
            int Cmax)
    {
        Map<Integer, List<Integer>> planning = new HashMap<>();

        // Regroupe les secteurs par jour
        for (int i = 0; i < couleurs.length; i++) {
            planning.computeIfAbsent(couleurs[i], k -> new ArrayList<>()).add(i);
        }

        boolean change = true;
        int newJour = planning.size();

        while (change) {
            change = false;

            for (int jour : new ArrayList<>(planning.keySet())) {
                List<Integer> secteurs = planning.get(jour);

                int total = secteurs.stream().mapToInt(s -> quantites[s]).sum();

                // Dépassement → on déplace un secteur dans un nouveau jour
                if (total > N * Cmax) {
                    int s = secteurs.remove(secteurs.size() - 1);

                    planning.computeIfAbsent(newJour, k -> new ArrayList<>()).add(s);
                    couleurs[s] = newJour;

                    newJour++;
                    change = true;
                    break;
                }
            }
        }

        return planning;
    }
}
