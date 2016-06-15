/*
    FlightLogger is an app for logging UAV flights in an efficient, easy, and contained way
    Copyright (C) 2016 Spencer Plant

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package ca.lakeland.plantsd.flightlogger;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class FlightLogsActivity extends HomeScreen implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

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
        lvFlightLogs.setOnItemLongClickListener(this);
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
        customAdapter = new FlightLogsAdapter(this, R.layout.adapter_flight_log_row, stor.getFlightLogs());
        lvFlightLogs.setAdapter(customAdapter);
    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        // Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
        FlightLog fl = stor.getFlightLogs().get(position);
        // Log.i("-----------------|", "that would be " + fl.getDate());
        Intent intent = new Intent(v.getContext(), FlightLogInfoActivity.class);
        intent.putExtra("FLIGHT_LOG", fl);
        intent.putExtra("ADMIN", false);
        startActivity(intent);
    }

    public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id) {
        final int pos = position;
        final View view = v;

        // check if admin
        if (HomeScreen.getAdminLoggedIn()) {
            // pop up a dialog asking if user wishes to delete the pilot

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            // Yes button clicked
                            FlightLog fl = stor.getFlightLogs().get(pos);
                            Intent intent = new Intent(view.getContext(), FlightLogInfoActivity.class);
                            intent.putExtra("FLIGHT_LOG", fl);
                            intent.putExtra("ADMIN", true);
                            startActivity(intent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            // No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(FlightLogsActivity.this);
            builder.setMessage("Do you wish to add Admin comments to this flight log?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }

        return true;
    }
}
