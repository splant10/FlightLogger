package ca.lakeland.plantsd.flightlogger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by plantsd on 6/3/2016.
 */
public class ChecklistAdapter extends ArrayAdapter<DoneChecklist> {

    List checklists;
    LayoutInflater vi;

    public ChecklistAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ChecklistAdapter(Context context, int resource, List<DoneChecklist> checklists) {
        super(context, resource, checklists);
        this.checklists = checklists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.adapter_checklist_row, null);
        }

        DoneChecklist dc = (DoneChecklist) checklists.get(position);

        if (dc != null) {

            TextView txt1 = (TextView) v.findViewById(R.id.txtChecklistDate);
            if (txt1 != null) {
                txt1.setText(dc.getDate());
            }

            TextView txt2 = (TextView) v.findViewById(R.id.txtChecklistAuthor);
            if (txt2 != null) {
                txt2.setText(dc.getAuthor());
            }
        }
        return v;
    }

}
