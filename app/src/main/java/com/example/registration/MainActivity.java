package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {

    Button btn;
    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        splashImage = findViewById( R.id.splashview );

        Animation myanim = AnimationUtils.loadAnimation( this,R.anim.myanimation );
        splashImage.startAnimation( myanim );

        Thread mythread = new Thread( new Runnable() {
            @Override
            public void run () {
                try {
                    sleep(3000);
                    Intent i = new Intent(MainActivity.this,Login.class );
                    startActivity( i );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        } );
        mythread.start();
    }
}

