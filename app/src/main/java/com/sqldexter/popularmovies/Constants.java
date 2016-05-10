package com.sqldexter.popularmovies;

/**
 * Created by HOME on 31-03-2016.
 */
public class Constants {
    public static final String BASE_URL="http://api.themoviedb.org/3/movie/";
    public static final String POPULAR_MOVIE_URL=
            "http://api.themoviedb.org/3/movie/popular?api_key="+BuildConfig.THE_MOVIE_DB_API_KEY;
    public static final String TOP_RATED_MOVIE_URL=
            "http://api.themoviedb.org/3/movie/top_rated?api_key="+BuildConfig.THE_MOVIE_DB_API_KEY;

    public static String getVideoURL(String movieId){
        String path=BASE_URL+movieId+"/videos?api_key="+BuildConfig.THE_MOVIE_DB_API_KEY;
        return path;
    }
    public static String getReviewsURL(String movieId){
        String path=BASE_URL+movieId+"/reviews?api_key="+BuildConfig.THE_MOVIE_DB_API_KEY;
        return path;
    }


}
