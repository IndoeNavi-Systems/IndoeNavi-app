package com.indoenavisystems.indoenavi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class LocationSelector extends Activity {

    SearchView searchView;
    ListView listView;

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide the action bar for more visual space.

        setContentView(R.layout.activity_location_selector);

        listView = findViewById(R.id.closestLocationsList);
        searchView = findViewById(R.id.locationSearch);

        searchView.setIconifiedByDefault(false);

        arrayList = new ArrayList<>();
        arrayList.add("ZBC-Ringsted");

        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = (String)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(LocationSelector.this, com.indoenavisystems.indoenavi.SelectDestination.class);
                intent.putExtra("location",selectedItem);
                startActivity(intent);
            }
        });
    }
}