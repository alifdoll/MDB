package com.alif.submission.mdb.ui.search;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alif.submission.mdb.OnActionListener;
import com.alif.submission.mdb.R;
import com.alif.submission.mdb.ui.movie.adapter.MovieListAdapter;
import com.alif.submission.mdb.ui.movie.detail.activity.MovieDetailActivity;
import com.alif.submission.mdb.ui.movie.item.MovieItem;
import com.alif.submission.mdb.ui.show.adapter.ShowListAdapter;
import com.alif.submission.mdb.ui.show.detail.activity.ShowDetailActivity;
import com.alif.submission.mdb.ui.show.item.ShowItem;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements OnActionListener {

    public static final int TYPE_MOVIE = 0;
    public static final int TYPE_TV = 1;
    public static final String ARG_TYPE = "type";
    public static final String ARG_QUERY = "query";

    private SearchViewModel searchViewModel;
    private ProgressBar progressBar;
    private int type = TYPE_MOVIE;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("MDB:debug", "SearchFragment onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.fragmentSearch_progressBar);

        final RecyclerView recyclerView = view.findViewById(R.id.rv_movie_search);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        String query = "";
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_TYPE);
            query = getArguments().getString(ARG_QUERY);
        }

        if (type == TYPE_MOVIE) {
            final MovieListAdapter adapterMovie = new MovieListAdapter();
            recyclerView.setAdapter(adapterMovie);
            searchViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SearchViewModel.class);
            searchViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<MovieItem>>() {
                @Override
                public void onChanged(ArrayList<MovieItem> movieItems) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (movieItems == null || movieItems.size() == 0) {
                        Toast.makeText(getContext(), "No result for query", Toast.LENGTH_LONG).show();
                    } else {
                        adapterMovie.setData(getContext(), movieItems, SearchFragment.this);
                        adapterMovie.notifyDataSetChanged();
                    }
                }
            });
        } else {
            final ShowListAdapter showListAdapter = new ShowListAdapter();
            recyclerView.setAdapter(showListAdapter);
            searchViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SearchViewModel.class);
            searchViewModel.getTvShows().observe(getViewLifecycleOwner(), new Observer<ArrayList<ShowItem>>() {
                @Override
                public void onChanged(ArrayList<ShowItem> showItems) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (showItems == null || showItems.size() == 0) {
                        Toast.makeText(getContext(), "No result for query", Toast.LENGTH_LONG).show();
                    } else {
                        showListAdapter.setData(getContext(), showItems, SearchFragment.this);
                        showListAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        progressBar.setVisibility(View.VISIBLE);
        if (type == TYPE_MOVIE) searchViewModel.searchMovie(query);
        else searchViewModel.searchTvShows(query);
    }

    @Override
    public void StartActivity(int index) {
        Intent intent = new Intent(
                getContext(),
                type == TYPE_MOVIE ? MovieDetailActivity.class : ShowDetailActivity.class
        );
        intent.putExtra(
                type == TYPE_MOVIE ? MovieDetailActivity.MOVIE_EXTRA : ShowDetailActivity.SHOW_EXTRA,
                type == TYPE_MOVIE ? searchViewModel.getMovie(index) : searchViewModel.getTvShow(index)
        );
        startActivity(intent);
    }

    @Override
    public void OnDeleteFromFavorite(int position) {
    }
}
