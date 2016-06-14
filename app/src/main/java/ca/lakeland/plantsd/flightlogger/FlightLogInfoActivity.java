package ca.lakeland.plantsd.flightlogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/*
A flea and a fly in a flue
Were imprisoned, so what could they do?
Said the fly, "let us flee!"
"Let us fly!" said the flea.
So they flew through a flaw in the flue.
 */

public class FlightLogInfoActivity extends FlightLogsActivity {

    Storage stor;
    public String selectedEmail;
    private FlightLog fl;

    String root = Environment.getExternalStorageDirectory().toString();
    File myDir;
    int EMAIL = 101; // result code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_log_info);
        fl = (FlightLog) getIntent().getSerializableExtra("FLIGHT_LOG");
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

            Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mActionBarToolbar);
            getSupportActionBar().setTitle("Flight Log #" + fl.getFlightLogNum());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void onEmailClick(View view) {
        // Hold on to your butts

        // http://www.learn-android-easily.com/2013/01/adding-radio-buttons-in-dialog.html
        final CharSequence[] emails = stor.getEmails().toArray(new CharSequence[0]);

        if (emails.length == 0) {
            Toast.makeText(FlightLogInfoActivity.this, "There aren't any email addresses stored. User the menubar to add emails", Toast.LENGTH_LONG);
        } else {
            // popup alertdialog with edittext
            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context)
                    .setTitle("Choose Recipient");

            alertDialog.setSingleChoiceItems(emails, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    selectedEmail = emails[item].toString();
                }
            });
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    // http://stackoverflow.com/questions/9974987/how-to-send-an-email-with-a-file-attachment-in-android
                    String outfileName = "flightLogFile.txt";
                    File outfile = new File(myDir, outfileName);

                    try {
                        FileOutputStream fos = new FileOutputStream(outfile);
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String jsonData = gson.toJson(fl);
                        //System.out.println("Saving:  " + jsonData);
                        fos.write(jsonData.getBytes());
                        fos.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Uri path = Uri.fromFile(outfile);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    // set intent type to email
                    i.setType("vnd.android.cursor.dir/email");

                    String recipient[] = {selectedEmail};
                    i.putExtra(Intent.EXTRA_EMAIL, recipient);
                    i.putExtra(Intent.EXTRA_SUBJECT, "Flight Log #" + fl.getFlightLogNum() + ", " + fl.getDate());
                    i.putExtra(Intent.EXTRA_TEXT, "Please find attached the file for this flight log");
                    i.putExtra(Intent.EXTRA_STREAM, path);

                    try {
                        // http://stackoverflow.com/questions/13872569/how-to-delete-file-from-sd-card-after-mail-send-successfully
                        startActivityForResult(Intent.createChooser(i, "Send mail..."), EMAIL);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(FlightLogInfoActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }

                    myDir.deleteOnExit();
                    //System.out.println("Deleted .../FL_temp: " + deleted);
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();
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
