package ca.lakeland.plantsd.flightlogger;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsMenu {

    private static Storage stor;

    public static void adminLoginLogoutButton(Context ctxt) {
        final Activity context = (Activity) ctxt;
        stor = Storage.getInstance();

        Boolean adminLoggedIn = HomeScreen.getAdminLoggedIn();

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
                                        HomeScreen.setAdminLoggedIn(true);
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
                            HomeScreen.setAdminLoggedIn(false);
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
}
