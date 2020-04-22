package com.alif.submission.mdb.ui.show.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alif.submission.mdb.BuildConfig;
import com.alif.submission.mdb.ui.show.item.ShowItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ShowMainViewModel extends ViewModel {

    private static final String API_KEY = BuildConfig.API_KEY;
    private MutableLiveData<ArrayList<ShowItem>> listShow = new MutableLiveData<>();
    private ArrayList<ShowItem> listItems = new ArrayList<>();

    public void setShow(){

        final String URL = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=en-US";
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

                        JSONObject show = list.getJSONObject(i);
                        ShowItem item = new ShowItem();

//                        item.setId(show.getInt("id"));
                        item.setId(i);
                        item.setTitle(show.getString("name"));
                        item.setOverview(show.getString("overview"));
                        item.setFavorite(false);
                        item.setPosterPath(POSTER_PATH + POSTER_SIZE + show.getString("poster_path"));
                        listItems.add(item);
                    }
                    listShow.postValue(listItems);
                }
                catch (Exception e){
                    Log.e("ViewModel Show Error", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("ViewModel Show Error", error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<ShowItem>> getShow(){return listShow;}
    public ArrayList<ShowItem> getListShow(){return listItems;}
}
