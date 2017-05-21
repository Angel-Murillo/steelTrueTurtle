package com.example.andriod.steeltrueturtle.client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.andriod.steeltrueturtle.R;
import com.example.andriod.steeltrueturtle.fireBaseManager;
import com.example.andriod.steeltrueturtle.googleLogin;
import com.example.andriod.steeltrueturtle.host.lineCreation;
import com.example.andriod.steeltrueturtle.steelTurtleUser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class nearbyLines extends AppCompatActivity implements AdapterView.OnItemClickListener, GoogleApiClient.OnConnectionFailedListener
{
    private FirebaseAuth mAuth;
    private Button join,viewDetails;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private ListView lines;
    private ArrayList<String> nLines=new ArrayList<>();

    private fireBaseManager manageLines = new fireBaseManager();
    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity_nearby_lines);

        lines = (ListView)findViewById(R.id.nearbyLines);
        lines.setOnItemClickListener(this);
        viewDetails =(Button) findViewById(R.id.viewDetails);
        join = (Button) findViewById(R.id.join);
        // Configure sign-in to request the user's ID, email address, and basic profile. ID and
        // basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,  this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        final ArrayAdapter<String> arrayofLines = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,nLines);
        lines.setAdapter(arrayofLines);
        manageLines.displayLines(nLines);
        //delay code for 3 seconds
        try {
            Thread.sleep(3000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        // Save user information to firebase and redirect control to detailsPage
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();

            }
        });

        // Shows details about the line client want to view info about
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //redirect the user to detailsPage page

                String lineToJoin = getLineClicked();

                //executes only when a user has clicked on a line from the listView
                if(lineToJoin!=null) {
                    //passes the line that the client clicked on
                    //to nearbyLinesPopUp to lookup in firebase for the
                    //line and show info about the line
                    Intent intent = new Intent(nearbyLines.this, nearbyLinesPopUp.class);
                    intent.putExtra("lineName", lineToJoin);
                    startActivity(intent);
                }
                else{
                    //if no line was clicked then nothing happens
                }
            }
        });
        //does not allow user to go to the previous page
        onBackPressed();
    }
    public void createUser() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // references nearbyLine
        mFirebaseDatabase = mFirebaseInstance.getReference("NearbyLine");
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser clientAuth = mAuth.getCurrentUser();
        //gmail will be used to register the client in firebase
        //with the line he/she clicked on
        String gmail = clientAuth.getEmail().toString();
        //get the putextra from acquireInformation
        Bundle extras = getIntent().getExtras();
        //name and phone will be used to register the client in firebase
        //with the line he/she clicked on
        String name = extras.getString("name");
        String phone = extras.getString("phone");

        steelTurtleUser user = new steelTurtleUser(name, phone, gmail);
        //gets the line that client clicked on
        String lineToJoin = getLineClicked();

        //executes only when a user has clicked on a line from the listView
        if(lineToJoin!=null) {
            //registers client in the firebase to the line he/she picked
            //lineJoin is the line that the client picked
            //userId is the grabled number that was given to the client
            //user is an object that contains the information, name,phone, gmail
            mFirebaseDatabase.child(lineToJoin).child(userId).setValue(user);

            //redirect the user to detailsPage page
            Intent intent = new Intent(nearbyLines.this, detailsPage.class);
            //passes the name,phone,lineToJoin(line pressed on this page activity)
            //to detailsPage to provide detail to the user about
            //themselves and the line they picked
            intent.putExtra("name", name);
            intent.putExtra("phones", phone);
            intent.putExtra("lineJoined", lineToJoin);
            startActivity(intent);
        }
        else{
            //if no line was clicked then nothing happens
        }
    }
    //used to know what line the client clicked on
    private String nameOfLine;
    public String getLineClicked()
    {
        return nameOfLine;
    }
    public void lineClicked(String nameOfLine){this.nameOfLine= nameOfLine;}
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position +"parent is: "+parent.toString());
        String nameOfLine = lines.getItemAtPosition(position).toString();
        lineClicked(nameOfLine);

    }
    //displays logout option in the header
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_out_menu, menu);
        return true;
    }
    //handles the logout option
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logOut:

                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(client);
                Intent intent=new Intent(nearbyLines.this,googleLogin.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    @Override
    public void onBackPressed()
    {
        //can not go back
    }
}