package ca.lakeland.plantsd.flightlogger;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewFlightLogActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FlightLog fl;

    Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat(("MMM-dd-yyyy"));
    String today = df.format(myCalendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flight_log);

        EditText etLogDate = (EditText) findViewById(R.id.etLogDate);
        etLogDate.setText(today);

        // ***********  Spinner for pilot selection ************ //
        Spinner pilotSpinner = (Spinner) findViewById(R.id.spinLogPilot);
        pilotSpinner.setOnItemSelectedListener(this);
        List<String> pilotNames = new ArrayList<String>();
        ArrayList<Pilot> pilotList = HomeScreen.getPilotList();

        // Iterate over pilots and add to spinner
        for (int i = 0; i < pilotList.size(); ++i) {
            pilotNames.add(pilotList.get(i).getName());
        }
        // Create adapter for the spinner
        ArrayAdapter<String> pilotNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pilotNames);

        // Attach data adapter to spinner
        pilotSpinner.setAdapter(pilotNameAdapter);
        // *********** End spinner for pilot selection ************ //

        // ***********  Spinner for spotter selection ************ //
        Spinner spotterSpinner = (Spinner) findViewById(R.id.spinLogSpotter);
        spotterSpinner.setOnItemSelectedListener(this);
        ArrayList<String> spotterList = HomeScreen.getSpotterList();

        // Create adapter for the spinner
        ArrayAdapter<String> spotterNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spotterList);

        // Attach data adapter to spinner
        spotterSpinner.setAdapter(spotterNameAdapter);
        // *********** End spinner for spotter selection ************ //

        // ***********  Spinner for Payload selection ************ //
        Spinner payloadSpinner = (Spinner) findViewById(R.id.spinLogPayload);
        payloadSpinner.setOnItemSelectedListener(this);
        List<String> payloadList = HomeScreen.getPayloads();
        ArrayAdapter<String> payloadAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, payloadList);
        payloadSpinner.setAdapter(payloadAdapter);
        // *********** End spinner for payload selection ************ //

        // This makes a date picker dialog pop up when clicking the date edittext
        EditText et = (EditText) findViewById(R.id.etLogDate);
        SetDate sd = new SetDate(et, this);

        // Set time picker listeners on the time fields in the first info box
        LinearLayout llLogFlightInfos = (LinearLayout) findViewById(R.id.llLogFlightInfos);
        EditText et2 = (EditText) llLogFlightInfos.getChildAt(0).findViewById(R.id.etInfoTakeoff);
        et2.setOnTouchListener(timePickerListener);
        EditText et3 = (EditText) llLogFlightInfos.getChildAt(0).findViewById(R.id.etInfoLand);
        et3.setOnTouchListener(timePickerListener);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    public void onNothingSelected(AdapterView<?> arg0) {

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

    public void btnSubmit(View view) {
        try {
            fl = new FlightLog();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong; couldn't save the flight log", Toast.LENGTH_SHORT).show();
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
                        String suffix = (selectedHour >= 12) ? "pm" : "am";
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
}
