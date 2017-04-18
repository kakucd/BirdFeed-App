package com.httpstwitter.birdfeed;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    private String item, address, tags, website, phone, image, handle;
    private FirebaseDatabase mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

            address = null;
            tags = null;
            handle = null;
            hours.clear();
            tweets.clear();
            data.clear();
            hours.clear();

        //get queried data from menu activity
        data = getIntent().getStringArrayListExtra("data");
        tag = getIntent().getStringArrayListExtra("tag");
        item = getIntent().getStringExtra("item");
        //instantiate listView to by used for display
        listView = (ListView) findViewById(R.id.query);
        //instantiate adapter to translate ArrayList data to ListView to be displayed on phone screen
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, data) {
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

                mdatabase = FirebaseDatabase.getInstance();
                item =  (String) listView.getItemAtPosition(position);
                new getPhone().execute();
                new getAddress().execute();
                new getWebsite().execute();
                new getTags().execute();
                new getHours().execute();
                new getHandle().execute();
                new getTweets().execute();
                new getURL().execute();
                //Toast message will pop up indicating list item and position clicked.
                /*Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +item , Toast.LENGTH_LONG)
                        .show();*/
                if(address != null) {
                    info();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        address = null;
        hours.clear();
        tweets.clear();
    }

    public void info() {
        Intent intent = new Intent(this, Display.class);

        intent.putStringArrayListExtra("hours", hours);
        intent.putStringArrayListExtra("tweets", tweets);
        intent.putExtra("tags", tags);
        intent.putExtra("item", item);
        intent.putExtra("address", address);
        intent.putExtra("phone", phone);
        intent.putExtra("website", website);
        intent.putExtra("image", image);
        intent.putExtra("handle", handle);

        startActivity(intent);

    }

    private class getPhone extends AsyncTask<Void, Void, ValueEventListener> {
        @Override
        protected ValueEventListener doInBackground(Void... voids) {
            ValueEventListener number = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    phone = (String) dataSnapshot.getValue();
                    //System.out.println("(search)PHONE: "+phone);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            return number;
        }

        @Override
        protected void onPostExecute(ValueEventListener child) {
            DatabaseReference call = mdatabase.getReference("restaurants/"+item+"/phone");
            call.addListenerForSingleValueEvent(child);
        }
    }

    private class getAddress extends AsyncTask<Void, Void, ValueEventListener> {

        @Override
        protected ValueEventListener doInBackground(Void... voids) {
            ValueEventListener addr = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    address = (String) dataSnapshot.getValue();
                    //System.out.println(dataSnapshot.getKey()+": "+address);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            return addr;
        }

        @Override
        protected void onPostExecute(ValueEventListener addr) {
            DatabaseReference street = mdatabase.getReference("/restaurants/"+item+"/address");
            street.addListenerForSingleValueEvent(addr);
        }
    }

    private class getWebsite extends AsyncTask<Void, Void, ValueEventListener> {

        @Override
        protected ValueEventListener doInBackground(Void... voids) {
            ValueEventListener link = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    website = (String) dataSnapshot.getValue();
                    //System.out.println("Website: "+website);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            return link;
        }

        @Override
        protected void onPostExecute(ValueEventListener link) {
            DatabaseReference web = mdatabase.getReference("/restaurants/"+item+"/website");
            web.addListenerForSingleValueEvent(link);
        }
    }

    private class getHandle extends AsyncTask<Void, Void, ValueEventListener> {

        @Override
        protected ValueEventListener doInBackground(Void... voids) {
            ValueEventListener hand = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    handle = (String) dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            return hand;
        }

        @Override
        protected void onPostExecute(ValueEventListener jpg) {
            DatabaseReference picture = mdatabase.getReference("restaurants/"+item+"/handle");
            picture.addListenerForSingleValueEvent(jpg);
        }
    }

    private class getTags extends AsyncTask<Void, Void, ValueEventListener> {

        @Override
        protected ValueEventListener doInBackground(Void... voids) {
            ValueEventListener text = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    tags = (String) dataSnapshot.getValue();
                    //System.out.println("Tags: "+tags);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            return text;
        }

        @Override
        protected void onPostExecute(ValueEventListener text) {
            DatabaseReference temp = mdatabase.getReference("/restaurants/"+item+"/tags");
            temp.addListenerForSingleValueEvent(text);
        }
    }

    private class getHours extends AsyncTask<Void, Void, ChildEventListener> {
        @Override
        protected ChildEventListener doInBackground(Void... voids) {
            ChildEventListener child = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String counter = dataSnapshot.getKey();
                    String day;

                    switch(counter) {
                        case "1": day = "Monday";
                            break;
                        case "2": day = "Tuesday";
                            break;
                        case "3": day = "Wednesday";
                            break;
                        case "4": day = "Thursday";
                            break;
                        case "5": day = "Friday";
                            break;
                        case "6": day = "Saturday";
                            break;
                        case "7": day = "Sunday";
                            break;
                        default: day = "invalid";
                            break;
                    }

                    System.out.println("Hours: "+dataSnapshot.getKey()+": "+dataSnapshot.getValue());
                    String temp = day+": "+dataSnapshot.getValue();
                    hours.add(temp);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    int i = hours.indexOf(dataSnapshot.getValue());
                    String temp = dataSnapshot.getKey() + ": " + dataSnapshot.getValue();
                    hours.add(i, temp);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    String temp = dataSnapshot.getKey() + ": " + dataSnapshot.getValue();
                    hours.remove(temp);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            return child;
        }

        @Override
        protected void onPostExecute(ChildEventListener child) {
            DatabaseReference times = mdatabase.getReference("/hours/" + item);
            times.addChildEventListener(child);
        }
    }

    private class getTweets extends AsyncTask<Void, Void, ChildEventListener> {

        @Override
        protected ChildEventListener doInBackground(Void... voids) {

            ChildEventListener child = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String t = "@"+dataSnapshot.getKey()+": "+dataSnapshot.getValue();
                    tweets.add(t);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    int i = tweets.indexOf(dataSnapshot.getValue());
                    String t = "@"+dataSnapshot.getKey()+": "+dataSnapshot.getValue();
                    tweets.add(i, t);
                    System.out.println("int i: "+i+" tweets: "+tweets.get(i));
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
            };

            return child;
        }

        @Override
        protected void onPostExecute(ChildEventListener child) {
            DatabaseReference twitter = mdatabase.getReference("/tweets/"+item);
            twitter.addChildEventListener(child);
        }
    }

    private class getURL extends AsyncTask<Void, Void, ValueEventListener> {

        @Override
        protected ValueEventListener doInBackground(Void... voids) {
            ValueEventListener url = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    image = (String) dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            return url;
        }

        @Override
        protected void onPostExecute(ValueEventListener jpg) {
            DatabaseReference picture = mdatabase.getReference("restaurants/"+item+"/picture");
            picture.addListenerForSingleValueEvent(jpg);
        }
    }

    public void filters(View view) {
        Intent intent = new Intent(this, Filter.class);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}
