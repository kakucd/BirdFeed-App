package com.httpstwitter.birdfeed;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;

public class Display extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    static private ArrayList<String> master = new ArrayList<>();
    private String item, address, tags, phone, website, image;
    private GoogleApiClient mGoogleApiClient;
    private int i = 0;
    private String mLatitudeText, mLongitudeText;
    private FloatingActionButton fab, call, web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        master.clear();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(item);

        item = getIntent().getStringExtra("item");
        image = getIntent().getStringExtra("image");
        System.out.println("image: "+image);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageBitmap(getBitmap(image));
        //imageView.setImageDrawable(LoadImage(image));

        tags = getIntent().getStringExtra("tags");
        if (tags != null) {
            master.add(tags);
        }
        master.add("Address");
        address = (String) getIntent().getSerializableExtra("address");
        if (address != null) {
            master.add(address);
        }
        website = getIntent().getStringExtra("website");
        if (website != null) {
            master.add("Website");
            master.add(website);
        }
        phone = getIntent().getStringExtra("phone");
        if (phone != null) {
            master.add("Phone");
            master.add(phone);
        }
        master.add("Hours");
        ArrayList<String> hours = getIntent().getStringArrayListExtra("hours");
        ArrayList<String> tweets = getIntent().getStringArrayListExtra("tweets");

        for (int i = 0; i < hours.size(); i++) {
            master.add(hours.get(i));
        }

        master.add("Tweets");
        for (int i = 0; i < tweets.size(); i++) {
            master.add(tweets.get(i));
        }

        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(item);
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, master);
        listView.setAdapter(adapter);

        // Create an instance of GoogleAPIClient.
        // needed for location services
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 3);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 3);
        }

        call = (FloatingActionButton) findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = "tel:" + phone;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(phone));
                startActivity(intent);
            }
        });

        web = (FloatingActionButton) findViewById(R.id.web);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                website = "http://www." + website;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                //intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address = "http://maps.google.com/maps?saddr=" + mLatitudeText + "," + mLongitudeText + "&daddr=" + address;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //Sets default current location to Space Needle coordinates
                    //if user denies access to location
                    mLatitudeText = "47.6205 N";
                    mLongitudeText = "122.3493 W";
                }
                i++;
                return;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                }
                i++;
                return;
            }
            case 3: {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                }
                ++i;
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public static Drawable LoadImage(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            System.out.println("Here");
            Drawable d = Drawable.createFromStream(is, "src name");
            System.out.println("There");
            return d;
        } catch (Exception e) {
            System.out.println("exception caught");
            return null;
        }
    }

    public Bitmap getBitmap(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
