package com.example.andriod.steeltrueturtle.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.andriod.steeltrueturtle.HostOrJoin;
import com.example.andriod.steeltrueturtle.R;
import com.example.andriod.steeltrueturtle.host.hostInformation;
import com.example.andriod.steeltrueturtle.host.manageLines;
import com.example.andriod.steeltrueturtle.steelTurtleUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class queuerDetails extends AppCompatActivity {
    private TextView lineJoined,nameDisplay,positionDisplay;
    private DatabaseReference mPostReference= FirebaseDatabase.getInstance().getReference();
    private int position=0;
    private Button leaveLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queuer_details);
        lineJoined = (TextView) findViewById(R.id.lineJoin);
        nameDisplay = (TextView) findViewById(R.id.names);
        positionDisplay=(TextView) findViewById(R.id.position);
        leaveLine= (Button) findViewById(R.id.leave);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        String line = extras.getString("lineJoined");
        final String phone = extras.getString("phones");

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
        leaveLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(queuerDetails.this, HostOrJoin.class);
                startActivity(intent);
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
