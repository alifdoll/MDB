package com.alif.submission.mdb.ui.show.fragment;


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
import com.alif.submission.mdb.ui.show.adapter.ShowListAdapter;
import com.alif.submission.mdb.ui.show.detail.activity.ShowDetailActivity;
import com.alif.submission.mdb.ui.show.item.ShowItem;
import com.alif.submission.mdb.ui.show.viewmodel.ShowMainViewModel;

import java.util.ArrayList;

public class ShowFragment extends Fragment implements OnActionListener {

    private ShowListAdapter adapter;
    private ShowMainViewModel mainViewModel;
    private RecyclerView rv;
    private ProgressBar progressBar;


    public ShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.rv_show);
        progressBar = view.findViewById(R.id.progressBar);

        adapter = new ShowListAdapter();
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ShowMainViewModel.class);

        adapter.notifyDataSetChanged();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL));

        mainViewModel.setShow();
        showLoading(true);

        mainViewModel.getShow().observe(getViewLifecycleOwner(), new Observer<ArrayList<ShowItem>>() {
            @Override
            public void onChanged(ArrayList<ShowItem> showItems) {
                if (showItems != null) {
                    adapter.setData(getContext(),showItems, ShowFragment.this);
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
        ShowItem show = mainViewModel.getListShow().get(position);
        Intent intent = new Intent(getContext(), ShowDetailActivity.class);
        intent.putExtra(ShowDetailActivity.SHOW_EXTRA, show);
        startActivity(intent);
    }

    @Override
    public void OnDeleteFromFavorite(int position) {

    }

}
