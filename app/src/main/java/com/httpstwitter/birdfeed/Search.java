package com.httpstwitter.birdfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    private ListView listView;
    private String item;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //instantiate listView to by used for display
        listView = (ListView) findViewById(R.id.query);
        //get queried data from menu activity
        data = getIntent().getStringArrayListExtra("data");
        //instantiate adapter to translate ArrayList data to ListView to be displayed on phone screen
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, data);

        listView.setAdapter(adapter);
        System.out.println("ArrayAdapter instantiated");
        //When clicked, a list item will send item name to info activity/class
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("onItemClicked");
                int itemPosition = position;
                item =  (String) listView.getItemAtPosition(position);
                //Toast message will pop up indicating list item and position clicked.
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
