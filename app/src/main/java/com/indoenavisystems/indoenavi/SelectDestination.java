package com.indoenavisystems.indoenavi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.indoenavisystems.indoenavi.Interfaces.VolleyCallBack;
import com.indoenavisystems.indoenavi.adapters.RouteNodeArrayAdapter;
import com.indoenavisystems.indoenavi.handlers.ApiRequestHandler;
import com.indoenavisystems.indoenavi.models.Map;
import com.indoenavisystems.indoenavi.models.RouteNode;
import com.indoenavisystems.indoenavi.utilities.ApiUrlConstants;

import java.util.ArrayList;

public class SelectDestination extends AppCompatActivity {

    SearchView searchView;
    ListView listView;

    ArrayList<RouteNode> arrayList;
    ArrayAdapter<RouteNode> adapter;
    private ApiRequestHandler apiRequest;
    private Map map;
    private String selectedLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide the action bar for more visual space.
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_select_destination);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            selectedLocation = intent.getStringExtra("location");
            TextView tv = findViewById(R.id.locationHeader);
            tv.setText(selectedLocation);
        }

        apiRequest = new ApiRequestHandler(this);

        searchView = findViewById(R.id.destSearch);
        listView = findViewById(R.id.destList);

        searchView.setIconifiedByDefault(false);

        loadMapImage();

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
                RouteNode selectedItem = (RouteNode)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(SelectDestination.this, com.indoenavisystems.indoenavi.MainActivity.class);
                intent.putExtra("routeNode",selectedItem);
                intent.putExtra("map",map);
                startActivity(intent);
            }
        });

    }

    private void loadMapImage(){
        apiRequest.stringRequest(ApiUrlConstants.Map + "?area="+selectedLocation, Request.Method.GET, new VolleyCallBack() {
            @Override
            public void onSuccess(Object successMessage) {
                map = new Gson().fromJson(successMessage.toString(), Map.class);

                //When we get the new map, we add all destinations to the list
                arrayList = new ArrayList<RouteNode>();

                RouteNode[] nodes = map.getRouteNodes();
                //Add all route nodes that is a destination.
                for (RouteNode node : nodes){
                    if(node.getIsDestination()){
                        arrayList.add(node);
                    }
                }

                adapter=new RouteNodeArrayAdapter(getApplicationContext(), arrayList);

                listView.setAdapter(adapter);
            }
            @Override
            public void onFail(VolleyError error) {
            }
        });
    }
}