package com.alif.submission.mdb.ui.favorite.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alif.submission.mdb.R;
import com.alif.submission.mdb.ui.favorite.adapter.FavoriteSectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FavoriteFragment extends Fragment {

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        FavoriteSectionsPagerAdapter sectionsPagerAdapter = new FavoriteSectionsPagerAdapter(getChildFragmentManager(), getContext());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.fav_tabs);
        tabs.setupWithViewPager(viewPager);
        return view;
    }

}
