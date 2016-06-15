/*
    FlightLogger is an app for logging UAV flights in an efficient, easy, and contained way
    Copyright (C) 2016 Spencer Plant

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package ca.lakeland.plantsd.flightlogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by plantsd on 6/8/2016.
 *
 * some code structure from
 * http://stackoverflow.com/questions/15792186/singleton-pattern-with-combination-of-lazy-loading-and-thread-safety
 */
public class Storage {

    private FlightNum flightNum;
    private List<Pilot> pilots;
    private List<String> spotters;
    private List<DoneChecklist> doneChecklists;
    private List<FlightLog> flightLogs;
    private List<String> emails;
    private String adminPassword;

    private List<String> payloads = Arrays.asList("QX100","RX100","ADC-Micro");
    public List<String> getPayloads() {
        return payloads;
    }

    private Storage() {
        flightNum = getFlightNum();
        pilots = getPilots();
        spotters = getSpotters();
        doneChecklists = getDoneChecklists();
        flightLogs = getFlightLogs();
        emails = getEmails();
        adminPassword = "admin"; // beaut
    }

    private static class LazyHolder {
        private static final Storage INSTANCE = new Storage();
    }

    public static Storage getInstance() {
        return LazyHolder.INSTANCE;
    }

    // Lazy singleton structure for global objects
    public FlightNum getFlightNum() {
        if (flightNum == null) {
            flightNum = new FlightNum();
        }
        return flightNum;
    }


    public List<Pilot> getPilots() {
        if (pilots == null) {
            pilots = new ArrayList<Pilot>();
        }
        return pilots;
    }

    public List<String> getSpotters() {
        if (spotters == null) {
            spotters = new ArrayList<String>();
        }
        return spotters;
    }


    public List<DoneChecklist> getDoneChecklists() {
        if (doneChecklists == null) {
            doneChecklists = new ArrayList<DoneChecklist>();
        }
        return doneChecklists;
    }


    public List<FlightLog> getFlightLogs() {
        if (flightLogs == null) {
            flightLogs = new ArrayList<FlightLog>();
        }
        return flightLogs;
    }

    public List<String> getEmails() {
        if (emails == null) {
            emails = new ArrayList<>();
        }
        return emails;
    }

    // Only the best security for us.
    public String getAdminPassword() {
        return this.adminPassword;
    }

    // Setters
    public void setFlightNum(FlightNum flightnum) {
        this.flightNum = flightnum;
    }

    public void setPilots(List<Pilot> pilots) {
        this.pilots = pilots;
    }

    public void setSpotters(List<String> spotters) {
        this.spotters = spotters;
    }

    public void setDoneChecklists(List<DoneChecklist> doneChecklists) {
        this.doneChecklists = doneChecklists;
    }

    public void setFlightLogs(List<FlightLog> flightLogs) {
        this.flightLogs = flightLogs;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public void setAdminPassword(String pw) {
        this.adminPassword = pw;
    }

    public void clearAllData() {
        this.flightNum = null;
        this.pilots = null;
        this.spotters = null;
        this.doneChecklists = null;
        this.flightLogs = null;
        this.emails = null;
    }
}
