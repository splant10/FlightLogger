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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plantsd on 5/27/2016.
 */
public class FlightLogsAdapter  extends ArrayAdapter<FlightLog> {

    List flightLogs;
    LayoutInflater vi;

    public FlightLogsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public FlightLogsAdapter(Context context, int resource, List<FlightLog> flightLogs) {
        super(context, resource, flightLogs);
        this.flightLogs = flightLogs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.adapter_flight_log_row, null);
        }

        FlightLog fl = (FlightLog) flightLogs.get(position);

        if (fl != null) {

            TextView txt1 = (TextView) v.findViewById(R.id.lvtxtLogDate);
            if (txt1 != null) {
                txt1.setText(fl.getDate());
            }

            TextView txt2 = (TextView) v.findViewById(R.id.lvtxtLogPayload);
            if (txt2 != null) {
                txt2.setText(fl.getPayloadType());
            }

            TextView txt3 = (TextView) v.findViewById(R.id.lvtxtLogPilotSpotter);
            if (txt3 != null) {
                txt3.setText("Pilot: " + fl.getPilot().getName() + "   Spotter: " + fl.getSpotter());
            }

            TextView txt4 = (TextView) v.findViewById(R.id.lvtxtLogNum);
            if (txt4 != null) {
                txt4.setText(String.valueOf(fl.getFlightLogNum()));
            }

            TextView txt5 = (TextView) v.findViewById(R.id.lvtxtLogLocation);
            if (txt5 != null) {
                txt5.setText(fl.getLocation());
            }

            TextView txt6 = (TextView) v.findViewById(R.id.lvtxtLogFlights);
            if (txt6 != null) {
                txt6.setText("Flights: " + fl.getFlights().size());
            }
        }
        return v;
    }
}
