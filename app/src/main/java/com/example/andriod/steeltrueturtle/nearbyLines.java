package com.example.andriod.steeltrueturtle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class nearbyLines extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private FirebaseAuth mAuth;
    private Button join;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private int position;
    private ListView lines;
    private ArrayList<String> nLines=new ArrayList<>();
    //initialize fireBaseManager object
    private fireBaseManager help1 = new fireBaseManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_lines);

        lines=(ListView)findViewById(R.id.nearbyLines);
        lines.setOnItemClickListener(this);

        join = (Button) findViewById(R.id.join);

        final ArrayAdapter<String> arrayofLines = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,nLines);
        lines.setAdapter(arrayofLines);
        help1.displayLocationChild(nLines);
        //delay code for 1 seconds
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }


        // Save / update the user
        join.setOnClickListener(new View.OnClickListener() {
            steelTurtleUser client = new steelTurtleUser();
            @Override
            public void onClick(View view) {
                // Check for already existed userId
                Log.i("yoyo","user clicked on it");

                //client joins the line he wants
                //fireBaseManager helpGetLine = new fireBaseManager();
                createUser();



            }
        });

    }
    public void createUser() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_tritle").setValue("Realtrime Database");
        // references clients
        mFirebaseDatabase = mFirebaseInstance.getReference("NearbyLine");
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }
        Log.i("what up","im inside");
        //add the gmail to user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser clientAuth = mAuth.getCurrentUser();
        String gmail = clientAuth.getEmail().toString();
        //get the putextra from clientInformation
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        String phone = extras.getString("phone");

        steelTurtleUser user = new steelTurtleUser(name, phone, gmail);
        String lineToJoin = getLineClicked();
        mFirebaseDatabase.child(lineToJoin).child(userId).setValue(user);
        Log.i("look","jere");
        //redirect the user to queuerDetails page
        Intent intent = new Intent(nearbyLines.this, queuerDetails.class);
        intent.putExtra("name",name);
        intent.putExtra("phones",phone);
        intent.putExtra("lineJoined",lineToJoin);
        startActivity(intent);

    }


    private String nameOfLine;
    public int getPositionClicked()
    {
        return position;
    }
    public void positionClicked(int position)
    {
        this.position = position;
    }
    public String getLineClicked()
    {
        return nameOfLine;
    }
    public void lineClicked(String nameOfLine){this.nameOfLine= nameOfLine;}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position +"parent is: "+parent.toString());
        String nameOfLine = lines.getItemAtPosition(position).toString();
        lineClicked(nameOfLine);
        Log.i("look at what it gave",nameOfLine);
        positionClicked(position);
    }
}
