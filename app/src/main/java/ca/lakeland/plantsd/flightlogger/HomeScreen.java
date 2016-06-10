package ca.lakeland.plantsd.flightlogger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    Gson gson = new Gson();

    private String storageFileName = "storage-json.txt";

    // lazy singleton for getting stored data
    private Storage stor;

    boolean adminLoggedIn = false;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

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
            //System.out.println("Loading: " + json);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (adminLoggedIn) {
            getMenuInflater().inflate(R.menu.menu_admin, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                // User chose the "search" action
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    public void openFlightLogger(View view) {
        Intent intent = new Intent(this, FlightLogsActivity.class);
        startActivity(intent);
    }
    public void openFlightPlanner(View view) {
        Intent intent = new Intent(this, ChecklistsActivity.class);
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
