package scenario;

import algorithme.*;
import graph.graph;
import graph.rue;

import java.io.IOException;
import java.util.*;

public class HO1theme1 {

    public static void executer(Scanner sc) throws IOException {
        graph g = new graph();
        g.chargerDepuisFichier("graph1.txt");

        while (true) {
            System.out.println();
            System.out.println("HO1 Thème 1 ");
            System.out.println();
            System.out.println("1. H01 Thème 1 problematique 1");
            System.out.println("2. H01 Thème 1 problematique 2");
            System.out.println("0. retour");
            System.out.println();
            System.out.print("Votre choix : ");

            String choix = sc.nextLine().trim();

            switch (choix) {
                case "1":
                    while (true) {
                        System.out.println("1. H01 Thème 1 problematique 1 hypothese 1");
                        System.out.println("2. H01 Thème 1 problematique 1 hypothese 2");
                        System.out.println("0. retour");
                        System.out.println();
                        System.out.print("Votre choix : ");

                        String choixHyp = sc.nextLine().trim();

                        if (choixHyp.equals("1")) {
                            pb1hypothese1(g, sc);
                        } else if (choixHyp.equals("2")) {
                            pb1hypothese2(g, sc);
                        } else if (choixHyp.equals("0")) {
                            break;
                        } else {
                            System.out.println("Choix invalide.\n");
                        }
                    }
                    break;

                case "2":
                    problematique2(sc);
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Choix invalide.\n");
            }
        }
    }
    public static void pb1hypothese1(graph g, Scanner sc) {

        System.out.println("\nHypothèse 1");

        int idCT = g.getId("CT");

        System.out.println("Sommets disponibles : ");
        for (var c : g.getCroisements()) System.out.print(c.getNom() + " ");
        System.out.println();

        System.out.print("Extrémité 1 de l’arête : ");
        String nomA = sc.nextLine().trim();
        System.out.print("Extrémité 2 de l’arête : ");
        String nomB = sc.nextLine().trim();

        Integer idA = g.getId(nomA);
        Integer idB = g.getId(nomB);

        if (idA == null || idB == null) {
            System.out.println("Erreur : sommet inconnu.\n");
            return;
        }

        int longueurAB = getDistanceDirecte(g, idA, idB);
        if (longueurAB < 0) {
            System.out.println("Erreur : l’arête n’existe pas.\n");
            return;
        }

        Resultat res = dijkstra.executer(g.getAdj(), idCT);

        int near = (res.dist[idA] <= res.dist[idB]) ? idA : idB;
        int far  = (near == idA) ? idB : idA;

        int coutUneFois = res.dist[near] + longueurAB + res.dist[far];
        int coutAllerRetour = 2 * res.dist[near] + 2 * longueurAB;

        List<Integer> chemin;
        if (coutUneFois <= coutAllerRetour) {
            chemin = cheminTraverseUneFois(res, near, far);
        } else {
            chemin = cheminAllerRetour(res, near, far);
        }
        System.out.println("Chemin optimal : " + formater(g, chemin));
    }

