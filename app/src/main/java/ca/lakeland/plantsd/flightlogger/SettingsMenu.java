package ca.lakeland.plantsd.flightlogger;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsMenu extends AppCompatActivity {

    private Storage stor;
    private Pattern emailPattern = Pattern.compile("^[a-zA-Z]+[@][a-zA-Z]+\\.[a-zA-Z]+$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        stor = Storage.getInstance();

        if (HomeScreen.getAdminLoggedIn()) {
            TextView text = (TextView) findViewById(R.id.txtAdminLogin);
            text.setText("Logged in as Admin");
            Button loginButton = (Button) findViewById(R.id.btnSettingsAdminLogin);
            loginButton.setText("Logout");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void adminLoginLogoutButton(View view) {

        Boolean adminLoggedIn = HomeScreen.getAdminLoggedIn();

        if (!adminLoggedIn) { // not logged in as admin

            // popup alertdialog with edittext
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsMenu.this)
                    .setTitle("Admin Login")
                    .setMessage("Enter the password:");

            final EditText input = new EditText(SettingsMenu.this);
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
                                        TextView text = (TextView) findViewById(R.id.txtAdminLogin);
                                        text.setText("Logged in as Admin");
                                        Button loginButton = (Button) findViewById(R.id.btnSettingsAdminLogin);
                                        loginButton.setText("Logout");

                                        Toast.makeText(getApplicationContext(),
                                                "Logged in as admin", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Incorrect Password", Toast.LENGTH_SHORT).show();
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsMenu.this)
                    .setTitle("Logout")
                    .setMessage("Do you wish to log out of the admin account?");
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            HomeScreen.setAdminLoggedIn(false);
                            TextView text = (TextView) findViewById(R.id.txtAdminLogin);
                            text.setText("Not logged in as Admin");
                            Button loginButton = (Button) findViewById(R.id.btnSettingsAdminLogin);
                            loginButton.setText("Login");

                            Toast.makeText(getApplicationContext(),
                                    "Logged out", Toast.LENGTH_SHORT).show();
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

    public void addEmailAddress(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsMenu.this)
                .setTitle("Email Address")
                .setMessage("Enter an email address to save");

        final EditText input = new EditText(SettingsMenu.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        input.setLayoutParams(lp);
        alertDialog.setView(input)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do regex to verify it's an email format
                                // [a-zA-Z]+[@][a-zA-Z]+\.[a-zA-Z]+

                                Matcher matcher = emailPattern.matcher(input.getText().toString());
                                if (!matcher.matches()) { // if it doesn't match the format of an email
                                    Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_SHORT);
                                } else {
                                    // add email to list of emails
                                    stor.getEmails().add(input.getText().toString());
                                    Toast.makeText(getApplicationContext(), "address added", Toast.LENGTH_SHORT);
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
