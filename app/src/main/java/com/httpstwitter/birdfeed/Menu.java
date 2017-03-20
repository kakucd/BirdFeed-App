package com.httpstwitter.birdfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/*
 * Main menu class
 * Provides as a crossroad to get to the different activities of this app
 */

public class Menu extends AppCompatActivity {

    static private ArrayList<String> data = new ArrayList<>();
    static private ArrayList<String> tags = new ArrayList<>();
    private FirebaseDatabase mdatabase;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    @Override
        public void onStart() {
        super.onStart();

        mdatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mdatabase.getReference("/restaurants");

        myRef.orderByKey().addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Place place = dataSnapshot.getValue(Place.class);
                data.add(place.getName());
                tags.add(place.getTags());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //System.out.println("onChildRemoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //System.out.println("onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void login(View view) {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void settings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void search(View view) {
        Intent intent = new Intent(this, Search.class);

        intent.putStringArrayListExtra("data", data);
        intent.putStringArrayListExtra("tags", tags);
        startActivity(intent);
        //Clears all data in ArrayList
        //Prevents data from being displayed twice
        //data.clear();
    }

    public void signin(View view) {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void tweet(View view) {
        Intent intent = new Intent(this, Tweet.class);
        startActivity(intent);
    }
}
