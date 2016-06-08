package ca.lakeland.plantsd.flightlogger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeScreen extends AppCompatActivity {

    Gson gson = new Gson();

    String storageFileName = "storage-json.txt";
    Storage storage;

    Context context = this;

    private static List<String> payloads = Arrays.asList("QX100","RX100","ADC-Micro");
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

    private static List<DoneChecklist> checkLists = null;
    static public List<DoneChecklist> getCheckLists() {
        if (checkLists == null) {
            checkLists = new ArrayList<DoneChecklist>();
        }
        return checkLists;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        storage = new Storage();

        // try to access data about previous flight plans and logs

        try {

            Context appContext = this.getApplicationContext();

            // Import things from the file
            FileInputStream fis = appContext.openFileInput(storageFileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader buffreader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = buffreader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();

            storage = gson.fromJson(json, Storage.class);

            flightNum = storage.getFlightNum();
            pilots = storage.getPilots();
            spotters = storage.getSpotters();
            checkLists = storage.getDoneChecklists();
            flightLogs = storage.getFlightLogs();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openFlightLogger(View view) {
        Intent intent = new Intent(this, FlightLogsActivity.class);
        startActivity(intent);
    }
    public void openFlightPlanner(View view) {
        Intent intent = new Intent(this, AddPreflightChecklistActivity.class);
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

        Context appContext = this.getApplicationContext();
        FileOutputStream fos = null;

        // assign all the data.
        try {
            storage.setFlightNum(flightNum);
            storage.setPilots(pilots);
            storage.setSpotters(spotters);
            storage.setDoneChecklists(checkLists);
            storage.setFlightLogs(flightLogs);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            // Saving all objects
            fos = appContext.openFileOutput(storageFileName, Context.MODE_PRIVATE);
            String jsonData = gson.toJson(storage);
            //System.out.println(jsonData);
            fos.write(jsonData.getBytes());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // To be called on click of FlightLogger text on home screen.
    // Verifies the user wishes to clear all data.
    public void clearStoredData(View view) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        File dir = getFilesDir();
                        File fileToDelete = new File(dir, storageFileName);

                        fileToDelete.delete();

                        flightNum = null;
                        pilots = null;
                        spotters = null;
                        checkLists = null;
                        flightLogs = null;

                        Toast.makeText(context, "All local data deleted", Toast.LENGTH_SHORT).show();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        Context appContext = HomeScreen.this;
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setMessage("Do you wish to delete all locally stored data?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void onLogoClick(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }
}
