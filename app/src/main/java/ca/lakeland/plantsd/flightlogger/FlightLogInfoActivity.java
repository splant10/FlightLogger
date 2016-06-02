package ca.lakeland.plantsd.flightlogger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FlightLogInfoActivity extends FlightLogsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_log_info);
        FlightLog fl = (FlightLog) getIntent().getSerializableExtra("FLIGHT_LOG");

        // Fill out form with flightlog info
        TextView txtNumber = (TextView) findViewById(R.id.txtFLInfoNumber);
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

        txtNumber.setText("Flight Log #" + fl.getFlightLogNum());
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
        }



    }

    public void onEmailClick(View view) {
        //String filename="contacts_sid.vcf"; // this file is a dummy one to hold a place here
        //File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        //Uri path = Uri.fromFile(filelocation);

        Intent i = new Intent(Intent.ACTION_SEND);
        // set intent type to email
        i.setType("vnd.android.cursor.dir/email");

        String recipient[] = {"splant.10@gmail.com"};
        i.putExtra(Intent.EXTRA_EMAIL, recipient);
        i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        i.putExtra(Intent.EXTRA_TEXT, "BOOODDDYYY bois");
        // i.putExtra(Intent.EXTRA_STREAM, path);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
