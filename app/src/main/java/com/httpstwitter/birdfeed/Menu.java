package com.httpstwitter.birdfeed;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/*
 * Main menu class
 * Provides as a crossroad to get to the different activities of this app
 */

public class Menu extends AppCompatActivity {

    static private ArrayList<String> data = new ArrayList<>();
    static private ArrayList<String> tags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
        public void onStart() {
        super.onStart();

        //Request permissions for location services
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        new Query().execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        data.clear();
        tags.clear();
    }

    public void settings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void search(View view) {
        Intent intent = new Intent(this, Search.class);
        intent.putStringArrayListExtra("data", data);
        intent.putStringArrayListExtra("tag", tags);
        startActivity(intent);
    }

    public void signin(View view) {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void tweet(View view) {
        Intent intent = new Intent(this, Tweet.class);
        startActivity(intent);
    }

    // Will need this request if sreaches filtered by distance
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private class Query extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... voids) {

            FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
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
                    //System.out.println("onChildChanged");
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Place place = dataSnapshot.getValue(Place.class);
                    int i = data.indexOf(place);
                    data.remove(i);
                    tags.remove(i);
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
            return null;
        }
    }
}
