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

    private String storageFileName = "storage-json.txt";

    // lazy singleton for getting stored data
    private Storage stor;

    private FlightNum flightNum;
    private List<Pilot> pilots;
    private List<String> spotters;
    private List<DoneChecklist> checkLists;
    private List<FlightLog> flightLogs;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        stor = Storage.getInstance();

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
            System.out.println("Loading: " + json);

            Storage storageTemp = gson.fromJson(json, Storage.class);

            stor.setFlightNum(storageTemp.getFlightNum());
            stor.setPilots(storageTemp.getPilots());
            stor.setSpotters(storageTemp.getSpotters());
            stor.setDoneChecklists(storageTemp.getDoneChecklists());
            stor.setFlightLogs(storageTemp.getFlightLogs());

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

    @Override
    public void onPause() {
        super.onPause();

        Context appContext = this.getApplicationContext();
        FileOutputStream fos = null;

        try {
            // Saving
            fos = appContext.openFileOutput(storageFileName, Context.MODE_PRIVATE);
            String jsonData = gson.toJson(stor);
            //System.out.println("Saving:  " + jsonData);
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
                        // Yes button clicked
                        File dir = getFilesDir();
                        File fileToDelete = new File(dir, storageFileName);

                        Boolean deleted = fileToDelete.delete();

                        if (deleted) {
                            stor.clearAllData();
                            Toast.makeText(context, "All local data deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Local storage could not be deleted", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
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
