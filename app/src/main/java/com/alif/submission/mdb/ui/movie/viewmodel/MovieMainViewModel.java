package com.alif.submission.mdb.ui.movie.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alif.submission.mdb.BuildConfig;
import com.alif.submission.mdb.ui.movie.item.MovieItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieMainViewModel extends ViewModel {

    private static final String API_KEY = BuildConfig.API_KEY;
    private MutableLiveData<ArrayList<MovieItem>> listMovie = new MutableLiveData<>();
    private ArrayList<MovieItem> listItems = new ArrayList<>();

    public void setMovie(){

        final String URL = "https://api.themoviedb.org/3/discover/movie?api_key="
                + API_KEY
                + "&language=en-US";
        final String POSTER_PATH = "https://image.tmdb.org/t/p/",POSTER_SIZE = "original";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++){

                        JSONObject movie = list.getJSONObject(i);
                        MovieItem item = new MovieItem();

//                        item.setId(movie.getInt("id"));
                        item.setId(i);
                        item.setTitle(movie.getString("title"));
                        item.setOverview(movie.getString("overview"));
                        item.setFavorite(false);
                        item.setPosterPath(POSTER_PATH + POSTER_SIZE + movie.getString("poster_path"));
                        listItems.add(item);
                    }
                    listMovie.postValue(listItems);
                }
                catch (Exception e){
                    Log.e("ViewModel Movie Error",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("ViewModel Movie Error",error.getMessage());
            }
        });

    }

    public LiveData<ArrayList<MovieItem>> getMovie(){return listMovie; }
    public ArrayList<MovieItem> getListMovie(){return listItems;}

}
