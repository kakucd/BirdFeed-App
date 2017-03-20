package com.httpstwitter.birdfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
 * Search activity
 * Query to the database done before this class is called.
 * Technically class just presents info from database to user in a formatted way
 * dictated by the corresponding layout files.
 * If list item is clicked, the relevant data is passed onto the next activity (info class).
 */

public class Search extends AppCompatActivity {

    static private ArrayList<String> data = new ArrayList<>();
    static private ArrayList<String> tag = new ArrayList<>();
    static private ArrayList<String> hours = new ArrayList<>();
    static private ArrayList<String> tweets = new ArrayList<>();
    private ListView listView;
    private String item, address, tags;
    private ArrayAdapter<String> adapter;
    private FirebaseDatabase mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //get queried data from menu activity
        data = getIntent().getStringArrayListExtra("data");
        tag = getIntent().getStringArrayListExtra("tags");
        item = getIntent().getStringExtra("item");
        //instantiate listView to by used for display
        listView = (ListView) findViewById(R.id.query);
        //instantiate adapter to translate ArrayList data to ListView to be displayed on phone screen
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, data) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(data.get(position));
                text2.setText(tag.get(position));
                return view;
            }
        };

        listView.setAdapter(adapter);

        //When clicked, a list item will send item name to info activity/class
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                mdatabase = FirebaseDatabase.getInstance();

                item =  (String) listView.getItemAtPosition(position);
                query();
                //Toast message will pop up indicating list item and position clicked.
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +item , Toast.LENGTH_LONG)
                        .show();
                if(address != null) {
                    info();
                }
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        address = null;
        tags = null;
        hours.clear();
        tweets.clear();
    }

    public void query() {
        DatabaseReference myRef = mdatabase.getReference("/restaurants/"+item+"/address");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                address = (String) dataSnapshot.getValue();
                System.out.println(dataSnapshot.getKey()+": "+address);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference ref = mdatabase.getReference("/restaurants/"+item+"/tags");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tags = (String) dataSnapshot.getValue();
                //System.out.println("Tags: "+tags);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference r = mdatabase.getReference("/hours/"+item);
        r.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String temp = dataSnapshot.getKey() + ": " + dataSnapshot.getValue();
                hours.add(temp);
                //System.out.println("onChildAdded: "+temp);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference dbRef = mdatabase.getReference("/tweets/"+item);

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String t = "@"+dataSnapshot.getKey()+": "+dataSnapshot.getValue();
                tweets.add(t);
                //System.out.println("Tweet: "+t);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void info() {
        Intent intent = new Intent(this, Display.class);

        intent.putStringArrayListExtra("hours", hours);
        intent.putStringArrayListExtra("tweets", tweets);
        intent.putExtra("tags", tags);
        intent.putExtra("item", item);
        intent.putExtra("address", address);

        startActivity(intent);

    }
}
