package com.indoenavisystems.indoenavi.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.indoenavisystems.indoenavi.R;
import com.indoenavisystems.indoenavi.models.RouteNode;

import java.util.ArrayList;

public class RouteNodeArrayAdapter extends ArrayAdapter<RouteNode> {
    private Activity activity;
    private ArrayList<RouteNode> nodes;
    private static LayoutInflater inflater = null;

    public RouteNodeArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RouteNode> objects) {
        super(context, resource, objects);
        try {
            this.activity = activity;
            this.nodes = objects;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            RouteNode item = getItem(position);
            View v = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                v = inflater.inflate(R.layout.routenodelistview, null);

            } else {
                v = convertView;
            }

            TextView header = (TextView) v.findViewById(R.id.nodeName);

            header.setText(item.name);

            return v;
        } catch (Exception ex) {
            Log.e("RouteNode", "error", ex);
            return null;
        }
    }
}
