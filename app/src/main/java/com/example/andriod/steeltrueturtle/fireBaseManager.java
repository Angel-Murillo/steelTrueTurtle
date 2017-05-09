package com.example.andriod.steeltrueturtle;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    fireBaseManager(){

    }
    /*maybe later on
    private int lines;
    public void firebaseLines(int lines)
    {
        this.lines = lines;
    }
    public int Lines()
    {
        return lines;
    }*/
    //used in manageLines
    //deletes line that user wants to delete
    public void deleteLine(final int position)
    {
        Log.i("POSTIONNNNNNNNNNNNNN"," POSITION:::::::     " + position);
        mPostReference.child("Line").addValueEventListener(new ValueEventListener() {
            int compare = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser clientAuth = mAuth.getCurrentUser();
                final String googleGmail = clientAuth.getEmail().toString();
                int noMore = -1;
                if(position >-1)
                {
                    Log.i("eeeeeeeeeeeeee","I entered");
                    //User user = dataSnapshot.getValue(User.class);
                    Log.i("Hello ","At the beginning compare is"+compare+ "and pos is:" + position);
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        String gmail = snapshot.getValue(steelTurtleUser.class).getGmail();
                        Log.i("checkkkk","gmail is this: "+gmail +"googleGmail is this: "+googleGmail);
                        if(gmail==null)
                        {

                        }
                        else if(gmail.equals(googleGmail)) {
                            if (position == compare) {
                                Log.i("Hello2 ", "I entered" + compare + "and postion" + position);
                                snapshot.getRef().child("gmail").setValue(null);
                                snapshot.getRef().child("name").setValue(null);
                                snapshot.getRef().child("phone").setValue(null);
                                snapshot.getRef().child("lineName").setValue(null);
                                snapshot.getRef().child("location").setValue(null);
                                snapshot.getRef().child("time").setValue(null);
                                snapshot.getRef().child("description").setValue(null);
                                //positionClicked(noMore);

                            }
                            compare++;
                        }



                    }
                }

                Log.i("Hello1 ","I did not go");
                return;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("jjj", "Failed to read user", error.toException());
            }
        });
        Log.i("end","i ended yesssssssssssssssssssssssssssssss");
        return;
    }
    //used in manageLines
    //gets the users lines that he/she created
    public void displayHostLines(final ArrayList<String> mUser)
    {
        mPostReference.child("Line").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if(dataSnapshot.exists())
                //{
                mAuth= FirebaseAuth.getInstance();
                FirebaseUser clientAuth = mAuth.getCurrentUser();
                final String googleGmail = clientAuth.getEmail().toString();
                for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    //good to know this but dont need it
                            /*String name=postSnapShot.getValue(User.class).getName();
                            String email = postSnapShot.getValue(User.class).getEmail();
                            String sentence = name+ "\n" + email;
                            mUser.add(sentence);*/

                    String lineName=postSnapShot.getValue(steelTurtleUser.class).getLineName();
                    String gmail = postSnapShot.getValue(steelTurtleUser.class).getGmail();
                    Log.i("checkkkk","gmail is this: "+gmail +"googleGmail is this: "+googleGmail);
                    if(gmail==null)
                    {
                        Log.i("eeeeeeeeeeeeeeeeee","im inside");
                    }
                    else if(gmail.equals(googleGmail))
                        mUser.add(lineName);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //allows to retrieve location of all users in firebase/specifcally for queuers
    public void displayLocationChild(final ArrayList<String> mUser) {
        mPostReference.child("Line").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if(dataSnapshot.exists())
                //{
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    String address = postSnapShot.getValue(steelTurtleUser.class).getLineName();

                    mUser.add(address);
                    //if you want to use this change arrayAdapter<User>
                    //User user= d.getValue(User.class);
                    //mUser.add(user);
                    //arrayA.notifyDataSetChanged();

                }
                //}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //
    //display the client/queuers that have assigned to a particular line
    public void displayQueuers(final ArrayList<String> nQueuers,String line) {
        mPostReference.child("NearbyLine").child(line).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if(dataSnapshot.exists())
                //{
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    String name = postSnapShot.getValue(steelTurtleUser.class).getName();
                    Log.i("here check: ",name);
                    nQueuers.add(name);


                }
                //}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
