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
