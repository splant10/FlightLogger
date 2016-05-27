package ca.lakeland.plantsd.flightlogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    private static List<String> payloads = Arrays.asList("QX10","QX100","NEX5r","RX100","RX100M2",
            "RX100M3","ADC-Micro","RedEdge","HDR-AS100");
    static public List<String> getPayloads() {
        return payloads;
    }

    // Lazy Singletons for flight plan/log number and all pilots/spotters
    private static FlightNum flightNum = null;
    static public FlightNum getFlightNum() {
        if (flightNum == null) {
            flightNum = new FlightNum();
        }
        return flightNum;
    }

    private static ArrayList<Pilot> pilots = null;
    static public ArrayList<Pilot> getPilotList() {
        if (pilots == null) {
            pilots = new ArrayList<Pilot>();
        }
        return pilots;
    }

    private static ArrayList<String> spotters = null;
    static public ArrayList<String> getSpotterList() {
        if (spotters == null) {
            spotters = new ArrayList<String>();
        }
        return spotters;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // try to access data about previous flight plans and logs
        // will jsonify data on exit, save file locally.
        // getFlightNum(); // this creates a 0 flight num
        // flightNum.setFlightNum(42);
    }

    public void openFlightLogger(View view) {
        Intent intent = new Intent(this, FlightLogsActivity.class);
        startActivity(intent);
    }
    public void openFlightPlanner(View view) {
        Intent intent = new Intent(this, FlightPlansActivity.class);
        startActivity(intent);
    }

    public void btnPilotsClick(View view) {
        Intent intent = new Intent(this, PilotsActivity.class);
        startActivity(intent);
    }

    public static void addPilot(Pilot p) {
        pilots.add(p);
    }
}
