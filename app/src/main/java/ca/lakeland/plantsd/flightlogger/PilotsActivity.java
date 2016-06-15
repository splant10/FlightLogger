package ca.lakeland.plantsd.flightlogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PilotsActivity extends HomeScreen implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView lvPilots;
    private PilotsAdapter pilotsAdapter;

    private ListView lvSpotters;
    ArrayAdapter<String> spotAdapter;

    Storage stor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilots);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        stor = Storage.getInstance();

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

        // Set up the pilots and spotters list views
        lvPilots = (ListView) findViewById(R.id.lvPilots);
        pilotsAdapter = new PilotsAdapter(this, R.layout.adapter_pilot_row, stor.getPilots());
        lvPilots.setAdapter(pilotsAdapter);
        lvPilots.setOnItemClickListener(this);
        lvPilots.setOnItemLongClickListener(this);

        lvSpotters = (ListView) findViewById(R.id.lvSpotters);
        spotAdapter = new ArrayAdapter<String>(this, R.layout.adapter_pilot_row, R.id.txtPilotName, stor.getSpotters());
        lvSpotters.setAdapter(spotAdapter);
        lvSpotters.setOnItemLongClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // this reeaally doesn't seem proper to me. Would much rather do a
        //      adapter.notifyDataSetChanged()
        // type of call, but that just doesn't want to work here. or anywhere for that matter
        pilotsAdapter = new PilotsAdapter(this, R.layout.adapter_pilot_row, stor.getPilots());
        lvPilots.setAdapter(pilotsAdapter);
        spotAdapter = new ArrayAdapter<String>(this, R.layout.adapter_pilot_row, R.id.txtPilotName, stor.getSpotters());
        lvSpotters.setAdapter(spotAdapter);
    }



    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        //Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
        Pilot pilot = stor.getPilots().get(position);
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

    public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id) {
        final int pos = position;
        final AdapterView<?> av = l;

        // check if admin
        if (HomeScreen.getAdminLoggedIn()) {
            // pop up a dialog asking if user wishes to delete the pilot

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            // Yes button clicked
                            if (av.equals(lvPilots)) {
                                stor.getPilots().remove(pos);
                                pilotsAdapter.notifyDataSetChanged();
                                break;
                            } else if (av.equals(lvSpotters)) {
                                stor.getSpotters().remove(pos);
                                spotAdapter.notifyDataSetChanged();
                                break;
                            } else {
                                Toast.makeText(PilotsActivity.this, "well av is weird..", Toast.LENGTH_SHORT).show();
                                System.out.println("----" + av.toString());
                                break;
                            }

                        case DialogInterface.BUTTON_NEGATIVE:
                            // No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(PilotsActivity.this);
            builder.setMessage("Do you wish to remove this person from the app?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }

        return true;
    }

}
