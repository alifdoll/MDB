package com.alif.submission.mdb.ui.movie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alif.submission.mdb.OnActionListener;
import com.alif.submission.mdb.R;
import com.alif.submission.mdb.ui.movie.adapter.MovieListAdapter;
import com.alif.submission.mdb.ui.movie.detail.activity.MovieDetailActivity;
import com.alif.submission.mdb.ui.movie.item.MovieItem;
import com.alif.submission.mdb.ui.movie.viewmodel.MovieMainViewModel;

import java.util.ArrayList;

public class MovieFragment extends Fragment implements OnActionListener {

    private MovieListAdapter adapter;
    private MovieMainViewModel mainViewModel;
    private ProgressBar progressBar;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progressBar);

        adapter = new MovieListAdapter();
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MovieMainViewModel.class);

        adapter.notifyDataSetChanged();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL));

        mainViewModel.setMovie();
        showLoading(true);

        mainViewModel.getMovie().observe(getViewLifecycleOwner(), new Observer<ArrayList<MovieItem>>() {
            @Override
            public void onChanged(ArrayList<MovieItem> movieItems) {
                if (movieItems != null) {
                    adapter.setData(getContext(), movieItems, MovieFragment.this);
                    showLoading(false);
                }
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void StartActivity(int position) {
        MovieItem movie = mainViewModel.getListMovie().get(position);
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_EXTRA, movie);
        startActivity(intent);
    }

    @Override
    public void OnDeleteFromFavorite(int position) {

    }
}
