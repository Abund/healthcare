package com.example.healthcare.popups;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.healthcare.R;


public class BloodSugarPopUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width= dm.widthPixels;
        int height= dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(height*0.8));
    }
}
