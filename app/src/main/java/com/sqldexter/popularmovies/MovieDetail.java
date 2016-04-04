package com.sqldexter.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent i=getIntent();
        JSONObject movieObj= null;
        try {
            movieObj = new JSONObject(i.getStringExtra("movieObj"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,movieObj.toString(),Toast.LENGTH_LONG).show();
    }

}
