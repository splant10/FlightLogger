package ca.lakeland.plantsd.flightlogger;

/**
 * Created by plantsd on 5/20/2016.
 */
public class FlightNum {

    private static int flightNum;

    public FlightNum() {
        this.flightNum = 0;
    }

    public int getFlightNumber() {
        return this.flightNum;
    }

    public void setFlightNum(int num) {
        this.flightNum = num;
    }

}
