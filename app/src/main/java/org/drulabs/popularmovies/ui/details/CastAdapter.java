package org.drulabs.popularmovies.ui.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.drulabs.popularmovies.R;
import org.drulabs.popularmovies.config.AppConstants;
import org.drulabs.popularmovies.data.models.Cast;

import java.util.ArrayList;
import java.util.List;

class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastVH> {

    private List<Cast> castList;
    private Listener castInteractionListener;

    CastAdapter(@NonNull Listener listener) {
        this.castInteractionListener = listener;
        this.castList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CastVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_circ_image_details, parent, false);
        return new CastVH(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CastVH holder, int position) {
        Cast cast = castList.get(position);
        holder.bind(cast);
    }

    void reload(List<Cast> castList) {
        this.castList.clear();
        this.castList.addAll(castList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    class CastVH extends RecyclerView.ViewHolder {
        private ImageView imgCast;
        private TextView tvDescription;

        CastVH(View itemView) {
            super(itemView);
            imgCast = itemView.findViewById(R.id.img_circular_item);
            tvDescription = itemView.findViewById(R.id.tv_circ_item_details);
        }

        void bind(Cast cast) {
            String castProfileImage = AppConstants.TMDB_POSTER_BASE + cast.getProfilePath();

            Picasso.with(itemView.getContext())
                    .load(castProfileImage)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.ic_dead_smiley)
                    .into(imgCast);

            tvDescription.setText(cast.getName());

            imgCast.setOnClickListener(v -> castInteractionListener.onCastTapped
                    (castProfileImage));

            animate(itemView);
        }
    }

    private void animate(View itemView) {
        Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim
                .item_zoom_in);
        itemView.startAnimation(animation);

    }

    interface Listener {
        void onCastTapped(String urlToOpen);
    }
}
