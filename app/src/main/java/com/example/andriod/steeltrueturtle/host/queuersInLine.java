package com.example.andriod.steeltrueturtle.host;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;



public class queuersInLine extends AppCompatActivity implements AdapterView.OnItemClickListener, GoogleApiClient.OnConnectionFailedListener {
    private ListView queuerList;
    private Button queuerDetails;

    private ArrayList<String> nQueuers=new ArrayList<>();
    fireBaseManager queuers = new fireBaseManager();

    private GoogleApiClient host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity_queuers_in_line);
        queuerList = (ListView) findViewById(R.id.quererList);
        queuerDetails= (Button) findViewById(R.id.viewInformation);

        queuerList.setOnItemClickListener(this);
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

        final ArrayAdapter<String> arrayOfQueuers = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,nQueuers);

        queuerList.setAdapter(arrayOfQueuers);
        //lineClicked was passed from the lineManagement page
        //it passed the value of the line that the host wanted to manage
        Bundle extras = getIntent().getExtras();
        //linePressed will be used to retrieve the queuers in the line that the host specified to manage
        String linePressed = extras.getString("lineClicked");

        //displays the queuers in the line that the host pressed on the page, lineManagement
        queuers.displayQueuers(nQueuers,linePressed);

        try {
            Thread.sleep(2000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        queuerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sends the name of the queuer pressed to queuerInLinePopUp
                //for information retrieval of the queuer clicked on
                String queuer=getQueuerClicked();
                if(queuer!=null){
                    Intent intent = new Intent(queuersInLine.this, queuerInLinePopUp.class);
                    intent.putExtra("queuerName",queuer);
                    startActivity(intent);
                }
                else{
                    //if no queuers in the line the queuerDetails button will not display anything
                }

            }
        });

    }
    int position;
    private String nameOfQueuer;
    // retrieves/and initializes position);
    public int getPositionClicked()
    {

        return position;
    }
    public void positionClicked(int position)
    {

        this.position = position;
    }
    //gets the position of the item inside the ListView clicked by the user

    public String getQueuerClicked()
    {

        return nameOfQueuer;
    }
    public void queuerClicked(String nameOfQueuer){
        this.nameOfQueuer= nameOfQueuer;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position +"parent is: "+parent.toString());

        String nameOfLine = queuerList.getItemAtPosition(position).toString();
        Log.i("inside queuerClicked",nameOfLine);
        queuerClicked(nameOfLine);
        positionClicked(position);
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
                Intent intent=new Intent(queuersInLine.this,googleLogin.class);
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