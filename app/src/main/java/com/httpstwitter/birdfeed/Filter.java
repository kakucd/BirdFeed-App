package com.httpstwitter.birdfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;

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
        data = getIntent().getStringArrayListExtra("data");
        tag = getIntent().getStringArrayListExtra("tag");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.mexican:
                if (checked)
                    filters.add("Mexican");
                break;
            case R.id.american:
                if (checked)
                    filters.add("American");
                break;
            case R.id.asian:
                if(checked) {
                    filters.add("Japanese");
                    filters.add("Korean");
                    filters.add("Chinese");
                }
                break;
            case R.id.vegan:
                if(checked) {
                    filters.add("Vegan");
                    filters.add("Vegetarian");
                }
                break;
            case R.id.seafood:
                if(checked)
                    filters.add("Seafood");
                break;
            case R.id.bakery:
                if(checked) {
                    filters.add("Bakery");
                    filters.add("Coffee");
                }
                break;
            case R.id.italian:
                if(checked)
                    filters.add("Italian");
                break;
            case R.id.bar:
                if(checked)
                    filters.add("Bar");
                break;
        }

        for(int i = 0; i < filters.size(); i++) {
            System.out.println("Index: "+i+" Filter: "+filters.get(i));
        }
    }

    public void filterSearch(View view) {
        boolean flag;
        Intent intent = new Intent(this, Search.class);
        for(int t = 0; t < tag.size(); t++) {
            flag = false;
            for(int f = 0; f < filters.size(); f++) {
                if(tag.get(t).contains(filters.get(f))) {
                    flag = true;
                }
            }
            if(!flag) {
                tag.remove(t);
                data.remove(t);
            }
        }

        for(int i = 0; i < data.size(); i++) {
            System.out.println("Name: "+data.get(i)+" Tags: "+tag.get(i));
        }

        intent.putStringArrayListExtra("filters", filters);
        intent.putStringArrayListExtra("data", data);
        intent.putStringArrayListExtra("tag", tag);
        intent.putExtra("item", item);
        startActivity(intent);
    }

}
