package com.sqldexter.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sqldexter.popularmovies.fragment.MovieDetailFragment;
import com.sqldexter.popularmovies.R;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG=MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
       
        Intent i=getIntent();
        String title=null;
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putString(MovieDetailFragment.DETAIL_DATA, i.getStringExtra("movieObj"));

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       

    }

    

}
