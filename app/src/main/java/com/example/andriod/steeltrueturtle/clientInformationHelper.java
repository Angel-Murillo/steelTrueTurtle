package com.example.andriod.steeltrueturtle;

import android.text.TextUtils;
import android.util.Log;
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

/**
 * Created by bola on 5/7/2017.
 */

public class clientInformationHelper
{
    
    private String userId;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    clientInformationHelper()
    {

    }

    /**
     * Creating new steelTurtleUser node under 'steelTurtleUsers'
     */
    public void createsteelTurtleUser(String name, String phone,EditText inputName,EditText inputPhone,TextView loginDetails) {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        // references clients
        mFirebaseDatabase = mFirebaseInstance.getReference("Clients");
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }
        //add the gmail to steelTurtleUser
        FirebaseUser clientAuth = mAuth.getCurrentUser();
        String gmail = clientAuth.getEmail().toString();

        steelTurtleUser steelTurtleUser = new steelTurtleUser(name, phone, gmail);

        mFirebaseDatabase.child(userId).setValue(steelTurtleUser);

        addsteelTurtleUserChangeListener(inputName,inputPhone,loginDetails);
    }

    /**
     * steelTurtleUser data change listener
     */
    public void addsteelTurtleUserChangeListener(final EditText inputName, final EditText inputPhone, final TextView loginDetails) {
        // steelTurtleUser data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                steelTurtleUser steelTurtleUser = dataSnapshot.getValue(steelTurtleUser.class);

                // Check for null
                if (steelTurtleUser == null) {
                   
                    return;
                }

                

                // Display newly updated name and phone
                loginDetails.setText("Login as: " + steelTurtleUser.getGmail());

                // clear edit text
                inputName.setText("");
                inputPhone.setText("");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                
            }
        });
    }
}
