package com.alif.submission.mdb.ui.favorite.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alif.submission.mdb.OnActionListener;
import com.alif.submission.mdb.R;
import com.alif.submission.mdb.database.DatabaseFavorite;
import com.alif.submission.mdb.ui.movie.adapter.MovieListAdapter;
import com.alif.submission.mdb.ui.movie.detail.activity.MovieDetailActivity;
import com.alif.submission.mdb.ui.movie.item.MovieItem;

import java.util.ArrayList;

public class MovieFavoriteFragment extends Fragment implements OnActionListener {

    private ArrayList<MovieItem> movieItems;
    private MovieListAdapter movieListAdapter = new MovieListAdapter();

    public MovieFavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        movieItems = DatabaseFavorite.getInstance(getContext()).getAllFavoriteMovies();
        movieListAdapter.setData(getContext(), movieItems, this);
        RecyclerView rv = view.findViewById(R.id.rv_movie_favorite);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(movieListAdapter);
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void StartActivity(int position) {
        MovieItem movie = movieItems.get(position);
        Log.d("MDB_DEBUG"," Movie Sent is " + movie.getTitle() );
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_EXTRA, movie);
        startActivity(intent);
    }

    @Override
    public void OnDeleteFromFavorite(int position) {
        movieListAdapter.deleteFavoriteItem(position);
    }
}
