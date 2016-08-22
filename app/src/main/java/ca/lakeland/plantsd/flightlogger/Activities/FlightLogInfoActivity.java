package ca.lakeland.plantsd.flightlogger.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ca.lakeland.plantsd.flightlogger.Objects.AdminComment;
import ca.lakeland.plantsd.flightlogger.Objects.Flight;
import ca.lakeland.plantsd.flightlogger.Objects.FlightLog;
import ca.lakeland.plantsd.flightlogger.Background.FlightLogToCSV;
import ca.lakeland.plantsd.flightlogger.R;
import ca.lakeland.plantsd.flightlogger.Objects.Storage;

/*
A flea and a fly in a flue
Were imprisoned, so what could they do?
Said the fly, "let us flee!"
"Let us fly!" said the flea.
So they flew through a flaw in the flue.
 */

public class FlightLogInfoActivity extends AppCompatActivity {

    Storage stor;
    public String selectedEmail;
    private FlightLog fl;
    private boolean adminMode;
    private EditText etAdminComment;

    String root = Environment.getExternalStorageDirectory().toString();
    File myDir;
    int EMAIL = 101; // result code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_log_info);

        // Put a back arrow in the toolbar
        // icon is selected in style/Apptheme
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fl = (FlightLog) getIntent().getSerializableExtra("FLIGHT_LOG");
        adminMode = (Boolean) getIntent().getSerializableExtra("ADMIN");

        stor = Storage.getInstance();

        myDir = new File(root + "/FL_temp");
        myDir.mkdirs();

        // Fill out form with flightlog info
        TextView txtSerial = (TextView) findViewById(R.id.txtFLSerial2);
        TextView txtDate = (TextView) findViewById(R.id.txtFLDate2);
        TextView txtLocation = (TextView) findViewById(R.id.txtFLLocation2);
        TextView txtPilot = (TextView) findViewById(R.id.txtFLPilot2);
        TextView txtSpotter = (TextView) findViewById(R.id.txtFLSpotter2);
        TextView txtWind = (TextView) findViewById(R.id.txtFLWind2);
        TextView txtTemp = (TextView) findViewById(R.id.txtFLTemp2);
        TextView txtWeather = (TextView) findViewById(R.id.txtFLWeather2);
        TextView txtPurpose = (TextView) findViewById(R.id.txtFLPurpose2);
        TextView txtPayload = (TextView) findViewById(R.id.txtFLPayload2);
        TextView txtAltitude = (TextView) findViewById(R.id.txtFLAltitude2);
        TextView txtComments = (TextView) findViewById(R.id.txtFLComments2);

        txtSerial.setText(fl.getSerialNum());
        txtDate.setText(fl.getDate());
        txtLocation.setText(fl.getLocation());
        txtPilot.setText(fl.getPilot().getName());
        txtSpotter.setText(fl.getSpotter());
        txtWind.setText(String.valueOf(fl.getWindSpeed()) + " km/h");
        txtTemp.setText(String.valueOf(fl.getTemperature()) + " \u2103");
        txtWeather.setText(fl.getWeatherConditions());
        txtPurpose.setText(fl.getPurposeOfFlight());
        txtPayload.setText(fl.getPayloadType());
        txtAltitude.setText(String.valueOf(fl.getMaxAltitude()) + " ft");
        txtComments.setText(fl.getComments());

        // Set parameters for the layout to be included for each flight below
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = 6;
        lp.setMargins(margin, margin, margin, margin);

        LinearLayout adminLayout = (LinearLayout) findViewById(R.id.llFLAdmin);
        LinearLayout infoLayout = (LinearLayout) findViewById(R.id.llFLInfoFlights);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ArrayList<Flight> flights = fl.getFlights();
        for (int i=0; i < flights.size(); ++i) {
            Flight f = flights.get(i);

            View newView = inflater.inflate(R.layout.layout_flight_info_form, null);
            TextView txtFlightNum = (TextView) newView.findViewById(R.id.txtFLFlightNum);
            TextView txtTakeoff = (TextView) newView.findViewById(R.id.txtFLTakeoff2);
            TextView txtLand = (TextView) newView.findViewById(R.id.txtFLLand2);
            TextView txtTime = (TextView) newView.findViewById(R.id.txtFLTime2);
            TextView txtBattNum = (TextView) newView.findViewById(R.id.txtFLBttery2);
            TextView txtStartVolt = (TextView) newView.findViewById(R.id.txtFLStartVolt2);
            TextView txtEndVolt = (TextView) newView.findViewById(R.id.txtFLEndVolt2);

            txtFlightNum.setText("Flight #" + String.valueOf(i+1) + ":");
            txtTakeoff.setText(f.getTakeoffTime());
            txtLand.setText(f.getLandTime());
            txtTime.setText(f.getFlightTime() + " minutes");
            txtBattNum.setText(f.getBattPackNum());
            txtStartVolt.setText(String.valueOf(f.getBattStartVoltage()) + " V");
            txtEndVolt.setText(String.valueOf(f.getBattEndVoltage()) + " V");

            newView.setLayoutParams(lp);
            infoLayout.addView(newView);
        }

