package com.sqldexter.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HOME on 31-03-2016.
 */
public class ImageAdapter extends BaseAdapter {
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
        ImageView imageView;
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
            posterPath=item.get("poster_path").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String imageUrl="http://image.tmdb.org/t/p/w185"+posterPath;
        Picasso.with(context).load(imageUrl).into(imageView);
        return imageView;

    }
}
