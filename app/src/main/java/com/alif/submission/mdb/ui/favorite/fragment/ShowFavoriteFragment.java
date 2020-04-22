package com.alif.submission.mdb.ui.favorite.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alif.submission.mdb.OnActionListener;
import com.alif.submission.mdb.R;
import com.alif.submission.mdb.database.DatabaseFavorite;
import com.alif.submission.mdb.ui.movie.detail.activity.MovieDetailActivity;
import com.alif.submission.mdb.ui.movie.item.MovieItem;
import com.alif.submission.mdb.ui.show.adapter.ShowListAdapter;
import com.alif.submission.mdb.ui.show.detail.activity.ShowDetailActivity;
import com.alif.submission.mdb.ui.show.item.ShowItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowFavoriteFragment extends Fragment implements OnActionListener {

    private ArrayList<ShowItem> showItems;
    private ShowListAdapter adapter = new ShowListAdapter();

    public ShowFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showItems = DatabaseFavorite.getInstance(getContext()).getAllFavoriteShows();
        adapter.setData(getContext(), showItems, this);
        RecyclerView rv = view.findViewById(R.id.rv_show_favorite);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void StartActivity(int position) {
        ShowItem show = showItems.get(position);
        Intent intent = new Intent(getContext(), ShowDetailActivity.class);
        intent.putExtra(ShowDetailActivity.SHOW_EXTRA, show);
        startActivity(intent);
    }

    @Override
    public void OnDeleteFromFavorite(int position) {
        adapter.deleteFavoriteItem(position);
    }
}
