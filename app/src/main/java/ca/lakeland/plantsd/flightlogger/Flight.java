package ca.lakeland.plantsd.flightlogger;

/**
 * Created by plantsd on 5/25/2016.
 */
public class Flight {

    private String takeoffTime;
    private String landTime;
    private int flightTime; // total time in the air.
    private int battPackNum;
    private int battStartVoltage;
    private int battEndVoltage;

    public Flight(String takeoffTime, String landTime, int flightTime,
                  int packNum, int startVoltage, int endVoltage) {
        this.takeoffTime = takeoffTime;
        this.landTime = landTime;
        this.flightTime = flightTime;
        this.battPackNum = packNum;
        this.battStartVoltage = startVoltage;
        this.battEndVoltage = endVoltage;
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

    public int getBattPackNum() {
        return battPackNum;
    }

    public int getBattStartVoltage() {
        return battStartVoltage;
    }

    public int getBattEndVoltage() {
        return battEndVoltage;
    }
}
