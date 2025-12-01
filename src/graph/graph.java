package graph;
import java.io.*;
import java.util.*;

public class graph {
    private final List<List<rue>> adj;
    private final Map<String, Integer> nomVersId;
    private final List<croisement> croisements;
    private final List<Integer> capacites;

    public graph() {
        this.adj = new ArrayList<>();
        this.nomVersId = new HashMap<>();
        this.croisements = new ArrayList<>();
        this.capacites = new ArrayList<>();
    }

    private int ajouterCroisement(String nom) {
        if (nomVersId.containsKey(nom)) {
            return nomVersId.get(nom);
        }
        int id = croisements.size();
        nomVersId.put(nom, id);
        croisements.add(new croisement(id, nom));
        adj.add(new ArrayList<>());
        capacites.add(0);
        return id;
    }

    public void ajouterRueDoubleSens(String a, String b, int distance) {
        int A = ajouterCroisement(a);
        int B = ajouterCroisement(b);

        adj.get(A).add(new rue(A, B, distance));
        adj.get(B).add(new rue(B, A, distance));
    }

    public void chargerDepuisFichier(String fichier) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.isEmpty() || ligne.startsWith("#")) {
                    continue;
                }
                String[] parts = ligne.split("\\s+");
                if (parts.length == 3) {
                    String a = parts[0];
                    String b = parts[1];
                    int dist = Integer.parseInt(parts[2]);
                    ajouterRueDoubleSens(a, b, dist);
                }
                else if (parts.length == 4) {
                    String a = parts[0];
                    String b = parts[1];
                    int dist = Integer.parseInt(parts[2]);
                    int capaciteB = Integer.parseInt(parts[3]);

                    int A = ajouterCroisement(a);
                    int B = ajouterCroisement(b);

                    adj.get(A).add(new rue(A, B, dist));
                    adj.get(B).add(new rue(B, A, dist));
                    capacites.set(B, capaciteB);
                }
            }
        }
    }
    public int getId(String nom) {
        return nomVersId.get(nom);
    }

    public String getNom(int id) {
        return croisements.get(id).getNom();
    }

    public List<List<rue>> getAdj() {
        return adj;
    }
    public int getNbcroisements() {
        return croisements.size();
    }
    public croisement getcroisement(int id) {
        return croisements.get(id);
    }
    public List<croisement> getCroisements() {
        return Collections.unmodifiableList(croisements);
    }
    public int getDegre(int id) {
        return adj.get(id).size();
    }
    public int getCapacite(int id) {
        return capacites.get(id);
    }
}
