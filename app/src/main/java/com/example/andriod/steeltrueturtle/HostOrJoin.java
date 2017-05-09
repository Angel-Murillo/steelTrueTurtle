package com.example.andriod.steeltrueturtle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HostOrJoin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_or_join);

    }
    public void redirectHost(View view)
    {

        Intent i = new Intent(HostOrJoin.this, hostInformation.class);
        startActivity(i);

    }
    public void redirectClient(View view)
    {

        Intent i = new Intent(HostOrJoin.this, clientInformation.class);
        startActivity(i);

    }
}

