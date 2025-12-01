package algorithme;
import java.util.*;
public class shortcutting {
    public static List<Integer> appliquer(List<Integer> ordre) {
        Set<Integer> deja = new HashSet<>();
        List<Integer> resultat = new ArrayList<>();
        for (int v : ordre) {
            if (!deja.contains(v)) {
                resultat.add(v);
                deja.add(v);
            }
        }
        return resultat;
    }
}
