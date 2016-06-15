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
