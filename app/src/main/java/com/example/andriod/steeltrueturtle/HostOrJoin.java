package com.example.andriod.steeltrueturtle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class HostOrJoin extends AppCompatActivity {

    private TextView userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_or_join);

        userNameTextView = (TextView) findViewById(R.id.userNameTextView);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            userNameTextView.setText(firebaseUser.getDisplayName());
        }

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

