package ca.lakeland.plantsd.flightlogger;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class FlightLogsFragment extends Fragment {

    private ListView lvFlightLogs;
    private FlightLogsAdapter customAdapter;
    private Button emailButton;
    private MainActivity main;

    Storage stor;
    public String selectedEmail;
    int EMAIL = 101; // result code


    public FlightLogsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_logs, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main = (MainActivity)getActivity();
        stor = main.getStorage();

        if (stor.getFlightLogs().size() <= 0) { // no pilots
            ViewStub noFlightlogsStub = (ViewStub) main.findViewById(R.id.no_flightlogs_block);
            View inflated = noFlightlogsStub.inflate();
        }

        // Set up the pilots and spotters list views
        lvFlightLogs = (ListView) getView().findViewById(R.id.lvFlightLogs);
        customAdapter = new FlightLogsAdapter(main, R.layout.adapter_flight_log_row, stor.getFlightLogs());
        lvFlightLogs.setAdapter(customAdapter);
        lvFlightLogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
                FlightLog fl = stor.getFlightLogs().get(position);
                // Log.i("-----------------|", "that would be " + fl.getDate());
                Intent intent = new Intent(view.getContext(), FlightLogInfoActivity.class);
                intent.putExtra("FLIGHT_LOG", fl);
                intent.putExtra("ADMIN", false);
                startActivity(intent);
            }
        });
        //lvFlightLogs.setOnItemLongClickListener(this);

        /*
        // hide email button if no flight logs to email
        emailButton = (Button) getView().findViewById(R.id.btnEmailCsv);
        if (stor.getFlightLogs().size() <= 0) {
            emailButton.setVisibility(View.GONE);
        } else {
            // set the onclick function:
            emailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEmailClick(v);
                }
            });
        }
        */

    }

    public void onEmailClick(View view) {
        // Hold on to your butts

        // http://www.learn-android-easily.com/2013/01/adding-radio-buttons-in-dialog.html
        final CharSequence[] emails = stor.getEmails().toArray(new CharSequence[0]);

        if (emails.length == 0) {
            Toast.makeText(main, "There aren't any email addresses stored. User the menubar to add emails", Toast.LENGTH_LONG).show();
        } else {
            // popup alertdialog with edittext
            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(main)
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
                        Toast.makeText(main, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
