package ca.lakeland.plantsd.flightlogger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
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
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    Gson gson = new Gson();
    String fileFlightNum = "flightnum-json.txt";
    String filePilots = "pilots-json.txt";
    String fileSpotters = "spotters-json.txt";
    String fileFlightLogs = "flightlogs-json.txt";

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

        // try to access data about previous flight plans and logs
        // will jsonify data on exit, save file locally.
        // getFlightNum(); // this creates a 0 flight num
        // flightNum.setFlightNum(42);

        try {

            Context appContext = this.getApplicationContext();

            // Import flightnum from file
            FileInputStream fis = appContext.openFileInput(fileFlightNum);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader buffreader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = buffreader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            flightNum = gson.fromJson(json, FlightNum.class);

            // Import pilots from file
            fis = appContext.openFileInput(filePilots);
            isr = new InputStreamReader(fis);
            buffreader = new BufferedReader(isr);
            sb = new StringBuilder();
            while ((line = buffreader.readLine()) != null) {
                sb.append(line);
            }
            json = sb.toString();
            Type listOfPilots = new TypeToken<List<Pilot>>(){}.getType();
            pilots = gson.fromJson(json, listOfPilots);

            // Import spotters from file
            fis = appContext.openFileInput(fileSpotters);
            isr = new InputStreamReader(fis);
            buffreader = new BufferedReader(isr);
            sb = new StringBuilder();
            while ((line = buffreader.readLine()) != null) {
                sb.append(line);
            }
            json = sb.toString();
            Type listOfSpotters = new TypeToken<List<String>>(){}.getType();
            spotters = gson.fromJson(json, listOfSpotters);

            // Import flightlogs from file
            fis = appContext.openFileInput(fileFlightLogs);
            isr = new InputStreamReader(fis);
            buffreader = new BufferedReader(isr);
            sb = new StringBuilder();
            while ((line = buffreader.readLine()) != null) {
                sb.append(line);
            }
            json = sb.toString();
            Type listOfFlightLogs = new TypeToken<List<FlightLog>>(){}.getType();
            flightLogs = gson.fromJson(json, listOfFlightLogs);

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

        try {
            System.out.println(appContext);

            // Saving Flightnum
            fos = appContext.openFileOutput(fileFlightNum, Context.MODE_PRIVATE);
            String jsonData = gson.toJson(getFlightNum());
            //System.out.println(jsonData);
            fos.write(jsonData.getBytes());
            fos.close();

            // Saving Pilots
            fos = appContext.openFileOutput(filePilots, Context.MODE_PRIVATE);
            jsonData = gson.toJson(getPilotList());
            //System.out.println(jsonData);
            fos.write(jsonData.getBytes());
            fos.close();

            // Saving Spotters
            fos = appContext.openFileOutput(fileSpotters, Context.MODE_PRIVATE);
            jsonData = gson.toJson(getSpotterList());
            //System.out.println(jsonData);
            fos.write(jsonData.getBytes());
            fos.close();

            // Saving FlightLogs
            fos = appContext.openFileOutput(fileFlightLogs, Context.MODE_PRIVATE);
            jsonData = gson.toJson(getFlightLogs());
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
                        File file1 = new File(dir, fileFlightNum);
                        File file2 = new File(dir, filePilots);
                        File file3 = new File(dir, fileSpotters);
                        File file4 = new File(dir, fileFlightLogs);

                        boolean deleted1 = file1.delete();
                        boolean deleted2 = file2.delete();
                        boolean deleted3 = file3.delete();
                        boolean deleted4 = file4.delete();

                        flightNum = null;
                        pilots = null;
                        spotters = null;
                        flightLogs = null;

                        if (deleted1) {
                            System.out.println("deleted1");
                        }
                        if (deleted2) {
                            System.out.println("deleted2");
                        }
                        if (deleted3) {
                            System.out.println("deleted3");
                        }
                        if (deleted4) {
                            System.out.println("deleted4");
                        }

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
