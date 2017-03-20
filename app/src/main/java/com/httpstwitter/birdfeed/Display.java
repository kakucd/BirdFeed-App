package com.httpstwitter.birdfeed;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Locale;

public class Display extends AppCompatActivity {

    static private ArrayList<String> tweets = new ArrayList<>();
    static private ArrayList<String> hours = new ArrayList<>();
    static private ArrayList<String> master = new ArrayList<>();
    private ListView listView;
    private String item, address, tags;
    private ArrayAdapter<String> adapter;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(item);

        item = (String) getIntent().getSerializableExtra("item");
        tags = (String) getIntent().getSerializableExtra("tags");
        if(tags != null) {
            master.add(tags);
        }
        master.add("Address");
        address = (String) getIntent().getSerializableExtra("address");
        if(address != null) {
            master.add(address);
        }
        master.add("Hours");
        hours = getIntent().getStringArrayListExtra("hours");
        tweets = getIntent().getStringArrayListExtra("tweets");

        for(int i = 0; i < hours.size(); i++) {
            master.add(hours.get(i));
        }

        master.add("Tweets");
        for(int i = 0; i < tweets.size(); i++) {
            master.add(tweets.get(i));
        }

        for(int i = 0; i < master.size(); i++) {
            System.out.println("Master: "+master.get(i));
        }

        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(item);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,android.R.id.text1, master);
        listView.setAdapter(adapter);

        //Google access and location services
        /*if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }*/
    }

    @Override
    protected void onStart() {
        //mGoogleApiClient.connect();
        super.onStart();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                address = "http://maps.google.com/maps?saddr="+address;
                //Uri uri = Uri.parse(address);
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(address));
                //intent.setData(uri);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
        master.clear();
    }

}
