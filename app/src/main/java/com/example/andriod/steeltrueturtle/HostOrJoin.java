package com.example.andriod.steeltrueturtle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.andriod.steeltrueturtle.client.acquireInformation;
import com.example.andriod.steeltrueturtle.host.lineCreation;

public class HostOrJoin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView userNameTextView;
    private GoogleApiClient user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //delay code for 2 seconds so firebase can retrieve gmail information; name of current user
        //which is used in userNameTextView
        try {
            Thread.sleep(2000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_or_join);

        userNameTextView = (TextView) findViewById(R.id.userNameTextView);

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and
        // basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        user = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,  this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //retrieve user name from gmail
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            userNameTextView.setText(firebaseUser.getDisplayName());
        }

    }
    public void redirectHost(View view)
    {

        Intent i = new Intent(HostOrJoin.this, lineCreation.class);
        startActivity(i);

    }
    public void redirectClient(View view)
    {

        Intent i = new Intent(HostOrJoin.this, acquireInformation.class);
        startActivity(i);

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
                Auth.GoogleSignInApi.signOut(user);
                Intent intent=new Intent(HostOrJoin.this,googleLogin.class);
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

