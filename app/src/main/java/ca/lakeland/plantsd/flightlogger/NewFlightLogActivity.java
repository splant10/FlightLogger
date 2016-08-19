package ca.lakeland.plantsd.flightlogger;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewFlightLogActivity extends FlightLogsActivity implements AdapterView.OnItemSelectedListener {

    FlightLog fl;

    Storage stor;
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

    Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat(("MMM dd, yyyy"));
    String today = df.format(myCalendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flight_log);

        // Put a back arrow in the toolbar
        // icon is selected in style/Apptheme
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("New Flight Log");

        stor = Storage.getInstance();

        EditText etLogDate = (EditText) findViewById(R.id.etLogDate);
        etLogDate.setText(today);

        // ***********  Spinner for pilot selection ************ //
        Spinner pilotSpinner = (Spinner) findViewById(R.id.spinLogPilot);
        pilotSpinner.setOnItemSelectedListener(this);
        List<String> pilotNames = new ArrayList<String>();
        List<Pilot> pilotList = stor.getPilots();

        // Iterate over pilots and add to spinner
        for (int i = 0; i < pilotList.size(); ++i) {
            pilotNames.add(pilotList.get(i).getName());
        }
        // Create adapter for the spinner
        ArrayAdapter<String> pilotNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pilotNames);
        pilotNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach data adapter to spinner
        pilotSpinner.setAdapter(pilotNameAdapter);
        // *********** End spinner for pilot selection ************ //

        // ***********  Spinner for spotter selection ************ //
        Spinner spotterSpinner = (Spinner) findViewById(R.id.spinLogSpotter);
        spotterSpinner.setOnItemSelectedListener(this);
        List<String> spotterList = stor.getSpotters();

        // Create adapter for the spinner
        ArrayAdapter<String> spotterNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spotterList);
        spotterNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach data adapter to spinner
        spotterSpinner.setAdapter(spotterNameAdapter);
        // *********** End spinner for spotter selection ************ //

        // ***********  Spinner for Payload selection ************ //
        Spinner payloadSpinner = (Spinner) findViewById(R.id.spinLogPayload);
        payloadSpinner.setOnItemSelectedListener(this);
        List<String> payloadList = stor.getPayloads();
        ArrayAdapter<String> payloadAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, payloadList);
        payloadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payloadSpinner.setAdapter(payloadAdapter);
        // *********** End spinner for payload selection ************ //

        // This makes a date picker dialog pop up when clicking the date edittext
        EditText et = (EditText) findViewById(R.id.etLogDate);
        SetDate sd = new SetDate(et, this);

        // Set time picker listeners on the time fields in the first info box
        LinearLayout llLogFlightInfos = (LinearLayout) findViewById(R.id.llLogFlightInfos);
        final EditText et2 = (EditText) llLogFlightInfos.getChildAt(0).findViewById(R.id.etInfoTakeoff);
        et2.setOnTouchListener(timePickerListener);
        final EditText et3 = (EditText) llLogFlightInfos.getChildAt(0).findViewById(R.id.etInfoLand);
        et3.setOnTouchListener(timePickerListener);

        View.OnFocusChangeListener ofcl = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when something loses focus
                    try {
                        // http://stackoverflow.com/questions/4927856/how-to-calculate-time-difference-in-java
                        System.out.println("---------------------yoooooo");
                        String time1 = et2.getText().toString();
                        String time2 = et3.getText().toString();
                        Date date1 = timeFormat.parse(time1);
                        Date date2 = timeFormat.parse(time2);
                        long diff = date2.getTime() - date1.getTime();
                        long diffMinutes = diff / (60 * 1000) % 60;
                        System.out.println("---------------------" + String.valueOf(diffMinutes));
                    } catch (Exception e) {
                    }
                }
            }
        };
        et2.setOnFocusChangeListener(ofcl);
        et3.setOnFocusChangeListener(ofcl);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    public void onNothingSelected(AdapterView<?> arg0) {

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

    public void btnOneMoreFlight(View view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = 6;
        lp.setMargins(margin, margin, margin, margin);

        LinearLayout flightLayout = (LinearLayout) findViewById(R.id.llLogFlightInfos);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView = inflater.inflate(R.layout.activity_flight_info, null);

        EditText takeoffTime = (EditText) newView.findViewById(R.id.etInfoTakeoff);
        takeoffTime.setOnTouchListener(timePickerListener);
        EditText landTime = (EditText) newView.findViewById(R.id.etInfoLand);
        landTime.setOnTouchListener(timePickerListener);

        newView.setLayoutParams(lp);
        flightLayout.addView(newView);

        int childCount = ((ViewGroup) flightLayout).getChildCount();
        if (childCount == 2) { // If there are 2 flight info fields (after having just added one)
            // Make a delete button and place it beside the create button
            Button deleteButton = new Button(this);
            deleteButton.setText("Remove Previous Flight");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    btnOneLessFlight(view);
                }
            });
            LinearLayout llLogInfoButtons = (LinearLayout) findViewById(R.id.llLogInfoButtons);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            // place the button to the left of the create button
            llLogInfoButtons.addView(deleteButton, 0, lp2);
        }

    }

    public void btnOneLessFlight(View view) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.llLogFlightInfos);
        int childCount = ((ViewGroup) layout).getChildCount();
        layout.removeViewAt(childCount-1);

        // If there is only one flight info box left, remove delete button
        if ((childCount-1) == 1) {
            LinearLayout btnLayout = (LinearLayout) findViewById(R.id.llLogInfoButtons);
            btnLayout.removeViewAt(0);
        }
    }


    // to make a text field pop up a time picker dialog, go like this:
    // editText.setOnTouchListener(timePickerListener);
    View.OnTouchListener timePickerListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                final EditText et = (EditText) v;
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NewFlightLogActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String suffix = (selectedHour >= 12) ? " PM" : " AM";
                        selectedHour = selectedHour % 12;
                        // need to pad hours and minutes with zeroes
                        String selectedHourString = (selectedHour == 0) ? "12" : Integer.toString(selectedHour);
                        String selectedMinuteString = (selectedMinute < 10) ? "0" + Integer.toString(selectedMinute) : Integer.toString(selectedMinute);

                        et.setText( "" + selectedHourString + ":" + selectedMinuteString + suffix);
                    }
                }, hour, minute, false); // false: not 24 hour view
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

                return true;
            }
            return false;
        }
    };

    public void btnSubmit(View view) {
        final View v = view;
        // http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        submitFlightLog(v);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please verify the information in this form is correct. It cannot be changed after submission")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void submitFlightLog(View view) {
        try {
            // FlightLog(String serialNum, String date, String location, Pilot pilot, String spotter,
            //          float windSpeed, float temperature, String weatherConditions, String purposeOfFlight,
            //          String payloadType, ArrayList<Flight> flights, String comments, int flightLogNum,
            //          float maxAltitude)
            EditText etSerial = (EditText) findViewById(R.id.etLogSerial);
            EditText etDate = (EditText) findViewById(R.id.etLogDate);
            EditText etLoc = (EditText) findViewById(R.id.etLogLocation);
            EditText etWindSpeed = (EditText) findViewById(R.id.etLogWindSpeed);
            EditText etTemp = (EditText) findViewById(R.id.etLogTemperature);
            EditText etWCond = (EditText) findViewById(R.id.etLogWeather);
            EditText etPurp = (EditText) findViewById(R.id.etLogPurpose);
            EditText etAltitude = (EditText) findViewById(R.id.etLogAltitude);
            LinearLayout llFlightInfos = (LinearLayout) findViewById(R.id.llLogFlightInfos);
            EditText etComments = (EditText) findViewById(R.id.etLogComments);

            String serial;
            try {
                serial = etSerial.getText().toString();
            } catch (Exception e) {
                Toast.makeText(this, "Need to provide a serial!", Toast.LENGTH_SHORT).show();
                return;
            }
            String date;
            try {
                date = etDate.getText().toString();
            } catch (Exception e) {
                Toast.makeText(this, "Need to provide a date!", Toast.LENGTH_SHORT).show();
                return;
            }
            String location;
            try {
                location = etLoc.getText().toString();
            } catch (Exception e) {
                Toast.makeText(this, "Need to provide a location!", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Pilot> pilots = stor.getPilots();
            Pilot pilot = new Pilot("Colonel Sanders"); // This is necessary for Android to see pilot
            // isn't empty. Will be overwritten immediately below.
            if (pilots.size() != 0) {
                String pilotName = ((Spinner) findViewById(R.id.spinLogPilot)).getSelectedItem().toString();
                for (int i = 0; i < pilots.size(); ++i) {
                    Pilot p = pilots.get(i);
                    if (p.getName().equals(pilotName)) {
                        pilot = p;
                        p.incrementTakeoffsAndLandings();
                        break;
                    }
                }
            } else {
                Toast.makeText(this, "To add pilots, navigate to 'Pilots & Spotters' on the main screen", Toast.LENGTH_LONG).show();
                return;
            }

            String spotter;
            if (stor.getSpotters().size() != 0) {
                spotter = ((Spinner) findViewById(R.id.spinLogSpotter)).getSelectedItem().toString();
            } else {
                Toast.makeText(this, "To add spotters, navigate to 'Pilots & Spotters' on the main screen", Toast.LENGTH_LONG).show();
                return;
            }

            float windSpeed;
            try {
                windSpeed = Float.parseFloat(etWindSpeed.getText().toString());
            } catch (Exception e) {
                windSpeed = 0;
            }

            float temperature;
            try {
                temperature = Float.parseFloat(etTemp.getText().toString());
            } catch (Exception e) {
                temperature = 0;
            }

            String weatherConditions;
            try {
                weatherConditions = etWCond.getText().toString();
            } catch (Exception e) {
                weatherConditions = "";
            }

            String purpose;
            try {
                purpose = etPurp.getText().toString();
            } catch (Exception e) {
                purpose = "";
            }

            String payload;
            try {
                payload = ((Spinner) findViewById(R.id.spinLogPayload)).getSelectedItem().toString();
            } catch (Exception e) {
                payload = "";
            }

            float altitude;
            try {
                altitude = Float.parseFloat(etAltitude.getText().toString());
            } catch (Exception e) {
                altitude = 0;
            }

            ArrayList<Flight> flights = new ArrayList<Flight>();
            int flightCount = llFlightInfos.getChildCount();
            for (int i = 0; i < flightCount; ++i) { // iterate over flights
                try {
                    EditText etTakeoff = (EditText) llFlightInfos.getChildAt(i).findViewById(R.id.etInfoTakeoff);
                    EditText etLand = (EditText) llFlightInfos.getChildAt(i).findViewById(R.id.etInfoLand);
                    EditText etTime = (EditText) llFlightInfos.getChildAt(i).findViewById(R.id.etInfoTime);
                    EditText etBattNum = (EditText) llFlightInfos.getChildAt(i).findViewById(R.id.etInfoBattNum);
                    EditText etStartVolt = (EditText) llFlightInfos.getChildAt(i).findViewById(R.id.etInfoStartVolt);
                    EditText etEndVolt = (EditText) llFlightInfos.getChildAt(i).findViewById(R.id.etInfoEndVolt);

                    // times below are in format "4:32 PM" => "h:mm a"
                    String takeoff = etTakeoff.getText().toString();
                    String land = etLand.getText().toString();
                    int time = Integer.parseInt(etTime.getText().toString());
                    String battNum = etBattNum.getText().toString();
                    Float startVolt = Float.parseFloat(etStartVolt.getText().toString());
                    Float endVolt = Float.parseFloat(etEndVolt.getText().toString());
                    if (endVolt > startVolt) {
                        Toast.makeText(this, "End voltage cannot be higher than start voltage!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Flight flight = new Flight(takeoff, land, time, battNum, startVolt, endVolt);
                    pilot.addToFlightTime(time);
                    flights.add(flight);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Flight info not complete, or not proper!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String comments = etComments.getText().toString();
            int flightLogNum = stor.getFlightNum().getFlightNumber();
            stor.getFlightNum().incrementFlightNum();

            FlightLog fl = new FlightLog(serial, date, location, pilot, spotter, windSpeed, temperature, weatherConditions,
                    purpose, payload, flights, comments, flightLogNum, altitude);

            stor.getFlightLogs().add(fl);

            // ****************** add flight to csv file ************************ //
            // http://stackoverflow.com/questions/9961292/write-to-text-file-without-overwriting-in-java
            FlightLogToCSV fltcsv = new FlightLogToCSV("\",\"");
            File logsFile = stor.getCsvFile();

            try {
                Boolean firstLine = false;
                if (!logsFile.exists()) {
                    System.out.println("We had to make a new file.");
                    logsFile.createNewFile();
                    stor.setCsvFile(logsFile);
                    firstLine = true;
                }

                PrintWriter out = new PrintWriter(new FileWriter(logsFile, true));
                if (firstLine) {
                    // do csv header
                    String header = fltcsv.headerCSV();
                    out.append(header);
                    out.append("\n");
                }
                String flAsString = fltcsv.flightToCSV(fl);
                out.append(flAsString);
                out.append("\n");

                out.close();
            } catch (IOException e) {
                System.out.println("Couldn't log");
                e.printStackTrace();
            }

            // ****************** end add flight to csv file ************************ //

            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong; couldn't save the flight log", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        // Override the back button to verify user wishes to leave the new flight log
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle("Exit Flight Log")
                .setMessage("Are you sure you wish to leave this flight log? (Data will not be saved)");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }

                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        alertDialog.show();
    }
}
