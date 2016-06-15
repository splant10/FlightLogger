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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PilotInfoActivity extends PilotsActivity {

    String pilotName;
    String pilotFlights;
    String pilotTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilot_info);

        Bundle extras = getIntent().getExtras();
        pilotName = (String) extras.getSerializable("STRING_PILOT_NAME");
        pilotFlights = (String) extras.getSerializable("STRING_PILOT_FLIGHTS");
        pilotTime = (String) extras.getSerializable("STRING_PILOT_TIME");

        TextView textName = (TextView) findViewById(R.id.txtPilotName);
        TextView textFlights = (TextView) findViewById(R.id.txtPilotFlights);
        TextView textTime = (TextView)  findViewById(R.id.txtPilotTime);

        // convert minutes to 00:00 (hours:minutes)
        int time = Integer.parseInt(pilotTime);
        String output = String.valueOf(time/60) + ":"+ String.valueOf(time%60);

        textName.setText(pilotName);
        textFlights.setText(pilotFlights);
        textTime.setText(output);
    }
}
