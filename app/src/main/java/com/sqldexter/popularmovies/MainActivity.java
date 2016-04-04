package com.sqldexter.popularmovies;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private GridView movieGrid;
    private ImageAdapter imageAdapter;
    private Activity activity;
    private static final String LOG_TAG=MainActivity.class.getSimpleName();
    private Spinner sortBySpinner;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        movieGrid=(GridView)findViewById(R.id.movieGrid);
        movieGrid.setOnItemClickListener(this);
        sortBySpinner=(Spinner)findViewById(R.id.sortBy);
        progressBar=(ProgressBar)findViewById(R.id.apiProgressBar);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.sort_by_array,
                android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (getString(R.string.popular).equals(selectedOption)) {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    movieGrid.setVisibility(View.INVISIBLE);
                    new TaskThread(true).execute();
                }
                else{
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    movieGrid.setVisibility(View.INVISIBLE);
                    new TaskThread(false).execute();
                }

                Toast.makeText(activity, "selectedOption=" + selectedOption, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new TaskThread(true).execute();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,MovieDetail.class);
        intent.putExtra("movieObj",view.getTag().toString());
        startActivity(intent);
//        Log.d(LOG_TAG, "clicked on item="+position);
    }

    private class TaskThread extends AsyncTask<Void,Void,Void>{
        private JSONObject jsonObject=null;
        private boolean isPopular;
        public TaskThread(boolean isPopoular){
            this.isPopular=isPopoular;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                movieGrid.setVisibility(View.VISIBLE);
                imageAdapter=new ImageAdapter(activity,jsonObject.getJSONArray("results"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            movieGrid.setAdapter(imageAdapter);

        }

        @Override
        protected Void doInBackground(Void... params) {
            ConnectNetwork connectNetwork=null;
            if(isPopular)
                connectNetwork=new ConnectNetwork(Constants.POPULAR_MOVIE_URL);
            else
                connectNetwork=new ConnectNetwork(Constants.TOP_RATED_MOVIE_URL);
            try {
                jsonObject=connectNetwork.getJsonObjFromNetwork();
                Log.d(LOG_TAG,"jsonObject="+jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                connectNetwork.disconnect();
            }
            return null;
        }
    }
}