    private static class Particulier {
        final int u;
        final int v;
        Particulier(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }
    public static void pb1hypothese2(graph g, Scanner sc) {

        System.out.println("\nHypothèse 2 ");
        int idCT = g.getId("CT");
        System.out.println();
        System.out.print("Combien de particuliers (≤10) ? ");
        int k = Integer.parseInt(sc.nextLine().trim());

        List<Particulier> particuliers = new ArrayList<>();

        for (int i = 1; i <= k; i++) {
            System.out.println();
            System.out.print("Particulier " + i + " – Extrémité 1 : ");
            String A = sc.nextLine().trim();
            System.out.print("Particulier " + i + " – Extrémité 2 : ");
            String B = sc.nextLine().trim();

            Integer idA = g.getId(A);
            Integer idB = g.getId(B);

            if (idA == null || idB == null) {
                System.out.println("Erreur : sommet inconnu.\n");
                return;
            }

            int longueurAB = getDistanceDirecte(g, idA, idB);
            if (longueurAB < 0) {
                System.out.println("Erreur : il n'existe pas d'arête directe entre "
                        + A + " et " + B + ".\n");
                return;
            }

            particuliers.add(new Particulier(idA, idB));
        }

        int current = idCT;
        List<Integer> tour = new ArrayList<>();
        tour.add(current);

        boolean[] visite = new boolean[particuliers.size()];
        int restant = particuliers.size();
        final int INF = Integer.MAX_VALUE;

        while (restant > 0) {
            Resultat res = dijkstra.executer(g.getAdj(), current);

            int bestIdx = -1;
            int bestCost = INF;
            int bestEntry = -1;
            int bestExit  = -1;

            for (int i = 0; i < particuliers.size(); i++) {
                if (visite[i]) continue;
                Particulier p = particuliers.get(i);

                int dU = res.dist[p.u];
                int dV = res.dist[p.v];
                int w = getDistanceDirecte(g, p.u, p.v);

                if (w < 0) continue;
                if (dU < INF) {
                    int coutUV = dU + w;
                    if (coutUV < bestCost) {
                        bestCost = coutUV;
                        bestIdx = i;
                        bestEntry = p.u;
                        bestExit  = p.v;
                    }
                }
                if (dV < INF) {
                    int coutVU = dV + w;
                    if (coutVU < bestCost) {
                        bestCost = coutVU;
                        bestIdx = i;
                        bestEntry = p.v;
                        bestExit  = p.u;
                    }
                }
            }

            if (bestIdx == -1) {
                System.out.println("Aucun particulier restant n'est atteignable depuis "
                        + g.getNom(current) + ".\n");
                break;
            }

            visite[bestIdx] = true;
            restant--;

            List<Integer> cheminVersEntry = res.reconstruireChemin(bestEntry);
            for (int i = 1; i < cheminVersEntry.size(); i++) {
                tour.add(cheminVersEntry.get(i));
            }
            tour.add(bestExit);

            current = bestExit;
        }

        if (current != idCT) {
            Resultat resRetour = dijkstra.executer(g.getAdj(), current);
            if (resRetour.dist[idCT] == INF) {
                System.out.println("Attention : CT n'est pas atteignable depuis "
                        + g.getNom(current) + ".\n");
            } else {
                List<Integer> cheminRetour = resRetour.reconstruireChemin(idCT);
                for (int i = 1; i < cheminRetour.size(); i++) {
                    tour.add(cheminRetour.get(i));
                }
            }
        }

        System.out.println("\nTournée heuristique (plus proche voisin sur les arêtes) : ");
        System.out.println(formater(g, tour));
    }


    // Problématique 2 – CPP
    public static void executerCPP(graph g) {

        System.out.println("\n THÈME 1 — Problématique 2 — CPP \n");

        int idCT = g.getId("CT");

        List<Integer> circuit = CPP.calculerCircuit(g, idCT);

        System.out.println("Circuit du postier chinois : ");
        System.out.println(formater(g, circuit));
        System.out.println("\nLongueur du circuit : " + (circuit.size() - 1) + " arêtes");
    }

    public static void problematique2(Scanner sc) {
        System.out.println("\nTHÈME 1 — Problématique 2 — CPP\n");
        System.out.println("1. Cas 2.1 : tous les sommets de degré pair");
        System.out.println("2. Cas 2.2 : exactement deux sommets impairs");
        System.out.println("3. Cas 2.3 : cas général (plusieurs sommets impairs)");
        System.out.println("0. Retour");
        System.out.print("Votre choix : ");

        String choix = sc.nextLine().trim();
        String fichier;

        switch (choix) {
            case "1":
                fichier = "graph1.txt";
                break;
            case "2":
                fichier = "graphetheme1problematique2cas2.txt";
                break;
            case "3":
                fichier = "graphetheme1problematique2cas3.txt";
                break;
            default:
                return;
        }

        try {
            graph g = new graph();
            g.chargerDepuisFichier(fichier);
            executerCPP(g);
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier " + fichier + " : " + e.getMessage());
        }
    }

    private static int getDistanceDirecte(graph g, int u, int v) {
        for (rue r : g.getAdj().get(u))
            if (r.getDestination() == v) return r.getDistance();
        for (rue r : g.getAdj().get(v))
            if (r.getDestination() == u) return r.getDistance();
        return -1;
    }

    private static List<Integer> cheminTraverseUneFois(Resultat res, int near, int far) {
        List<Integer> c = new ArrayList<>(res.reconstruireChemin(near));
        c.add(far);
        List<Integer> ret = res.reconstruireChemin(far);
        Collections.reverse(ret);
        for (int i = 1; i < ret.size(); i++) c.add(ret.get(i));
        return c;
    }

    private static List<Integer> cheminAllerRetour(Resultat res, int near, int far) {
        List<Integer> c = new ArrayList<>(res.reconstruireChemin(near));
        c.add(far);
        c.add(near);
        List<Integer> ret = res.reconstruireChemin(near);
        Collections.reverse(ret);
        for (int i = 1; i < ret.size(); i++) c.add(ret.get(i));
        return c;
    }

    private static String formater(graph g, List<Integer> list) {
        List<String> s = new ArrayList<>();
        for (int id : list) s.add(g.getNom(id));
        return String.join(" -> ", s);
    }
}
