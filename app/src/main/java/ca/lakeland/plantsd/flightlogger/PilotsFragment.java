package ca.lakeland.plantsd.flightlogger;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PilotsFragment extends Fragment {

    private ListView lvPilots;
    private PilotsAdapter pilotsAdapter;

    private ListView lvSpotters;
    private ArrayAdapter<String> spotAdapter;

    Storage stor;


    // Container Activity must implement this interface
    public interface OnPilotSelectedListener {
        public void onPilotSelected(int position);
    }



    public PilotsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pilots, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity main = (MainActivity)getActivity();
        stor = main.getStorage();

        if (stor.getPilots().size() <= 0) { // no pilots
            ViewStub noPilotStub = (ViewStub) main.findViewById(R.id.no_pilots_block);
            View inflated = noPilotStub.inflate();
        }
        if (stor.getSpotters().size() <= 0) { // no spotters
            ViewStub noSpotterStub = (ViewStub) main.findViewById(R.id.no_spotters_block);
            View inflated = noSpotterStub.inflate();
        }

        ///*
        // Set up the pilots and spotters list views
        lvPilots = (ListView) getView().findViewById(R.id.lvPilots);
        pilotsAdapter = new PilotsAdapter(main, R.layout.adapter_pilot_row, stor.getPilots());
        lvPilots.setAdapter(pilotsAdapter);
        lvPilots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get pilot at location from storage - get stats. do popup
            }
        });
        //lvPilots.setOnItemLongClickListener(this);

        lvSpotters = (ListView) getView().findViewById(R.id.lvSpotters);
        //spotAdapter = new ArrayAdapter<String>(getActivity(), R.layout.adapter_pilot_row, R.id.txtPilotName, stor.getSpotters());
        spotAdapter = new SpotAdapter(main, R.layout.adapter_pilot_row, stor.getSpotters());
        lvSpotters.setAdapter(spotAdapter);
        //*/
    }

    public static void refresh(FragmentManager fragman) {
        PilotsFragment fragment = new PilotsFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragman.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, "PILOT_FRAGMENT");
        fragmentTransaction.commit();
    }

}
