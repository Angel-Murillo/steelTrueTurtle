package com.example.andriod.steeltrueturtle.host;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.andriod.steeltrueturtle.R;
import com.example.andriod.steeltrueturtle.steelTurtleUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class hostInformation extends AppCompatActivity {

    private static final String TAG = hostInformation.class.getSimpleName();
    private TextView loginDetails;

    private EditText inputName,inputPhone,inputlineName,inputLocation,inputTime,inputDescription;
    private Button createLine;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private FirebaseAuth mAuth;
    private Button proceed;

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

        createLine = (Button) findViewById(R.id.createLine);
        proceed = (Button) findViewById(R.id.proceed1);
        //proceed = (Button) findViewById(R.id.proceed);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // creates/references Lines
        mFirebaseDatabase = mFirebaseInstance.getReference("Line");

        //load up firebase user's information onto the form
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            inputName.setText(firebaseUser.getDisplayName());
        }

        inputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(hostInformation.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        inputTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        // Save steelTurtleUser information
        createLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String phone = inputPhone.getText().toString();
                String lineName = inputlineName.getText().toString();
                String location = inputLocation.getText().toString();
                String time = inputTime.getText().toString();
                String description = inputDescription.getText().toString();
                if(name.length()>0 &&phone.length()==10&& lineName.length() >0&& location.length() >0&&time.contains(":")&& time.length() >4&&time.length() <6&& description.length()>0)
                {

                    // Check for already existed userId
                    if (TextUtils.isEmpty(userId)) {
                        createsteelTurtleUser(name, phone, lineName, location, time, description);

                    }
                    Intent intent = new Intent(hostInformation.this, manageLines.class);
                    startActivity(intent);
                }
                else if(!time.contains(":")){
                    Snackbar spacesMessage = Snackbar.make(view, "Time requires a semicolon", Snackbar.LENGTH_SHORT);
                    spacesMessage.show();
                }
                else  if(phone.length()!=10){
                    Snackbar spacesMessage = Snackbar.make(view, "Sorry please make sure your phone number is correct", Snackbar.LENGTH_SHORT);
                    spacesMessage.show();
                }
                else {
                    Snackbar spacesMessage = Snackbar.make(view, "Please fill in all empty spaces", Snackbar.LENGTH_SHORT);
                    spacesMessage.show();
                }
                // Check for already existed userId
                if (TextUtils.isEmpty(userId)) {
                    createsteelTurtleUser(name, phone,lineName,location,time,description);
                }

                Toast.makeText(hostInformation.this, "Line created", Toast.LENGTH_LONG).show();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
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