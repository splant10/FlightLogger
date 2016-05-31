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
                txt3.setText("Pilot: " + fl.getPilot().getName() + "  |  Spotter: " + fl.getSpotter());
            }
        }
        return v;
    }
}
