package com.httpstwitter.birdfeed;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Display extends AppCompatActivity {

    ArrayList<String> tweets = new ArrayList<>();
    ArrayList<String> hours = new ArrayList<>();
    ArrayList<String> master = new ArrayList<>();
    TextView nameView;
    ListView listView;
    Toolbar toolbar;
    String item, address, tags;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        item = (String) getIntent().getSerializableExtra("item");
        //System.out.println("Name: "+item);
        tags = (String) getIntent().getSerializableExtra("tags");
        //System.out.println("Tags: "+tags);
        if(tags != null) {
            master.add(tags);
        }
        master.add("Address");
        address = (String) getIntent().getSerializableExtra("address");
        //System.out.println("Address: "+address);
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

        //nameView = (TextView)findViewById(R.id.viewName);
        //nameView.setText(item);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(item);

        listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,android.R.id.text1, master);
        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
