package ca.lakeland.plantsd.flightlogger;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    Gson gson = new Gson();
    private String storageFileName = "storage-json.txt";
    // lazy singleton for getting stored data
    private Storage stor;

    static boolean adminLoggedIn = false;

    Context ctxt = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            stor.setEmails(storageTemp.getEmails());
            stor.setAdminPassword(storageTemp.getAdminPassword());

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Set the fragment initially
        PilotsFragment fragment = new PilotsFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, "PILOT_FRAGMENT");
        fragmentTransaction.commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment pilotsFragment = getSupportFragmentManager().findFragmentByTag("PILOT_FRAGMENT");
                Fragment checklistFragment = getSupportFragmentManager().findFragmentByTag("CHECKLIST_FRAGMENT");

                if (pilotsFragment != null && pilotsFragment.isVisible()) {
                    pilotFabClick();
                } else if (checklistFragment != null && checklistFragment.isVisible()) {
                    checklistFabClick();
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    // Save data on pause
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



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // Selecting different options in the side menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_checklist) {
            ChecklistFragment fragment = new ChecklistFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment, "CHECKLIST_FRAGMENT");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_flightlog) {
            clearStorage();

        } else if (id == R.id.nav_pilot) {
            PilotsFragment fragment = new PilotsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment, "PILOT_FRAGMENT");
            fragmentTransaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    // Handle the FAB when on the pilot/spotter fragment
    private void pilotFabClick() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
        builder.setTitle("New Pilot or Spotter");

        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        final LinearLayout popupLayout = (LinearLayout)li.inflate(R.layout.new_pilot_popup, null);
        builder.setView(popupLayout);

        /////////////////////////////////////////
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText editText = (EditText) popupLayout.findViewById(R.id.etPilotName);
                String name = editText.getText().toString();
                RadioButton rb = (RadioButton) popupLayout.findViewById(R.id.radioPilot);

                if (rb.isChecked()){ // if checked pilot
                    Pilot pilot = new Pilot(name);
                    stor.getPilots().add(pilot);
                } else { // if checked spotter (unchecked pilot)
                    stor.getSpotters().add(name);
                }

                PilotsFragment.refresh(getSupportFragmentManager());

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    // Handle the FAB when on the checklist fragment
    private void checklistFabClick() {
        Intent intent = new Intent(MainActivity.this, NewChecklistActivity.class);
        startActivity(intent);
    }



    public Storage getStorage(){
        return this.stor;
    }



    public void clearStorage() {
        final Context appContext = MainActivity.this;

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        File dir = getFilesDir();
                        File fileToDelete = new File(dir, storageFileName);

                        Boolean deleted = fileToDelete.delete();

                        if (deleted) {
                            stor.clearAllData();
                            Toast.makeText(appContext, "All local data deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(appContext, "Local storage could not be deleted", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        break;
                }
            }
        };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(appContext);
        builder.setMessage("Do you wish to delete all locally stored data?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}
