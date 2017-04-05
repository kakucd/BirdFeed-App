package com.httpstwitter.birdfeed;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Filter extends AppCompatActivity {

    private ArrayList<String> filters = new ArrayList<>();
    static private ArrayList<String> data = new ArrayList<>();
    static private ArrayList<String> tag = new ArrayList<>();
    private String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        item = getIntent().getStringExtra("item");
        new getData().execute();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.mexican:
                if (checked) {
                    filters.add("Mexican");
                    filters.add("Latin");
                }
                break;
            case R.id.american:
                if (checked) {
                    filters.add("American");
                    filters.add("Burgers");
                }
                break;
            case R.id.asian:
                if(checked) {
                    filters.add("Japanese");
                    filters.add("Korean");
                    filters.add("Chinese");
                    filters.add("Sushi");
                    filters.add("Vietnamese");
                }
                break;
            case R.id.vegan:
                if(checked) {
                    filters.add("Vegan");
                    filters.add("Vegetarian");
                }
                break;
            case R.id.seafood:
                if(checked) {
                    filters.add("Seafood");
                    filters.add("Sushi");
                }
                break;
            case R.id.bakery:
                if(checked) {
                    filters.add("Bakery");
                    filters.add("Coffee");
                    filters.add("Pastries");
                    filters.add("Dessert");
                }
                break;
            case R.id.european:
                if(checked) {
                    filters.add("Italian");
                    filters.add("Pasta");
                    filters.add("French");
                    filters.add("German");
                }
                break;
            case R.id.bar:
                if(checked) {
                    filters.add("Bar");
                    filters.add("cocktails");
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        data.clear();
        tag.clear();
    }

    public void filterSearch(View view) {
        Intent intent = new Intent(this, Search.class);
        for(int t = 0; t < tag.size(); t++) {
            System.out.println("Before Search: Name: "+data.get(t)+" Tags: "+tag.get(t)+" "+t);
            boolean flag = false;
            for(int f = 0; f < filters.size(); f++) {
                if((tag.get(t)).contains(filters.get(f))) {
                    flag = true;
                }
                else if(flag) {
                    //do nothing
                }
                else { flag = false; }
                System.out.println("Flag: "+flag+" Filter: "+filters.get(f) +" Name: "+data.get(t)+" Tags: "+tag.get(t));
            }
            if(!flag) {
                tag.remove(t);
                data.remove(t);
                t--;
            }
        }

        intent.putStringArrayListExtra("filters", filters);
        intent.putStringArrayListExtra("data", data);
        intent.putStringArrayListExtra("tag", tag);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    public void menu(View view) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    private class getData extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... voids) {

            FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = mdatabase.getReference("/restaurants");

            myRef.orderByKey().addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Place place = dataSnapshot.getValue(Place.class);
                    data.add(place.getName());
                    tag.add(place.getTags());
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
                    tag.remove(i);
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
