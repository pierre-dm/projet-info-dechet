package scenario;

import algorithme.*;
import graph.graph;
import graph.rue;

import java.io.IOException;
import java.util.*;

/**
 * Thème 1 – HO2 : graphe orienté
 *  - Problématique 1 : Hypothèses 1 et 2
 *  - Problématique 2 : CPP dirigé (3 cas)
 */
public class HO2theme1 {

    /*------------------------------------------------------------
     *  Menu principal du thème 1 – HO2
     *------------------------------------------------------------*/
    public static void executer(Scanner sc) throws IOException {

        while (true) {
            System.out.println();
            System.out.println("HO2 Thème 1 (graphe orienté)");
            System.out.println();
            System.out.println("1. Problématique 1");
            System.out.println("2. Problématique 2 (CPP dirigé)");
            System.out.println("0. Retour");
            System.out.println();
            System.out.print("Votre choix : ");

            String choix = sc.nextLine().trim();

            switch (choix) {
                case "1":
                    menuProblematique1(sc);
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

    /*------------------------------------------------------------
     *  Sous-menu Problématique 1 – HO2
     *------------------------------------------------------------*/
    private static void menuProblematique1(Scanner sc) {
        while (true) {
            System.out.println();
            System.out.println("HO2 – Thème 1 – Problématique 1");
            System.out.println("1. Hypothèse 1 (un particulier sur une arête)");
            System.out.println("2. Hypothèse 2 (tournée d’une dizaine de particuliers)");
            System.out.println("0. Retour");
            System.out.println();
            System.out.print("Votre choix : ");

            String choixHyp = sc.nextLine().trim();
            if (choixHyp.equals("0")) return;

            try {
                graph g = new graph();
                // graphe orienté de base (sert aussi pour le cas 2.1 de la problématique 2)
                g.chargerDepuisFichierOriente("graph1HO2.txt");

                if (choixHyp.equals("1")) {
                    pb1hypothese1(g, sc);
                } else if (choixHyp.equals("2")) {
                    pb1hypothese2(g, sc);
                } else {
                    System.out.println("Choix invalide.\n");
                }
            } catch (IOException e) {
                System.out.println("Erreur de lecture du graphe : " + e.getMessage());
                return;
            }
        }
    }

    /*============================================================
     *  PROBLÉMATIQUE 1 – HYPOTHÈSE 1 (HO2, graphe orienté)
     *============================================================*/

    public static void pb1hypothese1(graph g, Scanner sc) {

        System.out.println("\nHO2 – Problématique 1 – Hypothèse 1");

        int idCT = g.getId("CT");
        if (idCT < 0) {
            System.out.println("Erreur : sommet CT introuvable.\n");
            return;
        }

        System.out.println("Sommets disponibles : ");
        g.getCroisements().forEach(c -> System.out.print(c.getNom() + " "));
        System.out.println();

        System.out.print("Extrémité U de l’arête du particulier : ");
        String nomU = sc.nextLine().trim();
        System.out.print("Extrémité V de l’arête du particulier : ");
        String nomV = sc.nextLine().trim();

        Integer uObj = g.getId(nomU);
        Integer vObj = g.getId(nomV);
        if (uObj == null || vObj == null) {
            System.out.println("Erreur : sommet inconnu.\n");
            return;
        }

        // on les copie dans des int modifiables
        int u = uObj;
        int v = vObj;

        // poids des arcs orientés
        int wUV = getDistanceArc(g, u, v); // arc U -> V
        int wVU = getDistanceArc(g, v, u); // arc V -> U

        // aucun arc dans aucun sens
        if (wUV < 0 && wVU < 0) {
            System.out.println("Erreur : il n’existe aucun arc entre " + nomU + " et " + nomV + ".\n");
            return;
        }

        // Cas où seul V->U existe : on inverse la convention, de sorte que l'on
        // considère toujours que "l'arc du particulier" est U->V.
        if (wUV < 0 && wVU >= 0) {
            int tmp = u; u = v; v = tmp;
            String tmpNom = nomU; nomU = nomV; nomV = tmpNom;
            int tmpW = wUV; wUV = wVU; wVU = tmpW;
        }

        // À partir d’ici : wUV >= 0 (U->V existe forcément).
        // wVU peut exister ou pas (double sens ou sens unique).

        // Dijkstra aller depuis CT
        Resultat aller = dijkstra.executer(g.getAdj(), idCT);
        // Dijkstra retour depuis CT dans le graphe inversé
        List<List<rue>> adjInv = construireAdjInverse(g);
        Resultat retour = dijkstra.executer(adjInv, idCT);

        final int INF = Integer.MAX_VALUE;

        if (aller.dist[u] == INF) {
            System.out.println("Le sommet " + nomU + " n’est pas atteignable depuis CT.\n");
            return;
        }

        List<Integer> meilleurChemin = null;
        int meilleurCout = INF;
        String description = "";

        /*-------------------- Cas 1 : seulement U->V --------------------*/
        if (wUV >= 0 && wVU < 0) {
            if (retour.dist[v] == INF) {
                System.out.println("Le sommet " + nomV + " ne peut pas revenir à CT.\n");
                return;
            }

            int cout = aller.dist[u] + wUV + retour.dist[v];

            List<Integer> chemin = new ArrayList<>();
            // CT -> ... -> U
            chemin.addAll(aller.reconstruireChemin(u));
            // U -> V (arc du particulier)
            chemin.add(v);
            // V -> ... -> CT (à partir du chemin CT->...->V dans le graphe inverse)
            List<Integer> vVersCt = retour.reconstruireChemin(v); // chemin CT -> ... -> V dans G^-1
            Collections.reverse(vVersCt);                         // V -> ... -> CT dans G
            for (int i = 1; i < vVersCt.size(); i++) {
                chemin.add(vVersCt.get(i));
            }

            meilleurChemin = chemin;
            meilleurCout   = cout;
            description    = "Cas 1 (sens unique U->V) : CT -> ... -> U -> V -> ... -> CT";
        }

        /*-------------------- Cas 2 : double sens U<->V --------------------*/
        if (wUV >= 0 && wVU >= 0) {
            if (aller.dist[v] == INF || retour.dist[u] == INF || retour.dist[v] == INF) {
                System.out.println("Certains chemins aller/retour ne sont pas atteignables.\n");
                // on ne retourne pas forcément : il se peut que le cas 1 ait déjà donné une solution
            } else {

                int dCTU = aller.dist[u];
                int dCTV = aller.dist[v];
                int dUCT = retour.dist[u];
                int dVCT = retour.dist[v];

                // 1) CT -> ... -> U -> V -> U -> ... -> CT
                int cout1 = dCTU + wUV + wVU + dUCT;
                // 2) CT -> ... -> U -> V -> ... -> CT
                int cout2 = dCTU + wUV + dVCT;
                // 3) CT -> ... -> V -> U -> V -> U -> ... -> CT
                int cout3 = dCTV + wVU + wUV + dUCT;
                // 4) CT -> ... -> V -> U -> V -> ... -> CT
                int cout4 = dCTV + wVU + dVCT;

                // 1)
                {
                    List<Integer> c = new ArrayList<>();
                    c.addAll(aller.reconstruireChemin(u));  // CT -> ... -> U
                    c.add(v);                               // U -> V
                    c.add(u);                               // V -> U
                    List<Integer> seq = retour.reconstruireChemin(u);
                    Collections.reverse(seq);               // U -> ... -> CT
                    for (int i = 1; i < seq.size(); i++) c.add(seq.get(i));

                    if (cout1 < meilleurCout) {
                        meilleurCout   = cout1;
                        meilleurChemin = c;
                        description    = "Cas 2 – chemin 1 : CT -> ... -> U -> V -> U -> ... -> CT";
                    }
                }

                // 2)
                {
                    List<Integer> c = new ArrayList<>();
                    c.addAll(aller.reconstruireChemin(u));  // CT -> ... -> U
                    c.add(v);                               // U -> V
                    List<Integer> seq = retour.reconstruireChemin(v);
                    Collections.reverse(seq);               // V -> ... -> CT
                    for (int i = 1; i < seq.size(); i++) c.add(seq.get(i));

                    if (cout2 < meilleurCout) {
                        meilleurCout   = cout2;
                        meilleurChemin = c;
                        description    = "Cas 2 – chemin 2 : CT -> ... -> U -> V -> ... -> CT";
                    }
                }

                // 3)
                {
                    List<Integer> c = new ArrayList<>();
                    c.addAll(aller.reconstruireChemin(v));  // CT -> ... -> V
                    c.add(u);                               // V -> U
                    c.add(v);                               // U -> V
                    List<Integer> seq = retour.reconstruireChemin(u);
                    Collections.reverse(seq);               // U -> ... -> CT
                    for (int i = 1; i < seq.size(); i++) c.add(seq.get(i));

                    if (cout3 < meilleurCout) {
                        meilleurCout   = cout3;
                        meilleurChemin = c;
                        description    = "Cas 2 – chemin 3 : CT -> ... -> V -> U -> V -> U -> ... -> CT";
                    }
                }

                // 4)
                {
                    List<Integer> c = new ArrayList<>();
                    c.addAll(aller.reconstruireChemin(v));  // CT -> ... -> V
                    c.add(u);                               // V -> U
                    c.add(v);                               // U -> V
                    List<Integer> seq = retour.reconstruireChemin(v);
                    Collections.reverse(seq);               // V -> ... -> CT
                    for (int i = 1; i < seq.size(); i++) c.add(seq.get(i));

                    if (cout4 < meilleurCout) {
                        meilleurCout   = cout4;
                        meilleurChemin = c;
                        description    = "Cas 2 – chemin 4 : CT -> ... -> V -> U -> V -> ... -> CT";
                    }
                }
            }
        }

        if (meilleurChemin == null) {
            System.out.println("Aucun chemin valide n’a pu être trouvé.\n");
            return;
        }

        System.out.println("\nStratégie retenue : " + description);
        System.out.println("Chemin trouvé : " + formater(g, meilleurChemin));
        System.out.println("Coût total : " + meilleurCout + "\n");
    }

    /*============================================================
     *  PROBLÉMATIQUE 1 – HYPOTHÈSE 2 (HO2, tournées)
     *============================================================*/

    private static class Particulier {
        final int u; // extrémité 1
        final int v; // extrémité 2

        Particulier(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    public static void pb1hypothese2(graph g, Scanner sc) {

        System.out.println("\nTHÈME 1 — HO2 — Problématique 1 — Hypothèse 2");

        int idCT = g.getId("CT");
        if (idCT < 0) {
            System.out.println("Erreur : sommet CT introuvable.\n");
            return;
        }

        System.out.print("Combien de particuliers (≤10) ? ");
        int k = Integer.parseInt(sc.nextLine().trim());

        List<Particulier> particuliers = new ArrayList<>();

        // Saisie des particuliers
        for (int i = 1; i <= k; i++) {
            System.out.println();
            System.out.print("Particulier " + i + " – Extrémité 1 (U) : ");
            String nomU = sc.nextLine().trim();
            System.out.print("Particulier " + i + " – Extrémité 2 (V) : ");
            String nomV = sc.nextLine().trim();

            Integer idU = g.getId(nomU);
            Integer idV = g.getId(nomV);
            if (idU == null || idV == null) {
                System.out.println("Erreur : sommet inconnu.\n");
                return;
            }

            int u = idU;
            int v = idV;

            int wUV = getDistanceArc(g, u, v);
            int wVU = getDistanceArc(g, v, u);

            if (wUV < 0 && wVU < 0) {
                System.out.println("Erreur : il n'existe aucun arc entre " + nomU + " et " + nomV + ".\n");
                return;
            }

            // si seul V->U existe, on inverse (on veut toujours U->V existant)
            if (wUV < 0 && wVU >= 0) {
                int tmp = u; u = v; v = tmp;
            }

            particuliers.add(new Particulier(u, v));
        }

        final int INF = Integer.MAX_VALUE;
        List<Integer> tour = new ArrayList<>();
        int current = idCT;
        tour.add(current);

        boolean[] visite = new boolean[particuliers.size()];
        int restant = particuliers.size();

        // Heuristique "plus proche particulier" adaptée au graphe orienté
        while (restant > 0) {

            Resultat resFromA = dijkstra.executer(g.getAdj(), current);

            int bestIdx = -1;
            int bestCost = INF;
            List<Integer> bestSegment = null;
            int bestNewCurrent = -1;

            for (int i = 0; i < particuliers.size(); i++) {
                if (visite[i]) continue;

                Particulier p = particuliers.get(i);
                int u = p.u;
                int v = p.v;

                int dAu = resFromA.dist[u];
                int dAv = resFromA.dist[v];

                int wUV = getDistanceArc(g, u, v); // on a garanti que U->V existe
                int wVU = getDistanceArc(g, v, u); // peut exister ou non

                if (wUV < 0) continue; // sécurité

                // Candidat 1 : A -> ... -> U -> V
                if (dAu < INF) {
                    int cost1 = dAu + wUV;
                    if (cost1 < bestCost) {
                        bestCost = cost1;
                        bestIdx  = i;

                        List<Integer> seg = resFromA.reconstruireChemin(u);
                        seg.add(v);              // U -> V
                        bestSegment   = seg;
                        bestNewCurrent = v;
                    }
                }

                // Candidat 2 : A -> ... -> V -> U -> V (si V->U existe)
                if (wVU >= 0 && dAv < INF) {
                    int cost2 = dAv + wVU + wUV;
                    if (cost2 < bestCost) {
                        bestCost = cost2;
                        bestIdx  = i;

                        List<Integer> seg = resFromA.reconstruireChemin(v);
                        seg.add(u);              // V -> U
                        seg.add(v);              // U -> V
                        bestSegment   = seg;
                        bestNewCurrent = v;
                    }
                }
            }

            if (bestIdx == -1) {
                System.out.println("Aucun particulier restant n'est atteignable depuis "
                        + g.getNom(current) + ".\n");
                break;
            }

            // ajoute le segment sans répéter le sommet de départ
            for (int i = 1; i < bestSegment.size(); i++) {
                tour.add(bestSegment.get(i));
            }

            visite[bestIdx] = true;
            restant--;
            current = bestNewCurrent;  // on repart de V
        }

        // Retour final à CT
        if (current != idCT) {
            Resultat resBack = dijkstra.executer(g.getAdj(), current);
            if (resBack.dist[idCT] == INF) {
                System.out.println("Attention : CT n'est pas atteignable depuis "
                        + g.getNom(current) + ".\n");
            } else {
                List<Integer> back = resBack.reconstruireChemin(idCT);
                for (int i = 1; i < back.size(); i++) {
                    tour.add(back.get(i));
                }
            }
        }

        System.out.println("\nTournée heuristique (HO2, plus proche particulier) : ");
        System.out.println(formater(g, tour));
    }

    /*============================================================
     *  PROBLÉMATIQUE 2 – CPP dirigé (3 cas HO2)
     *============================================================*/

    public static void executerCPP(graph g) {
        System.out.println("\nTHÈME 1 – HO2 – Problématique 2 – CPP dirigé\n");

        int idCT = g.getId("CT");
        if (idCT < 0) {
            System.out.println("Erreur : sommet CT introuvable.\n");
            return;
        }

        // La classe CPP doit travailler sur le graphe orienté tel quel
        // (arcs en sens unique respectés).
        List<Integer> circuit = CPP.calculerCircuit(g, idCT);

        System.out.println("Circuit du postier chinois : ");
        System.out.println(formater(g, circuit));
        System.out.println("\nLongueur du circuit (en nombre d’arcs) : "
                + (circuit.size() - 1));
    }

    public static void problematique2(Scanner sc) {
        System.out.println("\nTHÈME 1 – HO2 – Problématique 2 – CPP dirigé\n");
        System.out.println("1. Cas 2.1 : tous les sommets de degré pair (in = out)");
        System.out.println("2. Cas 2.2 : exactement deux sommets impairs");
        System.out.println("3. Cas 2.3 : cas général (plusieurs sommets impairs)");
        System.out.println("0. Retour");
        System.out.print("Votre choix : ");

        String choix = sc.nextLine().trim();
        String fichier;

        switch (choix) {
            case "1":
                fichier = "graph1HO2.txt";
                break;
            case "2":
                fichier = "graphetheme1problematique2cas2HO2.txt";
                break;
            case "3":
                fichier = "graphetheme1problematique2cas3HO2.txt";
                break;
            default:
                return;
        }

        try {
            graph g = new graph();
            g.chargerDepuisFichierOriente(fichier);
            executerCPP(g);
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier " + fichier + " : " + e.getMessage());
        }
    }

    /*============================================================
     *  FONCTIONS UTILITAIRES
     *============================================================*/

    /** distance de l’arc orienté u -> v, -1 s’il n’existe pas */
    private static int getDistanceArc(graph g, int u, int v) {
        for (rue r : g.getAdj().get(u)) {
            if (r.getDestination() == v) {
                return r.getDistance();
            }
        }
        return -1;
    }

    /** construit la liste d’adjacence du graphe inverse (tous les arcs renversés) */
    private static List<List<rue>> construireAdjInverse(graph g) {
        List<List<rue>> adj = g.getAdj();
        int n = adj.size();
        List<List<rue>> inv = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            inv.add(new ArrayList<>());
        }
        for (int u = 0; u < n; u++) {
            for (rue r : adj.get(u)) {
                int v = r.getDestination();
                int d = r.getDistance();
                // dans le graphe inverse, on ajoute l'arc v -> u
                inv.get(v).add(new rue(u, d));
            }
        }
        return inv;
    }

    /** formatage d’un chemin sous forme CT -> I6 -> I3 -> ... */
    private static String formater(graph g, List<Integer> list) {
        List<String> s = new ArrayList<>();
        for (int id : list) s.add(g.getNom(id));
        return String.join(" -> ", s);
    }
}
