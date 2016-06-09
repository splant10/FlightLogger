package ca.lakeland.plantsd.flightlogger;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class NewPilotActivity extends PilotsActivity {

    TextView title;
    TextView header;

    Storage stor;

    private boolean creatingPilot = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pilot);

        stor = Storage.getInstance();

        title = (TextView) findViewById(R.id.tvTitleAddPilot);
        header = (TextView) findViewById(R.id.txtTitleName);
    }

    public void btnCancelPilotClick(View view) {
        finish();
    }


    public void btnConfirmPilotClick(View view) {
        EditText pilotNameField = (EditText) findViewById(R.id.etPilotName);
        String name = pilotNameField.getText().toString();


        if (creatingPilot) {
            Pilot p = new Pilot(name);
            stor.getPilots().add(p);

        } else { // creating a spotter
            stor.getSpotters().add(name);
        }
        finish();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioPilot:
                if (checked) {
                    // Pilot selection
                    creatingPilot = true;
                    title.setText("Add a New Pilot");
                    header.setText("Pilot Name");
                    break;
                }
            case R.id.radioSpotter:
                if (checked) {
                    // Spotter selection.
                    creatingPilot = false;
                    title.setText("Add a New Spotter");
                    header.setText("Spotter Name");
                    break;
                }
        }
    }
}
