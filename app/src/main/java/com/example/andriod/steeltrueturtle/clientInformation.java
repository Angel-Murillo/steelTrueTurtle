package com.example.andriod.steeltrueturtle;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class clientInformation extends AppCompatActivity {


    private TextView loginDetails;
    private EditText inputName,inputPhone;
    private Button finished;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Button retrieveBtn;
    private String userId;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_information);
        mAuth = FirebaseAuth.getInstance();


        loginDetails = (TextView) findViewById(R.id.loginAs);
        inputName = (EditText) findViewById(R.id.name);
        inputPhone = (EditText) findViewById(R.id.phone);

        //Displays currently login gmail
        FirebaseUser clientAuth = mAuth.getCurrentUser();
        String gmail = clientAuth.getEmail().toString();
        loginDetails.setText("Login as: " + gmail);

        finished = (Button) findViewById(R.id.finish);
        //
        retrieveBtn = (Button) findViewById(R.id.createLine);
        mFirebaseInstance = FirebaseDatabase.getInstance();

        // references clients
        mFirebaseDatabase = mFirebaseInstance.getReference("Clients");


        // Save / update the user
        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String phone = inputPhone.getText().toString();


                // Check for already existed userId
                if (TextUtils.isEmpty(userId)) {
                    //createUser(name, phone);
                    clientInformationHelper test1 = new clientInformationHelper();
                    test1.createsteelTurtleUser(name,phone,inputName,inputPhone,loginDetails);
                    //createUser(name,phone,inputName,inputPhone);
                    //after it goes to the new page

                    Intent intent = new Intent(clientInformation.this, nearbyLines.class);
                    intent.putExtra("name",name);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }
            }
        });
    }

}
