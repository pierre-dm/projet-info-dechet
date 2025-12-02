package app;
import graph.*;
import algorithme.*;
import scenario.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        graph g = new graph();
        g.chargerDepuisFichier("graphtheme2.txt");

        int idCT = g.getId("CT");
        int C = 10;  // capacité max du camion

        HO1theme2.executerEtAfficher(g, C, idCT);

        // === THÈME 3 délégué à HO1theme3 ===
        HO1theme3.executerEtAfficher();
    }
}
