package scenario;

import graph.graph;
import algorithme.Coloration;
import algorithme.CapacitePlanning;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HO1theme3 {

    public static void executer(Scanner sc) throws Exception {
        graph g = new graph();
        g.chargerDepuisFichier("graph_secteur.txt");
        System.out.println();
        System.out.println("HO1 Thème 3 ");
        System.out.println("Sommets chargés : " + g.getNbcroisements());
        System.out.println("1. H01 Thème 3 Hypothese 1");
        System.out.println("2. H01 Thème 3 Hypothese 2");
        System.out.println("0. retour");
        System.out.print("Votre choix : ");
        String choix = sc.nextLine().trim();
        int[] jours = Coloration.welshPowell(g);
        int[] quantites = g.getToutesCapacites();
        int N = 1;
        int Cmax = 10;
        Map<Integer, List<Integer>> planningFinal = CapacitePlanning.decouperJours(g, jours, quantites, N, Cmax);
        switch (choix) {
            case "1":
                System.out.println("\nHypothèse 1 : Coloration");
                for (int i = 0; i < jours.length; i++) {
                    System.out.println(g.getNom(i) + " -> Jour " + (jours[i] + 1));
                }
                break;
            case "2":
                System.out.println("\nHypothèse 2 : Planning final (capacités)");
                for (int jour : planningFinal.keySet()) {
                    System.out.print("Jour " + (jour + 1) + " : ");
                    for (int secteur : planningFinal.get(jour)) {
                        System.out.print(g.getNom(secteur) + " ");
                    }
                    System.out.println();
                }
                break;
            case "0":
                return;
            default:
                System.out.println("Choix invalide.\n");
        }
    }
}











