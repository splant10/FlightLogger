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
