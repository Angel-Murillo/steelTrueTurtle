package com.example.andriod.steeltrueturtle.host;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.andriod.steeltrueturtle.R;
import com.example.andriod.steeltrueturtle.steelTurtleUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class queuerInLinePopUp extends AppCompatActivity {
    private TextView queuerName,phone;
    private DatabaseReference mPostReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity_queuer_in_line_pop_up);

        queuerName = (TextView) findViewById(R.id.queuerName);
        phone = (TextView) findViewById(R.id.phone);

        //queuerName is retrieve from queuersInLine page
        //and will be used for information retrieval of the queuer that was clicked on
        // in by the host; in queuersInLine page
        Bundle extras = getIntent().getExtras();
        final String nameSelected = extras.getString("queuerName");
        queuerName.setText("Name: "+nameSelected);

        //gets information of the user by looking through the Clients in firebase and retrieves
        //their data, in this case just phone information
        mPostReference.child("Clients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    String name = snapshot.getValue(steelTurtleUser.class).getName();
                    String phones =snapshot.getValue(steelTurtleUser.class).getPhone();
                    if(name.equals(nameSelected)) {
                        phone.setText("Phone: "+phones);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //enables for the pop up window to be shown on the screen
        DisplayMetrics queuerInformation = new DisplayMetrics();
        //normalizes the window; by a default structure
        getWindowManager().getDefaultDisplay().getMetrics(queuerInformation);

        int width = queuerInformation.widthPixels;
        int height = queuerInformation.heightPixels;
        //configures the size of the pop up window
        getWindow().setLayout((int)(width*.8),(int)(height*.4));

    }
}
