package com.sqldexter.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by HOME on 29-03-2016.
 */
public class ConnectNetwork {
    private static final String LOG_TAG=ConnectNetwork.class.getSimpleName();
    private String urlStr;
    private HttpURLConnection urlConnection;
    ConnectNetwork(String urlStr){
        this.urlStr=urlStr;
    }
    public JSONObject getJsonObjFromNetwork() throws IOException, JSONException {
        String response=getUrlResponse();
        return new JSONObject(response);
    }
    private InputStream hitUrl() throws IOException {
        Log.d(LOG_TAG,"Attempting url="+urlStr);
        URL url=new URL(urlStr);
        urlConnection=(HttpURLConnection)url.openConnection();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        Log.d(LOG_TAG,"ResponseCode="+urlConnection.getResponseCode());
        Log.d(LOG_TAG,"ResponseMessage="+urlConnection.getResponseMessage());
//        Log.d(LOG_TAG,"ResponseMessage="+urlConnection.get());
        return in;
    }
    public String getUrlResponse() throws IOException {
        InputStream in=hitUrl();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line=null;
        StringBuilder stringBuilder=new StringBuilder();
        while((line=reader.readLine())!=null){
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
    public void disconnect(){
        urlConnection.disconnect();
    }
}
