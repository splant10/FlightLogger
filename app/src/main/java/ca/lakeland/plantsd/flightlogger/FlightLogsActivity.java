package ca.lakeland.plantsd.flightlogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

public class FlightLogsActivity extends HomeScreen implements AdapterView.OnItemClickListener {

    private ListView lvFlightLogs;
    private FlightLogsAdapter customAdapter;

    Storage stor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stor = Storage.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabFL);
        fab.setOnClickListener(new View.OnClickListener() {
            // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            // .setAction("Action", null).show();
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewFlightLogActivity.class);
                startActivity(intent);
            }
        });

        // Set up the pilots and spotters list views
        lvFlightLogs = (ListView) findViewById(R.id.lvFlightLogs);
        customAdapter = new FlightLogsAdapter(this, R.layout.adapter_flight_log_row, stor.getFlightLogs());
        lvFlightLogs.setAdapter(customAdapter);
        lvFlightLogs.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        // Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
        FlightLog fl = stor.getFlightLogs().get(position);
        // Log.i("-----------------|", "that would be " + fl.getDate());
        Intent intent = new Intent(v.getContext(), FlightLogInfoActivity.class);
        intent.putExtra("FLIGHT_LOG", fl);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // this reeaally doesn't seem proper to me. Would much rather do a
        //      adapter.notifyDataSetChanged()
        // type of call, but that just doesn't want to work here. or anywhere for that matter
        customAdapter = new FlightLogsAdapter(this, R.layout.adapter_flight_log_row, stor.getFlightLogs());
        lvFlightLogs.setAdapter(customAdapter);
    }
}
