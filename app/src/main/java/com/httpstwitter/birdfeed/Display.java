package com.httpstwitter.birdfeed;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import java.io.InputStream;
import java.util.ArrayList;

public class Display extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    static private ArrayList<String> master = new ArrayList<>();
    private String item, address, tags, phone, website, image;
    private GoogleApiClient mGoogleApiClient;
    private int i = 0;
    private String mLatitudeText, mLongitudeText;
    private FloatingActionButton fab, call, web, tweet;
    private ImageView imageView;
    private FirebaseDatabase mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        master.clear();
        mdatabase = FirebaseDatabase.getInstance();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(item);

        item = getIntent().getStringExtra("item");
        image = getIntent().getStringExtra("image");

        imageView = (ImageView)findViewById(R.id.imageView);

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
            website = "http://www." +website;
            master.add("Website");
            master.add(website);
        }
        phone = getIntent().getStringExtra("phone");
        if (phone != null) {
            master.add("Phone");
            System.out.println("PHONE: "+phone);
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

        imageView = (ImageView)findViewById(R.id.imageView);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 2);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 3);
            return;
        }

        new ImageDownloader(imageView).execute(image);

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
                if(website != null) {
                //website = "http://www." + website;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                startActivity(intent);
                } else {
                    Snackbar.make(view, "Restaurant does not have a website", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
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

        tweet = (FloatingActionButton) findViewById(R.id.tweet);
        tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                        .getActiveSession();
                final Intent intent = new ComposerActivity.Builder(Display.this)
                        .session(session)
                        .hashtags("#birdfeed")
                        .createIntent();
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
                i++;
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

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon = null;
            System.out.println("URL: "+url);
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                System.out.println("error caught: "+e.toString());
                Log.e("Error", e.getMessage());
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void back(View view) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}
