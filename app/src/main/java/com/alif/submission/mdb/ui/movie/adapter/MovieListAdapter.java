package com.alif.submission.mdb.ui.movie.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alif.submission.mdb.OnActionListener;
import com.alif.submission.mdb.R;
import com.alif.submission.mdb.database.DatabaseFavorite;
import com.alif.submission.mdb.ui.movie.item.MovieItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private ArrayList<MovieItem> mData = new ArrayList<>();
    private OnActionListener listener;
    private DatabaseFavorite databaseFavorite;

    public void setData(Context context, ArrayList<MovieItem> item, OnActionListener onActionListener) {
        mData.clear();
        mData.addAll(item);
        listener = onActionListener;
        databaseFavorite = DatabaseFavorite.getInstance(context);
        notifyDataSetChanged();
    }

    public void deleteFavoriteItem(int position) {
        Log.d("MDB_DEBUG", "deleteFromFavorite(position) (BEF). position: " + position + " mData.size():" + mData.size());
        Log.d("MDB_DEBUG", "title: " + mData.get(position).getTitle());
        mData.remove(position);
        notifyDataSetChanged();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
        Log.d("MDB_DEBUG", "deleteFromFavorite(position) (AFT). position: " + position + " mData.size():" + mData.size());
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        databaseFavorite.close();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item_list, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {
        final MovieItem movie = mData.get(position);
        Log.d("MDB_DEBUG", movie.getTitle() + " " + position);
        movie.setFavorite(databaseFavorite.movieIsFavorite(movie.getId()));

        holder.bind(movie, holder);
        holder.btnAddFav.setText(movie.isFavorite() ? R.string.btn_remove_favorite : R.string.btn_add_favorite);
        holder.btnAddFav.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(movie.isFavorite() ? "#b71c1c" : "#03A9F4")));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MDB_DEBUG", movie.getTitle() + "  DATA IS SENT " + position);
//                Toast.makeText(holder.itemView.getContext(), "YOU CHOOSE " + movie.getTitle(), Toast.LENGTH_SHORT).show();
                listener.StartActivity(position);
            }
        });

        holder.btnAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movie.isFavorite()) {
                    databaseFavorite.deleteFavoriteMovie(movie.getId());
                    listener.OnDeleteFromFavorite(position);
                    Toast.makeText(holder.itemView.getContext(), R.string.toast_remove_favorite, Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseFavorite.addFavoriteMovie(movie);
                    Toast.makeText(holder.itemView.getContext(), R.string.toast_add_favorite, Toast.LENGTH_SHORT).show();
                }

                movie.setFavorite(!movie.isFavorite());
                holder.btnAddFav.setText(movie.isFavorite() ? R.string.btn_remove_favorite : R.string.btn_add_favorite);
                holder.btnAddFav.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(movie.isFavorite() ? "#b71c1c" : "#03A9F4")));
                //Toast.makeText(holder.itemView.getContext(), "Added To Favorite", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView title, overview;
        private Button btnAddFav;
        private ImageView photo;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.movie_title);
            overview = itemView.findViewById(R.id.movie_overview);
            photo = itemView.findViewById(R.id.movie_photo);
            btnAddFav = itemView.findViewById(R.id.movie_button_add_favorite);
        }

        void bind(MovieItem movie, MovieViewHolder holder) {
            holder.title.setText(movie.getTitle());
            holder.overview.setText(movie.getOverview());
            Glide.with(holder.itemView.getContext())
                    .load(movie.getPosterPath())
                    .apply(new RequestOptions())
                    .into(holder.photo);
        }
    }
}
