package com.example.andriod.steeltrueturtle.client;

import android.content.Intent;
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
import com.example.andriod.steeltrueturtle.host.lineCreation;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class acquireInformation extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView loginDetails;
    private EditText inputName,inputPhone;
    private Button finished;

    private FirebaseAuth mAuth;
    private String userId;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity_acquire_information);
        mAuth = FirebaseAuth.getInstance();

        loginDetails = (TextView) findViewById(R.id.loginAs);
        inputName = (EditText) findViewById(R.id.name);
        inputPhone = (EditText) findViewById(R.id.phone);

        finished = (Button) findViewById(R.id.finish);
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
        //Displays currently login gmail
        FirebaseUser clientAuth = mAuth.getCurrentUser();
        String gmail = clientAuth.getEmail().toString();
        loginDetails.setText("Login as: " + gmail);

        // creates a steelTurtleuser(client) and saves their information to firebase/also redirect client to the nearbyLine page
        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String phone = inputPhone.getText().toString();

                if(phone.length()!=10){
                    Snackbar spacesMessage = Snackbar.make(view, "Sorry please make sure your phone number is correct", Snackbar.LENGTH_SHORT);
                    spacesMessage.show();
                    return;
                }
                else if(name.length()==0){
                    Snackbar spacesMessage = Snackbar.make(view, "Sorry please enter a your name", Snackbar.LENGTH_SHORT);
                    spacesMessage.show();
                    return;
                }else if(phone.length() == 0){
                    Snackbar spaceMessage = Snackbar.make(view, "Sorry please enter your phone", Snackbar.LENGTH_SHORT);
                    spaceMessage.show();
                    return;
                }else if(haveSpecialCharacter(name)){
                    Snackbar spaceMessage = Snackbar.make(view, "Sorry please don't have any special characters in your name", Snackbar.LENGTH_SHORT);
                    spaceMessage.show();
                    return;
                }else if(haveCharacter(phone)){
                    Snackbar spaceMessage = Snackbar.make(view, "Sorry please don't have any characters in your phone number", Snackbar.LENGTH_SHORT);
                    spaceMessage.show();
                    return;
                }else if(haveNumbers(name)){
                    Snackbar spaceMessage = Snackbar.make(view, "Sorry please don't have any numbers in your name", Snackbar.LENGTH_SHORT);
                    spaceMessage.show();
                    return;
                }else if(haveSpecialCharacter(phone)){
                    Snackbar spaceMessage = Snackbar.make(view, "Sorry please don't have any special characters in your phone number", Snackbar.LENGTH_SHORT);
                    spaceMessage.show();
                    return;
                }else{
                    createsteelTurtleUser(name,phone,inputName,inputPhone,loginDetails);

                    Intent intent = new Intent(acquireInformation.this, nearbyLines.class);
                    //passes name and phone to nearbyLines pages for when the client
                    //want to join a line in nearbyLines they can register into that line
                    //with the information they provided in the acquireInformation page
                    intent.putExtra("name",name);
                    intent.putExtra("phone",phone);
                    //redirect to nearbyLine page
                    startActivity(intent);
                }
            }
        });
    }
    public void createsteelTurtleUser(String name, String phone,EditText inputName,EditText inputPhone,TextView loginDetails) {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        // references clients/ or create Client on database if it does not exist
        mFirebaseDatabase = mFirebaseInstance.getReference("Clients");

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }
        //add the gmail to steelTurtleUser object
        FirebaseUser clientAuth = mAuth.getCurrentUser();
        String gmail = clientAuth.getEmail().toString();

        steelTurtleUser client = new steelTurtleUser(name, phone, gmail);

        //sets the information that the user provided in the editText
        // to firebase, which pertains to a userId that is a garbled name,
        // consisting of numbers and letters
        mFirebaseDatabase.child(userId).setValue(client);

        clearTextListener(inputName,inputPhone,loginDetails);
    }
    //clears the editText
    public void clearTextListener(final EditText inputName, final EditText inputPhone, final TextView loginDetails) {
        // steelTurtleUser data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                steelTurtleUser client = dataSnapshot.getValue(steelTurtleUser.class);
                // Check for null
                if (client == null) {
                    return;
                }

                loginDetails.setText("Login as: " + client.getGmail());

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
                Intent intent=new Intent(acquireInformation.this,googleLogin.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean haveSpecialCharacter(String name){
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        return m.find();
    }

    private boolean haveNumbers(String name){
        Pattern p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        return m.find();
    }

    private boolean haveCharacter(String number){
        Pattern p = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(number);
        return m.find();
    }

}

