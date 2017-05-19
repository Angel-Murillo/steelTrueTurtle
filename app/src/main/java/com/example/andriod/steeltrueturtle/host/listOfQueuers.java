package com.example.andriod.steeltrueturtle.host;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.andriod.steeltrueturtle.R;
import com.example.andriod.steeltrueturtle.fireBaseManager;

import java.util.ArrayList;

public class listOfQueuers extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView queueList;
    private ArrayList<String> nClients=new ArrayList<>();
    fireBaseManager queuers = new fireBaseManager();
    private Button queuerDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_queuers);
        queueList = (ListView) findViewById(R.id.quererList);
        queuerDetails= (Button) findViewById(R.id.viewInformation);
        queueList.setOnItemClickListener(this);
        final ArrayAdapter<String> arrayOfQueuers = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,nClients);

        queueList.setAdapter(arrayOfQueuers);
        Bundle extras = getIntent().getExtras();
        String linePressed = extras.getString("lineClicked");
        Log.i("here","i am");
        queuers.displayQueuers(nClients,linePressed);

        try {
            Thread.sleep(2000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        queuerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("here","yo");
                Intent intent = new Intent(listOfQueuers.this, queuerPopUp.class);
                String name = getQueuerClicked();
                Log.i("THE NAME:::", name);
                intent.putExtra("queuerName",name);

                startActivity(intent);
            }
        });

    }
    int position;
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
    private String nameOfQueuer;
    public String getQueuerClicked()
    {
        return nameOfQueuer;
    }
    public void queuerClicked(String nameOfQueuer){this.nameOfQueuer= nameOfQueuer;}
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position +"parent is: "+parent.toString());

        String nameOfLine = queueList.getItemAtPosition(position).toString();
        Log.i("inside queuerClicked",nameOfLine);
        queuerClicked(nameOfLine);
        positionClicked(position);
    }

}