package com.indoenavisystems.indoenavi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class SelectDestination extends AppCompatActivity {

    SearchView searchView;
    ListView listView;

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide the action bar for more visual space.
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_select_destination);

        searchView = findViewById(R.id.destSearch);
        listView = findViewById(R.id.destList);

        searchView.setIconifiedByDefault(false);

        arrayList = new ArrayList<>();
        arrayList.add("D.30");
        arrayList.add("D.31");
        arrayList.add("D.32");
        arrayList.add("D.33");
        arrayList.add("C.35");
        arrayList.add("P.03");

        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = (String)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(SelectDestination.this, com.indoenavisystems.indoenavi.MainActivity.class);
                intent.putExtra("destination",selectedItem);
                startActivity(intent);
            }
        });

    }
}