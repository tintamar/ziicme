package org.tselex.ziicme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;



/**
 * Created by zappa_000 on 07/07/2015.
 */
    public class ZreceptionAdapter extends ArrayAdapter<Zcontact> {
public ZreceptionAdapter(Context context, ArrayList<Zcontact> user) {
        super(context, 0, user);
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Zcontact user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.ecran_demande_rubrique, parent, false);
        }
        // Lookup view for data population
        TextView enom = (TextView) convertView.findViewById(R.id.nom);
        TextView enumgsm = (TextView) convertView.findViewById(R.id.numgsm);
        // Populate the data into the template view using the data object
        enom.setText(user.nom);
        enumgsm.setText(user.numgsm);
        // Return the completed view to render on screen
        return convertView;
        }
}
