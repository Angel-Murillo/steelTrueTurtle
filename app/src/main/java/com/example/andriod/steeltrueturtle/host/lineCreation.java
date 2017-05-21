package com.example.andriod.steeltrueturtle.host;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andriod.steeltrueturtle.R;
import com.example.andriod.steeltrueturtle.googleLogin;
import com.example.andriod.steeltrueturtle.steelTurtleUser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class lineCreation extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private String userId;
    private TextView loginDetails;
    private EditText inputName,inputPhone,inputLineName,inputLocation,inputTime,inputDescription;
    private Button createLine;
    private Button proceedToLineManagement;

    private GoogleApiClient host;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity_line_creation);
        mAuth=FirebaseAuth.getInstance();

        loginDetails = (TextView) findViewById(R.id.loginAs);
        inputName = (EditText) findViewById(R.id.name);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputLineName = (EditText) findViewById(R.id.lineName);
        inputLocation = (EditText) findViewById(R.id.location);
        inputTime = (EditText) findViewById(R.id.time);
        inputDescription = (EditText) findViewById(R.id.description);
        // Configure sign-in to request the user's ID, email address, and basic profile. ID and
        // basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        host = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,  this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //Displays currently login gmail
        FirebaseUser clientAuth = mAuth.getCurrentUser();
        if(clientAuth!=null)
        {
            String gmail = clientAuth.getEmail().toString();
            loginDetails.setText("Login as: " + gmail );
        }

        createLine = (Button) findViewById(R.id.createLine);
        proceedToLineManagement = (Button) findViewById(R.id.manageLine);


        mFirebaseInstance = FirebaseDatabase.getInstance();

        // creates/references Lines
        mFirebaseDatabase = mFirebaseInstance.getReference("hostInformation_lineInformation");

        //load up firebase user's information onto the form
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            inputName.setText(firebaseUser.getDisplayName());
        }

        // Save steelTurtleUser information/ and checks user input
        createLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String phone = inputPhone.getText().toString();
                String lineName = inputLineName.getText().toString();
                String location = inputLocation.getText().toString();
                String time = inputTime.getText().toString();
                String description = inputDescription.getText().toString();
                //TODO **needs a time contains ":"
                //implement the clock
                //ADD more input safety, like name should have certain lengths
                if(name.length()>0 &&phone.length()==10&& lineName.length() >0&& location.length() >0&& time.length() >4&&time.length() <6&& description.length()>0)
                {
                    // Check for already existed userId
                    if (TextUtils.isEmpty(userId)) {
                        createsteelTurtleUser(name, phone, lineName, location, time, description);

                    }
                    Intent intent = new Intent(lineCreation.this, lineManagement.class);
                    startActivity(intent);
                }
                else if(name.length()==0 ||phone.length()==0|| lineName.length()==0|| location.length()==0|| time.length()==0|| description.length()==0)
                {
                    Snackbar spacesMessage = Snackbar.make(view, "Please fill in all empty spaces", Snackbar.LENGTH_SHORT);
                    spacesMessage.show();
                    return;
                }
                /*else if(!time.contains(":")){
                    Snackbar spacesMessage = Snackbar.make(view, "Time requires a semicolon", Snackbar.LENGTH_SHORT);
                    spacesMessage.show();
                    return;
                }*/
                else  if(phone.length()!=10){
                    Snackbar spacesMessage = Snackbar.make(view, "Sorry please make sure your phone number is correct", Snackbar.LENGTH_SHORT);
                    spacesMessage.show();
                    return;
                }
            }
        });

        proceedToLineManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(lineCreation.this,lineManagement.class);
                startActivity(intent);

            }
        });
    }

    private void createsteelTurtleUser(String name, String phone,String lineName,String location,String time, String description) {

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }
        //add the gmail to steelTurtleUser
        FirebaseUser clientAuth = mAuth.getCurrentUser();
        String gmail = clientAuth.getEmail().toString();

        steelTurtleUser steelTurtleUser = new steelTurtleUser(name, phone,lineName,location,time,description,gmail);

        //adds information provided by user such as their name, phone,lineName, location,time description,gmail to firebase
        mFirebaseDatabase.child(userId).setValue(steelTurtleUser);


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
                Auth.GoogleSignInApi.signOut(host);
                Intent intent=new Intent(lineCreation.this,googleLogin.class);
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
