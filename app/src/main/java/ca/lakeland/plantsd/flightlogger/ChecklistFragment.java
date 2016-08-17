package ca.lakeland.plantsd.flightlogger;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChecklistFragment extends Fragment {

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
    }

}
