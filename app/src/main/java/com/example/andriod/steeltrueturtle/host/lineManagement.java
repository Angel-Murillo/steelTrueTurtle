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

public class lineManagement extends AppCompatActivity implements AdapterView.OnItemClickListener, GoogleApiClient.OnConnectionFailedListener {

    private Button delButton,makeNewLine,manageLine;
    private ListView lineList;
    private ArrayList<String> hostLines=new ArrayList<>();

    private fireBaseManager manageLines;
    private GoogleApiClient host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity_line_management);

        manageLine = (Button) findViewById(R.id.manageLine);
        delButton = (Button) findViewById(R.id.delete);
        makeNewLine = (Button) findViewById(R.id.makeNewLine);
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

        lineList=(ListView)findViewById(R.id.lineList);

        lineList.setOnItemClickListener(this);

        manageLines = new fireBaseManager();


        final ArrayAdapter<String> arrayOfLines = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,hostLines);
        lineList.setAdapter(arrayOfLines);

        manageLines.displayHostLines(hostLines);
        //delay code for 2 seconds so lines can be loaded up on the screen
        try {
            Thread.sleep(2000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        manageLine.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(lineManagement.this, queuersInLine.class);
                //passes the name of the line pressed by the host, to queuerInline page for queuer retrieval
                String linePressed=getLineClicked();
                if(linePressed!=null)
                {
                    intent.putExtra("lineClicked",linePressed);
                    startActivity(intent);
                }
                else{
                    //do nothing if user did not press a line in the ListView
                }
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deleter = getPositionClicked();
                if(deleter>-1){
                    manageLines.deleteLine(deleter);
                    finish();
                    startActivity(getIntent());
                }
                else{
                    //do nothing if user did not press a line in the ListView
                }

            }
        });
        makeNewLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(lineManagement.this, lineCreation.class);
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

    private int position=-1;
    // retrieves/and initializes position
    public int getPositionClicked()
    {
        return position;
    }
    public void positionClicked(int position)
    {
        this.position = position;
    }
    //gets the position of the item inside the ListView clicked by the user
    private String nameOfLine;
    public String getLineClicked()
    {
        return nameOfLine;
    }
    public void lineClicked(String nameOfLine){this.nameOfLine= nameOfLine;}
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position +"parent is: "+parent.toString());

        String nameOfLine = lineList.getItemAtPosition(position).toString();
        lineClicked(nameOfLine);
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
                Intent intent=new Intent(lineManagement.this,googleLogin.class);
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