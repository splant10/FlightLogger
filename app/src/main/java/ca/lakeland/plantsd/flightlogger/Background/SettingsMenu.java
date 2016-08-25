package ca.lakeland.plantsd.flightlogger.Background;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ca.lakeland.plantsd.flightlogger.Activities.MainActivity;
import ca.lakeland.plantsd.flightlogger.Objects.Storage;

public class SettingsMenu {

    private static Storage stor;
    private static String selectedAddress;

    public static void adminLoginLogoutButton(Context ctxt) {
        final Activity context = (Activity) ctxt;
        stor = Storage.getInstance();

        Boolean adminLoggedIn = MainActivity.getAdminLoggedIn();

        if (!adminLoggedIn) { // not logged in as admin

            // popup alertdialog with edittext
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Admin Login")
                    .setMessage("Enter the password:");

            final EditText input = new EditText(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            input.setLayoutParams(lp);
            alertDialog.setView(input)
                    .setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String password = input.getText().toString();
                                    if (password.equals(stor.getAdminPassword())) {
                                        MainActivity.setAdminLoggedIn(true);
                                        Toast.makeText(context, "Logged in as administrator", Toast.LENGTH_SHORT).show();
                                        context.invalidateOptionsMenu();
                                    } else {
                                        Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();

        } else  { // logged in as admin
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Logout")
                    .setMessage("Do you wish to log out of the admin account?");
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.setAdminLoggedIn(false);
                            context.invalidateOptionsMenu();
                            Toast.makeText(context, "Logged out of administrator account", Toast.LENGTH_SHORT).show();
                        }
                    });
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }

    public static void addEmailAddress(Context ctxt) {
        final Context context = ctxt;
        stor = Storage.getInstance();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle("Email Address")
                .setMessage("Enter an email address to save");

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        input.setLayoutParams(lp);
        alertDialog.setView(input)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String email = input.getText().toString();
                                if (isValidEmail(email)) {
                                    stor.getEmails().add(email);
                                    Toast.makeText(context, "Email address added", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    // http://stackoverflow.com/questions/1819142/how-should-i-validate-an-e-mail-address
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void changeAdminPassword(Context ctxt) {
        final Context context = ctxt;
        stor = Storage.getInstance();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle("Change Admin Password")
                .setMessage("Please enter a new password");

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        final EditText inputP1 = new EditText(context);
        inputP1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputP1.setHint("Enter new password");
        inputP1.setLayoutParams(lp);
        layout.addView(inputP1);

        final EditText inputP2 = new EditText(context);
        inputP2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputP2.setHint("Re-enter new password");
        inputP2.setLayoutParams(lp);
        layout.addView(inputP2);

        alertDialog.setView(layout);

        alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String p1 = inputP1.getText().toString();
                                String p2 = inputP2.getText().toString();
                                if (p1.equals(p2)) {
                                    stor.setAdminPassword(p1);
                                    Toast.makeText(context, "Password changed", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    public static void emailFile(Context ctxt, File file) {
        // Hold on to your butts
        final File fileToSend = file;
        final Context context = ctxt;

        stor = Storage.getInstance();

        // http://www.learn-android-easily.com/2013/01/adding-radio-buttons-in-dialog.html
        final CharSequence[] emails = stor.getEmails().toArray(new CharSequence[0]);

        if (emails.length == 0) {
            Toast.makeText(context, "There aren't any email addresses stored. User the menubar to add emails", Toast.LENGTH_LONG).show();
        } else {
            // popup alertdialog with edittext
            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context)
                    .setTitle("Choose Recipient");

            alertDialog.setSingleChoiceItems(emails, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    selectedAddress = emails[item].toString();
                }
            });
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    File outfile = fileToSend;

                    Uri path = Uri.fromFile(outfile);
                    String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();

                    Intent i = new Intent(Intent.ACTION_SEND);
                    // set intent type to email
                    i.setType("vnd.android.cursor.dir/email");

                    String recipient[] = {selectedAddress};
                    Calendar myCalendar = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat(("MMM dd, yyyy"));
                    String today = df.format(myCalendar.getTime());

                    i.putExtra(Intent.EXTRA_EMAIL, recipient);
                    i.putExtra(Intent.EXTRA_SUBJECT, "Flight Logs as of " + today);
                    i.putExtra(Intent.EXTRA_TEXT, "Please find attached the file for the stored flight logs");
                    i.putExtra(Intent.EXTRA_STREAM, path);

                    try {
                        // http://stackoverflow.com/questions/13872569/how-to-delete-file-from-sd-card-after-mail-send-successfully
                        context.startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
