package ca.lakeland.plantsd.flightlogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class EmailsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Storage stor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emails);

        stor = Storage.getInstance();

        ListView lvEmails = (ListView) findViewById(R.id.lvEmails);
        EmailsAdapter customAdapter = new EmailsAdapter(this, R.layout.adapter_pilot_row, stor.getEmails());
        lvEmails.setAdapter(customAdapter);
        lvEmails.setOnItemClickListener(this);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Email Addresses");
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        //Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
        String email = stor.getEmails().get(position);
        // Log.i("-----------------|", " that would be " + fl.getDate());
    }
}
