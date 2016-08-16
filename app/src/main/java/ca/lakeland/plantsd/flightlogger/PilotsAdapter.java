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
            TextView  tt2 = (TextView) v.findViewById(R.id.txtPilotFlights);
            TextView  tt3 = (TextView) v.findViewById(R.id.txtPilotFlightTime);

            if (tt1 != null) {
                tt1.setText(pilot.getName());
            }
            if (tt2 != null) {
                tt2.setText("Flights: "+String.valueOf(pilot.getTakeoffsAndLandings()));
            }
            if (tt3 != null) {
                tt3.setText("Flighttime: "+intToTime(pilot.getFlightTime()));
            }
        }
        return v;
    }

    private String intToTime(Integer i) {
        String result = String.format("%02d:%02d", i/60, i%60);
        return result;
    }

    public void updateList() {
        notifyDataSetChanged();
    }
}
