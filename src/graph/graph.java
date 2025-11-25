package graph;
import java.io.*;
import java.util.*;

public class graph {
    private final List<List<rue>> adj;
    private final Map<String, Integer> nomVersId;
    private final List<croisement> croisements;

    public graph() {
        this.adj = new ArrayList<>();
        this.nomVersId = new HashMap<>();
        this.croisements = new ArrayList<>();
    }

    private int ajouterCroisement(String nom) {
        if (nomVersId.containsKey(nom)) {
            return nomVersId.get(nom);
        }
        int id = croisements.size();
        nomVersId.put(nom, id);
        croisements.add(new croisement(id, nom));
        adj.add(new ArrayList<>());
        return id;
    }

    public void ajouterRueDoubleSens(String a, String b, int distance) {
        int idA = ajouterCroisement(a);
        int idB = ajouterCroisement(b);

        adj.get(idA).add(new rue(idB, distance));
        adj.get(idB).add(new rue(idA, distance));
    }

    public void chargerDepuisFichier(String fichier) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fichier));
        String ligne;

        while ((ligne = br.readLine()) != null) {
            ligne = ligne.trim();
            if (ligne.isEmpty() || ligne.startsWith("#")) continue;

            String[] parts = ligne.split("\\s+");
            if (parts.length != 3){
                continue;
            }
            String a = parts[0];
            String b = parts[1];
            int dist = Integer.parseInt(parts[2]);
            ajouterRueDoubleSens(a, b, dist);
        }
        br.close();
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
}
