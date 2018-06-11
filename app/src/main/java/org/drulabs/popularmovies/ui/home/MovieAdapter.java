package org.drulabs.popularmovies.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.drulabs.popularmovies.R;
import org.drulabs.popularmovies.config.AppConstants;
import org.drulabs.popularmovies.data.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieVH> {

    private static final String TMDB_POSTER_BASE = AppConstants.TMDB_POSTER_BASE;

    private final List<Movie> movieList;

    private final int spanCount;

    private final MovieInteractionListener listener;

    private int lastKnownPosition = -1;

    MovieAdapter(@NonNull MovieInteractionListener listener, int spanCount) {
        this.movieList = new ArrayList<>();
        this.listener = listener;
        this.spanCount = spanCount;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,
                parent, false);
        return new MovieVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {
        Movie movie = movieList.get(position);
        holder.bind(movie, position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    void append(@NonNull List<Movie> movies) {
        int oldSize = this.movieList.size();
        this.movieList.addAll(movies);
        notifyItemRangeInserted(oldSize, movies.size());
    }

    void clearAndReload(@NonNull List<Movie> movies) {
        this.movieList.clear();
        lastKnownPosition = -1;
        notifyDataSetChanged();
        this.movieList.addAll(movies);
        notifyItemRangeInserted(1, movies.size());
    }

    class MovieVH extends RecyclerView.ViewHolder {

        final ImageView posterImg;

        MovieVH(@NonNull View itemView) {
            super(itemView);
            posterImg = itemView.findViewById(R.id.img_movie_poster);
        }

        void bind(@NonNull Movie movie, int position) {

            String posterUrl = TMDB_POSTER_BASE + movie.getPosterPath();

            Picasso.with(itemView.getContext())
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(posterImg);
            itemView.setOnClickListener(v -> listener.onClick(movie));

            animate(itemView, position);
        }
    }

    @Override
    public long getItemId(int position) {
        setHasStableIds(true);
        return super.getItemId(position);
    }

    private void animate(View itemView, int position) {
        if (position > lastKnownPosition) {
            Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim
                    .item_fall_up_animation);

            int offset = itemView.getContext().getResources().getInteger(R.integer
                    .anim_duration_offset);
            offset = offset + (position % spanCount) * offset;

            animation.setStartOffset(offset);
            itemView.startAnimation(animation);
            lastKnownPosition = position;
        }
    }

    /**
     * Click interaction listener
     */
    interface MovieInteractionListener {
        void onClick(Movie movie);
    }
}
