package ca.lakeland.plantsd.flightlogger;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class NewPilotActivity extends PilotsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pilot);
    }

    public void btnCancelPilotClick(View view) {
        finish();
    }


    public void btnConfirmPilotClick(View view) {
        EditText pilotNameField = (EditText) findViewById(R.id.etPilotName);
        String name = pilotNameField.getText().toString();
        Pilot p = new Pilot(name);
        HomeScreen.getPilotList().add(p);
        Log.d("..................", HomeScreen.getPilotList().toString());
        finish();
    }

    public void updatePilotList(ArrayList<Pilot> pilots) {
        ListView lvPilots = (ListView) findViewById(R.id.lvPilots);
        PilotsAdapter customAdapter = new PilotsAdapter(this, R.layout.adapter_pilot_row, pilots);
        lvPilots.setAdapter(customAdapter);
    }
}
