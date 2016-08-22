package ca.lakeland.plantsd.flightlogger.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;

import ca.lakeland.plantsd.flightlogger.Activities.ChecklistInfoActivity;
import ca.lakeland.plantsd.flightlogger.Activities.MainActivity;
import ca.lakeland.plantsd.flightlogger.Adapters.ChecklistAdapter;
import ca.lakeland.plantsd.flightlogger.Objects.DoneChecklist;
import ca.lakeland.plantsd.flightlogger.Objects.Storage;
import ca.lakeland.plantsd.flightlogger.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChecklistFragment extends Fragment {

    private ListView lvPreflightChecklists;
    private ChecklistAdapter customAdapter;
    Storage stor;

    public ChecklistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity main = (MainActivity)getActivity();
        stor = main.getStorage();

        if (stor.getDoneChecklists().size() <= 0) { // no checklists
            ViewStub noChecklistsStub = (ViewStub) main.findViewById(R.id.no_checklists_block);
            View inflated = noChecklistsStub.inflate();
        }

        lvPreflightChecklists = (ListView) getView().findViewById(R.id.lvPreflightChecklists);
        customAdapter = new ChecklistAdapter(main, R.layout.adapter_checklist_row, stor.getDoneChecklists());
        lvPreflightChecklists.setAdapter(customAdapter);
        lvPreflightChecklists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("Hello THERE", "you clicked item: " + id + " at position: " + position);
                DoneChecklist dl = stor.getDoneChecklists().get(position);
                // Log.i("-----------------|", "that would be " + fl.getDate());
                Intent intent = new Intent(view.getContext(), ChecklistInfoActivity.class);
                intent.putExtra("DONE_CHECKLIST", dl);
                startActivity(intent);
            }
        });
    }

    public static void refresh(FragmentManager fragman) {
        ChecklistFragment fragment = new ChecklistFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragman.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, "CHECKLIST_FRAGMENT");
        fragmentTransaction.commit();
    }

}
