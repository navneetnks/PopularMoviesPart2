package com.sqldexter.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sqldexter.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by navneet on 16/5/16.
 */
public class ReviewAdapter extends BaseAdapter {
    private static final String LOG_TAG=ReviewAdapter.class.getSimpleName();
    private JSONArray data;
    private Context context;

    public ReviewAdapter(JSONArray data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int i) {
        JSONObject item= null;
        try {
            item = data.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            view=((Activity)context).getLayoutInflater().inflate(R.layout.review_item,null);
            viewHolder=new ViewHolder();
            viewHolder.author=(TextView)view.findViewById(R.id.author);
            viewHolder.content=(TextView)view.findViewById(R.id.content);
            view.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder) view.getTag();
        }
        JSONObject item=null;
        item=(JSONObject) getItem(position);
        if(item!=null){
            try {
                viewHolder.author.setText(item.getString("author"));
                viewHolder.content.setText(item.getString("content"));
                viewHolder.id = item.getString("id");
            }
            catch (JSONException e){
                e.printStackTrace();
                Log.d(LOG_TAG,e.getMessage());
            }
        }
        return view;
    }

    static class ViewHolder{
        TextView author,content;
        String id;
    }
}
