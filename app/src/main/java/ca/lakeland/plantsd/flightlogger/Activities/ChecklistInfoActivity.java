package ca.lakeland.plantsd.flightlogger.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ca.lakeland.plantsd.flightlogger.Objects.DoneChecklist;
import ca.lakeland.plantsd.flightlogger.R;

public class ChecklistInfoActivity extends AppCompatActivity {

    String day;
    String authorName;
    Bitmap sig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_info);

        // Put a back arrow in the toolbar
        // icon is selected in style/Apptheme
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try {
            DoneChecklist dc = (DoneChecklist) getIntent().getSerializableExtra("DONE_CHECKLIST");
            day = dc.getDate();
            authorName = dc.getAuthor();

            TextView txtAuthor = (TextView) findViewById(R.id.txtChkInfoAuthor);

            txtAuthor.setText(authorName);

        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading checklist data", Toast.LENGTH_SHORT).show();
            finish();
        }



        // get bitmap signature image
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            File signatureImg = new File(path, day + authorName + ".png");

            if (signatureImg.exists()) {
                sig = BitmapFactory.decodeFile(signatureImg.getAbsolutePath());
                ImageView imgSignature = (ImageView) findViewById(R.id.imgChkInfoSignature);
                imgSignature.setImageBitmap(sig);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Checklist for " + day);
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
}
