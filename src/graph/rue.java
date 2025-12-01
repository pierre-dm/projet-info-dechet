package graph;

public class rue {
    private final int destination;
    private final int distance;

    public rue(int destination, int distance) {
        this.destination = destination;
        this.distance = distance;
    }
    public int getDestination() {
        return destination;
    }
    public int getDistance() {
        return distance;
    }
}
