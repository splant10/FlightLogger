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
 * Created by plantsd on 5/20/2016.
 * http://stackoverflow.com/questions/8166497/custom-adapter-for-list-view
 */
public class PilotsAdapter extends ArrayAdapter<Pilot> {

    List pilots;
    LayoutInflater vi;

    public PilotsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public PilotsAdapter(Context context, int resource, List<Pilot> pilots) {
        super(context, resource, pilots);
        this.pilots = pilots;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.adapter_pilot_row, null);
        }

        Pilot pilot = (Pilot) pilots.get(position);

        if (pilot != null) {
            TextView  tt1 = (TextView) v.findViewById(R.id.txtPilotName);

            if (tt1 != null) {
                tt1.setText(pilot.getName());
            }
        }
        return v;
    }

    public void updateList() {
        notifyDataSetChanged();
    }
}
