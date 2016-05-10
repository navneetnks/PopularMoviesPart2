package com.sqldexter.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by navneet on 10/5/16.
 */
public class TrailerAdapter extends BaseAdapter {
    private JSONArray data;
    private Context context;

    public TrailerAdapter(JSONArray data, Context context) {
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
        JSONObject item=null;
        if(view==null){

            view=((Activity)context).getLayoutInflater().inflate(R.layout.trailer_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tralerImage=(ImageView) view.findViewById(R.id.trailer_image);
            viewHolder.trailerText=(TextView) view.findViewById(R.id.trailer_text);
            view.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder) view.getTag();
        }
        item=(JSONObject) getItem(position);
        if(item!=null){
            viewHolder.tralerImage.setBackgroundColor(Color.BLACK);
            viewHolder.trailerText.setText("Trailer "+position);
        }
        return view;
    }
    class ViewHolder{
        ImageView tralerImage;
        TextView trailerText;
    }
}
