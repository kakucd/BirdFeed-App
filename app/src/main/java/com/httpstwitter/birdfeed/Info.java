package com.httpstwitter.birdfeed;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import android.widget.LinearLayout;

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
    TextView textView, nameView, tagsView, hoursView;
    String item, address, tags;

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

        for(int i = 0; i < hours.size(); i++) {
            System.out.println(hours.get(i));
        }

        //System.out.println("Name: "+item);
        nameView = (TextView)findViewById(R.id.viewName);
        nameView.setText(item);

        tagsView = (TextView)findViewById(R.id.tagsView);
        tagsView.setText(tags);

        textView = (TextView)findViewById(R.id.address);
        textView.setText(address);

        LayoutInflater linf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linf = LayoutInflater.from(this);

        LinearLayout tbl_layout = (LinearLayout)findViewById(R.id.hours);

        for (int i = 0; i < hours.size(); i++) {
            //View v = linf.inflate(R.layout.hours, null);//Pass your lineraLayout
            //((TextView) v.
            hoursView = (TextView)findViewById(R.id.hoursView);
            hoursView.setText(hours.get(i));
            //tbl_layout.addView(hoursView);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //hours.clear();
    }
}
