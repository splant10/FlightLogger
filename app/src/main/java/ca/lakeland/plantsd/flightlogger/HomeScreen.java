package ca.lakeland.plantsd.flightlogger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    Gson gson = new Gson();

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

    private static List<Pilot> pilots = null;
    static public List<Pilot> getPilotList() {
        if (pilots == null) {
            pilots = new ArrayList<Pilot>();
        }
        return pilots;
    }

    private static List<String> spotters = null;
    static public List<String> getSpotterList() {
        if (spotters == null) {
            spotters = new ArrayList<String>();
        }
        return spotters;
    }

    private static List<FlightLog> flightLogs = null;
    static public List<FlightLog> getFlightLogs() {
        if (flightLogs == null) {
            flightLogs = new ArrayList<FlightLog>();
        }
        return flightLogs;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // try to access data about previous flight plans and logs
        // will jsonify data on exit, save file locally.
        // getFlightNum(); // this creates a 0 flight num
        // flightNum.setFlightNum(42);

        try {
            /*
            // Import flightnum from file
            FileInputStream fis = this.openFileInput("flightnum.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader buffreader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = buffreader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            flightNum.setFlightNum(gson.fromJson(json, Integer.class));
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onPause() {
        super.onPause();

        FileOutputStream fos = null;

        try {
            Context appContext = this.getApplicationContext();
            System.out.println(appContext);

            // Saving Flightnum
            fos = appContext.openFileOutput("flightnum-json.txt", Context.MODE_PRIVATE);
            String jsonData = gson.toJson(getFlightNum());
            System.out.println(jsonData);
            fos.write(jsonData.getBytes());
            fos.close();

            // Saving Pilots
            fos = appContext.openFileOutput("pilots-json.txt", Context.MODE_PRIVATE);
            jsonData = gson.toJson(getPilotList());
            System.out.println(jsonData);
            fos.write(jsonData.getBytes());
            fos.close();

            // Saving Spotters
            fos = appContext.openFileOutput("spotters-json.txt", Context.MODE_PRIVATE);
            jsonData = gson.toJson(getSpotterList());
            System.out.println(jsonData);
            fos.write(jsonData.getBytes());
            fos.close();

            // Saving FlightLogs
            fos = appContext.openFileOutput("flightlogs-json.txt", Context.MODE_PRIVATE);
            jsonData = gson.toJson(getFlightLogs());
            System.out.println(jsonData);
            fos.write(jsonData.getBytes());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
