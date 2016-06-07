package com.sqldexter.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sqldexter.popularmovies.fragment.MovieDetailFragment;
import com.sqldexter.popularmovies.fragment.MovieFragment;
import com.sqldexter.popularmovies.R;

public class MainActivity extends AppCompatActivity implements MovieFragment.Callback {
    private static final String TAG=MainActivity.class.getSimpleName();
    private boolean mTwoPane;
    private static final String DETAIL_FRAG_TAG="detail";
    private Bundle savedInstanceState;
    public static final String MOVIE_OBJECT_KEY="movieObj";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            Log.d(TAG,"movie_detail_container is found");
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            this.savedInstanceState=savedInstanceState;

        } else {
            Log.d(TAG,"movie_detail_container is NOT found");
            mTwoPane = false;
//            getSupportActionBar().setElevation(0f);
        }
        MovieFragment forecastFragment =  ((MovieFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movie));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(String data) {
        if(mTwoPane) {

            Bundle args = new Bundle();
            args.putString(MOVIE_OBJECT_KEY,data);
            Fragment fragment=new MovieDetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAIL_FRAG_TAG)
                    .commit();

        }
        else{
            Intent intent=new Intent(this,MovieDetailActivity.class);
            intent.putExtra(MOVIE_OBJECT_KEY, data);
            startActivity(intent);
        }
    }
    @Override
    public void onInitialization(String data){
        if(mTwoPane) {
            if (savedInstanceState == null) {
                Bundle args = new Bundle();
                args.putString(MOVIE_OBJECT_KEY,data);
                Fragment fragment=new MovieDetailFragment();
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment, DETAIL_FRAG_TAG)
                        .commit();
            }
        }
    }
}
