package com.example.andriod.steeltrueturtle;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.andriod.steeltrueturtle.steelTurtleUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by bola on 5/7/2017.
 */

public class fireBaseManager {
    private DatabaseReference mPostReference= FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;

    //used to initialize object
    public fireBaseManager(){

    }
    //used in the deleteLine method in this class
    //deletes the same line that was deleted in deleteLine
    //but this time in the child NearbyLine
    public void deleteLineWithQueuers(final String lineName)
    {
        mPostReference.child("NearbyLine").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    String lines = snapshot.getKey().toString();

                    if(lines.equals(lineName)){
                        snapshot.getRef().setValue(null);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FAILED", "Failed", databaseError.toException());
            }
        });
    }
    //used in HOST,lineManagement
    //deletes line that user wants to delete from 'Line' in firebase
    //Logic:compare the gmails that are under 'Line' from firebase, with the
    //gmail that the user log in as and goes through each object under 'Line'
    //until the position(relative to the lines that pertain to the current host)
    //of the line clicked by the Host is reached
    public void deleteLine(final int position)
    {

        mPostReference.child("hostInformation_lineInformation").addValueEventListener(new ValueEventListener() {
            int compare = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser clientAuth = mAuth.getCurrentUser();
                final String googleGmail = clientAuth.getEmail().toString();
                int noMore = -1;
                if(position >-1)
                {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        String gmail = snapshot.getValue(steelTurtleUser.class).getGmail();
                        if(gmail==null)
                        {

                        }
                        else if(gmail.equals(googleGmail)) {
                            if (position == compare) {
                                Log.i("Hello2 ", "I entered" + compare + "and postion" + position);
                                snapshot.getRef().child("gmail").setValue(null);
                                snapshot.getRef().child("name").setValue(null);
                                snapshot.getRef().child("phone").setValue(null);
                                String lineName = snapshot.getValue(steelTurtleUser.class).getLineName();
                                deleteLineWithQueuers(lineName);
                                snapshot.getRef().child("lineName").setValue(null);
                                snapshot.getRef().child("location").setValue(null);
                                snapshot.getRef().child("time").setValue(null);
                                snapshot.getRef().child("description").setValue(null);
                            }
                            compare++;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FAILED", "Failed", error.toException());
            }
        });
    }
    //used in lineManagement
    //adds all the lines that pertain to the current user using the application
    //to an array which then the lines can be viewed by a ListView in the
    //lineManagement page
    //Logic:compare the gmails that are under 'Line' from firebase, with the
    //gmail that the user log in as
    public void displayHostLines(final ArrayList<String> hostLines)
    {
        mPostReference.child("hostInformation_lineInformation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAuth= FirebaseAuth.getInstance();
                FirebaseUser clientAuth = mAuth.getCurrentUser();
                final String googleGmail = clientAuth.getEmail().toString();
                for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    String lineName=postSnapShot.getValue(steelTurtleUser.class).getLineName();
                    String gmail = postSnapShot.getValue(steelTurtleUser.class).getGmail();

                    if(gmail==null)
                    {

                    }
                    else if(gmail.equals(googleGmail)) {
                        hostLines.add(lineName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FAILED", "Failed", databaseError.toException());
            }
        });
    }
    //used in CLIENT, nearbyLines
    //adds all the lines that have been created in firebase
    //which can be looked at through nearbyLine listView
    public void displayLines(final ArrayList<String> mUser) {
        mPostReference.child("hostInformation_lineInformation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    String address = postSnapShot.getValue(steelTurtleUser.class).getLineName();

                    mUser.add(address);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FAILED", "Failed", databaseError.toException());
            }
        });
    }
    //used in HOST,queuersInLine
    //display the clients that have signed up to a particular line
    //that the host created
    public void displayQueuers(final ArrayList<String> nQueuers,String line) {
        mPostReference.child("NearbyLine").child(line).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    String name = postSnapShot.getValue(steelTurtleUser.class).getName();
                    nQueuers.add(name);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FAILED", "Failed", databaseError.toException());
            }
        });
    }

    //used in CLIENT,detailsPage
    //removes client from the line he registered in firebase;from NearbyLines
    public void removeFromLine(final String lineName,final String phone)
    {

        mPostReference.child("NearbyLine").child(lineName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    String phones = snapshot.getValue(steelTurtleUser.class).getPhone();

                    if(phones==null)
                    {

                    }
                    else if(phones.equals(phone)){
                        //Log.i("name:",names);
                        snapshot.getRef().child("gmail").setValue(null);
                        snapshot.getRef().child("name").setValue(null);
                        snapshot.getRef().child("phone").setValue(null);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FAILED", "Failed", error.toException());
            }
        });

    }
}