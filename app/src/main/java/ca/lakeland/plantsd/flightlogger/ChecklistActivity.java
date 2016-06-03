package ca.lakeland.plantsd.flightlogger;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ChecklistActivity extends HomeScreen implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView clearButton;
    private CheckBox chkNOTAM, chkEnviro, chkEye, chkSideArms, chkLanding,
            chkMotors, chkRotors, chkBatt, chkControl, chkInertial, chkCamera, chkCell;
    DrawingView dv;
    Spinner spinPilots;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        clearButton = (TextView) findViewById(R.id.include1).findViewById(R.id.txtbtnCanvasClear);
        clearButton.setOnClickListener(this);

        chkNOTAM = (CheckBox) findViewById(R.id.chkPreNOTAM);
        chkEnviro = (CheckBox) findViewById(R.id.chkPreEnviro);
        chkEye = (CheckBox) findViewById(R.id.chkPreEye);
        chkSideArms = (CheckBox) findViewById(R.id.chkPreSideArms);
        chkLanding = (CheckBox) findViewById(R.id.chkPreLandingGear);
        chkMotors = (CheckBox) findViewById(R.id.chkPreMotors);
        chkRotors = (CheckBox) findViewById(R.id.chkPreRotors);
        chkBatt = (CheckBox) findViewById(R.id.chkPreBattery);
        chkControl = (CheckBox) findViewById(R.id.chkPreController);
        chkInertial = (CheckBox) findViewById(R.id.chkPreInertial);
        chkCamera = (CheckBox) findViewById(R.id.chkPreCamera);
        chkCell = (CheckBox) findViewById(R.id.chkPreCell);

        dv = (DrawingView) findViewById(R.id.include1).findViewById(R.id.canvas);

        // ***********  Spinner for pilot selection ************ //
        spinPilots = (Spinner) findViewById(R.id.include1).findViewById(R.id.spinChecklistPilot);
        spinPilots.setOnItemSelectedListener(this);
        List<String> pilotNames = new ArrayList<String>();
        List<Pilot> pilotList = HomeScreen.getPilotList();

        // Iterate over pilots and add to spinner
        for (int i = 0; i < pilotList.size(); ++i) {
            pilotNames.add(pilotList.get(i).getName());
        }
        // Create adapter for the spinner
        ArrayAdapter<String> pilotNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pilotNames);

        // Attach data adapter to spinner
        spinPilots.setAdapter(pilotNameAdapter);
        // End spinner for pilot selection

        btnSubmit = (Button) findViewById(R.id.include1).findViewById(R.id.btnPreSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.txtbtnCanvasClear) {
            dv.startNew();
        }
        else if (v.getId()==R.id.btnPreSubmit) {
            btnSubmit(v);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    // What happens when you hit the submit button.
    public void btnSubmit(View v) {
        // if all are checked
        if (chkNOTAM.isChecked() & chkEnviro.isChecked() & chkEye.isChecked()
                & chkSideArms.isChecked() & chkLanding.isChecked() & chkMotors.isChecked()
                & chkRotors.isChecked() & chkBatt.isChecked() & chkControl.isChecked()
                & chkInertial.isChecked() & chkCamera.isChecked() & chkCell.isChecked()){
            // Toast.makeText(this, "all are checked!", Toast.LENGTH_SHORT).show();
            dv.setDrawingCacheEnabled(true);

            // Get today's date.
            Calendar myCalendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(("MMM dd, yyyy"));
            String today = df.format(myCalendar.getTime());

            // make a new completed checklist with the pilot and signature for today.
            String pilotName = spinPilots.getSelectedItem().toString();
            DoneChecklist doneChecklist = new DoneChecklist(today, pilotName, dv.getDrawingCache());
            HomeScreen.getCheckLists().add(doneChecklist);

            /*
            // http://stackoverflow.com/questions/649154/save-bitmap-to-location
            // Thanks to Ulrich Scheller for this. Retrieved June 3 2016
            String path = Environment.getExternalStorageDirectory().toString();
            FileOutputStream out = null;
            File signatureImg = new File(path, UUID.randomUUID().toString()+".png");
            try {
                out = new FileOutputStream(signatureImg);
                dv.getDrawingCache().compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Signature could not be saved", Toast.LENGTH_SHORT).show();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Signature could not be saved", Toast.LENGTH_SHORT).show();
                }
            }
            Toast.makeText(getApplicationContext(),"Signature saved", Toast.LENGTH_SHORT).show();
            */

            dv.destroyDrawingCache();
            finish();

        } else {
            Toast.makeText(this, "You haven't completed the checklist. Please do so.", Toast.LENGTH_SHORT).show();
        }
    }
}
