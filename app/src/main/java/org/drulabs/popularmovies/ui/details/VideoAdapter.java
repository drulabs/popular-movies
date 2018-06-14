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
import org.drulabs.popularmovies.data.models.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoVH> {

    private List<Video> videos;
    private Listener videoInteractionListener;

    VideoAdapter(@NonNull Listener listener) {
        this.videoInteractionListener = listener;
        this.videos = new ArrayList<>();
    }

    @NonNull
    @Override
    public VideoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_circ_image_details, parent, false);
        return new VideoVH(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoVH holder, int position) {
        Video video = videos.get(position);
        holder.bind(video);
    }

    void reload(List<Video> videos) {
        this.videos.clear();
        this.videos.addAll(videos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoVH extends RecyclerView.ViewHolder {
        private ImageView imgVideo, imgShare, imgYoutube;
        private TextView tvDescription;

        VideoVH(View itemView) {
            super(itemView);
            imgVideo = itemView.findViewById(R.id.img_circular_item);
            imgShare = itemView.findViewById(R.id.img_circ_share);
            imgYoutube = itemView.findViewById(R.id.img_circ_youtube);
            tvDescription = itemView.findViewById(R.id.tv_circ_item_details);
            imgYoutube.setVisibility(View.VISIBLE);
            imgShare.setVisibility(View.VISIBLE);
        }

        void bind(Video video) {
            String videoThumbUrl = String.format(Locale.getDefault(), AppConstants
                    .VIDEO_THUMB_BASE, video.getKey());

            Picasso.with(itemView.getContext())
                    .load(videoThumbUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.ic_dead_smiley)
                    .into(imgVideo);

            tvDescription.setText(video.getName());

            imgShare.setOnClickListener(v -> videoInteractionListener.onShareTapped(video));
            imgVideo.setOnClickListener(v -> videoInteractionListener.onVideoTapped(video));

            animate(itemView);
        }
    }

    private void animate(View itemView) {
        Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim
                .item_zoom_in);
        itemView.startAnimation(animation);

    }

    interface Listener {
        void onVideoTapped(Video video);

        void onShareTapped(Video video);
    }
}
