package com.httpstwitter.birdfeed;

import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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

public class Display extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    static private ArrayList<String> tweets = new ArrayList<>();
    static private ArrayList<String> hours = new ArrayList<>();
    static private ArrayList<String> master = new ArrayList<>();
    private ListView listView;
    private String item, address, tags;
    private ArrayAdapter<String> adapter;
    private GoogleApiClient mGoogleApiClient;

    private String mLatitudeText, mLongitudeText;
    private String current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(item);

        item = (String) getIntent().getSerializableExtra("item");
        tags = (String) getIntent().getSerializableExtra("tags");
        if (tags != null) {
            master.add(tags);
        }
        master.add("Address");
        address = (String) getIntent().getSerializableExtra("address");
        if (address != null) {
            master.add(address);
        }
        master.add("Hours");
        hours = getIntent().getStringArrayListExtra("hours");
        tweets = getIntent().getStringArrayListExtra("tweets");

        for (int i = 0; i < hours.size(); i++) {
            master.add(hours.get(i));
        }

        master.add("Tweets");
        for (int i = 0; i < tweets.size(); i++) {
            master.add(tweets.get(i));
        }

        for (int i = 0; i < master.size(); i++) {
            System.out.println("Master: " + master.get(i));
        }

        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(item);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, master);
        listView.setAdapter(adapter);

        // Create an instance of GoogleAPIClient.
        // needed for location services
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    //.addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address = "http://maps.google.com/maps?saddr=" + mLatitudeText + "," + mLongitudeText + "&daddr=" + address;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
                //intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        master.clear();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else{
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                current = "873 Wheeler ST S, Tacoma, WA 98444";
            }
            return;
        }
        // other 'case' lines to check for other
        // permissions this app might request
    }

}

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //sets current location coordinates to most recent data
            mLatitudeText = (String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText = (String.valueOf(mLastLocation.getLongitude()));
        } else {
            //Sets default current location to Space Needle coordinates
            //if user denies access to location
            mLatitudeText = "47.6205 N";
            mLongitudeText = "122.3493 W";
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
