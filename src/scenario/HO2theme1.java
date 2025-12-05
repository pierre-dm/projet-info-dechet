package scenario;

import graph.graph;

import java.io.IOException;
import java.util.Scanner;

public class HO2theme1 {
    public static void executer(Scanner sc)throws IOException {
        graph g = new graph();
        g.chargerDepuisFichier("graph1.txt");
        System.out.println("HO2 Thème 1 ");
        System.out.println("1. H02 Thème 1 problematique 1");
        System.out.println("2. H02 Thème 1 problematique 2");
        System.out.println("0. retour");
        System.out.print("Votre choix : ");
        String choix = sc.nextLine().trim();
        switch (choix) {
            case "1":
                System.out.println("1. H02 Thème 1 problematique 1 hypothese 1");
                System.out.println("2. H02 Thème 1 problematique 1 hypothese 2");
                System.out.println("0. retour");
                System.out.print("Votre choix : ");
                choix = sc.nextLine().trim();
                switch (choix) {
                    case "1":
                       // pb1hypothese1(g, sc);
                        break;
                    case "2":
                        //pb1hypothese2(g, sc);
                        break;
                    case "0":
                        return;
                }
                break;
            case "2":
                //problematique2(g);
                break;
            case "0":
                return;
            default:
                System.out.println("Choix invalide.\n");
        }

    }
}
