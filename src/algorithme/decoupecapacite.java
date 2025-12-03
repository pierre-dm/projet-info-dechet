package algorithme;
import java.util.*;
public class decoupecapacite {
    public static List<List<Integer>> decouper(List<Integer> ordre, int[] capacites, int C, int idDepot) {
        List<List<Integer>> tournees = new ArrayList<>();
        List<Integer> courante = new ArrayList<>();
        int charge = 0;
        courante.add(idDepot);
        int startIndex = 0;
        if (!ordre.isEmpty() && ordre.get(0) == idDepot) {
            startIndex = 1;
        }
        for (int i = startIndex; i < ordre.size(); i++) {
            int v = ordre.get(i);
            int c = capacites[v];

            if (charge + c > C) {
                courante.add(idDepot);
                tournees.add(new ArrayList<>(courante));
                courante.clear();
                courante.add(idDepot);
                charge = 0;
            }

            courante.add(v);
            charge += c;
        }
        courante.add(idDepot);
        tournees.add(courante);
        return tournees;
    }
}
