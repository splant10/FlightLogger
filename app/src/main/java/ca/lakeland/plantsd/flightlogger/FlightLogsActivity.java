package ca.lakeland.plantsd.flightlogger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FlightLogsActivity extends HomeScreen implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView lvFlightLogs;
    private FlightLogsAdapter customAdapter;
    private Button emailButton;

    Storage stor;
    public String selectedEmail;
    int EMAIL = 101; // result code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stor = Storage.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabFL);
        fab.setOnClickListener(new View.OnClickListener() {
            // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            // .setAction("Action", null).show();
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewFlightLogActivity.class);
                startActivity(intent);
            }
        });

        // Set up the pilots and spotters list views
        lvFlightLogs = (ListView) findViewById(R.id.lvFlightLogs);
        customAdapter = new FlightLogsAdapter(this, R.layout.adapter_flight_log_row, stor.getFlightLogs());
        lvFlightLogs.setAdapter(customAdapter);
        lvFlightLogs.setOnItemClickListener(this);
        lvFlightLogs.setOnItemLongClickListener(this);

        // hide email button if no flight logs to email
        emailButton = (Button) findViewById(R.id.btnEmailCsv);
        if (stor.getFlightLogs().size() <= 0) {
            emailButton.setVisibility(View.GONE);
        } //else {
           // emailButton.setVisibility(View.VISIBLE);
       // }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // this reeaally doesn't seem proper to me. Would much rather do a
        //      adapter.notifyDataSetChanged()
        // type of call, but that just doesn't want to work here. or anywhere for that matter
        customAdapter = new FlightLogsAdapter(this, R.layout.adapter_flight_log_row, stor.getFlightLogs());
        lvFlightLogs.setAdapter(customAdapter);

        if (stor.getFlightLogs().size() > 0) {
            try {
                emailButton.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        // Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
        FlightLog fl = stor.getFlightLogs().get(position);
        // Log.i("-----------------|", "that would be " + fl.getDate());
        Intent intent = new Intent(v.getContext(), FlightLogInfoActivity.class);
        intent.putExtra("FLIGHT_LOG", fl);
        intent.putExtra("ADMIN", false);
        startActivity(intent);
    }

    public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id) {
        final int pos = position;
        final View view = v;

        // check if admin
        if (HomeScreen.getAdminLoggedIn()) {
            // pop up a dialog asking if user wishes to delete the pilot

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            // Yes button clicked
                            FlightLog fl = stor.getFlightLogs().get(pos);
                            Intent intent = new Intent(view.getContext(), FlightLogInfoActivity.class);
                            intent.putExtra("FLIGHT_LOG", fl);
                            intent.putExtra("ADMIN", true);
                            startActivity(intent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            // No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(FlightLogsActivity.this);
            builder.setMessage("Do you wish to add Admin comments to this flight log?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }

        return true;
    }

    public void onEmailClick(View view) {
        // Hold on to your butts

        // http://www.learn-android-easily.com/2013/01/adding-radio-buttons-in-dialog.html
        final CharSequence[] emails = stor.getEmails().toArray(new CharSequence[0]);

        if (emails.length == 0) {
            Toast.makeText(this, "There aren't any email addresses stored. User the menubar to add emails", Toast.LENGTH_LONG).show();
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

                    File outfile = stor.getCsvFile();

                    Uri path = Uri.fromFile(outfile);
                    String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();

                    Intent i = new Intent(Intent.ACTION_SEND);
                    // set intent type to email
                    i.setType("vnd.android.cursor.dir/email");

                    String recipient[] = {selectedEmail};
                    Calendar myCalendar = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat(("MMM dd, yyyy"));
                    String today = df.format(myCalendar.getTime());

                    i.putExtra(Intent.EXTRA_EMAIL, recipient);
                    i.putExtra(Intent.EXTRA_SUBJECT, "Flight Logs - " + today);
                    i.putExtra(Intent.EXTRA_TEXT, "Please find attached the file for the stored flight logs");
                    i.putExtra(Intent.EXTRA_STREAM, path);

                    try {
                        // http://stackoverflow.com/questions/13872569/how-to-delete-file-from-sd-card-after-mail-send-successfully
                        startActivityForResult(Intent.createChooser(i, "Send mail..."), EMAIL);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(FlightLogsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }

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
}
