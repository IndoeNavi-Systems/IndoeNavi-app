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

import com.IndoeNaviSystems.indoenavi.ApiRequest;
import com.IndoeNaviSystems.indoenavi.ApiUrlConstants;
import com.IndoeNaviSystems.indoenavi.Interfaces.VolleyCallBack;
import com.IndoeNaviSystems.indoenavi.models.Map;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    ApiRequest apiRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the action bar for more visual space.
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        apiRequest = new ApiRequest(this);

        TextView tv = (TextView) findViewById(R.id.destinationTextView);

        Button stopBtn = (Button) findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When navigation stopped, go back to destination view.
                finish();
            }
        });

        Intent intent = getIntent();
        String destination = intent.getStringExtra("destination");
        tv.setText(destination);

        String sessionUrl = ApiUrlConstants.NewSession+"?area=ZBC-Ringsted";
        String mostSearchedDestination = ApiUrlConstants.DestinationVisits+"?area=ZBC-Ringsted&destination=D.32";
        apiRequest.stringRequest(sessionUrl, Request.Method.POST, null);
        apiRequest.stringRequest(mostSearchedDestination,Request.Method.POST,null);

        apiRequest.stringRequest(ApiUrlConstants.Map + "?area=ZBC-Ringsted", Request.Method.GET, new VolleyCallBack() {
            @Override
            public void onSuccess(Object successMessage) {
                Gson gson = new Gson();
                Map map = gson.fromJson(successMessage.toString(), Map.class);

                ImageView image =(ImageView)findViewById(R.id.mapImageView);
                byte[] imageBytes = Base64.decode(map.getImageData(), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                image.setImageBitmap(decodedImage);
            }

            @Override
            public void onFail(VolleyError error) {
            }
        });

    }
}