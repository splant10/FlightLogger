package ca.lakeland.plantsd.flightlogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

public class AddPreflightChecklistActivity extends HomeScreen implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preflight_checklist);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabChecklist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPreflightChecklistActivity.this, ChecklistActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        // Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
        FlightLog fl = HomeScreen.getFlightLogs().get(position);
        // Log.i("-----------------|", "that would be " + fl.getDate());
        Intent intent = new Intent(v.getContext(), FlightLogInfoActivity.class);
        intent.putExtra("FLIGHT_LOG", fl);
        startActivity(intent);
    }

}
