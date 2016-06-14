package ca.lakeland.plantsd.flightlogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ChecklistsActivity extends HomeScreen implements AdapterView.OnItemClickListener {

    private ListView lvPreflightChecklists;
    private ChecklistAdapter customAdapter;
    Storage stor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklists);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stor = Storage.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabChecklist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChecklistsActivity.this, NewChecklistActivity.class);
                startActivity(intent);
            }
        });

        lvPreflightChecklists = (ListView) findViewById(R.id.lvPreflightChecklists);
        customAdapter = new ChecklistAdapter(this, R.layout.adapter_checklist_row, stor.getDoneChecklists());
        lvPreflightChecklists.setAdapter(customAdapter);
        lvPreflightChecklists.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        //Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
        DoneChecklist dl = stor.getDoneChecklists().get(position);
        // Log.i("-----------------|", "that would be " + fl.getDate());
        Intent intent = new Intent(v.getContext(), ChecklistInfoActivity.class);
        intent.putExtra("DONE_CHECKLIST", dl);
        startActivity(intent);
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
        customAdapter = new ChecklistAdapter(this, R.layout.adapter_checklist_row, stor.getDoneChecklists());
        lvPreflightChecklists.setAdapter(customAdapter);
    }

}
