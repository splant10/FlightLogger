package ca.lakeland.plantsd.flightlogger.Activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import ca.lakeland.plantsd.flightlogger.Fragments.ChecklistFragment;
import ca.lakeland.plantsd.flightlogger.Fragments.FlightLogsFragment;
import ca.lakeland.plantsd.flightlogger.Objects.Pilot;
import ca.lakeland.plantsd.flightlogger.Fragments.PilotsFragment;
import ca.lakeland.plantsd.flightlogger.R;
import ca.lakeland.plantsd.flightlogger.Background.SettingsMenu;
import ca.lakeland.plantsd.flightlogger.Objects.Storage;

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

    private static final int MENU_ADMIN = Menu.FIRST;
    private static final int MENU_ADD_EMAIL = MENU_ADMIN + 1;
    private static final int MENU_VIEW_EMAILS = MENU_ADD_EMAIL + 1;
    private static final int MENU_EMAIL_FLIGHTLOGS = MENU_VIEW_EMAILS + 1;
    private static final int MENU_LOGIN = MENU_EMAIL_FLIGHTLOGS + 1;
    private static final int MENU_ABOUT = MENU_LOGIN + 1;
    private static final int MENU_CHANGE_ADMIN_PASS = MENU_ABOUT + 1;
    private static final int MENU_CLEAR_DATA = MENU_CHANGE_ADMIN_PASS + 1;

    public static Boolean allowRefresh = false;


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
        FlightLogsFragment fragment = new FlightLogsFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, "FLIGHTLOG_FRAGMENT");
        fragmentTransaction.commit();
        setTitle(R.string.title_flightlogs);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment pilotsFragment = getSupportFragmentManager().findFragmentByTag("PILOT_FRAGMENT");
                Fragment checklistFragment = getSupportFragmentManager().findFragmentByTag("CHECKLIST_FRAGMENT");
                Fragment flightlogFragment = getSupportFragmentManager().findFragmentByTag("FLIGHTLOG_FRAGMENT");

                if (pilotsFragment != null && pilotsFragment.isVisible()) {
                    pilotFabClick();
                } else if (checklistFragment != null && checklistFragment.isVisible()) {
                    checklistFabClick();
                } else if (flightlogFragment != null && flightlogFragment.isVisible()) {
                    flightlogFabClick();
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



    /**
     * Gets called every time the user presses the menu button.
     * Use if your menu is dynamic.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, MENU_ADD_EMAIL, Menu.NONE, "Add an email address");
        menu.add(0, MENU_VIEW_EMAILS, Menu.NONE, "View email addresses");
        menu.add(0, MENU_EMAIL_FLIGHTLOGS, Menu.NONE, "Email flight logs");
        if(adminLoggedIn) {
            menu.add(0, MENU_ADMIN, Menu.NONE, "Admin Logged In").setIcon(R.drawable.ic_lock_open).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, MENU_CHANGE_ADMIN_PASS, Menu.NONE, "Change Admin Password");
            menu.add(0, MENU_ABOUT, Menu.NONE, "About");
            menu.add(0, MENU_CLEAR_DATA, Menu.NONE, "Clear Data");
            menu.add(0, MENU_LOGIN, Menu.NONE, "Administrator Logout");
        } else {
            menu.add(0, MENU_ABOUT, Menu.NONE, "About");
            menu.add(0, MENU_LOGIN, Menu.NONE, "Login as Administrator");
        }

        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ADMIN:
                Toast.makeText(this, "Logged in as administrator", Toast.LENGTH_SHORT).show();
                return true;
            case MENU_ADD_EMAIL:
                SettingsMenu.addEmailAddress(this);
                return true;
            case MENU_VIEW_EMAILS:
                Intent emailIntent = new Intent(this, EmailsActivity.class);
                startActivity(emailIntent);
                return true;
            case MENU_EMAIL_FLIGHTLOGS:
                SettingsMenu.emailFile(this, stor.getCsvFile());
                return true;
            case MENU_CHANGE_ADMIN_PASS:
                SettingsMenu.changeAdminPassword(this);
                return true;
            case MENU_ABOUT:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            case MENU_LOGIN:
                SettingsMenu.adminLoginLogoutButton(this);
                return true;
            case MENU_CLEAR_DATA:
                clearStorage();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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



    // Refresh the fragments on resume.
    @Override
    public void onResume() {
        super.onResume();

        if (allowRefresh) {
            allowRefresh = false;

            // Don't need to refresh Pilots/Spotters fragment, since the activity isn't left when
            // using that fragment.
            Fragment checklistFragment = getSupportFragmentManager().findFragmentByTag("CHECKLIST_FRAGMENT");
            Fragment flightlogFragment = getSupportFragmentManager().findFragmentByTag("FLIGHTLOG_FRAGMENT");

            if (checklistFragment != null && checklistFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction().detach(checklistFragment).attach(checklistFragment).commit();
            } else if (flightlogFragment != null && flightlogFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction().detach(flightlogFragment).attach(flightlogFragment).commit();
            }

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

            setTitle(R.string.title_checklists);

        } else if (id == R.id.nav_flightlog) {
            FlightLogsFragment fragment = new FlightLogsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment, "FLIGHTLOG_FRAGMENT");
            fragmentTransaction.commit();

            setTitle(R.string.title_flightlogs);

        } else if (id == R.id.nav_pilot) {
            PilotsFragment fragment = new PilotsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment, "PILOT_FRAGMENT");
            fragmentTransaction.commit();

            setTitle(R.string.title_pilots);

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


    // Handle the FAB when on the flightlog fragment
    private void flightlogFabClick() {
        Intent intent = new Intent(MainActivity.this, NewFlightLogActivity.class);
        startActivity(intent);
    }



    public Storage getStorage(){
        return this.stor;
    }



    public static Boolean getAdminLoggedIn() {
        return adminLoggedIn;
    }
    public static void setAdminLoggedIn(Boolean bool) {
        adminLoggedIn = bool;
    }



    // should be moved to settings
    public void clearStorage() {
        final Context appContext = MainActivity.this;

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            ((ActivityManager)appContext.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
                            System.out.println("all data cleared");
                        } catch (Exception e) {
                            System.out.println("not a new enough system");
                        }
                        /*
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
                        */



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
