package scenario;

import algorithme.*;
import graph.graph;
import graph.rue;

import java.io.IOException;
import java.util.*;

public class HO1theme1 {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("=== HO1 - Thème 1 ===");
            System.out.println("1. Exécuter l'hypothèse 1 (un particulier sur une arête)");
            System.out.println("2. Exécuter l'hypothèse 2 (non implémentée pour l'instant)");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            String choix = sc.nextLine().trim();

            switch (choix) {
                case "1":
                    executerHypothese1(sc);
                    break;
                case "2":
                    System.out.println("Hypothèse 2 : à implémenter plus tard.\n");
                    break;
                case "0":
                    System.out.println("Au revoir.");
                    return;
                default:
                    System.out.println("Choix invalide.\n");
            }
        }
    }

    /**
     * Hypothèse 1 : un seul particulier sur une arête (A,B).
     * On part de CT, on visite le particulier (donc on traverse l'arête),
     * puis on retourne à CT en choisissant le meilleur côté.
     */
    private static void executerHypothese1(Scanner sc) {
        try {
            graph g = new graph();
            g.chargerDepuisFichier("graph1.txt");

            int idCT = g.getId("CT");

            System.out.println("\nGraphe chargé depuis graph1.txt");
            System.out.println("Sommets disponibles :");
            g.getCroisements().forEach(c -> System.out.print(c.getNom() + " "));
            System.out.println("\n");

            // Saisie des extrémités de l'arête où se trouve le particulier
            System.out.print("Nom du premier sommet de l'arête (ex : I1) : ");
            String nomA = sc.nextLine().trim();
            System.out.print("Nom du second sommet de l'arête (ex : I2) : ");
            String nomB = sc.nextLine().trim();

            int idA, idB;
            try {
                idA = g.getId(nomA);
                idB = g.getId(nomB);
            } catch (Exception e) {
                System.out.println("Erreur : un des sommets n'existe pas dans le graphe.\n");
                return;
            }

            int longueurArete = getDistanceEntre(g, idA, idB);
            if (longueurArete < 0) {
                System.out.println("Erreur : il n'existe pas d'arête directe entre "
                        + nomA + " et " + nomB + ".\n");
                return;
            }

            // Dijkstra depuis CT
            Resultat res = dijkstra.executer(g.getAdj(), idCT);
            int[] dist = res.dist;

            final int INF = Integer.MAX_VALUE;
            if (dist[idA] == INF || dist[idB] == INF) {
                System.out.println("Au moins une des extrémités n'est pas atteignable depuis CT.\n");
                return;
            }

            // Déterminer le sommet le plus proche de CT parmi A et B
            int near = idA;
            int far = idB;
            if (dist[idB] < dist[idA]) {
                near = idB;
                far = idA;
            }
            String nomNear = g.getNom(near);
            String nomFar = g.getNom(far);

            int distNear = dist[near];
            int distFar = dist[far];

            System.out.println("Sommet le plus proche de CT : " + nomNear + " ("
                    + distNear + " unités)");
            System.out.println("Autre extrémité : " + nomFar + " (" + distFar + " unités)");
            System.out.println("Longueur de l'arête " + nomA + "-" + nomB + " : "
                    + longueurArete + "\n");

            // On compare :
            // - Option 1 : CT -> near -> far -> CT (on traverse l'arête une seule fois)
            // - Option 2 : CT -> near -> far -> near -> CT (on traverse l'arête aller-retour)
            int coutOption1 = distNear + longueurArete + distFar;
            int coutOption2 = 2 * distNear + 2 * longueurArete;

            List<Integer> tour;
            int coutTotal;
            System.out.println(coutOption1);
            System.out.println(coutOption2);
            System.out.println(distFar);
            System.out.println(distNear + longueurArete);
            if (distFar < distNear + longueurArete) {
                // Option 1 : on revient par le côté le plus loin
                coutTotal = coutOption1;
                tour = construireTourTraverserUneFois(g, res, near, far);
                System.out.println("Stratégie choisie : traverser l'arête une seule fois "
                        + "et revenir par " + nomFar + ".");
            } else {
                // Option 2 : on fait un aller-retour sur l'arête du côté le plus proche
                coutTotal = coutOption2;
                tour = construireTourAllerRetour(g, res, near, far);
                System.out.println("Stratégie choisie : aller-retour sur l'arête du côté "
                        + nomNear + ".");
            }

            // Affichage du chemin complet CT -> ... -> CT
            System.out.println("Chemin optimal : " + formaterChemin(g, tour));
            System.out.println("Distance totale : " + coutTotal + "\n");

        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier graph1.txt : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Recherche la distance de l'arête (u,v) dans le graphe (non orienté).
     * Retourne -1 si l'arête n'existe pas.
     */
    private static int getDistanceEntre(graph g, int u, int v) {
        for (rue r : g.getAdj().get(u)) {
            if (r.getDestination() == v) {
                return r.getDistance();
            }
        }
        for (rue r : g.getAdj().get(v)) {
            if (r.getDestination() == u) {
                return r.getDistance();
            }
        }
        return -1;
    }

    /**
     * Construit le tour CT -> ... -> near -> far -> ... -> CT
     * en traversant l'arête une seule fois.
     */
    private static List<Integer> construireTourTraverserUneFois(graph g,
                                                                Resultat res,
                                                                int near,
                                                                int far) {
        List<Integer> tour = new ArrayList<>();

        // chemin CT -> near
        List<Integer> cheminCTversNear = res.reconstruireChemin(near); // [CT,...,near]
        tour.addAll(cheminCTversNear);

        // traversée de l'arête near -> far (le particulier est dessus)
        tour.add(far);

        // retour far -> CT : on inverse le chemin CT -> far
        List<Integer> cheminCTversFar = res.reconstruireChemin(far);   // [CT,...,far]
        Collections.reverse(cheminCTversFar);                         // [far,...,CT]
        // on saute le premier (far) pour éviter les doublons
        for (int i = 1; i < cheminCTversFar.size(); i++) {
            tour.add(cheminCTversFar.get(i));
        }
        return tour;
    }

    /**
     * Construit le tour CT -> ... -> near -> far -> near -> ... -> CT
     * (aller-retour sur l'arête du côté le plus proche).
     */
    private static List<Integer> construireTourAllerRetour(graph g,
                                                           Resultat res,
                                                           int near,
                                                           int far) {
        List<Integer> tour = new ArrayList<>();

        // CT -> near
        List<Integer> cheminCTversNear = res.reconstruireChemin(near);
        tour.addAll(cheminCTversNear);

        // near -> far (on passe devant le particulier)
        tour.add(far);
        // far -> near (retour sur la même rue)
        tour.add(near);

        // near -> CT : on inverse le chemin CT -> near
        List<Integer> cheminRetour = new ArrayList<>(cheminCTversNear);
        Collections.reverse(cheminRetour); // [near,...,CT]
        for (int i = 1; i < cheminRetour.size(); i++) {
            tour.add(cheminRetour.get(i));
        }
        return tour;
    }

    /**
     * Convertit une liste d'identifiants de sommets en chaîne "CT -> I6 -> I3 -> ..."
     */
    private static String formaterChemin(graph g, List<Integer> chemin) {
        List<String> noms = new ArrayList<>();
        for (int id : chemin) {
            noms.add(g.getNom(id));
        }
        return String.join(" -> ", noms);
    }
}
