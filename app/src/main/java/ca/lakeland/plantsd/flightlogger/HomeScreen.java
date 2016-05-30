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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    String fileFlightNum = "flightnum.txt";
    String filePilots = "pilots.txt";
    String fileSpotters = "spotters.txt";
    String fileFlightLogs = "flightLogs.txt";
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

    private static ArrayList<FlightLog> flightLogs = null;
    static public ArrayList<FlightLog> getFlightLogs() {
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

            // import pilots list from file
            fis = this.openFileInput("pilots.txt");
            isr = new InputStreamReader(fis);
            buffreader = new BufferedReader(isr);
            sb = new StringBuilder();
            while ((line = buffreader.readLine()) != null) {
                sb.append(line);
            }
            json = sb.toString();
            // http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            pilots = gson.fromJson(json, new TypeToken<ArrayList<Pilot>>(){}.getType());

            // import spotters list from file
            fis = this.openFileInput("spotters.txt");
            isr = new InputStreamReader(fis);
            buffreader = new BufferedReader(isr);
            sb = new StringBuilder();
            while ((line = buffreader.readLine()) != null) {
                sb.append(line);
            }
            json = sb.toString();
            spotters = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());

            // import flightlogs from file
            fis = this.openFileInput("flightlogs.txt");
            isr = new InputStreamReader(fis);
            buffreader = new BufferedReader(isr);
            sb = new StringBuilder();
            while ((line = buffreader.readLine()) != null) {
                sb.append(line);
            }
            json = sb.toString();
            flightLogs = gson.fromJson(json, new TypeToken<ArrayList<FlightLog>>(){}.getType());

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
        // Store things on pause; onStop() is not guaranteed to be called
        String jsonFlightNum = gson.toJson(getFlightNum().getFlightNumber());
        String jsonPilots = gson.toJson(getPilotList());
        String jsonSpotters = gson.toJson(getSpotterList());
        String jsonFlightLogs = gson.toJson(getFlightLogs());

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(fileFlightNum, Context.MODE_PRIVATE);
            outputStream.write(jsonFlightNum.getBytes());
            outputStream.close();

            outputStream = openFileOutput(filePilots, Context.MODE_PRIVATE);
            outputStream.write(jsonPilots.getBytes());
            outputStream.close();

            outputStream = openFileOutput(fileSpotters, Context.MODE_PRIVATE);
            outputStream.write(jsonSpotters.getBytes());
            outputStream.close();

            outputStream = openFileOutput(fileFlightLogs, Context.MODE_PRIVATE);
            outputStream.write(jsonFlightLogs.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
