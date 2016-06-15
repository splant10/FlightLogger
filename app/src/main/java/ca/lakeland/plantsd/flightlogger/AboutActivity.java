/*
    FlightLogger is an app for logging UAV flights in an efficient, easy, and contained way
    Copyright (C) 2016 Spencer Plant

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package ca.lakeland.plantsd.flightlogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    String myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        myEmail = getResources().getString(R.string.my_email);
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
}
