package com.alif.submission.mdb.ui.show.detail.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.alif.submission.mdb.MainActivity;
import com.alif.submission.mdb.R;
import com.alif.submission.mdb.ui.show.item.ShowItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ShowDetailActivity extends AppCompatActivity {

    public static final String SHOW_EXTRA = "show_extra";
    private TextView showTitle,showOverview;
    private ImageView showPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        showTitle = findViewById(R.id.show_detail_title);
        showOverview = findViewById(R.id.show_detail_overview);
        showPhoto = findViewById(R.id.show_detail_photo);

        ShowItem show = getIntent().getParcelableExtra(SHOW_EXTRA);

        showTitle.setText(show.getTitle());
        showOverview.setText(show.getOverview());
        Glide.with(this)
                .load(show.getPosterPath())
                .apply(new RequestOptions())
                .into(showPhoto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(show.getTitle());
        this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}
