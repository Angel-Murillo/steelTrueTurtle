package com.example.andriod.steeltrueturtle;
import android.content.Intent;
import android.os.Bundle;
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


public class hostInformation extends AppCompatActivity {

    private static final String TAG = hostInformation.class.getSimpleName();
    private TextView loginDetails;

    private EditText inputName,inputPhone,inputlineName,inputLocation,inputTime,inputDescription;
    private Button btnSave;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Button createLine;
    private String userId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_information);
        mAuth=FirebaseAuth.getInstance();

        loginDetails = (TextView) findViewById(R.id.loginAs);
        inputName = (EditText) findViewById(R.id.name);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputlineName = (EditText) findViewById(R.id.lineName);
        inputLocation = (EditText) findViewById(R.id.location);
        inputTime = (EditText) findViewById(R.id.time);
        inputDescription = (EditText) findViewById(R.id.description);
        //Displays currently login gmail
        FirebaseUser clientAuth = mAuth.getCurrentUser();
        String gmail = clientAuth.getEmail().toString();
        loginDetails.setText("Login as: " + gmail );

        btnSave = (Button) findViewById(R.id.btn_save);

        createLine = (Button) findViewById(R.id.createLine);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // creates/references Lines
        mFirebaseDatabase = mFirebaseInstance.getReference("Line");

        // Save steelTurtleUser information
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String phone = inputPhone.getText().toString();
                String lineName = inputlineName.getText().toString();
                String location = inputLocation.getText().toString();
                String time = inputTime.getText().toString();
                String description = inputDescription.getText().toString();

                // Check for already existed userId
                if (TextUtils.isEmpty(userId)) {
                    createsteelTurtleUser(name, phone,lineName,location,time,description);

                }
            }
        });

        createLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(hostInformation.this,manageLines.class);
                startActivity(intent);

            }
        });
    }

    private void createsteelTurtleUser(String name, String phone,String lineName,String location,String time, String description) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }
        //add the gmail to steelTurtleUser
        FirebaseUser clientAuth = mAuth.getCurrentUser();
        String gmail = clientAuth.getEmail().toString();

        steelTurtleUser steelTurtleUser = new steelTurtleUser(name, phone,lineName,location,time,description,gmail);

        mFirebaseDatabase.child(userId).setValue(steelTurtleUser);

        addsteelTurtleUserChangeListener();
    }

    /**
     * steelTurtleUser data change listener
     */
    private void addsteelTurtleUserChangeListener() {
        // steelTurtleUser data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                steelTurtleUser steelTurtleUser = dataSnapshot.getValue(steelTurtleUser.class);

                // Check for null
                if (steelTurtleUser == null) {
                    return;
                }

                // clear edit text
                inputName.setText("");
                inputPhone.setText("");
                inputlineName.setText("");
                inputLocation.setText("");
                inputTime.setText("");
                inputDescription.setText("");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read steelTurtleUser", error.toException());
            }
        });
    }
}