package scenario;

import graph.graph;
import algorithme.Coloration;
import algorithme.CapacitePlanning;

import java.util.List;
import java.util.Map;

public class HO1theme3 {

    public static void executerEtAfficher() throws Exception {
        System.out.println();
        System.out.println("THEME 3");

        // Charger le graphe des secteurs
        graph gSecteurs = new graph();
        gSecteurs.chargerDepuisFichier("graph_secteur.txt");

        // === Hypothèse 1 : Coloration ===
        int[] jours = Coloration.welshPowell(gSecteurs);

        System.out.println("\nHypothèse 1 : Coloration");
        for (int i = 0; i < jours.length; i++) {
            System.out.println(gSecteurs.getNom(i) + " -> Jour " + (jours[i] + 1));
        }

        // === Hypothèse 2 : Capacité ===
        int[] quantites = gSecteurs.getToutesCapacites();
        int N = 1;      // nombre de camions disponibles
        int Cmax = 10;  // capacité maximale par camion

        Map<Integer, List<Integer>> planningFinal =
                CapacitePlanning.decouperJours(gSecteurs, jours, quantites, N, Cmax);

        System.out.println("\nHypothèse 2 : Planning final (capacités)");
        for (int jour : planningFinal.keySet()) {
            System.out.print("Jour " + (jour + 1) + " : ");
            for (int secteur : planningFinal.get(jour)) {
                System.out.print(gSecteurs.getNom(secteur) + " ");
            }
            System.out.println();
        }
    }
}
