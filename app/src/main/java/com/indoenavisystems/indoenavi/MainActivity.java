package com.indoenavisystems.indoenavi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.indoenavisystems.indoenavi.handlers.ApiRequestHandler;
import com.indoenavisystems.indoenavi.handlers.MapHandler;
import com.indoenavisystems.indoenavi.models.Map;
import com.indoenavisystems.indoenavi.models.RouteNode;
import com.indoenavisystems.indoenavi.models.TrackedSPE;
import com.indoenavisystems.indoenavi.models.Vec2;
import com.android.volley.Request;
import com.indoenavisystems.indoenavi.utilities.ApiUrlConstants;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ApiRequestHandler apiRequest;
    private MapHandler mapHandler;
    private RouteNode routeNodeDestination;

    public MainActivity(){
        mapHandler = MapHandler.getInstance();
        apiRequest = new ApiRequestHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the action bar for more visual space.
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        getIntents();

        createStopButtonEvent();
        sendStatisticsData();
        startPositionPointerThread();
    }

    private void createStopButtonEvent(){
        Button stopBtn = (Button) findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When navigation stopped, go back to destination view.
                finish();
            }
        });
    }
    private void sendStatisticsData(){
        String sessionUrl = ApiUrlConstants.NewSession+"?area=ZBC-Ringsted";
        String mostSearchedDestination = ApiUrlConstants.DestinationVisits+"?area=ZBC-Ringsted&destination=D.32";
        apiRequest.stringRequest(sessionUrl, Request.Method.POST, null);
        apiRequest.stringRequest(mostSearchedDestination,Request.Method.POST,null);
    }

    private void getIntents(){
        //Set headline text to the name of the selected destination
        TextView tv = (TextView) findViewById(R.id.destinationTextView);
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            routeNodeDestination = (RouteNode) intent.getSerializableExtra("routeNode");
            tv.setText(routeNodeDestination.getName());

            //Find map and set it to handler
            Map mapToSet = (Map) intent.getSerializableExtra("map");

            if(mapToSet != null){
                mapHandler.setMap(mapToSet);
                ImageView mapImage = findViewById(R.id.mapImage);
                mapImage.setImageBitmap(mapHandler.getMap().getBitmap());
            }
        }
    }
    private void startPositionPointerThread(){
        new ScheduledThreadPoolExecutor(2).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Test
                mapHandler.calculateIndoorPosition(
                        new TrackedSPE("00:00:00:01", 4.12),
                        new TrackedSPE("00:00:00:02", 4.12),
                        new TrackedSPE("00:00:00:03", 7.0));

                // Update position pointer with latest indoor position
                Vec2 position = mapHandler.getLastKnownIndoorPosition();
                ImageView mapPositionImage = findViewById(R.id.mapPositionImage);
                mapPositionImage.setX((float)position.getX() - 8);
                mapPositionImage.setY((float)position.getY() + 36);

                //Set the position of the destination circle
                Vec2 destinationPosition = routeNodeDestination.getPosition();

                createCircleImage(destinationPosition, R.drawable.destinationcircle);
            }
        }, 0, 250, TimeUnit.MILLISECONDS);
    }

    private void createCircleImage(Vec2 destinationPosition, int drawableId) {
        FrameLayout fl = findViewById(R.id.mapFrame);
        ImageView iv = new ImageView(getApplicationContext());

        //Set image
        iv.setImageResource(drawableId);
        //Set size and dimensions
        //Get the pixel density (dp) to scale up the pixels
        float factor = iv.getResources().getDisplayMetrics().density;
        int imageSize = (int) (16 * factor);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageSize, imageSize);
        iv.setLayoutParams(params);

        //Set position
        iv.setX((float) destinationPosition.getX() - 8);
        iv.setY((float) destinationPosition.getY() + 36);

        //Add to FrameLayout
        fl.addView(iv);
        fl.invalidate();
    }
}
