package app;
import graph.*;
import scenario.HO1Prob1Hyp1;

public class Main {
    public static void main(String[] args) throws Exception {
        graph g = new graph();
        g.chargerDepuisFichier("graph1.txt");  // HO1
        HO1Prob1Hyp1 scenario = new HO1Prob1Hyp1();
        scenario.calculer(g, "CT", "I3", "I4");
    }
}