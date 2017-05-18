package com.example.andriod.steeltrueturtle.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.andriod.steeltrueturtle.R;

public class hostInformationPopUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pop_up);
        DisplayMetrics queuerInformation = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(queuerInformation);

        int width = queuerInformation.widthPixels;
        int height = queuerInformation.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.4));
    }
}
