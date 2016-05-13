package com.sqldexter.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MovieDetail extends AppCompatActivity {
    private static final String LOG_TAG=MovieDetail.class.getSimpleName();
    private ImageView imageView,imageBg;
    private TextView title,synopsis,rating,releaseDate;
    private JSONObject movieObj;
    private ListView trailerLV,reviewLV;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        imageView=(ImageView)findViewById(R.id.detail_image);
//        imageBg=(ImageView)findViewById(R.id.detail_bg_image);
//        title=(TextView)findViewById(R.id.title);
        synopsis=(TextView)findViewById(R.id.synopsis);
        rating=(TextView)findViewById(R.id.rating);
        releaseDate=(TextView)findViewById(R.id.release_date);
        trailerLV=(ListView)findViewById(R.id.trailer_list);
        reviewLV=(ListView)findViewById(R.id.review_list);
        Intent i=getIntent();
        String title=null;
        String movieId=null;
        context=this;
        try {
            movieObj = new JSONObject(i.getStringExtra("movieObj"));
            title=movieObj.getString("original_title");
            movieId=movieObj.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTitle(title);
        Log.d(LOG_TAG, ""+movieObj);
        new TaskThread(Constants.getVideoURL(movieId),1).execute();
        new TaskThread(Constants.getReviewsURL(movieId),2).execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            releaseDate.setText(movieObj.getString("release_date"));
//            title.setText(movieObj.getString("original_title"));
            String imageUrl="http://image.tmdb.org/t/p/w185"+movieObj.getString("poster_path");
            rating.setText(movieObj.getString("vote_average") +"/10");
            synopsis.setText(movieObj.getString("overview") );
            Picasso.with(this).load(imageUrl).into(imageView);
//            imageBg.setAlpha(0.15f);
//            Picasso.with(this).load(imageUrl).into(imageBg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class TaskThread extends AsyncTask<Void,Void,Void> {
        private String url;
        private int callType=0;
        private JSONObject jsonObject;
        public TaskThread(String url,int callType){
            this.url=url;
            this.callType=callType;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            if(callType==1)
                loadVideosUI(jsonObject);
            else
                loadReviewsUI(jsonObject);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ConnectNetwork connectNetwork=null;

            connectNetwork=new ConnectNetwork(url);

            try {
                jsonObject=connectNetwork.getJsonObjFromNetwork();
                Log.d(LOG_TAG,"jsonObject="+jsonObject.toString());
                Log.d(LOG_TAG,"url="+url);
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

    private void loadVideosUI(JSONObject jsonObject){
        JSONArray data= null;
        try {
            data = jsonObject.getJSONArray("results");
            Log.d(LOG_TAG,"TrailerData="+data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TrailerAdapter trailerAdapter=new TrailerAdapter(data,context);
        trailerLV.setAdapter(trailerAdapter);
        HelperUtility.setListViewHeightBasedOnChildren(trailerLV);

        reviewLV.setAdapter(new TrailerAdapter(data,context));
        HelperUtility.setListViewHeightBasedOnChildren(reviewLV);
//        trailerLV.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                return (event.getAction() == MotionEvent.ACTION_MOVE);
//            }
//        });

    }
    private void loadReviewsUI(JSONObject jsonObject){

    }
}
