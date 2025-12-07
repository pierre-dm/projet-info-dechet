package app;

import scenario.*;
import java.util.*;

public class menu {
    public static void executer() throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("    MENU");
            System.out.println();
            System.out.println("1. H01 Thème 1");
            System.out.println("2. H01 Thème 2");
            System.out.println("3. H01 Thème 3");
            System.out.println("4. H02 Thème 1");
            System.out.println("5. H02 Thème 2");
            System.out.println("6. H02 Thème 3");
            System.out.println("0. retour");
            System.out.println();
            System.out.print("Votre choix : ");

            String choix = sc.nextLine().trim();

            switch(choix) {
                case "1":
                    HO1theme1.executer(sc);
                    break;
                case "2":
                    HO1theme2.executer(sc);
                    break;
                case "3":
                    HO1theme3.executer(sc);
                    break;
                case "4":
                    HO2theme1.executer(sc);
                    break;
                case "5":
                    HO2theme2.executer(sc);
                    break;
                case "6":
                    HO2theme3.executer(sc);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Choix invalide.\n");
            }
        }
    }
}
