package com.example.andriod.steeltrueturtle.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.example.andriod.steeltrueturtle.R;
import com.example.andriod.steeltrueturtle.steelTurtleUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class nearbyLinesPopUp extends AppCompatActivity {
    private TextView name,phone,lineName,time,location,description;
    private DatabaseReference mPostReference= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity_nearby_lines_pop_up);
        name =(TextView) findViewById(R.id.hostName);
        phone = (TextView) findViewById(R.id.phone);
        lineName = (TextView) findViewById(R.id.lineName);
        time = (TextView) findViewById(R.id.time);
        location = (TextView) findViewById(R.id.location);
        description = (TextView) findViewById(R.id.description);

        //recieved from nearbyLines
        Bundle extras = getIntent().getExtras();
        // lineName will be used to get information about the line
        // that the user pressed on nearbyLines page
        final String lineToBeReferenced= extras.getString("lineName");
        //Log.i("look here",lineToBeReferenced);
        mPostReference.child("Line").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    String names = snapshot.getValue(steelTurtleUser.class).getName();
                    String phones =snapshot.getValue(steelTurtleUser.class).getPhone();
                    String lines = snapshot.getValue(steelTurtleUser.class).getLineName();
                    String times =snapshot.getValue(steelTurtleUser.class).getTime();
                    String locations =snapshot.getValue(steelTurtleUser.class).getLocation();
                    String descriptions =snapshot.getValue(steelTurtleUser.class).getDescription();
                    //Log.i("compared to:", lines);
                    if(lines.equals(lineToBeReferenced)) {
                        name.setText("Name: "+names);
                        phone.setText("Phone: "+phones);
                        lineName.setText("Line Name: "+lines);
                        time.setText("Time: "+times);
                        location.setText("Location: "+locations);
                        description.setText("Description: "+descriptions);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DisplayMetrics queuerInformation = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(queuerInformation);

        int width = queuerInformation.widthPixels;
        int height = queuerInformation.heightPixels;



        getWindow().setLayout((int)(width*.8),(int)(height*.6));
    }
}
