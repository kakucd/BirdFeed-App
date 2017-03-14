package com.httpstwitter.birdfeed;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/*
 * Info activity
 * Displays information about the selected restaurant that comes from the database.
 * Based upon the database structure, multiple queries are required to display all important
 * information to the user. Keeping the database references and listView adapters straight presents
 * a challenge.
 */

public class Info extends AppCompatActivity {

    FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
    ArrayList<String> hours = new ArrayList<>();
    ListView listView;
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        item = (String) getIntent().getSerializableExtra("item");
        DatabaseReference myRef = mdatabase.getReference("/hours/"+item);
        listView = (ListView) findViewById(R.id.hours);

        myRef.orderByValue().addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String temp = (String) dataSnapshot.getValue();
                hours.add(temp);
                System.out.println(dataSnapshot.getKey() + " " + temp);
                i++;
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, hours);

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
