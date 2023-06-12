package com.indoenavisystems.indoenavi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.indoenavisystems.indoenavi.ApiRequest;
import com.indoenavisystems.indoenavi.ApiUrlConstants;
import com.indoenavisystems.indoenavi.Interfaces.VolleyCallBack;
import com.indoenavisystems.indoenavi.handlers.MapHandler;
import com.indoenavisystems.indoenavi.models.Map;
import com.indoenavisystems.indoenavi.models.TrackedSPE;
import com.indoenavisystems.indoenavi.models.Vec2;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ApiRequest apiRequest;
    private MapHandler mapHandler;

    public MainActivity(){
        mapHandler = MapHandler.getInstance();
        apiRequest = new ApiRequest(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the action bar for more visual space.
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        createStopButtonEvent();
        sendStatisticsData();
        loadMapImage();
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
        TextView tv = (TextView) findViewById(R.id.destinationTextView);
        Intent intent = getIntent();
        String destination = intent.getStringExtra("destination");
        tv.setText(destination);

        String sessionUrl = ApiUrlConstants.NewSession+"?area=ZBC-Ringsted";
        String mostSearchedDestination = ApiUrlConstants.DestinationVisits+"?area=ZBC-Ringsted&destination=D.32";
        apiRequest.stringRequest(sessionUrl, Request.Method.POST, null);
        apiRequest.stringRequest(mostSearchedDestination,Request.Method.POST,null);
    }

    private void loadMapImage(){
        apiRequest.stringRequest(ApiUrlConstants.Map + "?area=ZBC-Ringsted", Request.Method.GET, new VolleyCallBack() {
            @Override
            public void onSuccess(Object successMessage) {
                mapHandler.setMap(new Gson().fromJson(successMessage.toString(), Map.class));

                ImageView mapImage = findViewById(R.id.mapImage);
                mapImage.setImageBitmap(mapHandler.getMap().getBitmap());
            }
            @Override
            public void onFail(VolleyError error) {
            }
        });
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
                Vec2 position = mapHandler.getIndoorPosition();
                ImageView mapPositionImage = findViewById(R.id.mapPositionImage);
                mapPositionImage.setX((float)position.x - 8);
                mapPositionImage.setY((float)position.y + 36);
            }
        }, 0, 250, TimeUnit.MILLISECONDS);
    }
}
