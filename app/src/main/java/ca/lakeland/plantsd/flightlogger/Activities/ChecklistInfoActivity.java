package ca.lakeland.plantsd.flightlogger.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;

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
            getSupportActionBar().setTitle("Checklist for " + day);

            txtAuthor.setText(authorName);

        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading checklist data", Toast.LENGTH_SHORT).show();
            finish();
        }


        // get bitmap signature image
        Bitmap bmap = getImageBitmapFromInternal(getApplicationContext(), day+authorName, "png");
        try {
            ImageView imgSignature = (ImageView) findViewById(R.id.imgChkInfoSignature);
            imgSignature.setImageBitmap(bmap);
        } catch (Exception e) {
            e.printStackTrace();
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

    // http://stackoverflow.com/a/19339672 - Retrieved August 24
    private Bitmap getImageBitmapFromInternal(Context context, String name, String extension) {
        name=name+"."+extension;
        try{
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        return null;
    }
}
