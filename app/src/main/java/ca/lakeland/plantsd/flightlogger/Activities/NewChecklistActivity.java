package ca.lakeland.plantsd.flightlogger.Activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.lakeland.plantsd.flightlogger.Objects.DoneChecklist;
import ca.lakeland.plantsd.flightlogger.Background.DrawingView;
import ca.lakeland.plantsd.flightlogger.Objects.Pilot;
import ca.lakeland.plantsd.flightlogger.R;
import ca.lakeland.plantsd.flightlogger.Objects.Storage;

public class NewChecklistActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView clearButton;
    private CheckBox chkNOTAM, chkEnviro, chkEye, chkSideArms, chkLanding,
            chkMotors, chkRotors, chkBatt, chkControl, chkInertial, chkCamera, chkCell;
    DrawingView dv;
    Spinner spinAuthors;
    Button btnSubmit;
    Storage stor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_checklist);

        // Put a back arrow in the toolbar
        // icon is selected in style/Apptheme
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Preflight Checklist");

        stor = Storage.getInstance();

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

        // ***********  Spinner for author selection ************ //
        spinAuthors = (Spinner) findViewById(R.id.include1).findViewById(R.id.spinChecklistAuthor);

        spinAuthors.setOnItemSelectedListener(this);
        List<String> authorNames = new ArrayList<String>();
        List<Pilot> pilotList = stor.getPilots();

        // Iterate over pilots and add to spinner
        for (int i = 0; i < pilotList.size(); ++i) {
            authorNames.add(pilotList.get(i).getName());
        }

        // add all the spotters to the list too
        authorNames.addAll(stor.getSpotters());

        // http://stackoverflow.com/questions/203984/how-do-i-remove-repeated-elements-from-arraylist
        Set<String> hs = new HashSet<>();
        hs.addAll(authorNames);
        authorNames.clear();
        authorNames.addAll(hs);

        // Create adapter for the spinner
        ArrayAdapter<String> pilotNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, authorNames);
        pilotNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // make options bigger

        // Attach data adapter to spinner
        spinAuthors.setAdapter(pilotNameAdapter);
        // End spinner for author selection

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
        switch (item.getItemId()) {
            case android.R.id.home: // if choosing back arrow
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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


    public void clickArea(View view) {
        // when clicking anywhere in a linear layout child in the scrollview (that has a checkbox
        // and textview explanation children), check the checkbox

        LinearLayout thisLL = (LinearLayout) view;
        CheckBox checkBox = (CheckBox) thisLL.getChildAt(0);
        checkBox.setChecked(!checkBox.isChecked());
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
            String author = spinAuthors.getSelectedItem().toString();
            DoneChecklist doneChecklist = new DoneChecklist(today, author);
            stor.getDoneChecklists().add(doneChecklist);

            saveImageToInternal(getApplicationContext(), dv.getDrawingCache(), today+author, "png");

            dv.destroyDrawingCache();
            MainActivity.allowRefresh = true;

            finish();

        } else {
            Toast.makeText(this, "You haven't completed the checklist. Please do so.", Toast.LENGTH_SHORT).show();
        }
    }

    // http://stackoverflow.com/a/19339672 - Retrieved August 24
    private void saveImageToInternal(Context context, Bitmap b, String name, String extension){
        name=name+"."+extension;
        FileOutputStream out;
        try {
            out = context.openFileOutput(name, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
