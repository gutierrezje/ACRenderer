package com.example.acrenderer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private RenderDisplay mRenderDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //mRenderDisplay = new RenderDisplay(this);
        setContentView(R.layout.main_activity);
        //setContentView(mRenderDisplay);
    }
}
