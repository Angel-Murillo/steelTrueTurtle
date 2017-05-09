package com.example.andriod.steeltrueturtle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class listOfQueuers extends AppCompatActivity {
    private ListView queueList;
    private ArrayList<String> nClients=new ArrayList<>();
    fireBaseManager queuers = new fireBaseManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_queuers);
        queueList = (ListView) findViewById(R.id.quererList);
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





    }
}










