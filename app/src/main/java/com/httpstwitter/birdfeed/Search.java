package com.httpstwitter.birdfeed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    static private ArrayList<String> data = new ArrayList<>();
    static private ArrayList<Place> places = new ArrayList<>();
    ListView listView;
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
        listView = (ListView) findViewById(R.id.query);

        DatabaseReference myRef = mdatabase.getReference("/restaurants");

        myRef.orderByKey().addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("onChildAdded at i = "+ i);
                Place place = dataSnapshot.getValue(Place.class);
                data.add(place.getName());
                places.add(place);
                System.out.println(dataSnapshot.getKey() + " Name: " + place.getName() + " Address: "+place.getAdd() + " Score: " +place.getScore());
                System.out.println(dataSnapshot.getKey() + " Tags: " + place.getTags() + " Money: " + place.getMoney());
                i++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("onChildChanged");
                Place place = dataSnapshot.getValue(Place.class);
                System.out.println(dataSnapshot.getKey() + " Name: " + place.getName() + " Address: "+place.getAdd() + " Score: " +place.getScore());
                System.out.println(dataSnapshot.getKey() + " Tags: " + place.getTags() + " Money: " + place.getMoney());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("onChildRemoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                System.out.println("onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, data);

        listView.setAdapter(adapter);
        System.out.println("ArrayAdapter instantiated");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("onItemClicked");
                int itemPosition = position;
                item =  (String) listView.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +item , Toast.LENGTH_LONG)
                        .show();
                info(view);
            }
        });

    }

    public void info(View view) {
        Intent intent = new Intent(this, Info.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }
}
