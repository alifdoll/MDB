package com.alif.submission.mdb.ui.show.adapter;

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
import com.alif.submission.mdb.ui.show.item.ShowItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ShowListAdapter extends RecyclerView.Adapter<ShowListAdapter.ShowViewHolder> {

    private ArrayList<ShowItem> sData = new ArrayList<>();
    private OnActionListener listener;
    private DatabaseFavorite databaseFavorite;

    public void setData(Context context, ArrayList<ShowItem> item, OnActionListener onActionListener) {
        sData.clear();
        sData.addAll(item);
        listener = onActionListener;
        databaseFavorite = DatabaseFavorite.getInstance(context);
        notifyDataSetChanged();
    }

    public void deleteFavoriteItem(int position) {
        Log.d("MDB_DEBUG", "deleteFromFavorite(position) (BEF). position: " + position + " mData.size():" + sData.size());
        Log.d("MDB_DEBUG", "title: " + sData.get(position).getTitle());
        sData.remove(position);
        notifyDataSetChanged();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, sData.size());
        Log.d("MDB_DEBUG", "deleteFromFavorite(position) (AFT). position: " + position + " mData.size():" + sData.size());
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        databaseFavorite.close();
    }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_item_list, viewGroup, false);
        return new ShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShowViewHolder holder, final int position) {
        final ShowItem show = sData.get(position);
        show.setFavorite(databaseFavorite.showIsFavorite(show.getId()));

        holder.bind(show, holder);
        holder.btnAddFav.setText(show.isFavorite() ? R.string.btn_remove_favorite : R.string.btn_add_favorite);
        holder.btnAddFav.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(show.isFavorite() ? "#b71c1c" : "#03A9F4")));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(holder.itemView.getContext(), "YOU CHOOSE " + show.getTitle(), Toast.LENGTH_SHORT).show();
                listener.StartActivity(position );
            }
        });

        holder.btnAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show.isFavorite()){
                    databaseFavorite.deleteFavoriteShow(show.getId());
                    listener.OnDeleteFromFavorite(position);
                    Toast.makeText(holder.itemView.getContext(), R.string.toast_remove_favorite, Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseFavorite.addFavoriteShow(show);
                    Toast.makeText(holder.itemView.getContext(), R.string.toast_add_favorite, Toast.LENGTH_SHORT).show();

                }
                show.setFavorite(!show.isFavorite());
                holder.btnAddFav.setText(show.isFavorite() ? R.string.btn_remove_favorite : R.string.btn_add_favorite);
                holder.btnAddFav.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(show.isFavorite() ? "#b71c1c" : "#03A9F4")));

            }
        });
    }

    @Override
    public int getItemCount() {
        return sData.size();
    }

    class ShowViewHolder extends RecyclerView.ViewHolder {

        private TextView title, overview;
        private ImageView photo;
        private Button btnAddFav;

        ShowViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.show_title);
            overview = itemView.findViewById(R.id.show_overview);
            photo = itemView.findViewById(R.id.show_photo);
            btnAddFav = itemView.findViewById(R.id.show_button_add_favorite);
        }

        void bind(ShowItem show, ShowViewHolder holder) {
            holder.title.setText(show.getTitle());
            holder.overview.setText(show.getOverview());
            Glide.with(holder.itemView.getContext())
                    .load(show.getPosterPath())
                    .apply(new RequestOptions())
                    .into(holder.photo);
        }
    }
}