        // Add each admin comment box to the info layout
        List<AdminComment> adminComments = fl.getAdminComments();
        if (adminComments != null) {
            for (int i = 0; i < adminComments.size(); ++i) {
                AdminComment aComment = adminComments.get(i);

                View adminCommentsView = inflater.inflate(R.layout.layout_admin_comments, null);
                TextView txtAdminComments = (TextView) adminCommentsView.findViewById(R.id.txtAdminComments);
                EditText etAdminComment = (EditText) adminCommentsView.findViewById(R.id.etAdminComment);

                String myString = "Admin Comments - " + aComment.getDate();
                txtAdminComments.setText(myString);
                etAdminComment.setText(aComment.getComment());
                etAdminComment.setEnabled(false);

                Button btnSubmitAdminComment = (Button) adminCommentsView.findViewById(R.id.btnSaveAdminComments);

                // http://stackoverflow.com/questions/3805599/add-delete-view-from-layout
                ((ViewGroup) btnSubmitAdminComment.getParent()).removeView(btnSubmitAdminComment);

                adminCommentsView.setLayoutParams(lp);
                infoLayout.addView(adminCommentsView);

            }
        }

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Flight Log #" + fl.getFlightLogNum());

        if (adminMode) { // if we long clicked as an admin and find ourselves here
            View newAdminCommentsView = inflater.inflate(R.layout.layout_admin_comments, null);
            etAdminComment = (EditText) newAdminCommentsView.findViewById(R.id.etAdminComment);
            etAdminComment.setEnabled(true);

            Button btnSubmitAdminComment = (Button) newAdminCommentsView.findViewById(R.id.btnSaveAdminComments);
            btnSubmitAdminComment.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Calendar myCalendar = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat(("MMM dd, yyyy"));
                    String today = df.format(myCalendar.getTime());

                    AdminComment ac = new AdminComment(etAdminComment.getText().toString(), today);
                    if (fl.getAdminComments() == null) {
                        List<AdminComment> comments = Arrays.asList(ac);
                        fl.setAdminComments(comments);
                    } else {
                        fl.getAdminComments().add(ac);
                    }
                    int index = fl.getFlightLogNum() - 1;
                    stor.getFlightLogs().remove(index);
                    stor.getFlightLogs().add(index, fl);

                    // amend the csv line regarding that flight log
                    try {
                        List<String> csvLines = stor.getStringListFromFile();
                        // convert this fl to csv string.
                        FlightLogToCSV fltcsv = new FlightLogToCSV("\",\"");
                        String flCsv = fltcsv.flightToCSV(fl);
                        csvLines.remove(index+1); // +1 is to account for the header line in the csv
                        csvLines.add(index+1, flCsv);
                        // build the result csv:
                        String oneString = new String();
                        for (String line : csvLines) {
                            oneString += line;
                            oneString += "\n";
                        }
                        // System.out.println(oneString); // seems to work out well enough.
                        stor.setCsvFile(oneString);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            });

            adminLayout.addView(newAdminCommentsView);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // if choosing back arrow
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==EMAIL){
            try {
                // http://stackoverflow.com/questions/4943629/how-to-delete-a-whole-folder-and-content
                if (myDir.isDirectory()) {
                    // clear 'myDir'
                    String[] children = myDir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        boolean deleted = new File(myDir, children[i]).delete();
                        // System.out.println("myDir["+i+"] deleted: " + deleted);
                    }
                }
            } catch (Exception e) {

            }
        }
    }
}
