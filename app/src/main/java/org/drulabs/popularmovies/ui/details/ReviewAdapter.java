package org.drulabs.popularmovies.ui.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.drulabs.popularmovies.R;
import org.drulabs.popularmovies.data.models.Review;

import java.util.ArrayList;
import java.util.List;

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewVH> {

    private List<Review> reviewList;
    private Listener reviewInteractionListener;

    ReviewAdapter(@NonNull Listener listener) {
        this.reviewInteractionListener = listener;
        this.reviewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReviewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_review, parent, false);
        return new ReviewVH(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewVH holder, int position) {
        Review review = reviewList.get(position);
        holder.bind(review);
    }

    void reload(List<Review> videos) {
        this.reviewList.clear();
        this.reviewList.addAll(videos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ReviewVH extends RecyclerView.ViewHolder {
        private TextView tvReviewContent, tvAuthor;

        ReviewVH(View itemView) {
            super(itemView);
            tvReviewContent = itemView.findViewById(R.id.tv_review_content);
            tvAuthor = itemView.findViewById(R.id.tv_review_author);
        }

        void bind(Review review) {
            tvReviewContent.setText(review.getContent());
            tvAuthor.setText(review.getAuthor());
            itemView.setOnClickListener(v -> reviewInteractionListener.onReviewTapped(review
                    .getUrl()));
            animate(itemView);
        }
    }

    private void animate(View itemView) {
        Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim
                .item_fall_up_animation);
        itemView.startAnimation(animation);

    }

    interface Listener {
        void onReviewTapped(String urlToOpen);
    }
}
