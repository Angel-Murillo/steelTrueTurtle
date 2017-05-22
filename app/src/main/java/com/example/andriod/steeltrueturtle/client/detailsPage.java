package com.example.andriod.steeltrueturtle.client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.andriod.steeltrueturtle.HostOrJoin;
import com.example.andriod.steeltrueturtle.R;
import com.example.andriod.steeltrueturtle.fireBaseManager;
import com.example.andriod.steeltrueturtle.googleLogin;
import com.example.andriod.steeltrueturtle.steelTurtleUser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class detailsPage extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView lineJoined,nameDisplay,positionDisplay;
    private FirebaseAuth mAuth;
    private DatabaseReference mPostReference= FirebaseDatabase.getInstance().getReference();
    //position is used to know the position of the client he/she is enrolled in
    private int position=0;
    private Button leaveLine;
    // used in the leaveLine button to remove client from the line he registered in
    // firebase
    public fireBaseManager deleteClient;
    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //delay code for 2 seconds so queuer can be put on the database under the line he chose
        // in nearbyLines activity
        try {
            Thread.sleep(2000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity_details_page);
        lineJoined = (TextView) findViewById(R.id.lineJoin);
        nameDisplay = (TextView) findViewById(R.id.names);
        positionDisplay=(TextView) findViewById(R.id.position);
        leaveLine= (Button) findViewById(R.id.leave);
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
        Bundle extras = getIntent().getExtras();
        //acquired from nearbyLines to display to the user
        //information regarding them and the line they joined
        final String name = extras.getString("name");
        final String phone = extras.getString("phones");

        //used to reference the line that client chose
        final String line = extras.getString("lineJoined");

        //sets text for the (TextViews)lineJoin, and names
        lineJoined.setText("Line Join: "+ line);
        nameDisplay.setText("Name: "+name);

        //get the position of the user, by referencing NearbyLine and the
        // line(passed from the nearbyLines page) that the client chose
        mPostReference.child("NearbyLine").child(line).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    position++;
                    String phones =snapshot.getValue(steelTurtleUser.class).getPhone();
                    if(phones.equals(phone)){
                        //acquires the position by comparing phone from the client
                        // and the phones from everyone that is currently enroll in the line
                        // that the client chose in the nearbyLines page
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
                Intent intent = new Intent(detailsPage.this, HostOrJoin.class);
                deleteClient=new fireBaseManager();

                //removes client from the line he registered in firebase
                deleteClient.removeFromLine(line,phone);
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
                Intent intent=new Intent(detailsPage.this,googleLogin.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
