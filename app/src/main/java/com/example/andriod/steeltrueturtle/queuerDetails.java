package com.example.andriod.steeltrueturtle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class queuerDetails extends AppCompatActivity {
    private TextView lineJoined,nameDisplay,positionDisplay;
    private DatabaseReference mPostReference= FirebaseDatabase.getInstance().getReference();
    private int position=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queuer_details);
        lineJoined = (TextView) findViewById(R.id.lineJoin);
        nameDisplay = (TextView) findViewById(R.id.names);
        positionDisplay=(TextView) findViewById(R.id.position);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        String line = extras.getString("lineJoined");
        final String phone = extras.getString("phones");
        Log.i("aqui","here");
        Log.i("name",name);
        Log.i("line",line);
        lineJoined.setText("Line Join: "+ line);
        nameDisplay.setText("Name: "+name);

        //get the position of the user
        mPostReference.child("NearbyLine").child(line).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    position++;
                    String phones =snapshot.getValue(steelTurtleUser.class).getPhone();
                    if(phones.equals(phone)){
                        positionDisplay.setText("Position: "+position);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        onBackPressed();

    }
    @Override
    public void onBackPressed()
    {
        //can not go back
    }
}