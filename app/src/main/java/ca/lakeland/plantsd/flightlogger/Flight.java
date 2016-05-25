package ca.lakeland.plantsd.flightlogger;

/**
 * Created by plantsd on 5/25/2016.
 */
public class Flight {

    private String takeoffTime;
    private String landTime;
    private int flightTime; // total time in the air.

    public Flight(String takeoffTime, String landTime, int flightTime) {
        this.takeoffTime = takeoffTime;
        this.landTime = landTime;
        this.flightTime = flightTime;
    }

    public String getTakeoffTime() {
        return takeoffTime;
    }

    public String getLandTime() {
        return landTime;
    }

    public int getFlightTime() {
        return flightTime;
    }
}
