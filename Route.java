package model;

public class Route {

    private String routeId;
    private Station source;
    private Station destination;
    private double distanceKm;

    public Route(String routeId, Station source, Station destination, double distanceKm) {
        this.routeId = routeId;
        this.source = source;
        this.destination = destination;
        this.distanceKm = distanceKm;
    }

    public String getRouteId() {
        return routeId;
    }

    public Station getSource() {
        return source;
    }

    public Station getDestination() {
        return destination;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double calculateDistance() {
        return distanceKm;
    }

    public void displayRoute() {
        System.out.println("Route ID     : " + routeId);
        System.out.println("From         : " + source.getStationName());
        System.out.println("To           : " + destination.getStationName());
        System.out.printf ("Distance     : %.2f km%n", distanceKm);
    }

    public String toFileString() {
        return routeId + "," + source.getStationId() + "," + destination.getStationId() + "," + distanceKm;
    }

    @Override
    public String toString() {
        return "Route ID: " + routeId
                + " | From: " + source.getStationName()
                + " -> To: " + destination.getStationName()
                + " | Distance: " + distanceKm + " km";
    }
}
