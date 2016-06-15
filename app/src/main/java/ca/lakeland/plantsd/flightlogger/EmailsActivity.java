package ca.lakeland.plantsd.flightlogger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class EmailsActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    Storage stor;
    EmailsAdapter customAdapter;

    private static final int MENU_ADMIN = Menu.FIRST;
    private static final int MENU_ADD_EMAIL = MENU_ADMIN + 1;
    private static final int MENU_VIEW_EMAILS = MENU_ADMIN + 2;
    private static final int MENU_LOGIN = MENU_ADMIN + 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emails);

        stor = Storage.getInstance();

        ListView lvEmails = (ListView) findViewById(R.id.lvEmails);
        customAdapter = new EmailsAdapter(this, R.layout.adapter_pilot_row, stor.getEmails());
        lvEmails.setAdapter(customAdapter);
        lvEmails.setOnItemLongClickListener(this);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Email Addresses");
    }

    /**
     * Gets called every time the user presses the menu button.
     * Use if your menu is dynamic.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, MENU_ADD_EMAIL, Menu.NONE, "Add an email address");
        if (HomeScreen.getAdminLoggedIn()) {
            menu.add(0, MENU_ADMIN, Menu.NONE, "Admin Logged In").setIcon(R.drawable.ic_lock_open_black_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, MENU_LOGIN, Menu.NONE, "Administrator Logout");
        } else {
            menu.add(0, MENU_LOGIN, Menu.NONE, "Login as Administrator");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ADMIN:
                Toast.makeText(this, "Logged in as administrator", Toast.LENGTH_SHORT).show();
                return true;
            case MENU_ADD_EMAIL:
                SettingsMenu.addEmailAddress(this);
                return true;
            case MENU_LOGIN:
                SettingsMenu.adminLoginLogoutButton(this);

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id) {
        //Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
        final int pos = position;
        String email = stor.getEmails().get(position);
        // Log.i("-----------------|", " that would be " + fl.getDate());

        if (HomeScreen.getAdminLoggedIn()) { // admin
            // make a dialog asking if want to delete the email.
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            // Yes button clicked
                            stor.getEmails().remove(pos);
                            customAdapter.notifyDataSetChanged();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            // No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(EmailsActivity.this);
            builder.setMessage("Do you wish to delete this email address?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
            return true;
        } else { // regular user
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
