package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashBoard extends AppCompatActivity {

    Button lend,rent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        lend=findViewById(R.id.lend);
        rent=findViewById(R.id.rent);

    lend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(DashBoard.this, LenderMaps.class);
            startActivity(i);
        }
    });

    rent.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(DashBoard.this, UserMapsActivity.class);
            startActivity(i);
        }
    });
    }


}