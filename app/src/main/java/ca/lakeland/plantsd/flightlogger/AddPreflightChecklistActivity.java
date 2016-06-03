package ca.lakeland.plantsd.flightlogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class AddPreflightChecklistActivity extends HomeScreen implements AdapterView.OnItemClickListener {

    private ListView lvPreflightChecklists;
    private ChecklistAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preflight_checklist);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabChecklist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPreflightChecklistActivity.this, ChecklistActivity.class);
                startActivity(intent);
            }
        });

        lvPreflightChecklists = (ListView) findViewById(R.id.lvPreflightChecklists);
        customAdapter = new ChecklistAdapter(this, R.layout.adapter_checklist_row, HomeScreen.getCheckLists());
        lvPreflightChecklists.setAdapter(customAdapter);
        lvPreflightChecklists.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
    }

}
