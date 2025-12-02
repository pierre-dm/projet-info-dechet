package app;
import graph.*;
import algorithme.*;
import scenario.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        graph g = new graph();
        g.chargerDepuisFichier("graphtheme2.txt");

        int idCT = g.getId("CT");
        int C = 10;  // capacité max du camion

        HO1theme2.executerEtAfficher(g, C, idCT);
        System.out.println();
        System.out.println("THEME 3");
        graph gSecteurs = new graph();
        gSecteurs.chargerDepuisFichier("graph_secteur.txt");
        int[] jours = Coloration.welshPowell(gSecteurs);

        System.out.println("\nHypothèse 1 : Coloration");
        for (int i = 0; i < jours.length; i++) {
            System.out.println(gSecteurs.getNom(i) + " -> Jour " + (jours[i] + 1));
        }

// Hypothèse 2 : Capacité
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