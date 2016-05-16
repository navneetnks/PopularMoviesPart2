package com.sqldexter.popularmovies;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by navneet on 10/5/16.
 */
public class TrailerAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    private static final String LOG_TAG=TrailerAdapter.class.getSimpleName();
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
//            viewHolder.tralerImage.setBackgroundColor(Color.BLACK);
            viewHolder.tralerImage.setImageResource(R.drawable.play_button);
            viewHolder.trailerText.setText("Trailer "+position);
            try {
                viewHolder.id = item.getString("key");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }



    static class ViewHolder{
        ImageView tralerImage;
        TextView trailerText;
        String id;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ViewHolder viewHolder=(ViewHolder)view.getTag();
//        Toast.makeText(context,"clicked on item that has id="+viewHolder.id,Toast.LENGTH_LONG).show();
        watchYoutubeVideo(viewHolder.id);
    }
    private void watchYoutubeVideo(String id){
        String url="http://www.youtube.com/watch?v="+id;
        Log.d(LOG_TAG,"yrl="+url);
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            context.startActivity(intent);
        }
    }
}
