package com.example.largeimages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class Activity1 extends AppCompatActivity {

    private int toggle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
    }


    public void changeImage(View view) {
        if (toggle == 0) {
            view.setBackgroundResource(R.drawable.dino2);
            toggle = 1;
        } else {
            view.setBackgroundResource(R.drawable.dino1);
            toggle = 0;
        }
    }
}