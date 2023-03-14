package com.indoenavisystems.indoenavi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the action bar for more visual space.
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

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

    }
}