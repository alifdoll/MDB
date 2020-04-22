package com.alif.submission.mdb.ui.favorite.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alif.submission.mdb.R;
import com.alif.submission.mdb.ui.favorite.fragment.MovieFavoriteFragment;
import com.alif.submission.mdb.ui.favorite.fragment.ShowFavoriteFragment;

public class FavoriteSectionsPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public FavoriteSectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    private final int[] TABS = new int[]{
            R.string.movie_favorite_list,
            R.string.show_favorite_list
    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new MovieFavoriteFragment();
                break;

            case 1:
                fragment = new ShowFavoriteFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TABS[position]);
    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
