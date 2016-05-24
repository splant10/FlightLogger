package ca.lakeland.plantsd.flightlogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewFlightLogActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FlightLog fl;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat(("MMM-dd-yyyy"));
    String today = df.format(c.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flight_log);

        EditText etLogDate = (EditText) findViewById(R.id.etLogDate);
        etLogDate.setText(today);

        // Spinner for pilot selection
        Spinner pilotSpinner = (Spinner) findViewById(R.id.spinLogPilot);
        pilotSpinner.setOnItemSelectedListener(this);
        List<String> pilotNames = new ArrayList<String>();
        ArrayList<Pilot> pilotList = HomeScreen.getPilotList();

        // Iterate over pilots and add to spinner
        for (int i = 0; i < pilotList.size(); ++i) {
            pilotNames.add(pilotList.get(i).getName());
        }
        // Create adapter for the spinner
        ArrayAdapter<String> pilotNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pilotNames);

        // Attach data adapter to spinner
        pilotSpinner.setAdapter(pilotNameAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
