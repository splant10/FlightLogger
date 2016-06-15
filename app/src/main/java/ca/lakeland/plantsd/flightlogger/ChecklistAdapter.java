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
                String auth = "Completed by " + dc.getAuthor();
                txt2.setText(auth);
            }
        }
        return v;
    }

}
