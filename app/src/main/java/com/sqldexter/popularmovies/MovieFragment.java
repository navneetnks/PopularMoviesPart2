package com.sqldexter.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.sqldexter.popularmovies.adapter.ImageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by HOME on 04-06-2016.
 */
public class MovieFragment extends Fragment implements AdapterView.OnItemClickListener{
    private GridView movieGrid;
    private ImageAdapter imageAdapter;
    private static final String LOG_TAG=MovieFragment.class.getSimpleName();
    private Spinner sortBySpinner;
    private ProgressBar progressBar;
    private volatile JSONObject jsonObject;

    private int spinnerPosition;
    private boolean isSpinnerTouched=false;
    private static final String MOVIE_DATA="mov_data";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        movieGrid=(GridView)rootView.findViewById(R.id.movieGrid);
        movieGrid.setOnItemClickListener(this);
        sortBySpinner=(Spinner)rootView.findViewById(R.id.sortBy);
        progressBar=(ProgressBar)rootView.findViewById(R.id.apiProgressBar);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(),R.array.sort_by_array,
                android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter);
        // restoring status if it is saved else fetching data using API
        if(savedInstanceState!=null && savedInstanceState.containsKey(MOVIE_DATA))
        {
            try {
                jsonObject=new JSONObject(savedInstanceState.getString(MOVIE_DATA));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            displayData();
            Log.d(LOG_TAG, "Instance restored");
        }
        else {
            new TaskThread(true).execute();
            Log.d(LOG_TAG, "API called");
        }
        sortBySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouched = true;
                return false;
            }
        });
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                Log.d(LOG_TAG, "selectedOption=" + selectedOption);
                spinnerPosition = position;

                if (isSpinnerTouched) {
                    if (getString(R.string.popular).equals(selectedOption)) {
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        movieGrid.setVisibility(View.INVISIBLE);
                        new TaskThread(true).execute();
                    } else {
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        movieGrid.setVisibility(View.INVISIBLE);
                        new TaskThread(false).execute();
                    }
                    Toast.makeText(getActivity(), "selectedOption=" + selectedOption, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ((Callback)getActivity()).onItemSelected(view.getTag().toString());

//        Log.d(LOG_TAG, "clicked on item="+position);
    }
    private class TaskThread extends AsyncTask<Void,Void,Void> {

        private boolean isPopular;
        public TaskThread(boolean isPopoular){
            this.isPopular=isPopoular;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            displayData();


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
                Log.d(LOG_TAG, "jsonObject=" + jsonObject.toString());
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
    private void displayData(){
        try {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            movieGrid.setVisibility(View.VISIBLE);
            if(jsonObject!=null) {
                imageAdapter = new ImageAdapter(getActivity(), jsonObject.getJSONArray("results"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        movieGrid.setAdapter(imageAdapter);
        String data=null;
        if(jsonObject!=null) {
            try {
                data = jsonObject.getJSONArray("results").getString(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(data!=null)
            ((Callback)getActivity()).onInitialization(data);
    }
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String data);
        public void onInitialization(String data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(MOVIE_DATA,jsonObject.toString());
    }
}
