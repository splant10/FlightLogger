package ca.lakeland.plantsd.flightlogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PilotsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvPilots;
    private PilotsAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilots);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabP);

        // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        // .setAction("Action", null).show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewPilotActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvPilots = (ListView) findViewById(R.id.lvPilots);
        customAdapter = new PilotsAdapter(this, R.layout.adapter_pilot_row, HomeScreen.getPilotList());
        lvPilots.setAdapter(customAdapter);

        lvPilots.setOnItemClickListener(this);

    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        //Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
        Pilot pilot = HomeScreen.getPilotList().get(position);
        //Log.i("-----------------|", "that would be " + pilot.getName());
        Intent intent = new Intent(this, PilotInfoActivity.class);
        String pilotName = pilot.getName();
        String pilotFlights = Integer.toString(pilot.getTakeoffsAndLandings());
        String pilotTime = Integer.toString(pilot.getFlightTime());
        intent.putExtra("STRING_PILOT_NAME", pilotName);
        intent.putExtra("STRING_PILOT_FLIGHTS", pilotFlights);
        intent.putExtra("STRING_PILOT_TIME", pilotTime);

        startActivity(intent);
    }
}
