package com.sqldexter.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.sqldexter.popularmovies.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HOME on 31-03-2016.
 */
public class ImageAdapter extends BaseAdapter {
    private static final String LOG_TAG= ImageAdapter.class.getSimpleName();
    private JSONArray itemArray;
    private Context context;

    public ImageAdapter(Context context,JSONArray itemArray){
        this.itemArray=itemArray;
        this.context=context;
    }
    @Override
    public int getCount() {
        return itemArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return itemArray.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView=(ImageView)((Activity)context).getLayoutInflater().inflate(R.layout.grid_item,null);

        } else {
            imageView = (ImageView) convertView;
        }
        JSONObject item=null;
        String posterPath=null;
        try {
            item=itemArray.getJSONObject(position);
            posterPath=item.getString("poster_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String imageUrl="http://image.tmdb.org/t/p/w185"+posterPath;
        Picasso.with(context).load(imageUrl).placeholder(R.drawable.no_image_available).error(R.drawable.img_not_found).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG,"onSuccess");
            }

            @Override
            public void onError() {
                Log.d(LOG_TAG,"On error");
            }
        });
        imageView.setTag(item);
        return imageView;

    }
}
