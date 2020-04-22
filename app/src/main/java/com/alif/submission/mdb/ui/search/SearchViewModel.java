package com.alif.submission.mdb.ui.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alif.submission.mdb.ui.movie.item.MovieItem;
import com.alif.submission.mdb.ui.show.item.ShowItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.alif.submission.mdb.BuildConfig.API_KEY;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<ArrayList<MovieItem>> movies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ShowItem>> tvShows = new MutableLiveData<>();

    public LiveData<ArrayList<MovieItem>> getMovies() {
        return movies;
    }

    public MovieItem getMovie(int index) {
        return movies.getValue() == null ? null : movies.getValue().get(index);
    }

    public LiveData<ArrayList<ShowItem>> getTvShows() {
        return tvShows;
    }

    public ShowItem getTvShow(int index) {
        return tvShows.getValue() == null ? null : tvShows.getValue().get(index);
    }

     void searchMovie(String query) {
        final String URL = "https://api.themoviedb.org/3/search/movie?api_key="
                + API_KEY
                + "&language=en-US&query="
                + query;
        final String POSTER_PATH = "https://image.tmdb.org/t/p/", POSTER_SIZE = "original";

        (new AsyncHttpClient()).get(URL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    ArrayList<MovieItem> movieItems = new ArrayList<>();
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        MovieItem item = new MovieItem();
                        item.setId(movie.getInt("id"));
                        item.setTitle(movie.getString("title"));
                        item.setOverview(movie.getString("overview"));
                        item.setFavorite(false);
                        item.setPosterPath(POSTER_PATH + POSTER_SIZE + movie.getString("poster_path"));
                        movieItems.add(item);
                    }
                    movies.postValue(movieItems);
                } catch (Exception e) {
                    Log.e("ViewModel Movie Error", e.getMessage() == null ? "" : e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                Log.e("ViewModel Movie Error", e.getMessage() == null ? "" : e.getMessage());
            }
        });
    }

     void searchTvShows(String query) {
        final String URL = "https://api.themoviedb.org/3/search/tv?api_key="
                + API_KEY
                + "&language=en-US&query="
                + query;
        final String POSTER_PATH = "https://image.tmdb.org/t/p/", POSTER_SIZE = "original";

        (new AsyncHttpClient()).get(URL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    ArrayList<ShowItem> showItems = new ArrayList<>();
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tv = list.getJSONObject(i);
                        ShowItem item = new ShowItem();
                        item.setId(tv.getInt("id"));
                        item.setTitle(tv.getString("name"));
                        item.setOverview(tv.getString("overview"));
                        item.setFavorite(false);
                        item.setPosterPath(POSTER_PATH + POSTER_SIZE + tv.getString("poster_path"));
                        showItems.add(item);
                    }
                    tvShows.postValue(showItems);
                } catch (Exception e) {
                    Log.e("ViewModel Movie Error", e.getMessage() == null ? "" : e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                Log.e("ViewModel Movie Error", e.getMessage() == null ? "" : e.getMessage());
            }
        });
    }

}

