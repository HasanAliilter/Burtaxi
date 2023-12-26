package com.burta.burtaxi;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class burtaxi_splash_screen extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burtaxi_splash_screen);


        timer =new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                Intent intent =new Intent(burtaxi_splash_screen.this,FragmentDorduncu.class);
                startActivity(intent);
            }
        },5000);
    }
}