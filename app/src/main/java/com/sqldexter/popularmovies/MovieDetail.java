package com.sqldexter.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetail extends AppCompatActivity {
    private static final String TAG=MovieDetail.class.getSimpleName();
    private ImageView imageView,imageBg;
    private TextView title,synopsis,rating,releaseDate;
    private JSONObject movieObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        imageView=(ImageView)findViewById(R.id.detail_image);
        imageBg=(ImageView)findViewById(R.id.detail_bg_image);
        title=(TextView)findViewById(R.id.title);
        synopsis=(TextView)findViewById(R.id.synopsis);
        rating=(TextView)findViewById(R.id.rating);
        releaseDate=(TextView)findViewById(R.id.release_date);

        Intent i=getIntent();

        try {
            movieObj = new JSONObject(i.getStringExtra("movieObj"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, ""+movieObj);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            releaseDate.setText(movieObj.getString("release_date"));
            title.setText(movieObj.getString("original_title"));
            String imageUrl="http://image.tmdb.org/t/p/w185"+movieObj.getString("poster_path");
            rating.setText(movieObj.getString("vote_average"));
            synopsis.setText(movieObj.getString("overview") );
            Picasso.with(this).load(imageUrl).into(imageView);
            imageBg.setAlpha(0.15f);
            Picasso.with(this).load(imageUrl).into(imageBg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
