package scenario;

import algorithme.*;
import graph.graph;
import graph.rue;

import java.io.IOException;
import java.util.*;

public class HO2theme1 {
    public static void executer(Scanner sc) throws IOException {

        while (true) {
            System.out.println();
            System.out.println("HO2 Thème 1 (graphe orienté)");
            System.out.println();
            System.out.println("1. H01 Thème 1 problematique 1");
            System.out.println("2. H01 Thème 1 problematique 2");
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

    private static void menuProblematique1(Scanner sc) {
        while (true) {
            System.out.println();
            System.out.println("1. H01 Thème 1 problematique 1 hypothese 1");
            System.out.println("2. H01 Thème 1 problematique 1 hypothese 2");
            System.out.println("0. retour");
            System.out.println();
            System.out.print("Votre choix : ");

            String choixHyp = sc.nextLine().trim();
            if (choixHyp.equals("0")) return;

            try {
                graph g = new graph();
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
    public static void pb1hypothese1(graph g, Scanner sc) {

        System.out.println("\nHypothèse 1");
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
        int u = uObj;
        int v = vObj;
        int wUV = getDistanceArc(g, u, v);
        int wVU = getDistanceArc(g, v, u);
        if (wUV < 0 && wVU < 0) {
            System.out.println("Erreur : il n’existe aucun arc entre " + nomU + " et " + nomV + ".\n");
            return;
        }
        if (wUV < 0 && wVU >= 0) {
            int tmp = u; u = v; v = tmp;
            String tmpNom = nomU; nomU = nomV; nomV = tmpNom;
            int tmpW = wUV; wUV = wVU; wVU = tmpW;
        }
        Resultat aller = dijkstra.executer(g.getAdj(), idCT);
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
        if (wUV >= 0 && wVU < 0) {
            if (retour.dist[v] == INF) {
                System.out.println("Le sommet " + nomV + " ne peut pas revenir à CT.\n");
                return;
            }

            int cout = aller.dist[u] + wUV + retour.dist[v];

            List<Integer> chemin = new ArrayList<>();
            chemin.addAll(aller.reconstruireChemin(u));
            chemin.add(v);
            List<Integer> vVersCt = retour.reconstruireChemin(v);
            Collections.reverse(vVersCt);
            for (int i = 1; i < vVersCt.size(); i++) {
                chemin.add(vVersCt.get(i));
            }

            meilleurChemin = chemin;
            meilleurCout   = cout;
            description    = "Cas 1 (sens unique U->V) : CT -> ... -> U -> V -> ... -> CT";
        }
        if (wUV >= 0 && wVU >= 0) {
            if (aller.dist[v] == INF || retour.dist[u] == INF || retour.dist[v] == INF) {
                System.out.println("Certains chemins aller/retour ne sont pas atteignables.\n");
            } else {

                int dCTU = aller.dist[u];
                int dCTV = aller.dist[v];
                int dUCT = retour.dist[u];
                int dVCT = retour.dist[v];
                int cout1 = dCTU + wUV + wVU + dUCT;
                int cout2 = dCTU + wUV + dVCT;
                int cout3 = dCTV + wVU + wUV + dUCT;
                int cout4 = dCTV + wVU + dVCT;
                {
                    List<Integer> c = new ArrayList<>();
                    c.addAll(aller.reconstruireChemin(u));
                    c.add(v);
                    c.add(u);
                    List<Integer> seq = retour.reconstruireChemin(u);
                    Collections.reverse(seq);               // U -> ... -> CT
                    for (int i = 1; i < seq.size(); i++) c.add(seq.get(i));

                    if (cout1 < meilleurCout) {
                        meilleurCout   = cout1;
                        meilleurChemin = c;
                        description    = "Cas 2 – chemin 1 : CT -> ... -> U -> V -> U -> ... -> CT";
                    }
                }
                {
                    List<Integer> c = new ArrayList<>();
                    c.addAll(aller.reconstruireChemin(u));
                    c.add(v);
                    List<Integer> seq = retour.reconstruireChemin(v);
                    Collections.reverse(seq);
                    for (int i = 1; i < seq.size(); i++) c.add(seq.get(i));

                    if (cout2 < meilleurCout) {
                        meilleurCout   = cout2;
                        meilleurChemin = c;
                        description    = "Cas 2 – chemin 2 : CT -> ... -> U -> V -> ... -> CT";
                    }
                }
                {
                    List<Integer> c = new ArrayList<>();
                    c.addAll(aller.reconstruireChemin(v));
                    c.add(u);
                    c.add(v);
                    List<Integer> seq = retour.reconstruireChemin(u);
                    Collections.reverse(seq);
                    for (int i = 1; i < seq.size(); i++) c.add(seq.get(i));

                    if (cout3 < meilleurCout) {
                        meilleurCout   = cout3;
                        meilleurChemin = c;
                        description    = "Cas 2 – chemin 3 : CT -> ... -> V -> U -> V -> U -> ... -> CT";
                    }
                }
                {
                    List<Integer> c = new ArrayList<>();
                    c.addAll(aller.reconstruireChemin(v));
                    c.add(u);
                    c.add(v);
                    List<Integer> seq = retour.reconstruireChemin(v);
                    Collections.reverse(seq);
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

    private static class Particulier {
        final int u;
        final int v;

        Particulier(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    public static void pb1hypothese2(graph g, Scanner sc) {

        System.out.println("\n Hypothèse 2");

        int idCT = g.getId("CT");
        if (idCT < 0) {
            System.out.println("Erreur : sommet CT introuvable.\n");
            return;
        }

        System.out.print("Combien de particuliers (≤10) ? ");
        int k = Integer.parseInt(sc.nextLine().trim());

        List<Particulier> particuliers = new ArrayList<>();

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

                int wUV = getDistanceArc(g, u, v);
                int wVU = getDistanceArc(g, v, u);

                if (wUV < 0) continue;

                if (dAu < INF) {
                    int cost1 = dAu + wUV;
                    if (cost1 < bestCost) {
                        bestCost = cost1;
                        bestIdx  = i;

                        List<Integer> seg = resFromA.reconstruireChemin(u);
                        seg.add(v);
                        bestSegment   = seg;
                        bestNewCurrent = v;
                    }
                }

                if (wVU >= 0 && dAv < INF) {
                    int cost2 = dAv + wVU + wUV;
                    if (cost2 < bestCost) {
                        bestCost = cost2;
                        bestIdx  = i;

                        List<Integer> seg = resFromA.reconstruireChemin(v);
                        seg.add(u);
                        seg.add(v);
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

            for (int i = 1; i < bestSegment.size(); i++) {
                tour.add(bestSegment.get(i));
            }

            visite[bestIdx] = true;
            restant--;
            current = bestNewCurrent;
        }
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


    public static void executerCPP(graph g) {
        System.out.println("\nTHÈME 1 – HO2 – Problématique 2 – CPP dirigé\n");

        Integer idCTObj = g.getId("CT");
        if (idCTObj == null) {
            System.out.println("Erreur : sommet CT introuvable.\n");
            return;
        }
        int idCT = idCTObj;

        // Ici on utilise un Hierholzer ORIENTÉ (pas le CPP non orienté de HO1)
        List<Integer> circuit = calculerCircuitEulerienOriente(g, idCT);

        System.out.println("Circuit du postier chinois : ");
        System.out.println(formater(g, circuit));
        System.out.println("\nLongueur du circuit (en nombre d’arcs) : "
                + (circuit.size() - 1));
    }

    public static void problematique2(Scanner sc) {
        System.out.println("\nProblématique 2 – CPP dirigé\n");
        System.out.println();
        System.out.println("1. Cas 2.1 : tous les sommets de degré pair (in = out)");
        System.out.println("2. Cas 2.2 : exactement deux sommets impairs");
        System.out.println("3. Cas 2.3 : cas général (plusieurs sommets impairs)");
        System.out.println("0. Retour");
        System.out.println();
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

    private static int getDistanceArc(graph g, int u, int v) {
        for (rue r : g.getAdj().get(u)) {
            if (r.getDestination() == v) {
                return r.getDistance();
            }
        }
        return -1;
    }

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
                inv.get(v).add(new rue(u, d));
            }
        }
        return inv;
    }

    private static String formater(graph g, List<Integer> list) {
        List<String> s = new ArrayList<>();
        for (int id : list) s.add(g.getNom(id));
        return String.join(" -> ", s);
    }
    private static List<Integer> circuitEulerienOriente(graph g, int start) {
        List<List<rue>> adj = g.getAdj();
        int n = adj.size();
        int[] pos = new int[n];

        Deque<Integer> pile = new ArrayDeque<>();
        List<Integer> circuit = new ArrayList<>();

        pile.push(start);

        while (!pile.isEmpty()) {
            int u = pile.peek();
            List<rue> edges = adj.get(u);

            if (pos[u] < edges.size()) {
                rue e = edges.get(pos[u]++);
                int v = e.getDestination();
                pile.push(v);
            } else {
                circuit.add(pile.pop());
            }
        }
        Collections.reverse(circuit);
        return circuit;
    }
    private static List<Integer> calculerCircuitEulerienOriente(graph g, int start) {
        int n = g.getNbcroisements();

        // Copie modifiable des successeurs orientés
        List<Deque<Integer>> succ = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            succ.add(new ArrayDeque<>());
        }
        for (int u = 0; u < n; u++) {
            for (rue r : g.getAdj().get(u)) {
                succ.get(u).add(r.getDestination()); // u -> v
            }
        }

        Deque<Integer> pile = new ArrayDeque<>();
        List<Integer> circuit = new ArrayList<>();

        pile.push(start);
        while (!pile.isEmpty()) {
            int v = pile.peek();
            Deque<Integer> voisins = succ.get(v);

            if (!voisins.isEmpty()) {
                int u = voisins.pollFirst(); // consomme l'arc v -> u
                pile.push(u);
            } else {
                // plus d'arcs sortants : on ajoute au circuit
                circuit.add(pile.pop());
            }
        }

        // on a construit le circuit à l'envers
        Collections.reverse(circuit);
        return circuit;
    }
}
