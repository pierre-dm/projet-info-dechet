package graph;

public class rue {
    private final int destination;
    private final int distance;
    private final int poids;

    public rue(int destination, int distance,int poids) {
        this.destination = destination;
        this.distance = distance;
        this.poids = poids;
    }
    public int getDestination() {
        return destination;
    }
    public int getDistance() {
        return distance;
    }
    public int getPoids() {
        return poids;
    }
}
