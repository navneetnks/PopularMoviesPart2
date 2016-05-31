package com.sqldexter.popularmovies.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HOME on 01-06-2016.
 */
public class SharedPref {
    private static final String MOVIE_FILE="movie_file";
    public static void saveMovieId(Context context, String movieId){
        SharedPreferences sharedPreferences=context.getSharedPreferences(MOVIE_FILE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(movieId,movieId);
        editor.commit();
    }
    public static boolean movieSaved(Context context, String movieId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MOVIE_FILE, context.MODE_PRIVATE);
        String movie = sharedPreferences.getString(movieId, null);
        return movie != null;
    }
}
