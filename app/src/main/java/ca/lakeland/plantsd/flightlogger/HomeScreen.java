package ca.lakeland.plantsd.flightlogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    private int flight_num;
    private ArrayList<Pilot> pilots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // try to access data about previous flight plans and logs
        // will jsonify data on exit, save file locally.
        // setFlight_num(somefile.flight_num)
    }

    public void openFlightLogger(View view) {
        Intent intent = new Intent(this, FlightLogsActivity.class);
        startActivity(intent);
    }
    public void openFlightPlanner(View view) {
        Intent intent = new Intent(this, FlightPlansActivity.class);
        startActivity(intent);
    }

    // Getters and Setters
    public int getFlight_num() {
        return flight_num;
    }

    public void setFlight_num(int num) {
        this.flight_num = num;
    }

    public ArrayList<Pilot> getPilots() {
        return this.pilots;
    }

    public void addPilot(Pilot p) {
        pilots.add(p);
    }
}
