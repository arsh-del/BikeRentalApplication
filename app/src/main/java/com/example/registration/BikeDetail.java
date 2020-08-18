package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class BikeDetail extends AppCompatActivity {

    TextView markertxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_detail);

        markertxt=findViewById(R.id.marker);

        String title=getIntent().getStringExtra("title");
        markertxt.setText(title);

    }
}