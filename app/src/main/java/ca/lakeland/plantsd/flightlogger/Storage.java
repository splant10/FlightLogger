package ca.lakeland.plantsd.flightlogger;

import java.util.List;

/**
 * Created by plantsd on 6/8/2016.
 */
public class Storage {
    FlightNum flightNum;
    List<Pilot> pilots;
    List<String> spotters;
    List<DoneChecklist> doneChecklists;
    List<FlightLog> flightLogs;

    public Storage() {
        flightNum = HomeScreen.getFlightNum();
        pilots = HomeScreen.getPilotList();
        spotters = HomeScreen.getSpotterList();
        doneChecklists = HomeScreen.getCheckLists();
        flightLogs = HomeScreen.getFlightLogs();
    }

    public FlightNum getFlightNum() {
        return flightNum;
    }

    public void setFlightNum(FlightNum flightnum) {
        this.flightNum = flightnum;
    }

    public List<Pilot> getPilots() {
        return pilots;
    }

    public void setPilots(List<Pilot> pilots) {
        this.pilots = pilots;
    }

    public List<String> getSpotters() {
        return spotters;
    }

    public void setSpotters(List<String> spotters) {
        this.spotters = spotters;
    }

    public List<DoneChecklist> getDoneChecklists() {
        return doneChecklists;
    }

    public void setDoneChecklists(List<DoneChecklist> doneChecklists) {
        this.doneChecklists = doneChecklists;
    }

    public List<FlightLog> getFlightLogs() {
        return flightLogs;
    }

    public void setFlightLogs(List<FlightLog> flightLogs) {
        this.flightLogs = flightLogs;
    }
}
