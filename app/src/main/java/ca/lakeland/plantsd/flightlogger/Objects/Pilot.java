package ca.lakeland.plantsd.flightlogger.Objects;

import java.io.Serializable;

/**
 * Created by plantsd on 5/20/2016.
 */
public class Pilot implements Serializable {

    String name;
    int takeoffsAndLandings;
    int flightTime;

    public Pilot(String name) {
        this.name = name;
        this.takeoffsAndLandings = 0;
        this.flightTime = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTakeoffsAndLandings() {
        return takeoffsAndLandings;
    }

    public void incrementTakeoffsAndLandings() {
        this.takeoffsAndLandings += 1;
    }

    public int getFlightTime() {
        return flightTime;
    }

    public void addToFlightTime(int flightTime) {
        this.flightTime += flightTime;
    }
}
