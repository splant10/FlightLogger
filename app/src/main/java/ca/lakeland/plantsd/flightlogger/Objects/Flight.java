package ca.lakeland.plantsd.flightlogger.Objects;

import java.io.Serializable;

/**
 * Created by plantsd on 5/25/2016.
 */
public class Flight implements Serializable {

    private String takeoffTime;
    private String landTime;
    private int flightTime; // total time in the air.
    private String battPackNum;
    private Float battStartVoltage;
    private Float battEndVoltage;

    public Flight(String takeoffTime, String landTime, int flightTime,
                  String packNum, float startVoltage, float endVoltage) {
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

    public String getBattPackNum() {
        return battPackNum;
    }

    public float getBattStartVoltage() {
        return battStartVoltage;
    }

    public float getBattEndVoltage() {
        return battEndVoltage;
    }
}
