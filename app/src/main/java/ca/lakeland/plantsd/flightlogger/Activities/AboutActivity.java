package ca.lakeland.plantsd.flightlogger.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ca.lakeland.plantsd.flightlogger.R;

public class AboutActivity extends AppCompatActivity {

    String myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        myEmail = getResources().getString(R.string.my_email);

        // Put a back arrow in the toolbar
        // icon is selected in style/Apptheme
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("About");
    }

    public void emailSpencer(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        // set intent type to email
        i.setType("vnd.android.cursor.dir/email");

        String recipient[] = {myEmail};
        i.putExtra(Intent.EXTRA_EMAIL, recipient);
        i.putExtra(Intent.EXTRA_SUBJECT, "Flight Logger app issue");
        i.putExtra(Intent.EXTRA_TEXT, "Please describe exactly the context of your issue here:\n\n");

        try {
            // http://stackoverflow.com/questions/13872569/how-to-delete-file-from-sd-card-after-mail-send-successfully
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AboutActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
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
