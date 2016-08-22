package ca.lakeland.plantsd.flightlogger.Objects;

/**
 * Created by plantsd on 5/20/2016.
 */
public class FlightNum {

    private int flightNum;

    public FlightNum() {
        this.flightNum = 1;
    }

    public int getFlightNumber() {
        return this.flightNum;
    }

    public void incrementFlightNum() {
        this.flightNum += 1;
    }

    public void setFlightNum(int i) {
        this.flightNum = i;
    }

}
