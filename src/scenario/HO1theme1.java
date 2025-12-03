package scenario;

import algorithme.*;
import graph.graph;
import graph.rue;

import java.io.IOException;
import java.util.*;

public class HO1theme1 {
    public static void executer(Scanner sc)throws IOException {
        graph g = new graph();
        g.chargerDepuisFichier("graphtheme2.txt");
        System.out.println("HO1 Thème 1 ");
        System.out.println("1. H01 Thème 1 problematique 1");
        System.out.println("2. H01 Thème 1 problematique 2");
        System.out.println("0. retour");
        System.out.print("Votre choix : ");
        String choix = sc.nextLine().trim();
        switch (choix) {
            case "1":
                choix = sc.nextLine().trim();
                switch (choix) {
                    case "1":
                        pb1hypothese1(g, sc);
                    case "2":
                        pb1hypothese2(g, sc);
                }
                break;
            case "2":
                problematique2(g);
                break;
            case "0":
                return;
            default:
                System.out.println("Choix invalide.\n");
        }

    }

    public static void pb1hypothese1(graph g, Scanner sc) {

        System.out.println("\nTHÈME 1 — Problématique 1 — Hypothèse 1");

        int idCT = g.getId("CT");

        System.out.println("Sommets disponibles : ");
        for (var c : g.getCroisements()) System.out.print(c.getNom() + " ");
        System.out.println("\n");

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

        // Coûts
        int coutUneFois = res.dist[near] + longueurAB + res.dist[far];
        int coutAllerRetour = 2 * res.dist[near] + 2 * longueurAB;

        List<Integer> chemin;
        if (coutUneFois <= coutAllerRetour) {
            System.out.println("→ Stratégie : traverser l’arête une seule fois.\n");
            chemin = cheminTraverseUneFois(res, near, far);
        } else {
            System.out.println("→ Stratégie : aller-retour sur l’arête.\n");
            chemin = cheminAllerRetour(res, near, far);
        }

        System.out.println("Chemin optimal : " + formater(g, chemin));
    }

    public static void pb1hypothese2(graph g, Scanner sc) {

        System.out.println("\n=== THÈME 1 — Problématique 1 — Hypothèse 2 ===\n");
        int idCT = g.getId("CT");

        System.out.print("Combien de particuliers (≤10) ? ");
        int k = Integer.parseInt(sc.nextLine().trim());

        List<Integer> points = new ArrayList<>();
        points.add(idCT);

        Resultat resCT = dijkstra.executer(g.getAdj(), idCT);

        for (int i = 1; i <= k; i++) {
            System.out.print("Particulier " + i + " – Extrémité A : ");
            String A = sc.nextLine().trim();
            System.out.print("Particulier " + i + " – Extrémité B : ");
            String B = sc.nextLine().trim();

            int idA = g.getId(A);
            int idB = g.getId(B);

            if (idA < 0 || idB < 0) {
                System.out.println("Sommet inconnu.\n");
                return;
            }

            // On prend l'extrémité la plus proche de CT
            int near = (resCT.dist[idA] <= resCT.dist[idB]) ? idA : idB;
            points.add(near);
        }

        List<Integer> tournee = plusprochevoisin.calculer(g, points);

        System.out.println("\nTournée (plus proche voisin) : ");
        System.out.println(formater(g, tournee));
    }

    public static void problematique2(graph g) {

        System.out.println("\n=== THÈME 1 — Problématique 2 — CPP ===\n");

        int idCT = g.getId("CT");

        List<Integer> circuit = CPP.calculerCircuit(g, idCT);

        System.out.println("Circuit du postier chinois : ");
        System.out.println(formater(g, circuit));
        System.out.println("\nLongueur du circuit : " + (circuit.size() - 1) + " arêtes (approx.)");
    }
    private static int getDistanceDirecte(graph g, int u, int v) {
        for (rue r : g.getAdj().get(u))
            if (r.getDestination() == v) return r.getDistance();
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
