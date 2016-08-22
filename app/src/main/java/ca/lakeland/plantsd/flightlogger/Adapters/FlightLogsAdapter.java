package ca.lakeland.plantsd.flightlogger.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.lakeland.plantsd.flightlogger.Objects.FlightLog;
import ca.lakeland.plantsd.flightlogger.R;

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

            TextView txt1 = (TextView) v.findViewById(R.id.lvtxtLocation);
            if (txt1 != null) {
                String s = fl.getLocation() + ", " + fl.getPilot().getName() + " and " + fl.getSpotter();
                txt1.setText(s);
            }

            TextView txt2 = (TextView) v.findViewById(R.id.lvtxtLogDate);
            if (txt2 != null) {
                txt2.setText(fl.getDate());
            }
        }
        return v;
    }
}
