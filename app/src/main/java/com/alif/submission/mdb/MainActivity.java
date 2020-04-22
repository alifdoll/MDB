package com.alif.submission.mdb;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.alif.submission.mdb.reminder.NotificationActivity;
import com.alif.submission.mdb.ui.search.SearchFragment;
import com.alif.submission.mdb.ui.favorite.fragment.FavoriteFragment;
import com.alif.submission.mdb.ui.movie.fragment.MovieFragment;
import com.alif.submission.mdb.ui.show.fragment.ShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.SearchView;


public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private BottomNavigationView bottomNavigationView;

    private Fragment activeFragment;
    private MovieFragment movieFragment = new MovieFragment();
    private ShowFragment showFragment = new ShowFragment();
    private FavoriteFragment favoriteFragment = new FavoriteFragment();
    private SearchFragment searchFragment = new SearchFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                     case R.id.navigation_movie:
                        fragment = new MovieFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_show:
                        fragment = new ShowFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_favorite:
                        fragment = new FavoriteFragment();
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
            }
        });
//        activeFragment = movieFragment;
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.nav_host_fragment, movieFragment)
//                .add(R.id.nav_host_fragment, showFragment)
//                .add(R.id.nav_host_fragment, favoriteFragment)
//                .hide(showFragment)
//                .hide(favoriteFragment)
//                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        searchView = (SearchView) (menu.findItem(R.id.menu_search)).getActionView();
        setupSearchView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_language:
                intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                break;

            case R.id.notification_setting:
                intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment,fragment);
        transaction.commit();
    }

    private void setupSearchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 0){
                    int type;
                    if (bottomNavigationView.getSelectedItemId() == R.id.navigation_movie){
                        type = SearchFragment.TYPE_MOVIE;
                    }else if (bottomNavigationView.getSelectedItemId() == R.id.navigation_show){
                        type = SearchFragment.TYPE_TV;
                    }else type = -1;

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putInt(SearchFragment.ARG_TYPE, type);
                    bundle.putString(SearchFragment.ARG_QUERY, query);
                    searchFragment = new SearchFragment();
                    searchFragment.setArguments(bundle);
                    transaction.replace(R.id.nav_host_fragment, searchFragment);
                    transaction.commit();
                } else {
                   bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
                   bottomNavigationView.getMenu().getItem(0).setChecked(true);
                   loadFragment(new MovieFragment());
                }
                return false;
            }
        });
    }
}
