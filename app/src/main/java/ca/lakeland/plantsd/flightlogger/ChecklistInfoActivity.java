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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ChecklistInfoActivity extends AppCompatActivity {

    String day;
    String authorName;
    Bitmap sig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_info);

        try {
            DoneChecklist dc = (DoneChecklist) getIntent().getSerializableExtra("DONE_CHECKLIST");
            day = dc.getDate();
            authorName = dc.getAuthor();

            TextView txtAuthor = (TextView) findViewById(R.id.txtChkInfoAuthor);

            txtAuthor.setText(authorName);

        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading checklist data", Toast.LENGTH_SHORT).show();
            finish();
        }



        // get bitmap signature image
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            File signatureImg = new File(path, day + authorName + ".png");

            if (signatureImg.exists()) {
                sig = BitmapFactory.decodeFile(signatureImg.getAbsolutePath());
                ImageView imgSignature = (ImageView) findViewById(R.id.imgChkInfoSignature);
                imgSignature.setImageBitmap(sig);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Checklist for " + day);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
