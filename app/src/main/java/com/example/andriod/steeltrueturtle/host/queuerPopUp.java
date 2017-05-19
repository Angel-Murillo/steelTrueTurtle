package com.example.andriod.steeltrueturtle.host;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andriod.steeltrueturtle.R;
import com.example.andriod.steeltrueturtle.steelTurtleUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class queuerPopUp extends AppCompatActivity {
    private TextView name1,phone;
    private DatabaseReference mPostReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queuer_pop_up);
        name1 = (TextView) findViewById(R.id.queuerName);
        phone = (TextView) findViewById(R.id.phone);
        Bundle extras = getIntent().getExtras();
        final String nameSelected = extras.getString("queuerName");
        name1.setText("Name: "+nameSelected);

        //get the queuer Information
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


        DisplayMetrics queuerInformation = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(queuerInformation);

        int width = queuerInformation.widthPixels;
        int height = queuerInformation.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.4));

    }
}
