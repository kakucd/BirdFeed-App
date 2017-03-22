package com.httpstwitter.birdfeed;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/*
 * Info activity
 * Displays information about the selected restaurant that comes from the database.
 * Based upon the database structure, multiple queries are required to display all important
 * information to the user. Keeping the database references and listView adapters straight presents
 * a challenge.
 */

public class Info extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    private ArrayList<String> tweets = new ArrayList<>();
    private ArrayList<String> hours = new ArrayList<>();
    private TextView textView, nameView, tagsView, hoursView, tweetsView, websiteView, phoneView, webView, cellView;
    private LinearLayout linearLayout;
    private String item, address, tags, website, phone;
    private GoogleApiClient mGoogleApiClient;
    private String mLatitudeText, mLongitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        item = (String) getIntent().getSerializableExtra("item");
        address = getIntent().getStringExtra("address");
        tags = getIntent().getStringExtra("tags");
        hours = getIntent().getStringArrayListExtra("hours");
        tweets = getIntent().getStringArrayListExtra("tweets");

        for (int i = 0; i < tweets.size(); i++) {
            System.out.println(tweets.get(i));
        }

        //System.out.println("Name: "+item);
        nameView = (TextView) findViewById(R.id.viewName);
        nameView.setText(item);

        tagsView = (TextView) findViewById(R.id.tagsView);
        tagsView.setText(tags);

        textView = (TextView) findViewById(R.id.address);
        textView.setText(address);

        website = getIntent().getStringExtra("website");
        if (website != null) {
            webView = (TextView) findViewById(R.id.website);
            webView.setText("Website");
            websiteView = (TextView) findViewById(R.id.websiteView);
            websiteView.setText(website);
        }
        phone = getIntent().getStringExtra("phone");
        if (phone != null) {
            cellView = (TextView) findViewById(R.id.phone);
            cellView.setText("Phone");
            phoneView = (TextView) findViewById(R.id.phoneView);
            phoneView.setText(phone);
        }

        for (int i = 0; i < hours.size(); i++) {
            hoursView = (TextView) findViewById(R.id.hoursView);
            hoursView.setText(hours.get(i));
        }

        for (int i = 0; i < tweets.size(); i++) {
            linearLayout = (LinearLayout) findViewById(R.id.tweets);
            tweetsView = (TextView) findViewById(R.id.tweetsView);
            tweetsView.setText(tweets.get(i));
            linearLayout.removeView(tweetsView);
            linearLayout.addView(tweetsView);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address = "http://maps.google.com/maps?saddr=" + mLatitudeText + "," + mLongitudeText + "&daddr=" + address;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
                startActivity(intent);
            }
        });

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
            System.out.println("Permission Denied");
            mLatitudeText = "47.6205 N";
            mLongitudeText = "122.3493 W";
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
            //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void phoneCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("phone"));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            System.out.println("Permission Denied for phone calls");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
            return;
        } else {
            System.out.println("Permission granted for phone calls");
        }
        startActivity(callIntent);
    }

    public void websiteCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("website"));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            System.out.println("Permission Denied internet access");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.INTERNET}, 1);
            return;
        } else {
            System.out.println("Permission granted for internet access");
        }
        startActivity(callIntent);
    }
}
