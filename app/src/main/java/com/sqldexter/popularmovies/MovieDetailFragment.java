package com.sqldexter.popularmovies;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sqldexter.popularmovies.adapter.ReviewAdapter;
import com.sqldexter.popularmovies.adapter.TrailerAdapter;
import com.sqldexter.popularmovies.utility.SharedPref;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by HOME on 04-06-2016.
 */
public class MovieDetailFragment extends Fragment {
    private static final String LOG_TAG=MovieDetailFragment.class.getSimpleName();
    static final String DETAIL_DATA="movieObj";
    private ImageView imageView,imageBg;
    private TextView title,synopsis,rating,releaseDate;
    private JSONObject movieObj;
    private ListView trailerLV,reviewLV;
    private Context context;
    private boolean isFavorite,buttonFavClicked;
    private Button favButton;
    private String movieId=null;
    private String jsonTrailerStr,jsonReviewsStr;
    private static final String TRAILER_DATA="trailer_data",REVIEWS_DATA="review_data";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        imageView=(ImageView)rootView.findViewById(R.id.detail_image);
//        imageBg=(ImageView)findViewById(R.id.detail_bg_image);
//        title=(TextView)findViewById(R.id.title);
        synopsis=(TextView)rootView.findViewById(R.id.synopsis);
        rating=(TextView)rootView.findViewById(R.id.rating);
        releaseDate=(TextView)rootView.findViewById(R.id.release_date);
        trailerLV=(ListView)rootView.findViewById(R.id.trailer_list);
        reviewLV=(ListView)rootView.findViewById(R.id.review_list);
        favButton=(Button)rootView.findViewById(R.id.fav_button);
        title=(TextView)rootView.findViewById(R.id.title);

        Bundle arguments = getArguments();
        String data=null;
        String titleStr=null;
        if (arguments != null) {
            data = arguments.getString(MovieDetailFragment.DETAIL_DATA);
            try {
                movieObj = new JSONObject(data);
                titleStr=movieObj.getString("original_title");
                movieId=movieObj.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(LOG_TAG, "" + movieObj);
            isFavorite= SharedPref.isMovieSaved(getContext(), movieId);
            buttonFavClicked=isFavorite;
        }

        try {
            title.setText(titleStr);
            String date=movieObj.getString("release_date");
            if(date!=null)
                releaseDate.setText(date.split("-")[0]);
//            title.setText(movieObj.getString("original_title"));
            String imageUrl="http://image.tmdb.org/t/p/w185"+movieObj.getString("poster_path");
            rating.setText(movieObj.getString("vote_average") +"/10");
            synopsis.setText(movieObj.getString("overview") );
            Picasso.with(getContext()).load(imageUrl).into(imageView);
//            imageBg.setAlpha(0.15f);
//            Picasso.with(this).load(imageUrl).into(imageBg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        setTitle(title);

        if(isFavorite)
            favButton.setText("Favorite");
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!buttonFavClicked){
                    favButton.setText("Favorite");
                    SharedPref.saveMovieId(getContext(), movieId,movieObj.toString());
                    isFavorite=true;
                    buttonFavClicked=true;
                }
                else{
                    Toast.makeText(getContext(),"Already marked as favorite",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // trailer data restore if saved
        if(savedInstanceState!=null && savedInstanceState.containsKey(TRAILER_DATA)){
            try {
                jsonTrailerStr=savedInstanceState.getString(TRAILER_DATA);
                JSONObject trailerJson=new JSONObject(jsonTrailerStr);
                loadVideosUI(trailerJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
            new TaskThread(Constants.getVideoURL(movieId),1).execute();

        // Review data restore if saved
        if(savedInstanceState!=null && savedInstanceState.containsKey(REVIEWS_DATA)) {
            try {
                jsonReviewsStr=savedInstanceState.getString(REVIEWS_DATA);
                JSONObject reviewJson=new JSONObject(jsonReviewsStr);
                loadReviewsUI(reviewJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
            new TaskThread(Constants.getReviewsURL(movieId),2).execute();

        return rootView;
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

            if(callType==1) {
                loadVideosUI(jsonObject);
                jsonTrailerStr=jsonObject.toString();
            }
            else {
                loadReviewsUI(jsonObject);
                jsonReviewsStr=jsonObject.toString();
            }
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
        TrailerAdapter trailerAdapter=new TrailerAdapter(data,getActivity());
        trailerLV.setAdapter(trailerAdapter);
        HelperUtility.setListViewHeightBasedOnChildren(trailerLV);
        trailerLV.setOnItemClickListener(trailerAdapter);

//        trailerLV.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                return (event.getAction() == MotionEvent.ACTION_MOVE);
//            }
//        });

    }
    private void loadReviewsUI(JSONObject jsonObject){
        JSONArray data= null;
        try {
            data = jsonObject.getJSONArray("results");
            Log.d(LOG_TAG,"TrailerData="+data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        reviewLV.setAdapter(new ReviewAdapter(data,getActivity()));
        HelperUtility.setListViewHeightBasedOnChildren(reviewLV);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(TRAILER_DATA,jsonTrailerStr);
        outState.putString(REVIEWS_DATA,jsonReviewsStr);

    }
}
