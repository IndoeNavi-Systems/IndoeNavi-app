package com.indoenavisystems.indoenavi.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.indoenavisystems.indoenavi.R;
import com.indoenavisystems.indoenavi.models.RouteNode;
import java.util.ArrayList;
import java.util.List;
import android.widget.Filterable;

public class RouteNodeArrayAdapter extends ArrayAdapter<RouteNode> implements Filterable {

    private LayoutInflater inflater;
    private List<RouteNode> originalList;
    private List<RouteNode> filteredList;

    public RouteNodeArrayAdapter(Context context, List<RouteNode> nodes) {
        super(context, 0, nodes);
        inflater = LayoutInflater.from(context);
        //Original unfiltered list
        originalList = new ArrayList<>(nodes);
        //Filtered list
        filteredList = new ArrayList<>(nodes);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public RouteNode getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            //Inflate the layout for each item in the list
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(android.R.id.text1);

            //Make text centered.
            holder.nameTextView.setGravity(Gravity.CENTER);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RouteNode node = getItem(position);

        if (node != null) {
            holder.nameTextView.setText(node.getName());
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<RouteNode> filteredNodes = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    //If no constraint, then return the original unfiltered list
                    filteredNodes.addAll(originalList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    //Filter original list, based on constraint
                    for (RouteNode node : originalList) {
                        if (node.getName().toLowerCase().contains(filterPattern)) {
                            filteredNodes.add(node);
                        }
                    }
                }

                results.values = filteredNodes;
                results.count = filteredNodes.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //Update the filtered list, and notify the change
                filteredList.clear();
                filteredList.addAll((List<RouteNode>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    private static class ViewHolder {
        //Added ViewHolder class, in case we want to add more TextViews for the list
        TextView nameTextView;
    }
}

