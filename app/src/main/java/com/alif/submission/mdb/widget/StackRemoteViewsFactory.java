package com.alif.submission.mdb.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.alif.submission.mdb.R;
import com.alif.submission.mdb.database.DatabaseFavorite;
import com.alif.submission.mdb.ui.movie.item.MovieItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    DatabaseFavorite databaseFavorite;
    private  ArrayList<MovieItem> movieItems = new ArrayList<>();
    private  ArrayList<String> posters = new ArrayList<>();
    private final Context context;

    public StackRemoteViewsFactory(Context context){
        this.context = context;

    }

    @Override
    public void onCreate() {
        databaseFavorite = DatabaseFavorite.getInstance(context);
    }

    @Override
    public void onDataSetChanged() {
        ArrayList<String> post_show = new ArrayList<>();
        for (int i = 0; i < databaseFavorite.getAllFavoriteMovies().size(); i++){
            String poster = databaseFavorite.getAllFavoriteMovies().get(i).getPosterPath();
            posters.add(poster);
        }
        for (int i = 0; i < databaseFavorite.getAllFavoriteShows().size(); i++){
            String poster = databaseFavorite.getAllFavoriteShows().get(i).getPosterPath();
            post_show.add(poster);
        }
        posters.addAll(post_show);
//        movieItems = databaseFavorite.getAllFavoriteMovies();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return posters.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_items);
        try {
            Bitmap bitmap = Glide.with(context)
                    .asBitmap()
                    .load(posters.get(position))
                    .apply(new RequestOptions())
                    .submit()
                    .get();

            rv.setImageViewBitmap(R.id.imageView, bitmap);

        }catch (Exception e){
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ImageBannerWidget.EXTRA_ITEM, position);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        rv.setOnClickFillInIntent(R.id.imageView, intent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
