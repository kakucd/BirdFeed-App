package com.httpstwitter.birdfeed;

/**
 * Created by Emily on 4/17/17.
 */

import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TimelineActivity extends ListActivity {

    private String handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        handle = getIntent().getStringExtra("handle");

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(handle)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();
        setListAdapter(adapter);
    }

    public void back(View view) {
        Intent intent = new Intent(this, Display.class);
        startActivity(intent);
    }
}
