package com.alif.submission.mdb.ui.movie.detail.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.alif.submission.mdb.R;
import com.alif.submission.mdb.ui.movie.item.MovieItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_EXTRA = "movie_extra";
    private TextView movieTitle,movieOverview;
    private ImageView moviePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);

        movieTitle = findViewById(R.id.movie_detail_title);
        movieOverview = findViewById(R.id.movie_detail_overview);
        moviePhoto = findViewById(R.id.movie_detail_photo);

        MovieItem movie = getIntent().getParcelableExtra(MOVIE_EXTRA);

        movieTitle.setText(movie.getTitle());
        movieOverview.setText(movie.getOverview());
        Glide.with(this)
                .load(movie.getPosterPath())
                .apply(new RequestOptions())
                .into(moviePhoto);

        Log.d("MDB_DEBUG", "Data Received is" + movie.getTitle());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(movie.getTitle());
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }
}
