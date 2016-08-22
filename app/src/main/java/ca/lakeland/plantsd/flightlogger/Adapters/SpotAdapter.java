package ca.lakeland.plantsd.flightlogger.Adapters;

/**
 * Created by plantsd on 8/16/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.lakeland.plantsd.flightlogger.R;

/**
 * Created by plantsd on 8/16/2016.
 * http://stackoverflow.com/questions/8166497/custom-adapter-for-list-view
 */
public class SpotAdapter extends ArrayAdapter<String> {

    List spotters;
    LayoutInflater vi;

    public SpotAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SpotAdapter(Context context, int resource, List<String> spotters) {
        super(context, resource, spotters);
        this.spotters = spotters;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.adapter_pilot_row, null);
        }

        String spotter = (String) spotters.get(position);

        if (spotter != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.txtPilotName);
            TextView tt2 = (TextView) v.findViewById(R.id.txtPilotFlights);
            TextView tt3 = (TextView) v.findViewById(R.id.txtPilotFlightTime);

            if (tt1 != null) {
                tt1.setText(spotter);
            }
            if (tt2 != null) {
                tt2.setText("");
            }
            if (tt3 != null) {
                tt3.setText("");
            }
        }
        return v;
    }

    public void updateList() {
        notifyDataSetChanged();
    }
}